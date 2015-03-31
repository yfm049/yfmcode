package com.year.pro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.BadLocationException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpUtils {

	private HttpClient hc;
	private Map<String, String> murl = new HashMap<String, String>();
	private Map<String, String> kv = new HashMap<String, String>();
	

	public HttpUtils() {
		hc = new DefaultHttpClient();
		HttpParams params = hc.getParams();
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
		murl.put("金华", "http://202.101.180.219:7001/yearcheck");
		murl.put("绍兴", "http://www.sxgs.gov.cn:7001/yearcheck");
		murl.put("杭州", "http://nj.hzaic.gov.cn/yearcheck");
		murl.put("湖州", "http://yw.huaic.gov.cn/yearcheck");
		murl.put("嘉兴", "http://60.190.145.34:7001/yearcheck");
		murl.put("温州", "http://61.175.211.169/yearcheck");
		murl.put("台州", "http://61.175.223.171:7001/yearcheck");
		murl.put("衢州", "http://61.130.53.3:7001/yearcheck");
		murl.put("宁波", "http://www.nbaic.gov.cn:7002/yearcheck");
		murl.put("丽水", "http://61.153.64.196:7001/yearcheck");
		kv.put("01","AAA");
		kv.put("02","AA");
		kv.put("03","A");
		kv.put("04","B");
		kv.put("05","C");
		kv.put("06","D");
		kv.put("07","未评定或未知");
	}

	public void initUrl() {
		int count = YearMainApp.urltext.getLineCount();
		System.out.println(count);
		for (int i = 0; i < count; i++) {
			try {
				int start = YearMainApp.urltext.getLineStartOffset(i);
				int end = YearMainApp.urltext.getLineEndOffset(i);
				if (end > start) {
					String text = YearMainApp.urltext.getText(start, end
							- start - 1);
					String[] mt = text.split("|");
					if (mt.length > 1) {
						murl.put(mt[0], mt[1]);
					}
				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String GetPassword(String url, Info info) {
		HttpPost hp = null;
		try {
			hp = new HttpPost(url
					+ "/client/flow/doGetPwdAction.do?_id=1369720218014");
			List<NameValuePair> lnp = new ArrayList<NameValuePair>();
			lnp.add(new BasicNameValuePair("loginInfo.identity.pasEntKey", info
					.getCode()));
			String gs = URLEncoder.encode(info.getGongsi(), "UTF-8");
			lnp.add(new BasicNameValuePair("loginInfo.identity.pasEntName", gs));
			String fr = URLEncoder.encode(info.getFaren(), "UTF-8");
			lnp.add(new BasicNameValuePair("loginInfo.identity.pasLegRep", fr));
			lnp.add(new BasicNameValuePair("loginInfo.identity.year", "2012"));
			lnp.add(new BasicNameValuePair("loginInfo.identity.pasMobile", info
					.getPhone()));
			lnp.add(new BasicNameValuePair("pasIdNum", info.getSfz()));
			hp.setEntity(new UrlEncodedFormEntity(lnp));
			HttpResponse hr = hc.execute(hp);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(hr.getEntity());
				JSONObject jo = new JSONObject(json);
				String msg = jo.getString("msg");
				if (msg.startsWith("您在工商年检网站申请的密码是")) {
					msg = msg.substring(msg.indexOf("：") + 1);
					return msg;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			hp.abort();
		}
		return null;
	}

	private boolean init(String url) {
		HttpGet hg = null;
		try {
			hg = new HttpGet(url + "/client/login.jsp");
			HttpResponse hr = hc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			hg.abort();
		}
		return false;
	}

	private void Login(String url, Info info) {
		if (info.getPassword() == null) {
			return;
		}
		HttpPost hp = null;
		try {
			hp = new HttpPost(url + "/client/flow/loginAction.do");
			List<NameValuePair> lnp = new ArrayList<NameValuePair>();
			lnp.add(new BasicNameValuePair("isCheckCertificate", "1"));
			lnp.add(new BasicNameValuePair("loginInfo.identity.pasEntKey", info
					.getCode()));
			lnp.add(new BasicNameValuePair("loginInfo.identity.pasPasswd", info
					.getPassword()));
			lnp.add(new BasicNameValuePair("loginInfo.identity.year", "2012"));
			hp.setEntity(new UrlEncodedFormEntity(lnp, "UTF-8"));
			HttpResponse hr = hc.execute(hp);
			if (hr.getStatusLine().getStatusCode() == 302) {
				Header hd = hr.getFirstHeader("Location");
				info.setLocation(hd.getValue());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			hp.abort();

		}
	}

	private void GetData(String url, Info info) {
		HttpGet hg=null;
		try {
			hg = new HttpGet(url + "/other/readOtherCircsRptAction.do?"
					+ info.getLocation() + "&fldResId=607");
			HttpResponse hr = hc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String html = EntityUtils.toString(hr.getEntity());
				Document doc = Jsoup.parse(html);
				Element ftel = doc.getElementById("otherInfoTel1");
				info.setFtel(ftel.val());
				Element fmobile = doc.getElementById("otherInfoMobTel1");
				info.setFmobile(fmobile.val());
				Element caiwu = doc.getElementById("otherInfoFinanPrin");
				info.setCaiwu(caiwu.val());
				Element ctel = doc.getElementById("otherInfoTel2");
				info.setCtel(ctel.val());
				Element cmobile = doc.getElementById("otherInfoMobTel2");
				info.setCmobile(cmobile.val());
				Element gongshang = doc.getElementById("otherInfoIcLia");
				info.setGongshang(gongshang.val());
				Element gtel = doc.getElementById("otherInfoTel3");
				info.setGtel(gtel.val());
				Element gmobile = doc.getElementById("otherInfoMobTel3");
				info.setGmobile(gmobile.val());
				Element gaddress = doc.getElementById("otherInfoEntCommAddr");
				info.setAddress(gaddress.val());

				Element otherInfoEntUnicode = doc
						.getElementById("otherInfoEntUnicode");
				info.setOtherInfoEntUnicode(otherInfoEntUnicode == null ? ""
						: otherInfoEntUnicode.val());

				Element otherInfoUniCodeIssAut = doc
						.getElementById("otherInfoUniCodeIssAut");
				info.setOtherInfoUniCodeIssAut(otherInfoUniCodeIssAut == null ? ""
						: otherInfoUniCodeIssAut.val());

				Element otherInfoTaxRegNo = doc
						.getElementById("otherInfoTaxRegNo");
				info.setOtherInfoTaxRegNo(otherInfoTaxRegNo == null ? ""
						: otherInfoTaxRegNo.val());

				Element otherInfoTaxReg = doc.getElementById("otherInfoTaxReg");
				info.setOtherInfoTaxReg(otherInfoTaxReg == null ? ""
						: otherInfoTaxReg.val());

				Element otherInfoYearAvgWage = doc
						.getElementById("otherInfoYearAvgWage");
				info.setOtherInfoYearAvgWage(otherInfoYearAvgWage == null ? ""
						: otherInfoYearAvgWage.val());

				Elements otherInfoOutNum = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoOutNum");
				info.setOtherInfoOutNum(otherInfoOutNum == null ? ""
						: otherInfoOutNum.val());

				Elements otherInfoMgrGro = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoMgrGro");
				info.setOtherInfoMgrGro(otherInfoMgrGro == null ? ""
						: otherInfoMgrGro.val());

				Elements otherInfoDifGro = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoDifGro");
				info.setOtherInfoDifGro(otherInfoDifGro == null ? ""
						: otherInfoDifGro.val());

				Elements otherInfoProfitGro = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoProfitGro");
				info.setOtherInfoProfitGro(otherInfoProfitGro == null ? ""
						: otherInfoProfitGro.val());

				Elements otherInfoExpNum = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoExpNum");
				info.setOtherInfoExpNum(otherInfoExpNum == null ? ""
						: otherInfoExpNum.val());

				Elements otherInfoServIn = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoServIn");
				info.setOtherInfoServIn(otherInfoServIn == null ? ""
						: otherInfoServIn.val());

				Elements otherInfoTaxGro = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoTaxGro");
				info.setOtherInfoTaxGro(otherInfoTaxGro == null ? ""
						: otherInfoTaxGro.val());

				Elements otherInfoStaGro = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoStaGro");
				info.setOtherInfoStaGro(otherInfoStaGro == null ? ""
						: otherInfoStaGro.val());

				Elements otherInfoRegGro = doc.getElementsByAttributeValue(
						"name", "otherInfoList[0].otherInfoRegGro");
				info.setOtherInfoRegGro(otherInfoRegGro == null ? ""
						: otherInfoRegGro.val());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			hg.abort();
		}
	}
	public void getBank(String url,Info info){
		
		String bankurl=url + "/other/viewBankRpt.do?pagination.currentPage=1&pagination.pageSize=5&"+info.getLocation()+"&serverFlag=";
		HttpGet hg=null;
		try {
			hg=new HttpGet(bankurl);
			HttpResponse hr = hc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json=EntityUtils.toString(hr.getEntity());
				if(json!=null&&!"".equals(json)){
					JSONObject jo=new JSONObject(json);
					JSONArray ja=jo.getJSONArray("bankAccJSONArray");
					for(int i=0;ja!=null&&i<ja.length();i++){
						JSONObject o=ja.getJSONObject(i);
						info.setEv_dhx_skyblue(info.getEv_dhx_skyblue()+(i+1)+".> 开户银行名称: "+o.getString("depBankName")+" | 银行账号"+o.getString("depBankAccount")+" | 信用等级 "+kv.get(o.getString("depBankAccType"))+"  ");
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			hg.abort();
		}
	}

	public void Caiji(Map<String, List<Info>> msl, String path) {
		Set<Entry<String, List<Info>>> mel = msl.entrySet();
		for (Entry<String, List<Info>> esl : mel) {
			String key = esl.getKey();
			String url = murl.get(key);
			if (!YearMainApp.isrun) {
				return;
			}
			if (url != null) {
				boolean flag = init(url);
				if (flag) {
					YearMainApp.msg.append("开始采集 " + key + "\r\n");
					List<Info> li = esl.getValue();
					for (Info info : li) {
						try {
							Thread.sleep(200);
							YearMainApp.msg.append("开始采集公司 " + info.getGongsi()
									+ "\r\n");
							String password = GetPassword(url, info);
							YearMainApp.msg.append("获取密码 " + password + "\r\n");
							if (password != null) {
								info.setPassword(password);
								Login(url, info);

								if (info.getLocation() != null) {
									YearMainApp.msg.append("登录成功\r\n");
									info.setLocation(info.getLocation().substring(
											info.getLocation().indexOf("corpid=")));
									GetData(url, info);
									getBank(url, info);
									YearMainApp.msg.append("获取数据成功\r\n");
								}
							}
							if (!YearMainApp.isrun) {
								return;
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							Write(key, info, path);
						}
					}
				} else {
					YearMainApp.msg.append(key + " 错误 " + url + "\r\n");
				}
			} else {
				YearMainApp.msg.append("初始化失败 " + key + " 地址不存在" + "\r\n");
			}
		}
	}

	public void Write(String key, Info info, String path) {
		try {
			boolean isfirst = false;
			File file = new File(path + "//" + key + ".csv");
			if (!file.getParentFile().exists()) {
				file.mkdirs();
			}

			if (!file.exists()) {
				file.createNewFile();
				YearMainApp.msg.append("创建 " + file.getName() + " 文件\r\n");
				isfirst = true;
			}
			FileOutputStream fos = new FileOutputStream(file, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			if (isfirst) {
				bw.write("注册号,企业名称,身份证,手机,登陆密码,法人,法人固话号码,法人移动号码,财务负责人,财务固话,财务移动号码,工商联络员,工商联络员固话,工商联络员移动号码" +
						",公司地址" +
						",组织机构代码证号" +
						",发证机关" +
						",税务证登记号" +
						",税务登记机关" +
						",职工年人均工资" +
						",产值" +
						",盈利额" +
						",亏损额" +
						", 营业额" +
						", 出口创汇额" +
						",服务营业收入" +
						",纳税总额" +
						",国税" +
						",地税" +
						",开户银行信息" +
						"\r\n");
				bw.flush();
			}
			YearMainApp.msg.append(info.toString());
			YearMainApp.msg
					.setCaretPosition(YearMainApp.msg.getText().length());

			bw.append(info.toString());
			bw.flush();

			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

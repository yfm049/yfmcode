package com.chinaip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.app.db.SqlUtil;

public class HttpUtils {

	private boolean isinit = false;
	private HttpClient client;
	private BasicHttpParams hcp;
	private Map<String, String> map;
	int count = 0;

	public HttpUtils() {
		hcp = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(hcp, 5000);
		HttpConnectionParams.setSoTimeout(hcp, 30000);
		client = new DefaultHttpClient(hcp);
	}

	public void init() {
		if (isinit) {
			return;
		}
		HttpPost hp = null;
		try {
			String url = "http://www.chinaip.com.cn/login";
			hp = new HttpPost(url);
			List<NameValuePair> lnp = new ArrayList<NameValuePair>();
			lnp.add(new BasicNameValuePair("NickName", ""));
			lnp.add(new BasicNameValuePair("channelid", "14,15,16"));
			lnp.add(new BasicNameValuePair("chanye", "1506"));
			lnp.add(new BasicNameValuePair("errorurl", "error.jsp"));
			lnp.add(new BasicNameValuePair("name", "cnipr"));
			lnp.add(new BasicNameValuePair("password", "123456"));
			lnp.add(new BasicNameValuePair("presearchword", ""));
			lnp.add(new BasicNameValuePair("searchitem", "ti"));
			lnp.add(new BasicNameValuePair("searchword", ""));
			lnp.add(new BasicNameValuePair("url",
					"zljs/index.jsp?navRootID=1506&t=2"));
			hp.setEntity(new UrlEncodedFormEntity(lnp, "UTF-8"));
			HttpResponse hr = client.execute(hp);
			int state = hr.getStatusLine().getStatusCode();
			if (state == 302) {
				MainApp.msg.append("初始化成功...\r\n");
				MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
			}
			isinit = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			hp.abort();
		}
	}

	public List<Node> GetChildNode(Node node) {
		List<Node> ln = new ArrayList<Node>();
		HttpGet hg = null;
		try {
			String url = "http://www.chinaip.com.cn/zljs/experience/renderIPCTree.jsp?IPCCode="
					+ node.getIPCCode()
					+ "&level="
					+ node.getLevel()
					+ "&recursedNodes="
					+ node.getRecursedNodes()
					+ "&"
					+ new Random().nextInt(999);
			hg = new HttpGet(url);
			HttpResponse hr = client.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				String html = EntityUtils.toString(hr.getEntity());

				Document doc = Jsoup.parse(html);
				Elements tables = doc.getElementsByTag("table");
				for (Element table : tables) {
					Elements zk = table
							.getElementsByAttributeValue("alt", "展开");
					Elements name = table.getElementsByClass("bluel");
					Node ipnode = new Node();
					if (zk != null && zk.size() > 0) {
						String value = zk.get(0).attr("onclick");
						value = value.substring(value.indexOf("'") + 1,
								value.lastIndexOf("'"));
						ipnode.SetValue(value);
					}
					if (name != null && name.size() > 0) {
						String value = name.get(0).attr("onclick");
						value = value.substring(value.indexOf("'") + 1,
								value.lastIndexOf("'"));
						ipnode.setIPCCode(value);
						ipnode.setName(name.get(0).text());
					}
					ln.add(ipnode);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			hg.abort();
		}
		return ln;
	}

	public String Search(String tj) {
		count = 0;
		map = new HashMap<String, String>();
		HttpGet hg = null;
		try {
			String url = "http://www.chinaip.com.cn/zljs/hyjs-jieguo-mixed.jsp?firstsearch=1&issearch=on&sortfield=RELEVANCE&extension=&sRecordNumber=50&searchType=0&searchFrom=0&savesearchword=ON&historyCount=&historyLockCount=&cizi=2&sortcolumn=RELEVANCE&R1=-&txtA=&txtC=&txtB=&txtE=&txtF=&txtH=&txtI=&txtJ=&txtK=&txtL=&txtM=&txtN=&txtP=&txtR=&txtS=&txtSearchWord=";
			url = url + tj;
			System.out.println(url);
			hg = new HttpGet(url);
			HttpResponse hr = client.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String html = EntityUtils.toString(hr.getEntity());
				if (html.indexOf("没有检索到符合条件的记录") > 0) {
					MainApp.msg.append("没有检索到符合条件的记录...\r\n");
					MainApp.msg
							.setCaretPosition(MainApp.msg.getText().length());
				}
				String src = getMatcher(
						"RecordFrame.jsp[\\?]?recordnum=[0-9]+&page=[0-9]+&[-]?[0-9]+",
						html);
				Document doc = Jsoup.parse(html);
				Elements forms = doc.getElementsByAttributeValue("name",
						"TurnPageForm");
				if (forms.size() > 0) {
					Elements inputs = forms.get(0).getElementsByTag("input");
					for (Element input : inputs) {
						map.put(input.attr("name"), input.attr("value"));
					}
				}
				return src;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			hg.abort();
			if (MainApp.isrun) {
				MainApp.msg.append("获取数据失败，正在重新获取...\r\n");
				MainApp.msg.setCaretPosition(MainApp.msg.getText().length());

			}
		} finally {
			hg.abort();
		}
		return null;
	}

	public String Search(int page) {
		HttpPost hg = null;
		String html=null;
		HttpResponse hr=null;
		List<NameValuePair> lnp=null;
		try {
			String url = "http://www.chinaip.com.cn/zljs/hyjs-jieguo-mixed.jsp";
			hg = new HttpPost(url);
			map.put("page", String.valueOf(page));
			lnp = new ArrayList<NameValuePair>();
			Set<String> ss = map.keySet();
			for (String key : ss) {
				String value = map.get(key);
				lnp.add(new BasicNameValuePair(key, value));
			}
			hg.setEntity(new UrlEncodedFormEntity(lnp, "UTF-8"));
			hr = client.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				html = EntityUtils.toString(hr.getEntity());
				return getMatcher(
						"RecordFrame.jsp[\\?]?recordnum=[0-9]+&page=[0-9]+&[-]?[0-9]+",
						html);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			hg.abort();
			if (MainApp.isrun) {
				MainApp.msg.append("获取数据失败，正在重新获取...\r\n");
				MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
			}
		} finally {
			hg.abort();
			hg=null;
			html=null;
			hr=null;
			lnp=null;
		}
		return null;
	}

	public String getMatcher(String regex, String source) {
		String result = null;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			result = matcher.group(0);// 只取第一组
		}
		return result;
	}


	public boolean getData(String page, boolean flag, int m) {
		HttpGet hg = null;
		Document doc=null;
		HttpResponse hr=null;
		String htmls=null;
		HttpEntity entity=null;
		Elements trs=null;
		String url = "http://www.chinaip.com.cn/zljs/" + page;
		try {
			System.out.println(url);
			hg = new HttpGet(url);
			hg.addHeader(
					"Cookie",
					"DisplayCookies=%u7533%u8BF7%u53F7%7C120%23%u516C%u5F00%uFF08%u516C%u544A%uFF09%u53F7%7C160%23%u4E3B%u5206%u7C7B%u53F7%7C120%23%u540D%u79F0%7C500%23%u5206%u7C7B%u53F7%7C160%23%u7533%u8BF7%uFF08%u4E13%u5229%u6743%uFF09%u4EBA%7C160%23%u53D1%u660E%uFF08%u8BBE%u8BA1%uFF09%u4EBA%7C160%23%u516C%u5F00%uFF08%u516C%u544A%uFF09%u65E5%7C160%23%u7533%u8BF7%u65E5%7C100%23%u672C%u56FD%u4E3B%u5206%u7C7B%u53F7%7C160%23%u4E13%u5229%u4EE3%u7406%u673A%u6784%7C160%23%u4EE3%u7406%u4EBA%7C100%23%u5730%u5740%7C160%23%u56FD%u7701%u4EE3%u7801%7C100;");

			hr = client.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				entity=hr.getEntity();
				if(entity==null){
					return false;
				}
				htmls = getData(entity.getContent());
				if(htmls==null){
					return false;
				}
				htmls = htmls.replaceAll(
						"\"\\);[\\s]+[\\}]{1}[\\s]+[\\}]{1}[\\s]+</script>",
						" ");
				htmls = htmls
						.replaceAll(
								"document.write\\(\"<TD height='25' nowrap class='gray'>&nbsp;?",
								"</SCRIPT><TD height='25' nowrap class='gray'>");
				htmls = htmls.replaceAll(
						"(?is)<script[^>]*?>.*?<\\/script>", "");
				doc = Jsoup.parse(htmls);
				trs = doc.getElementsByTag("tr");
				if (!flag) {
					if (trs.size() == 11) {
						SaveToDatabase(trs);
					} else {
						MainApp.msg.append("数据丢失，重新获取...\r\n");
						MainApp.msg.setCaretPosition(MainApp.msg.getText()
								.length());
						return false;
					}
				} else {
					if (m == 0 || trs.size() == (m + 1)) {
						SaveToDatabase(trs);
					} else {
						MainApp.msg.append("数据丢失，重新获取...\r\n");
						MainApp.msg.setCaretPosition(MainApp.msg.getText()
								.length());
						return false;
					}
				}

				return true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			MainApp.msg.append("保存数据失败，正在重新获取...\r\n");
			MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
		} finally {
			hg.abort();
			hg=null;
			trs=null;
			doc=null;
			htmls=null;
			hr=null;
			entity=null;
			url=null;
		}
		return false;
	}
	// 获取数据，使用缓存、解决内存溢出
	private String getData(InputStream inputStream) throws Exception {
		// Log.i("LOG", "getData");
		// 内存缓冲区
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = -1;
		byte[] buf = new byte[1024];// 每次读一K
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			byte[] bytes = outputStream.toByteArray();
			inputStream.close();
			outputStream.close();
			return new String(bytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			outputStream=null;
			inputStream=null;
			buf=null;
		}
	}

	private String sql = "insert into data(shenqinghao,gonggao,zhufenlei,mingcheng,fenleihao,shenqingren,famingren,gongkairi,shenqingri,benguozhufenlei,zhuanlidaili,dailiren,dizhi,guoshengdaima) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public void SaveToDatabase(Elements trs) {
		MainApp.msg.append("获取" + (trs.size() - 1) + "行数据,内存剩余"+(Runtime.getRuntime().freeMemory()/1048576f)+"M\r\n");
		MainApp.msg.setCaretPosition(MainApp.msg.getText().length());
		if (trs != null && trs.size() > 0) {
			for (int i = 1; i < trs.size(); i++) {
				Element tr = trs.get(i);
				Elements tds = tr.getElementsByTag("td");
				if (tds != null && tds.size() == 15) {
					SqlUtil.save(sql, new Object[] { tds.get(1).html(),
							tds.get(2).html(), tds.get(3).html(),
							tds.get(4).html(), tds.get(5).html(),
							tds.get(6).html(), tds.get(7).html(),
							tds.get(8).html(), tds.get(9).html(),
							tds.get(10).html(), tds.get(11).html(),
							tds.get(12).html(), tds.get(13).html(),
							tds.get(14).html() });
					count++;
				} else {
					MainApp.msg.append("丢失数据第" + i + "行--共" + tds.size()
							+ "列...\r\n");
					MainApp.msg
							.setCaretPosition(MainApp.msg.getText().length());
				}
			}
		}
		MainApp.msg.append("成功保存" + count + "条数据...\r\n");
		MainApp.msg.setCaretPosition(MainApp.msg.getText().length());

	}

}

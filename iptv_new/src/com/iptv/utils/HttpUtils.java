package com.iptv.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.iptv.pojo.Appinfo;
import com.iptv.pojo.BackData;
import com.iptv.pojo.Film;
import com.iptv.pojo.PrgItem;
import com.iptv.pojo.TvInfo;

public class HttpUtils {

	public static String baseurl = "http://iptv.xsj7188.com:88/";// 高清新视觉

	private HttpGet hg = null;
	private HttpClient hc;

	public HttpUtils() {
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 15000);
		HttpConnectionParams.setSoTimeout(params, 50000);
		HttpConnectionParams.setLinger(params, 60);
		hc = new DefaultHttpClient(params);
	}

	public HttpUtils(int timeout, int sotime) {
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, sotime);
		HttpConnectionParams.setLinger(params, 60);
		hc = new DefaultHttpClient(params);
	}
	//请求接口获取数据
	public String doget(String url) {
		try {
			Log.i("tvinfo", url);
			hg = new HttpGet(url);
			
			HttpResponse hr = hc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String xml = EntityUtils.toString(hr.getEntity(), "UTF-8");
				Log.i("tvinfo", xml);
				return xml.trim();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}
	//保存直播数据到数据库
	public boolean parsexml(String xml, SqliteUtils su) {
		if (xml != null && !"".equals(xml)) {
			SQLiteDatabase db = su.getWritableDatabase();
			db.beginTransaction();
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo", "xml- " + xml);
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					if ("ret".equals(ele.getName())) {
						return false;
					} else if ("Category".equals(ele.getName())) {
						String gid = ele.attributeValue("id");
						String name = ele.attributeValue("name");
						Iterator<Element> files = ele.elementIterator();
						while (files != null && files.hasNext()) {
							Element file = files.next();
							String tvid = file.elementTextTrim("id");
							String tname = file.elementTextTrim("name");
							String httpurl = file.elementTextTrim("httpurl");
							String hotlink = file.elementTextTrim("hotlink");
							String isflag = file.elementTextTrim("isflag");
							String logo=file.elementTextTrim("logo");
							String epg=file.elementTextTrim("epg");
							su.saveiptvgroup(db, gid, name, tvid);
							Log.i("tvinfo", gid + "-" + name + "-" + tvid);
							su.savetvdate(db, tvid, tname, httpurl, hotlink,
									isflag,logo,epg);
						}
					}
				}
				db.setTransactionSuccessful();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} finally {
				db.endTransaction();
				db.close();
			}
		}
		return false;
	}
	
	//获取回放频道列表
	public List<Film> getfilmlist(String xml) {
		List<Film> filmlist = new ArrayList<Film>();
		if (xml != null && !"".equals(xml)) {
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo", xml);
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					if (ele.getName().startsWith("film")) {
						Film film = new Film();
						film.setId(ele.elementTextTrim("id"));
						film.setName(ele.elementTextTrim("name"));
						film.setUrl(ele.elementTextTrim("url"));
						film.setLogo(ele.elementTextTrim("logo"));
						filmlist.add(film);
					} else if ("rq".equals(ele.getName())) {
						BackData.currtime = ele.getText().trim();
					} else if ("day".equals(ele.getName())) {
						BackData.cday = Integer.parseInt(ele.getText().trim());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filmlist;
	}
	//获取回放节目列表
	public List<PrgItem> getPrg(String url) {
		List<PrgItem> prglist = null;
		String xml = doget(url);
		if (xml != null && !"".equals(xml)) {
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo", xml);
				prglist = new ArrayList<PrgItem>();
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Element eitem = root.element("item");
				Iterator<Element> ite = eitem.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					PrgItem item = new PrgItem();
					Log.i("tvinfo", ele.asXML());
					if (ele != null && "prgItem".equals(ele.getName())) {
						item.setName(ele.elementTextTrim("name"));
						item.setDate(ele.elementTextTrim("date"));
						item.setTime(ele.elementTextTrim("time"));
						item.setP2purl(ele.elementTextTrim("url"));
						item.setHotlink(ele.elementTextTrim("hotlink"));
						prglist.add(item);
						Log.i("tvinfo", prglist.size() + "");
					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return prglist;
	}
	//获取用户信息
	public String getUserInfo(String xml) {
		String yxsj = "";
		if (xml != null && !"".equals(xml)) {
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo", xml);
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				yxsj = root.elementText("yxsj");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return yxsj;
	}
	//关闭http连接
	public void close() {
		try {
			Log.i("tvinfo", "isAborted _" + hg.isAborted() + "");
			if (hg != null && !hg.isAborted()) {
				hg.abort();
			}
			if (hc != null) {
				hc.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获取远程app升级信息
	public Appinfo getremoteapp(String url) {
		Appinfo info = new Appinfo();
		String xml = doget(HttpUtils.baseurl + url);
		Log.i("tvinfo", xml + "");
		if (xml != null && !"".equals(xml)) {
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				info.setMsg(root.elementText("message"));
				info.setVercode(Integer.parseInt(root.elementText("vercode")));
				info.setUrl(root.elementText("url"));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return info;
	}
	//保存预告信息
	public void SaveYuGaoData(SqliteUtils su) {
		try {
			hg = new HttpGet(baseurl + "epg.zip");
			HttpResponse hr = hc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = hr.getEntity().getContent();
				unZip(is,su);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//解压缩数据
	public void unZip(InputStream stream,SqliteUtils su) {
		ZipInputStream zis = null;
		SQLiteDatabase db=null;
		try {
			db = su.getWritableDatabase();
			db.beginTransaction();
			zis = new ZipInputStream(stream);
			InputStreamReader isr = new InputStreamReader(zis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while (zis.getNextEntry() != null) {
				String m = ToXmlString(br);
				SaveToData(m,db);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			db.close();
			if (zis != null) {
				try {
					stream.close();
					zis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private String ToXmlString(BufferedReader br) throws Exception {
		StringBuffer sb = new StringBuffer();
		String m = null;
		while ((m = br.readLine()) != null) {
			sb.append(m);
		}
		return sb.toString();
	}

	private void SaveToData(String m,SQLiteDatabase db) {
		try {
			String sql="insert into notice (epgname,week,time,name) values(?,?,?,?)";
			Document doc = DocumentHelper.parseText(m);
			Element root = doc.getRootElement();
			String id = root.attributeValue("ID", "");
			List<Element> liste=root.elements();
			for(Element et:liste){
				String rq=et.getName();
				List<Element> listitem=et.elements();
				for(Element item:listitem){
					String time=item.attributeValue("Time");
					String name=item.attributeValue("Name");
					db.execSQL(sql, new String[]{id,rq,time,name});
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}
	}
}

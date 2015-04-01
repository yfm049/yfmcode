package com.iptv.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.iptv.pojo.Appinfo;
import com.iptv.pojo.BackData;
import com.iptv.pojo.Film;
import com.iptv.pojo.PrgItem;
import com.iptv.pojo.TvInfo;

public class HttpUtils {

	// public static String baseurl="http://hq2014.gnway.net:3838/";
	//public static String baseurl="http://10.0.2.2:8080/dnetNew/";
	//public static String baseurl="http://10.120.148.53:8080/dnetNew/";
	public static String baseurl="http://iptv.xsj7188.com:88/";
	//public static String baseurl = "http://iptv.xsj7188.com:3838/";//新视觉
	//public static String baseurl = "http://iptv.dlerbh.com:3889/";//高清电视
	//public static String baseurl = "http://iptv.ovpbox.com:8889/";//德宝电视
	
	

	private HttpGet hg = null;
	private HttpClient hc;
	private Context context;

	public HttpUtils() {
		this.context=context;
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 15000);
		HttpConnectionParams.setSoTimeout(params, 50000);
		HttpConnectionParams.setLinger(params, 60);
		hc = new DefaultHttpClient(params);
	}
	public HttpUtils(Context context) {
		this.context=context;
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 15000);
		HttpConnectionParams.setSoTimeout(params, 50000);
		HttpConnectionParams.setLinger(params, 60);
		hc = new DefaultHttpClient(params);
	}
	public HttpUtils(Context context,int timeout,int sotime) {
		this.context=context;
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, sotime);
		HttpConnectionParams.setLinger(params, 60);
		hc = new DefaultHttpClient(params);
	}

	public String doget(String url) {
		try {
			Log.i("tvinfo", url);
			hg = new HttpGet(url);
			HttpResponse hr =hc.execute(hg);
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
	
	public boolean parsexml(String xml) {
		if (xml != null && !"".equals(xml)) {
			SqliteUtils su=new SqliteUtils(context);
			su.init();
			SQLiteDatabase db=su.getWritableDatabase();
			db.beginTransaction();
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo","xml- "+ xml);
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					if ("ret".equals(ele.getName())) {
						return false;
					}else if("Category".equals(ele.getName())){
						String gid=ele.attributeValue("id");
						String name=ele.attributeValue("name");
						Iterator<Element> files=ele.elementIterator();
						while(files!=null&&files.hasNext()){
							Element file=files.next();
							String tvid=file.elementTextTrim("id");
							String tname=file.elementTextTrim("name");
							String httpurl=file.elementTextTrim("httpurl");
							String hotlink=file.elementTextTrim("hotlink");
							String isflag=file.elementTextTrim("isflag");
							String logo=file.elementTextTrim("logo");
							su.saveiptvgroup(db,gid, name, tvid);
							Log.i("tvinfo", gid+"-"+name+"-"+tvid);
							su.savetvdate(db,tvid, tname, httpurl, hotlink, isflag,logo);
						}
					}
				}
				db.setTransactionSuccessful();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}finally{
				db.endTransaction();
				db.close();
			}
		}
		return false;
	}

	public List<TvInfo> gettvList(String url) {
		List<TvInfo> tvlist = null;
		String xml = doget(url);
		if (xml != null && !"".equals(xml)) {
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo", xml);
				tvlist = new ArrayList<TvInfo>();
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					Log.i("tvinfo", ele.getName());
					if("file".equals(ele.getName())){
						TvInfo info = new TvInfo();
						Log.i("tvinfo", ele.asXML());
						info.setId(ele.elementTextTrim("id"));
						info.setName(ele.elementTextTrim("name"));
						info.setHttpurl(ele.elementTextTrim("httpurl"));
						info.setHotlink(ele.elementTextTrim("hotlink"));
						info.setFlag(ele.elementTextTrim("isflag"));
						tvlist.add(info);
						Log.i("tvinfo", tvlist.size() + "");
					}
					
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("tvinfo", e.getMessage());
			}
		}
		return tvlist;
	}

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
					}else if("rq".equals(ele.getName())){
						BackData.currtime=ele.getText().trim();
					}else if("day".equals(ele.getName())){
						BackData.cday=Integer.parseInt(ele.getText().trim());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filmlist;
	}
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
				Element eitem=root.element("item");
				Iterator<Element> ite = eitem.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
						PrgItem item = new PrgItem();
						Log.i("tvinfo", ele.asXML());
						if(ele!=null&&"prgItem".equals(ele.getName())){
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
	public String getUserInfo(String xml){
		String yxsj="";
		if (xml != null && !"".equals(xml)) {
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo", xml);
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				yxsj=root.elementText("yxsj");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return yxsj;
	}
	public void close() {
		try {
			Log.i("tvinfo", "isAborted _" + hg.isAborted() + "");
			if (hg != null && !hg.isAborted()) {
				hg.abort();
			}
			if(hc!=null){
				hc.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Appinfo getremoteapp(String url){
		Appinfo info=new Appinfo();
		String xml=doget(HttpUtils.baseurl+url);
		Log.i("tvinfo", xml+"");
		if(xml!=null&&!"".equals(xml)){
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
}

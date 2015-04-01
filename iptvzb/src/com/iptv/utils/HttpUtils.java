package com.iptv.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
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

import android.util.Base64;
import android.util.Log;

public class HttpUtils {

	// public static String baseurl="http://hq2014.gnway.net:3838/";
	//public static String baseurl="http://10.0.2.2:8080/dnetNew/";
	//public static String baseurl = "http://iptv.xsj7188.com:3838/";//新视觉
	public static String baseurl = "http://iptv.dlerbh.com:3889/";//高清电视
	//public static String baseurl = "http://iptv.ovpbox.com:8889/";//德宝电视
	

	private HttpGet hg = null;
	private HttpClient hc;

	public void getclent() {
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 15000);
		HttpConnectionParams.setSoTimeout(params, 50000);
		HttpConnectionParams.setLinger(params, 60);
		hc = new DefaultHttpClient(params);
	}
	public void getclent(int timeout,int sotime) {
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
			getclent();
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
	public String doget(String url,int timeout,int sotime) {
		try {
			Log.i("tvinfo", url);
			hg = new HttpGet(url);
			getclent(timeout,sotime);
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

	public User parsexml(String xml) {
		User user = new User();
		List<LiveTV> livetvlist = new ArrayList<LiveTV>();
		user.setLivetvlist(livetvlist);
		if (xml != null && !"".equals(xml)) {
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					if ("ret".equals(ele.getName())) {
						user.setRet(ele.getTextTrim());
					}
					if (ele.getName().startsWith("liveTV")) {
						LiveTV tv = new LiveTV();
						tv.setName(ele.elementTextTrim("name"));
						tv.setUrl(ele.elementTextTrim("url"));
						livetvlist.add(tv);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
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

	public boolean sendrequest(String url) {
		try {
			hg = new HttpGet(url);
			getclent(2000,3000);
			HttpResponse hr = hc.execute(hg);
			int code = hr.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				Header[] headers = hr.getAllHeaders();
				for (Header her : headers) {
					Log.i("tvinfo", her.getName() + "--" + her.getValue());
				}
				InputStream is = hr.getEntity().getContent();
				int rd = is.read();
				Log.i("tvinfo", rd + "--");
				is.close();
				if (rd > 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public void close() {
		try {
			Log.i("tvinfo", "isAborted" + hg.isAborted() + "");
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

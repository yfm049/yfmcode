package com.iptv.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
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
import com.iptv.pojo.Logoinfo;
import com.iptv.pojo.Notice;
import com.iptv.pojo.PrgItem;
import com.iptv.pojo.TvInfo;

public class HttpUtils {

	// public static String baseurl="http://hq2014.gnway.net:3838/";
	//public static String baseurl="http://10.0.2.2:8080/dnetNew/";
	//public static String baseurl="http://10.120.148.53:8080/dnetNew/";
	//public static String baseurl="http://iptv.dlerbh.com:3889/";//高清电视
	//public static String baseurl = "http://iptv.xsj7188.com:5858/";//高清新视觉
	//public static String baseurl = "http://iptv.dlerbh.com:3889/";//高清电视
	//public static String baseurl = "http://iptv.ovpbox.com:8889/";//德宝电视
//	public static String baseurl = "http://iptv.xsj7188.com:88/";//高清新视觉
	public static String baseurl = "http://cer.seasoniptv.com:88/";//高清新视觉
//	public static String baseurl = "http://cer.dm248.com:88/";//互联网直播
	
	
	

	private HttpGet hg = null;
	private Context context;
	private static HttpClient httpClient;
	private static ThreadSafeClientConnManager conManager;
	public HttpUtils(){
		
	}
	public HttpUtils(Context context){
		this.context=context;
	}
	public static synchronized HttpClient getHttpClient(){
        if(null == httpClient){
            HttpParams params = new BasicHttpParams();
            
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            ConnManagerParams.setTimeout(params, 10000);
            HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);
            
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 80));
            
            conManager = new ThreadSafeClientConnManager(params, schReg);
            httpClient = new DefaultHttpClient(conManager, params);
        }
        
        return httpClient;
    }
	public String doget(String url) {
		try {
			Log.i("tvinfo", url);
			hg = new HttpGet(url);
			HttpResponse hr =getHttpClient().execute(hg);
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
	public void download(){
		
	}
	public int parsexml(String xml) {
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
						String ret=ele.getStringValue();
						Log.i("ret", ret+"---");
						return Integer.parseInt(ret);
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
							String epg=file.elementTextTrim("epg");
							String logo=file.elementTextTrim("logo");
							String mode=file.elementTextTrim("mode");
							su.saveiptvgroup(db,gid, name, tvid);
							Log.i("tvinfo", gid+"-"+name+"-"+tvid+" "+logo);
							su.savetvdate(db,tvid, tname, httpurl, hotlink, isflag,epg,logo,mode);
						}
					}
				}
				db.setTransactionSuccessful();
				return -2;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}finally{
				db.endTransaction();
				db.close();
			}
		}
		return -1;
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
	
	public List<Notice> ParseNotice(String xml) {
		List<Notice> prglist = null;
		if (xml != null && !"".equals(xml)) {
			try {
				prglist = new ArrayList<Notice>();
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator("programme");
				while (ite.hasNext()) {
					Element ele = ite.next();
						Notice item = new Notice();
						Log.i("tvinfo", ele.asXML() + " tvinfo");
						if(ele!=null&&"programme".equals(ele.getName())){
							item.setName(ele.elementTextTrim("title"));
							item.setData(ele.elementTextTrim("data"));
							item.setStart(ele.elementTextTrim("start"));
							item.setEnd(ele.elementText("stop"));
							prglist.add(item);
							Log.i("tvinfo", prglist.size() + " tvinfo");
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
	public String getlink(String xml){
		String link="";
		if (xml != null && !"".equals(xml)) {
			try {
				xml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
				Log.i("tvinfo", xml);
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				link=root.elementText("link");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return link;
	}
	

	public void close() {
		try {
			Log.i("tvinfo", "isAborted _" + hg.isAborted() + "");
			if (hg != null && !hg.isAborted()) {
				hg.abort();
			}
			if(conManager!=null){
				conManager.closeExpiredConnections();
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
	public Logoinfo getremotelogo(String url){
		Logoinfo info=new Logoinfo();
		String xml=doget(HttpUtils.baseurl+url);
		Log.i("tvinfo", xml+"");
		if(xml!=null&&!"".equals(xml)){
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				info.setVercode(Integer.parseInt(root.elementText("version")));
				info.setUrl(root.elementText("url"));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return info;
	}
}

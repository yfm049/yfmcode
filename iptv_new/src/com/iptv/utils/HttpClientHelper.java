package com.iptv.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.iptv.pojo.Notice;

public class HttpClientHelper {

	public static String baseurl = "http://iptv.xsj7188.com:88/";// 高清新视觉
	private static HttpClient httpClient;
	
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
            
            ThreadSafeClientConnManager conManager = new ThreadSafeClientConnManager(params, schReg);
            
            httpClient = new DefaultHttpClient(conManager, params);
        }
        
        return httpClient;
    }
	public static String DoGet(String url){
		HttpGet hg=null;
		String con=null;
		try {
			hg=new HttpGet(url);
			hg.addHeader("Connection", "close");
			HttpResponse  hr=getHttpClient().execute(hg);
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity  entity=hr.getEntity();
				con=EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		}
		return con;
	}
	//获取节目预告信息
	public static List<Notice> ParseNoticeXml(String xml){
		List<Notice> ln=new ArrayList<Notice>();
		if(xml!=null){
			
			try {
				Document doc = DocumentHelper.parseText(xml.trim());
				Log.i("info", xml+"--");
				Element root = doc.getRootElement();
				List<Element> liste=root.elements();
				Log.i("info", liste.size()+"--"+root.getName());
				for(Element et:liste){
					Log.i("info", et.getName());
					if("programme".equals(et.getName())){
							String rq=et.elementTextTrim("date");
							String start=et.elementTextTrim("start");
							String stop=et.elementTextTrim("stop");
							String title=et.elementTextTrim("title");
							Notice notice=new Notice();
							notice.setRiqi(rq);
							notice.setTime(start+"-"+stop);
							notice.setName(title);
							ln.add(notice);
							Log.i("info", title+"--"+ln.size());
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ln;
	}
	public static Bitmap CreateBitmapFromUrl(String url){
		Bitmap bitmap=null;
		HttpGet hg=null;
		InputStream in=null;
		try {
			hg=new HttpGet(url);
			//hg.addHeader("Connection", "close");
			HttpResponse  hr=getHttpClient().execute(hg);
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity  entity=hr.getEntity();
				in=entity.getContent();
				bitmap=BitmapFactory.decodeStream(in);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeCon(hg,in);
		}
		return bitmap;
	}
	public static void closeCon(HttpGet hg,InputStream in){
		if(hg!=null){
			hg.abort();
		}
		if(httpClient!=null){
			httpClient.getConnectionManager().closeIdleConnections(0, TimeUnit.SECONDS);
		}
	}
	
	
}

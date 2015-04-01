package com.iptv.utils;

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

public class HttpClientHelper {

	public static String baseurl = "";
	private static HttpClient httpClient;
	private static ThreadSafeClientConnManager conManager;
	
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
	public static String DoGet(String url){
		HttpGet hg=null;
		String con=null;
		try {
			LogUtils.write("tvinfo", url);
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
			hg.abort();
			conManager.closeExpiredConnections();
		}
		return con;
	}
	
	
	
}

package com.yfm.http;

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

import android.util.Log;

public class HttpUtils {


	public static String baseurl="http://yuaz.vxg202.10000net.cn/mrtpweb/";
	private HttpGet hg = null;
	private static HttpClient httpClient;
	private static ThreadSafeClientConnManager conManager;

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
			hg.abort();
		}
		return null;
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
	
}

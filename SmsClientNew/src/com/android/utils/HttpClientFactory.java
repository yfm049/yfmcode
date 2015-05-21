package com.android.utils;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpClientFactory {
	private static DefaultHttpClient httpClient;
	public static final String httpurl="http://yjgb.vxg201.10000net.cn/";
	//public static final String httpurl="http://10.129.213.18:8080/SmsServer/";
	public static DefaultHttpClient getHttpClient() {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setMaxTotalConnections(params, 200);
			ConnManagerParams.setTimeout(params, 60000 * 3);
			ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
			HttpConnectionParams.setConnectionTimeout(params, 60000 * 2);
			HttpConnectionParams.setSoTimeout(params, 60000 * 3);
			HttpHost localhost = new HttpHost("locahost", 80);
			connPerRoute.setMaxForRoute(new HttpRoute(localhost), 100);
			ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager cm = new ThreadSafeClientConnManager(
					params, schemeRegistry);
			httpClient = new DefaultHttpClient(cm, params);
		}
		return httpClient;

	}
	
	public static JSONObject postData(String url,HttpEntity entity){
		HttpPost hp = null;
		HttpResponse hr;
		JSONObject jo=null;
		try {
			LogUtils.write("smsclient",url);
			hp=new HttpPost(url);
			hp.setEntity(entity);
			hr=getHttpClient().execute(hp);
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String result=EntityUtils.toString(hr.getEntity(),"UTF-8");
				LogUtils.write("smsclient",result+"");
				jo=new JSONObject(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally{
			if(hp!=null){
				hp.abort();
			}
			
		}
		return jo;
	}
	public static JSONObject postData(String url,List<NameValuePair> lnvp){
		HttpPost hp = null;
		HttpResponse hr;
		JSONObject jo=null;
		try {
			LogUtils.write("smsclient",url);
			hp=new HttpPost(url);
			hp.setEntity(new UrlEncodedFormEntity(lnvp, "UTF-8"));
			hr=getHttpClient().execute(hp);
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String result=EntityUtils.toString(hr.getEntity(),"UTF-8");
				LogUtils.write("smsclient",result+"");
				jo=new JSONObject(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally{
			if(hp!=null){
				hp.abort();
			}
			
		}
		return jo;
	}
}

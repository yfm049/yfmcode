package android.lang;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
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
import org.apache.http.util.EntityUtils;

import android.app.Dctivity;
import android.util.Log;

public class HttpClient {

	private static final String CHARSET = "GBK";
	private static DefaultHttpClient customerHttpClient;
	
	

	public static synchronized DefaultHttpClient getHttpClient() {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			ConnManagerParams.setTimeout(params, 10000);
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 20000);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}

	public static String post(String url,List<NameValuePair> formparams) {
		HttpPost request = null;
		try {
			LogUtils.write("Send", "发送数据到服务器");
			request = new HttpPost(url);
			if(formparams!=null){
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,CHARSET);
				request.setEntity(entity);
			}
			DefaultHttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				long m=resEntity.getContentLength();
				if(m>0){
					return "true";
				}
				LogUtils.write("Send", "发送数据成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			request.abort();
			getHttpClient().getConnectionManager().closeExpiredConnections();
		}
		return null;

	}
	public static String get(String url) {
		HttpGet request = null;
		try {
			LogUtils.write("Send", "发送数据到服务器");
			request = new HttpGet(url);
			DefaultHttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				return EntityUtils.toString(resEntity, CHARSET);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			request.abort();
			getHttpClient().getConnectionManager().closeExpiredConnections();
		}
		return null;

	}
}

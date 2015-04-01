package shidian.tv.sntv.net;

import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import shidian.tv.sntv.tools.EncoderUitl;
import shidian.tv.sntv.tools.Utils;
import android.content.Context;

public class HttpUtils {

	private static String baseurl = "http://shijiazhuangtv.sinaapp.com/capi100";
	private static HttpClient customHttpClient;

	public static synchronized HttpClient getHttpClient() {
		if (customHttpClient == null) {
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setTimeout(params, 3000);
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 10000);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			customHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customHttpClient;
	}

	public String dopost(Context paramContext, String url,
			JSONObject jsonparam, String url2) {
		HttpPost hp = null;
		HttpEntity hrentity = null;
		try {
			hp = new HttpPost(baseurl + url);
			String param = postArgSet(paramContext, jsonparam);
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("msg",
					new StringBody(param, Charset.forName("UTF-8")));
			hp.setEntity(entity);
			HttpResponse hr = getHttpClient().execute(hp);
			if (hr.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				hp.abort();
				return null;

			} else {
				hrentity = hr.getEntity();
				String result = EntityUtils.toString(hrentity, "UTF-8");
				String ret = EncoderUitl.DESdecryptServerRetData(result, url2);
				return ret;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hp.abort();
		} finally {
			try {
				if (hrentity != null) {
					hrentity.consumeContent();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public String postArgSet(Context paramContext, JSONObject jsonparam) {
		try {
			Iterator<String> keys = jsonparam.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				jsonparam.put(key,
						EncoderArgs.EncoderArg(jsonparam.getString(key)));
			}
			jsonparam.put(
					"md5",
					Utils.MD5(jsonparam.getString("uid")
							+ jsonparam.getString("uuid")
							+ jsonparam.getString("time")
							+ jsonparam.getString("v")
							+ jsonparam.getString("skey")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return EncoderUitl.DESencoder(jsonparam.toString());
	}

	public static void closeHttpclient() {
		try {
			if (customHttpClient != null) {
				customHttpClient.getConnectionManager().shutdown();
				customHttpClient = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

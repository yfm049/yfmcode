package com.preview.utils;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.app.Dialog;
import android.util.Log;

public class HttpDao {
	public static String httpurl="http://192.168.0.2:8080/CameraServer/";
	private static final int REQUEST_TIMEOUT = 10 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	private static DefaultHttpClient hc;
	private static BasicHttpParams httpParams;

	static {
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		
	}

	public static String GetJsonData(String url, Dialog dialog) {

		Log.i("url", url);
		try {
			hc = new DefaultHttpClient(httpParams);
			HttpGet hg = new HttpGet(httpurl+url);
			HttpResponse hr = hc.execute(hg);
			Log.i("code", hr.getStatusLine().getStatusCode() + "");
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String con = EntityUtils.toString(hr.getEntity(), "UTF-8");
				Log.i("json", con);
				return con;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dialog != null) {
				dialog.cancel();
				dialog = null;
			}
		}

		return null;
	}

	public static String PostData(String url, List<NameValuePair> np,
			Dialog dialog) {
		Log.i("url", url);
		try {
			hc = new DefaultHttpClient(httpParams);
			HttpPost hg = new HttpPost(httpurl+url);
			if (np != null && np.size() > 0) {
				hg.setEntity(new UrlEncodedFormEntity(np, "UTF-8"));
			}

			HttpResponse hr = hc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String con = EntityUtils.toString(hr.getEntity(), "UTF-8");
				Log.i("json", con);
				return con;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dialog != null) {
				dialog.cancel();
				dialog = null;
			}
		}

		return null;
	}


	public static String GetJsonDatas(String url, Dialog dialog) {
		Log.i("url", url);
		try {
			hc = new DefaultHttpClient(httpParams);
			HttpGet hg = new HttpGet(httpurl+url);
			HttpResponse hr = hc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String con = EntityUtils.toString(hr.getEntity(), "UTF-8");
				Log.i("json", con);
				return con;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dialog != null) {
				dialog.cancel();
				dialog = null;
			}
		}

		return null;
	}
	public static String uploadImage(byte[] data,String code,String mode,String type,String jqmcode,String url, Dialog dialog){
		try {
			hc=new DefaultHttpClient(httpParams);
			HttpPost hp=new HttpPost(httpurl+url);
			MultipartEntity me=new MultipartEntity();
			me.addPart("type", new StringBody(type));
			me.addPart("mode", new StringBody(mode));
			me.addPart("code", new StringBody(code));
			me.addPart("jqmcode", new StringBody(jqmcode));
			me.addPart("imgfile", new ByteArrayBody(data, code+".jpg"));
			hp.setEntity(me);
			HttpResponse hr=hc.execute(hp);
			if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String con = EntityUtils.toString(hr.getEntity(), "UTF-8");
				Log.i("json", con);
				return con;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dialog != null) {
				dialog.cancel();
				dialog = null;
			}
		}
		return null;
	}
}

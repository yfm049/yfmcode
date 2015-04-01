package com.android.thread;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.utils.HttpClientFactory;

public class RequestThread extends Thread {

	private List<NameValuePair> lnvp;
	private Handler handler;
	private String url;
	private int what=1;
	public RequestThread(String url,List<NameValuePair> lnvp,Handler handler){
		this.lnvp=lnvp;
		this.handler=handler;
		this.url=url;
	}
	public RequestThread(String url,List<NameValuePair> lnvp,Handler handler,int what){
		this.lnvp=lnvp;
		this.handler=handler;
		this.url=url;
		this.what=what;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Log.d("smsclient", "data:" + lnvp);
		JSONObject result=HttpClientFactory.postData(HttpClientFactory.httpurl+url, lnvp);
		if(result!=null){
			Log.d("smsclient", "data:" + result);
		}else{
			Log.d("smsclient", "data: Õ¯¬Á«Î«Û ß∞‹");
		}
		Message msg=handler.obtainMessage(what, result);
		handler.sendMessage(msg);
	}

}

package com.android.thread;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.utils.HttpClientFactory;
import com.android.utils.LogUtils;

public class RequestThread extends Thread {

	private HttpEntity entity;
	private Handler handler;
	private String url;
	private int what=1;
	private int arg=1;
	
	public RequestThread(String url,List<NameValuePair> lnvp,Handler handler){
		this.handler=handler;
		this.url=url;
		valueToEntity(lnvp);
	}
	public RequestThread(String url,List<NameValuePair> lnvp,Handler handler,int what,int arg){
		this.handler=handler;
		this.url=url;
		this.what=what;
		this.arg=arg;
		valueToEntity(lnvp);
	}
	public RequestThread(String url,HttpEntity entity,Handler handler){
		this.handler=handler;
		this.url=url;
		this.entity=entity;
	}
	public RequestThread(String url,HttpEntity entity,Handler handler,int what,int arg){
		this.handler=handler;
		this.url=url;
		this.entity=entity;
		this.what=what;
		this.arg=arg;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		LogUtils.write("smsclient", "开始请求服务器");
		JSONObject result=HttpClientFactory.postData(HttpClientFactory.httpurl+url, entity);
		LogUtils.write("smsclient", "请求服务器结束");
		if(result!=null){
			LogUtils.write("smsclient", "data:" + result);
		}else{
			LogUtils.write("smsclient", "data: 网络请求失败");
		}
		Message msg=handler.obtainMessage();
		msg.what=what;
		msg.arg1=arg;
		msg.obj=result;
		handler.sendMessage(msg);
	}
	public void valueToEntity(List<NameValuePair> lnvp){
		try {
			Log.d("smsclient", "data:" + lnvp);
			entity=new UrlEncodedFormEntity(lnvp, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setWhat(int what) {
		this.what = what;
	}
	public void setArg(int arg) {
		this.arg = arg;
	}

}

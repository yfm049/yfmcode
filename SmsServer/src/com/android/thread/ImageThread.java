package com.android.thread;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.utils.HttpClientFactory;

public class ImageThread extends Thread {

	private Handler handler;
	private String url;
	private int what=1;
	public ImageThread(String url,Handler handler){
		this.handler=handler;
		this.url=url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Bitmap result=HttpClientFactory.GetBitMap(HttpClientFactory.httpurl+url);
		if(result!=null){
			Log.d("smsclient", "data:" + result);
		}else{
			Log.d("smsclient", "data: ÍøÂçÇëÇóÊ§°Ü");
		}
		Message msg=handler.obtainMessage(what, result);
		handler.sendMessage(msg);
	}

}

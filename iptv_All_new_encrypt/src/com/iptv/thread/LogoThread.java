package com.iptv.thread;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.iptv.utils.DomUtils;
import com.iptv.utils.HttpClientHelper;

public class LogoThread extends Thread {

	public static int logosuc=33,logofail=34;
	private Handler handler;
	private static Bitmap bitmap=null;
	public LogoThread(Handler handler){
		this.handler=handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String xml=HttpClientHelper.DoGet(HttpClientHelper.baseurl+"/icon.xml");
		if(xml!=null){
			String logo=DomUtils.ParseLogoXml(xml);
			if(logo!=null){
				if(bitmap==null){
					try {
						URL  url = new URL(logo);
						HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
						conn.setDoInput(true);
						conn.connect(); 
						InputStream inputStream=conn.getInputStream();
						bitmap = BitmapFactory.decodeStream(inputStream);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
                Message msg=handler.obtainMessage();
                if(bitmap!=null){
                	msg.what=logosuc;
                	msg.obj=bitmap;
                }else{
                	msg.what=logofail;
                }
                handler.sendMessage(msg);
			}
			
			
			
			
		}
		
		
	}

}

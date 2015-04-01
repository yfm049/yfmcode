package com.pro.task;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.pro.net.SocketClient;
import com.pro.utils.ComUtils;

public class CallThread extends Thread {

	
	private Context context;
	private String ip,pass,desknum;
	private int port;
	private Handler handler;
	private SharedPreferences sp;
	public CallThread(Context context,Handler handler,String ip,int port,String pass,String desknum){
		this.context=context;
		this.handler=handler;
		this.ip=ip;
		this.port=port;
		this.pass=pass;
		this.desknum=desknum;
		sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			SocketClient sc=new SocketClient(context,ip,port);
			Document calldoc=ComUtils.getDocument(context, "call.xml");
			Node password=calldoc.selectSingleNode("/Root/Body/Password");
			password.setText(pass);
			Node DeskId=calldoc.selectSingleNode("/Root/Body/DeskId");
			DeskId.setText(desknum);
			String result=sc.SendMsg(calldoc.asXML());
			parseResult(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg=handler.obtainMessage(100, "请求服务器超时");
			handler.sendMessage(msg);
		}
	}
	public void parseResult(String result){
		if(result!=null){
			try {
				Document res=DocumentHelper.parseText(result);
				Node text=res.selectSingleNode("/Root/Head/Text");
				Message msg=handler.obtainMessage(100, text);
				handler.sendMessage(msg);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Message msg=handler.obtainMessage(100, "请求超时");
			handler.sendMessage(msg);
		}
	}
}

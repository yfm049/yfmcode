package com.pro.task;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;

import com.pro.net.SocketClient;
import com.pro.utils.ComUtils;

public class LoginThread extends Thread {

	
	private Context context;
	private String ip,pass;
	private int port;
	private Handler handler;
	private SharedPreferences sp;
	public LoginThread(Context context,Handler handler,String ip,int port,String pass){
		this.context=context;
		this.handler=handler;
		this.ip=ip;
		this.port=port;
		this.pass=pass;
		sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			SocketClient sc=new SocketClient(context,ip,port);
			Document logindoc=ComUtils.getDocument(context, "login.xml");
			Node password=logindoc.selectSingleNode("/Root/Body/Password");
			password.setText(pass);
			String result=sc.SendMsg(logindoc.asXML());
			parseResult(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void parseResult(String result){
		try {
			Document doc=DocumentHelper.parseText(result);
			Node code=doc.selectSingleNode("/Root/Head/Code");
			Node Text=doc.selectSingleNode("/Root/Head/Text");
			if("0".equals(code.getText())){
				Editor e=sp.edit();
				e.putString("ip", ip);
				e.putInt("port", port);
				e.putString("pass", pass);
				e.commit();
				Message msg=handler.obtainMessage(200);
				handler.sendMessage(msg);
			}else{
				Message msg=handler.obtainMessage(100,Text.getText());
				handler.sendMessage(msg);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

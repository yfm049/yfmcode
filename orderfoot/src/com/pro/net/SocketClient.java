package com.pro.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;
import android.content.SharedPreferences;

public class SocketClient {

	private Context context;
	private String name;
	private int port;
	public SocketClient(Context context,String name,int port){
		this.context=context;
		this.name=name;
		this.port=port;
	}
	public SocketClient(Context context){
		this.context=context;
		SharedPreferences sp=this.context.getSharedPreferences("config", Context.MODE_PRIVATE);
		this.name=sp.getString("ip", "10.129.213.18");
		this.port=sp.getInt("port", 12000);
	}
	
	public String SendMsg(String msg){
		StringBuffer sb=new StringBuffer();
		Socket socket=null;
			try {
				socket=new Socket(name, port);
				//socket.setSoTimeout(15000);
				BufferedInputStream in=new BufferedInputStream(socket.getInputStream());
				OutputStream os=new BufferedOutputStream(socket.getOutputStream());
				os.write(msg.getBytes("UTF-8"));
				os.flush();
				ByteArrayOutputStream bos=new ByteArrayOutputStream();
				int len;
				byte[] buf=new byte[1024];
				while((len=in.read(buf))!=-1){
					bos.write(buf, 0, len);
					System.out.println(len+"--");
				}
				sb.append(new String(bos.toByteArray(),"UTF-8"));
				os.close();
				in.close();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return sb.toString();
	}
	
	
	public byte[] GetImageByte(String msg){
		Socket socket=null;
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
			try {
				socket=new Socket(name, port);
				socket.setSoTimeout(15000);
				InputStream in=socket.getInputStream();
				OutputStream os=socket.getOutputStream();
				os.write(msg.getBytes("UTF-8"));
				os.flush();
				
				int len;
				byte[] buf=new byte[2048];
				while((len=in.read(buf))!=-1){
					bos.write(buf, 0, len);
					System.out.println(len+"--");
				}
				os.close();
				in.close();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				socket=null;
			}finally{
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return bos.toByteArray();
	}
}

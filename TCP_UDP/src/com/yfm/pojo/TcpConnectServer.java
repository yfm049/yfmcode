package com.yfm.pojo;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.yfm.pro.Utils;

public class TcpConnectServer {

	private int imgid;
	private String ip;
	private int port;
	private String state;
	private Handler handler;
	private IoSession iosession;
	private String msg="";
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public IoSession getIosession() {
		return iosession;
	}


	public void setIosession(IoSession iosession) {
		this.iosession = iosession;
		InetSocketAddress  socket=(InetSocketAddress)iosession.getRemoteAddress();
		this.setIp(socket.getAddress().getHostAddress());
		this.setPort(socket.getPort());
		iscon=true;
	}

	private boolean iscon = false;
	private StringBuffer sb = new StringBuffer();
	public StringBuffer getSb() {
		return sb;
	}
	
	private TextView showtext;
	private String showtype = "文本";
	private String sendtype = "文本";
	private String fktype = "手动";
	private boolean huanhang = false;

	public TcpConnectServer(Handler handler) {
		this.handler = handler;
	}


	public void closecon() {
		if (iosession != null) {
			iosession.close(true);
			iscon = false;
		}
	}

	public int getImgid() {
		return imgid;
	}

	public void setShowtext(TextView showtext) {
		this.showtext = showtext;
	}

	public boolean isIscon() {
		return iscon;
	}

	public void setIscon(boolean iscon) {
		this.iscon = iscon;
	}


	public boolean isHuanhang() {
		return huanhang;
	}

	public void setHuanhang(boolean huanhang) {
		this.huanhang = huanhang;
	}


	public void setImgid(int imgid) {
		this.imgid = imgid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getShowtype() {
		return showtype;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	public String getSendtype() {
		return sendtype;
	}

	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public TextView getShowtext() {
		return showtext;
	}

	public String getFktype() {
		return fktype;
	}

	public void setFktype(String fktype) {
		this.fktype = fktype;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public void SendMsg() {
		new SendThread().start();
	}
	public void SendMsg(String mg) {
		new SendThread(mg).start();
	}

	class SendThread extends Thread {
		private String ng=null;
		public SendThread(){
			
		}
		public SendThread(String ng){
			this.ng=ng;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
				try {
					if (iosession != null) {
						byte[] p;
						if(ng!=null){
							if("十六进制".equals(sendtype)){
								p = Utils.getByteArray(ng);
							}else{
								p=ng.getBytes("GBK");
							}
						}else{
							if("十六进制".equals(sendtype)){
								p = Utils.getByteArray(msg);
							}else{
								p=msg.getBytes("GBK");
							}
						}
						
						IoBuffer ib=IoBuffer.wrap(p,0,p.length);
						Log.i("msg", "send"+p.length+"--"+ib.array().length);
						WriteFuture future = iosession.write(ib);;
						future.awaitUninterruptibly();
						if(ng!=null&&!"透明".equals(fktype)){
							Message mg=handler.obtainMessage();
							mg.what=4;
							mg.obj="发送成功";
							handler.sendMessage(mg);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message mg = handler.obtainMessage();
					mg.what = 4;
					mg.obj = "发送框输入数据格式错误 十六进制模式请使用空格隔开！";
					handler.sendMessage(mg);
				}
		}

	}


}

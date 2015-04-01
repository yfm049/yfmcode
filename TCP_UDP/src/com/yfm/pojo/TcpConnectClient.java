package com.yfm.pojo;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.yfm.pro.MainActivity;
import com.yfm.pro.Utils;

public class TcpConnectClient {

	private int imgid;
	private String ip;
	private int port;
	private ConnecThread thread;
	private String state;
	private NioSocketConnector ioconnector;
	private Handler handler;
	private IoSession iosession;
	private String msg;

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
	}

	private boolean iscon = false;
	private StringBuffer sb = new StringBuffer();

	public StringBuffer getSb() {
		return sb;
	}

	public void setSb(StringBuffer sb) {
		this.sb = sb;
	}

	private IoHandlerAdapter iohandler;
	private TextView showtext;
	private String showtype = "文本";
	private String sendtype = "文本";
	private boolean dingshi = false;

	public boolean isDingshi() {
		return dingshi;
	}

	public void setDingshi(boolean dingshi) {
		this.dingshi = dingshi;
		if (this.dingshi) {
			new dingshiThread().start();
		}
	}

	public long getDtime() {
		return dtime;
	}

	public void setDtime(long dtime) {
		this.dtime = dtime;
	}

	private boolean huanhang = false;
	private long dtime = 200;

	public TcpConnectClient(Handler handler, IoHandlerAdapter iohandler) {
		this.handler = handler;
		ioconnector = new NioSocketConnector();
		ioconnector.setHandler(iohandler);
	}

	public void startcon() {
		thread = new ConnecThread();
		thread.start();
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

	public IoHandlerAdapter getIohandler() {
		return iohandler;
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	class ConnecThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				InetSocketAddress address = new InetSocketAddress(ip, port);
				ConnectFuture future = ioconnector.connect(address);
				future.awaitUninterruptibly();
				iosession = future.getSession();
				iscon = true;
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.arg1 = 0;
				msg.obj = "连接成功";
				handler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.arg1 = -1;
				msg.obj = "连接失败";
				handler.sendMessage(msg);
			}
		}

	}

	public void SendMsg() {
		
		new SendThread().start();
	}

	class SendThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if (iosession != null&&MainActivity.issend) {
				try {
					byte[] p;
					if ("十六进制".equals(sendtype)) {
						p = Utils.getByteArray(msg);
					} else {
						p = msg.getBytes("GBK");
					}
					
					IoBuffer ib=IoBuffer.wrap(p,0,p.length);
					Log.i("msg", "send"+p.length+"--"+ib.array().length);
					WriteFuture future = iosession.write(ib);;
					future.awaitUninterruptibly();
					Message mg = handler.obtainMessage();
					mg.what = 4;
					mg.obj = "发送成功";
					handler.sendMessage(mg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message mg = handler.obtainMessage();
					mg.what = 4;
					mg.obj = "发送框输入数据格式错误 十六进制模式请使用空格隔开！";
					handler.sendMessage(mg);
				}

			}else{
				Message mg = handler.obtainMessage();
				mg.what = 4;
				mg.obj = "未选择连接";
				handler.sendMessage(mg);
			}
		}

	}

	class dingshiThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			while (dingshi&&MainActivity.issend) {
				try {
					if (iosession != null&&MainActivity.issend) {
						byte[] p;
						if ("十六进制".equals(sendtype)) {
							p = Utils.getByteArray(msg);
						} else {
							p = msg.getBytes("GBK");
						}
						IoBuffer ib=IoBuffer.wrap(p,0,p.length);
						Log.i("msg", "send"+p.length+"--"+ib.array().length);
						WriteFuture future = iosession.write(ib);;
						future.awaitUninterruptibly();
					}
					Thread.sleep(dtime);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message mg = handler.obtainMessage();
					mg.what = 4;
					mg.obj = "发送框输入数据格式错误 十六进制模式请使用空格隔开！";
					handler.sendMessage(mg);
					dingshi=false;
				}
			}
		}

	}

}

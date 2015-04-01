package com.iptv.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.iptv.pojo.Channel;
import com.iptv.utils.ComUtils;
import com.iptv.utils.HttpClientHelper;
import com.iptv.utils.LogUtils;
import com.iptv.utils.SqliteUtils;

public class ShouCangThread extends Thread {

	public static int shoucangsuc=420,shoucangfal=421;
	private Handler handler;
	private Channel channel;
	private String name;
	private SqliteUtils su;
	private String action;
	public ShouCangThread(Handler handler,Context context,Channel channel) {
		this.handler = handler;
		this.channel=channel;
		name=ComUtils.getConfig(context, "name", "");
		su=SqliteUtils.getinstance(context);
		if(channel.getIsflag()==0){
			this.action="add";
		}else{
			this.action="delete";
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String url="addshouchang.jsp?active="+ name+ "&name=" + channel.getId()+"&action="+action;
		String xml = HttpClientHelper.DoGet(HttpClientHelper.baseurl+url);
		LogUtils.write("http", xml+"");
		Message msg = handler.obtainMessage();
		msg.obj=url;
		if(xml!=null){
			if(Boolean.parseBoolean(ComUtils.replaceBlank(xml))){
				msg.what = shoucangsuc;
				if(channel.getIsflag()==0){
					channel.setIsflag(1);
					su.updateChannel(channel, 1);
				}else{
					channel.setIsflag(0);
					su.updateChannel(channel, 0);
				}
				
			}else{
				msg.what = shoucangfal;
			}
		}else{
			msg.what = shoucangfal;
		}
		handler.sendMessage(msg);
	}

}

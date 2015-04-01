package com.iptv.thread;

import java.io.File;

import android.os.Handler;
import android.os.Message;

public class AppDownloadThreadListener implements DownloadThreadListener {

	public static int appdownloadsuc=1100,appdownloadfail=1101;
	private Handler handler;
	public AppDownloadThreadListener(Handler handler){
		this.handler=handler;
	}
	@Override
	public void afterPerDown(String uri, long count, long rcount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void downCompleted(String uri, long count, long rcount,
			boolean isdown, File file) {
		// TODO Auto-generated method stub
		if (file != null&&file.length()>0) {
			Message msg = handler.obtainMessage();
			msg.what = appdownloadsuc;
			msg.obj = file;
			handler.sendMessage(msg);

		}else{
			handler.sendEmptyMessage(appdownloadfail);
		}
	}

	@Override
	public void returncode(int statecode) {
		// TODO Auto-generated method stub

	}


}

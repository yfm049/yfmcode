package com.iptv.thread;

import java.io.File;

import android.os.Handler;

import com.iptv.utils.ComUtils;
import com.iptv.utils.LogUtils;

public class LogoDownloadThreadListener implements DownloadThreadListener {

	public static int downloadsuc=116,downloadfail=117;
	private Handler handler;
	public LogoDownloadThreadListener(Handler handler){
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
		ComUtils.unZipLogo(file);
		LogUtils.write("LogoDownloadThreadListener", "logoœ¬‘ÿÕÍ≥…");
		handler.sendEmptyMessage(downloadsuc);
	}

	@Override
	public void returncode(int statecode) {
		// TODO Auto-generated method stub

	}

}

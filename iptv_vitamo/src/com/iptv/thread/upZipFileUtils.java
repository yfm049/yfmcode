package com.iptv.thread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.iptv.pro.UpdateService;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class upZipFileUtils implements DownloadThreadListener {

	public static int zipsuc=1000;
	private Handler handler;
	public upZipFileUtils(Handler handler){
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
		try {
			
			File wjj=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ UpdateService.path);
			if(!wjj.exists()){
				wjj.mkdir();
			}
			
			Log.i("tvinfo", "文件解压");
			byte[] buf=new byte[1024];
			ZipFile zifile=new ZipFile(file);
			Enumeration zList=zifile.entries();
			while(zList.hasMoreElements()){
				ZipEntry ze=(ZipEntry)zList.nextElement();
				String filename=ze.getName();
				Log.i("tvinfo", "解压 "+filename);
				File png=new File(wjj, filename);
				BufferedInputStream fis=new BufferedInputStream(zifile.getInputStream(ze));
				FileOutputStream fos=new FileOutputStream(png);
				int readLen=0;
				while ((readLen=fis.read(buf, 0, 1024))!=-1) {
					fos.write(buf, 0, readLen);
				}
				fis.close();
				fos.close();
			}
			zifile.close();
			handler.sendEmptyMessage(zipsuc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void returncode(int statecode) {
		// TODO Auto-generated method stub
		Log.i("tvinfo", "returncode "+statecode);
	}

}

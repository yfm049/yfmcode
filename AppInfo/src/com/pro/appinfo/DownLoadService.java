package com.pro.appinfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.pro.model.Appinfo;
import com.pro.net.HttpUtils;
import com.pro.utils.Utils;
import com.yfm.download.DownLoadThread;
import com.yfm.download.DownloadThreadListener;
//软件下载服务
public class DownLoadService extends Service {

	public static Map<Appinfo,DownLoadThread> lai=null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lai=new HashMap<Appinfo,DownLoadThread>();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Appinfo info=(Appinfo)intent.getSerializableExtra("appinfo");
		boolean exist=false;
		if(info!=null){
			Set<Appinfo> sai=lai.keySet();
			for(Appinfo app:sai){
				if(app.getFilename().equals(info.getFilename())){
					exist=true;
					Toast.makeText(DownLoadService.this, "下载已存在", Toast.LENGTH_SHORT).show();
					break;
				}
			}
			if(!exist){
				//添加程序下载到下载列表
				Toast.makeText(DownLoadService.this, "开始下载", Toast.LENGTH_SHORT).show();
				File file=new File(Environment.getExternalStorageDirectory(), info.getFilename());
				info.setFile(file);
				if(file.exists()){
					file.delete();
				}
				//开始下载程序
				DownLoadThread dt=new DownLoadThread(HttpUtils.baseuri+"/app/"+info.getFilename(),file,new DownloadThreadListenerImpl(info));
				lai.put(info, dt);
				dt.start();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	//程序下载进度监听
	class DownloadThreadListenerImpl implements DownloadThreadListener{

		private Appinfo appinfo;
		public DownloadThreadListenerImpl(Appinfo appinfo){
			this.appinfo=appinfo;
		}
		//软件下载数据读取进度更新
		@Override
		public void afterPerDown(String url, long totalSize, long updateTotalSize) {
			// TODO Auto-generated method stub
			if((updateTotalSize * 100 / totalSize) - 1 > appinfo.getDownload()){
				appinfo.setDownload(appinfo.getDownload()+1);
				Intent intent=new Intent("com.pro.appinfo.download");
				DownLoadService.this.sendBroadcast(intent);
			}
		}
		//软件下载完成后
		@Override
		public void downCompleted(String arg0, long arg1, long arg2,boolean arg3, File arg4) {
			// TODO Auto-generated method stub
			appinfo.setDownload(100);
			Intent intent=new Intent("com.pro.appinfo.download");
			DownLoadService.this.sendBroadcast(intent);
			new updateThread(appinfo).start();
		}
		//软件下载出现异常后 异常码
		@Override
		public void returncode(int arg0) {
			// TODO Auto-generated method stub
			appinfo.setDownload(-1);
			Intent intent=new Intent("com.pro.appinfo.download");
			DownLoadService.this.sendBroadcast(intent);
		}
		
	}
	//上报软件下载完成
	class updateThread extends Thread{
		private Appinfo appinfo;
		public updateThread(Appinfo appinfo){
			this.appinfo=appinfo;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Utils.updatecount(HttpUtils.baseuri+"/app!updatecount.action", appinfo);
		}
		
	}

}

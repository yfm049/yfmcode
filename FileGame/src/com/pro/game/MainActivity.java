package com.pro.game;

import java.io.File;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.pro.adapter.SimulatorAdapter;
import com.pro.pojo.Simulator;
import com.pro.utils.ConfigUtils;

public class MainActivity extends Activity {

	
	private SimulatorAdapter adapter;
	private ListView simulatorlist;
	private OnItemClickListenerImpl listener;
	private ProgressDialog pd;
	private String dirname="data";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("game", "MainActivitycreate");
		listener=new OnItemClickListenerImpl();
		simulatorlist=(ListView)this.findViewById(R.id.simulatorlist);
		adapter=new SimulatorAdapter(this, GameApp.Simulatorlist);
		simulatorlist.setAdapter(adapter);
		simulatorlist.setOnItemClickListener(listener);
	}

	
	
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			Simulator slr=GameApp.Simulatorlist.get(pos);
			ToFileActivity(slr);
		}
		
	}
	private void ToFileActivity(Simulator Simulator){
		Intent intent=new Intent(this,FileActivity.class);
		intent.putExtra("Simulator", Simulator);
		this.startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_set) {
			pd=new ProgressDialog(this);
			pd.setMessage("正在解压缩");
			pd.show();
			new ConfigThread().start();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				pd.setMessage(msg.obj.toString());
			}
			if(msg.what==0){
				pd.cancel();
				Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
			}
			
		}
		
	};
	class ConfigThread extends Thread{

		@SuppressLint("SdCardPath")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					InputStream in=MainActivity.this.getAssets().open("data.zip");
					File file=new File(Environment.getExternalStorageDirectory(), dirname);
					ConfigUtils.unzip(in, Environment.getExternalStorageDirectory().getAbsolutePath());
					handler.sendMessage(handler.obtainMessage(1, "解压缩完成"));
					if(ConfigUtils.isRooted()){
						ConfigUtils.Terminal("chmod 777 /data");
						ConfigUtils.Terminal("chmod 777 /data/data");
						File list=new File(file, "/data");
						if(list.exists()){
							File[] files=list.listFiles();
							if(files!=null){
								for(File p:files){
									handler.sendMessage(handler.obtainMessage(1, "正在查找"+p.getName()));
									String rs=ConfigUtils.readResult(ConfigUtils.Terminal("ls -l -d /data/data/"+p.getName()));
									if(!"".equals(rs)){
										handler.sendMessage(handler.obtainMessage(1, "正在配置"+p.getName()));
										String dcpath="/data/data/"+p.getName();
										ConfigUtils.Terminal("chmod -R 777 "+dcpath);
										String ycpath="/mnt/sdcard/data/data/"+p.getName()+"/*";
										ConfigUtils.Terminal("cp -R "+ycpath+" "+dcpath);
										String r[]=rs.split("[\\s]+");
										if(r.length>=6){
											ConfigUtils.Terminal("chgrp -R "+r[1]+" "+dcpath);
											ConfigUtils.Terminal("chown -R "+r[2]+" "+dcpath);
										}
										ConfigUtils.Terminal("chmod 751 "+dcpath);
										ConfigUtils.Terminal("chmod 771 "+dcpath+"/shared_prefs");
										ConfigUtils.Terminal("chmod 660 "+dcpath+"/shared_prefs/*");
									}else{
										handler.sendMessage(handler.obtainMessage(1, "没有发现"+p.getName()));
										Thread.sleep(500);
									}
								}
							}
							
						}
						ConfigUtils.Terminal("chmod 771 /data");
						ConfigUtils.Terminal("chmod 771 /data/data");
					}else{
						handler.sendMessage(handler.obtainMessage(0, "你没有root"));
					}
					if(file.exists()){
						handler.sendMessage(handler.obtainMessage(1, "删除临时文件"));
						ConfigUtils.deleteFile(file);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(0, "配置失败"));
			}finally{
				handler.sendMessage(handler.obtainMessage(0, "配置完成"));
			}
		}
		
	}

}

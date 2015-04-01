package com.android.installer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String filename="answer.apk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	public void install(View v){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File anfile=new File(Environment.getExternalStorageDirectory(), filename);
			if(coryfile(anfile)){
				String path=getsystempath();
				if(path!=null){
					rootinstall(path);
				}else{
					Toast.makeText(this, "获取系统路径失败", Toast.LENGTH_LONG).show();
				}
			}else{
				 Toast.makeText(this, "文件拷贝失败", Toast.LENGTH_LONG).show();
			}
			
		}else{
			 Toast.makeText(this, "SD卡不存在", Toast.LENGTH_LONG).show();
		}
	}
	public void uninstall(View v){
		String path=getsystempath();
		if(path!=null){
			rootuninstall(path);
		}
	}
	public boolean coryfile(File anfile){
		try {
			if(!anfile.exists()){
				anfile.createNewFile();
			}
			InputStream in=this.getAssets().open(filename);
			FileOutputStream fos=new FileOutputStream(anfile);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				fos.write(buffer, 0, byteread);
			}
			fos.close();
			in.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public String getsystempath() {
		String path=null;
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("mount");
			int i = process.waitFor();
			InputStream in = process.getInputStream();
			InputStreamReader bis = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(bis);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("system") > 0) {
					path = line.substring(0, line.indexOf("system") + 6);
				}
			}
			br.close();
			bis.close();
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	public void rootinstall(String path) {
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("su");
			OutputStream os = process.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeBytes("mount -o remount,rw -t yaffs2 " + path + " \n");
			dos.flush();
			dos.writeBytes("cat /sdcard/"+filename+" > /system/app/"+filename+ "\n");
			dos.flush();
			dos.writeBytes("chmod 644 /system/app/answer.apk\n");
			dos.flush();
			dos.writeBytes("mount -o remount,ro -t yaffs2 " + path + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			process.waitFor();
			dos.close();
			Toast.makeText(this, "安装成功", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "你的手机还没有root ,安装失败...", Toast.LENGTH_LONG).show();
		}
	}
	public void rootuninstall(String path) {
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("su");
			OutputStream os = process.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeBytes("mount -o remount,rw -t yaffs2 " + path + " \n");
			dos.flush();
			dos.writeBytes("rm -r /system/app/answer.apk"+ "\n");
			dos.flush();
			dos.writeBytes("mount -o remount,ro -t yaffs2 " + path + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			process.waitFor();
			dos.close();
			Toast.makeText(this, "卸载成功", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "你的手机还没有root ,安装失败...", Toast.LENGTH_LONG).show();
		}
	}
	
}

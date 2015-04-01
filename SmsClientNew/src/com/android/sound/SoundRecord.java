package com.android.sound;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

import com.android.db.SqlUtils;
import com.android.pojo.Record;
import com.android.utils.LogUtils;
import com.android.utils.Utils;

public class SoundRecord {
	private MediaRecorder PhoneRecorder;
	private File file;
	private int time;
	private Context context;
	private boolean isrecord=false;
	public boolean isIsrecord() {
		return isrecord;
	}

	public SoundRecord(Context context,int time){
		this.context=context;
		this.time=time;
	}
	
	public String getFileName(){
		if(file.exists()){
			return file.getName();
		}
		return "";
	}
	public void startRecord(){
		File dir=Utils.getFiledir();
		if(dir!=null){
			try {
				isrecord=true;
				LogUtils.write("PhoneRecord", "环境录音开始");
				PhoneRecorder=new MediaRecorder();
				PhoneRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				PhoneRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				PhoneRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				file=new File(dir, Utils.formData(new Date())+"_"+time+"s.mp3");
				PhoneRecorder.setOutputFile(file.getAbsolutePath());
				PhoneRecorder.prepare();
				PhoneRecorder.start();
				hanlder.sendEmptyMessageDelayed(100, time*1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogUtils.write("PhoneRecord", "环境录音出现异常"+e.getMessage());
				e.printStackTrace();
				StopRecord();
				if(file.exists()){
					file.delete();
				}
			}
		}
	}
	
	private Handler hanlder=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==100){
				StopRecord();
				SqlUtils su=SqlUtils.getinstance(context);
				Record record=new Record();
				record.setDatas(Utils.formData(new Date()));
				record.setFilename(getFileName());
				su.saveRecord(record);
			}
		}
		
	};
	
	public void StopRecord(){
		if(PhoneRecorder!=null){
			LogUtils.write("PhoneRecord", "环境录音停止");
			try {
				PhoneRecorder.release();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PhoneRecorder=null;
		}
		isrecord=false;
	}
	
}

package com.android.sound;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.media.MediaRecorder;

import com.android.utils.LogUtils;
import com.android.utils.Utils;

public class PhoneRecord {
	private MediaRecorder PhoneRecorder;
	private File file;
	private Context context;
	public String getFileName(){
		if(file.exists()){
			return file.getName();
		}
		return "";
	}
	public PhoneRecord(Context context){
		this.context=context;
	}
	public void startRecord(String phonenum){
		File dir=Utils.getFiledir();
		boolean bcall=Utils.getBooleanConfig(context, "call", true);
		if(dir!=null&&bcall){
			try {
				PhoneRecorder=new MediaRecorder();
				if("call".equals(Utils.getStringConfig(context, "callrecord", "call"))){
					PhoneRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
				}else{
					PhoneRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				}
				PhoneRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				PhoneRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				file=new File(dir, Utils.formData(new Date())+"_"+phonenum+"record.mp3");
				PhoneRecorder.setOutputFile(file.getAbsolutePath());
				PhoneRecorder.prepare();
				PhoneRecorder.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogUtils.write("PhoneRecord", "通话录音出现异常"+e.getMessage());
				e.printStackTrace();
				try {
					if(PhoneRecorder!=null){
						PhoneRecorder.release();
						PhoneRecorder=null;
					}
					if(file.exists()){
						file.delete();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void StopRecord(){
		if(PhoneRecorder!=null){
			try {
				PhoneRecorder.stop();
				PhoneRecorder.release();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PhoneRecorder=null;
		}
	}
	
}

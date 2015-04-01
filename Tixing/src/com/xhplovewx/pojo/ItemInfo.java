package com.xhplovewx.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xhplovewx.tixing.DialogActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class ItemInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	private int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String title;
	
	private int type;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getFirsttime() {
		return firsttime;
	}
	public void setFirsttime(Date firsttime) {
		this.firsttime = firsttime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	private String content;
	private String content2;
	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	private Date firsttime;
	private Date endtime;
	private int level;
	
	private int isread;

	public int getIsread() {
		return isread;
	}
	public void setIsread(int isread) {
		this.isread = isread;
	}
	
	public boolean isexceed(){
		boolean f=firsttime==null?true:firsttime.before(new Date());
		boolean e=endtime==null?true:endtime.before(new Date());
		if(!f||!e){
			return false;
		}else{
			return true;
		}
	}
	
	private PendingIntent fpi,epi;
	public void startTx(Context context,String action){
		if(firsttime!=null&&firsttime.after(new Date())){
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Intent intent=new Intent(context,DialogActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("item", 1);
			intent.putExtra("iteminfo", this.getId());
			fpi=PendingIntent.getActivity(context, 110, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			am.set(AlarmManager.RTC_WAKEUP, firsttime.getTime(), fpi);
		}
		if(endtime!=null&&endtime.after(new Date())){
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Intent intent=new Intent(context,DialogActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("item", 2);
			intent.putExtra("iteminfo", this.getId());
			epi=PendingIntent.getActivity(context, 109, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			am.set(AlarmManager.RTC_WAKEUP, endtime.getTime(), epi);
		}
		
	}
	
	public void Cancle(Context context){
		if(fpi!=null){
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.cancel(fpi);
			
		}
		if(epi!=null){
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.cancel(epi);
		}
	}

}

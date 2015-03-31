package com.preview.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class ConfigUtils {

	public static void savaDate(Context context,String key,String value){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static String GetString(Context context,String key,String defvalue){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defvalue);
	}
	public static SJinfo isQuJian(Context context,String start,String end,String type){
		SJinfo info=new SJinfo();
		Calendar dq=Calendar.getInstance();
		dq.setTime(new Date());
		Calendar s=Calendar.getInstance();
		s.setTime(new Date());
		Calendar e=Calendar.getInstance();
		e.setTime(new Date());
		String st=ConfigUtils.GetString(context, start, null);
		String ed=ConfigUtils.GetString(context, end, null);
		if(st!=null&&!"".equals(st)&&ed!=null&&!"".equals(ed)){
			String[] t=st.split(":");
			String[] d=ed.split(":");
			s.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
			s.set(Calendar.MINUTE, Integer.parseInt(t[1]));
			s.set(Calendar.SECOND, 0);
			e.set(Calendar.HOUR_OF_DAY, Integer.parseInt(d[0]));
			e.set(Calendar.MINUTE, Integer.parseInt(d[1]));
			e.set(Calendar.SECOND, 0);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Log.i("start", sdf.format(s.getTime()));
			Log.i("end", sdf.format(e.getTime()));
			Log.i("dq", sdf.format(dq.getTime()));
			if(s.getTime().before(dq.getTime())&&e.getTime().after(dq.getTime())){
				info.setIsflag(true);
				info.setType(type);
			}
		}
		return info;
	}
}

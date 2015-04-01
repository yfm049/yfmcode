package com.pro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

public class ComUtils {

	private static String name="config";
	public static int getIntConfig(Context context,String key,int value){
		SharedPreferences sp=context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return sp.getInt(key, value);
	}
	public static void setIntConfig(Context context,String key,int value){
		SharedPreferences sp=context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Editor e=sp.edit();
		e.putInt(key, value);
		e.commit();
	}
	public static String getImei(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	public static void setStringConfig(Context context, String name,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(name, value);
		e.commit();
	}
	
	public static String getStringConfig(Context context, String name,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return sp.getString(name, value);
	}
	
	private static Toast toast;
	public static void showToast(Context context,String msg){
		if(toast==null){
			toast=Toast.makeText(context, msg, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
		}else{
			toast.setText(msg);
		}
		toast.show();
		
	}
}

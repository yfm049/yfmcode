package com.android.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.telephony.SmsMessage;

public class ComUtils {

	public static SimpleDateFormat TimeFormat = new SimpleDateFormat(
			"HH:mm:ss");
	private static String configname = "config";
	private static String[] units = { "B", "KB", "MB", "GB", "TB", "EB", "ZB",
			"YB" };

	public static void setIntConfig(Context context, String name, int value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(name, value);
		e.commit();
	}

	public static void setLongConfig(Context context, String name, long value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putLong(name, value);
		e.commit();
	}

	public static void setFloatConfig(Context context, String name, float value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putFloat(name, value);
		e.commit();
	}

	public static void setStringConfig(Context context, String name,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(name, value);
		e.commit();
	}

	public static int getIntConfig(Context context, String name, int value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		return sp.getInt(name, value);
	}

	public static long getLongConfig(Context context, String name, long value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		return sp.getLong(name, value);
	}

	public static float getFloatConfig(Context context, String name, float value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		return sp.getFloat(name, value);
	}

	public static String getStringConfig(Context context, String name,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(configname,
				Context.MODE_PRIVATE);
		return sp.getString(name, value);
	}

	public static String formatFileSize(long bytes) {
		double db = bytes;
		int level = 0;
		while (db > 1000) {
			db /= 1000;
			level++;
		}
		BigDecimal bg = new BigDecimal(db);
		double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1 + " " + units[level];
	}

	public static final String getMessagesFromIntent(Intent intent) {
		StringBuilder body = new StringBuilder();// ¶ÌÐÅÄÚÈÝ
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object[] _pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] message = new SmsMessage[_pdus.length];
			for (int i = 0; i < _pdus.length; i++) {
				message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
			}
			for (SmsMessage currentMessage : message) {
				body.append(currentMessage.getDisplayMessageBody());
			}
		}
		return body.toString();
	}

	public static List<String> getliuliang(String body) {
		List<String> ls = new ArrayList<String>();
		Pattern pattern = Pattern
				.compile("(\\d{0,2}([\u4e00-\u9fa5]{1,50})\\d+(.\\d+)?M)|(\\d+@163.com)");
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			if (matcher.groupCount() > 0) {
				ls.add(matcher.group(0));
			}
		}
		return ls;
	}
	public static Date getDate(int hour,int minute,int second){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		if(new Date().after(calendar.getTime())){
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return calendar.getTime();
	}
	public static Date getNextDate(int day,int hour,int minute,int second){
		Calendar caleandar=Calendar.getInstance();
		caleandar.setTime(new Date());
		caleandar.add(Calendar.DAY_OF_MONTH, 1);
		caleandar.set(Calendar.HOUR_OF_DAY, hour);
		caleandar.set(Calendar.MINUTE, minute);
		caleandar.set(Calendar.SECOND, 0);
		return caleandar.getTime();
	}
	public static Date getDate(String time){
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		try {
			Calendar temp=Calendar.getInstance();
			temp.setTime(TimeFormat.parse(time));
			calendar.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, temp.get(Calendar.SECOND));
			if(new Date().after(calendar.getTime())){
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar.getTime();
	}
}

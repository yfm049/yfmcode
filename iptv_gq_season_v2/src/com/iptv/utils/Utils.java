package com.iptv.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Utils {

	private static SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	public static String getLocalMacAddressFromIp(Context context) {
		String uniqueId="b3e18e619ffc8689";
		try {
			uniqueId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			Log.d("debug", "uuid=" + uniqueId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueId;
	}

	

	public static String md5(String str, String charset) {
		try {
			byte[] tmpInput = null;
			if (null != str) {
				if (null != charset) {
					tmpInput = str.getBytes(charset);
				} else {
					tmpInput = str.getBytes();
				}
			} else {
				return null;
			}
			MessageDigest alg = MessageDigest.getInstance("MD5"); // or "SHA-1"
			alg.update(tmpInput);
			return byte1hex(alg.digest());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String byte1hex(byte[] inputByte) {
		if (null == inputByte) {
			return null;
		}
		String resultStr = "";
		String tmpStr = "";
		for (int n = 0; n < inputByte.length; n++) {
			tmpStr = (Integer.toHexString(inputByte[n] & 0XFF));
			if (tmpStr.length() == 1) {
				resultStr = resultStr + "0" + tmpStr;
			} else {
				resultStr = resultStr + tmpStr;
			}
		}
		return resultStr.toUpperCase();
	}


	public static int pageleft(int start, int end, int count) {
		int vcount = end - start;
		if ((start-vcount)>0) {
				return (start-vcount-1);
		}else{
			return 0;
		}
	}
	public static int pageright(int start, int end, int count) {
		if (end+1==count) {
				return end;
		}else{
			return end+1;
		}
	}
	public static int getversioncode(Context context){
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public static String longToString(long time){
		
		return sdf.format(time);
	}
	public static void setConfig(Context context,String key,String value){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor  editor=sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static String getConfig(Context context,String key,String def){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, def);
	}
	public static void setConfig(Context context,String key,int value){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor  editor=sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static int getConfig(Context context,String key,int def){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, def);
	}
	public static void setConfig(Context context,String key,boolean value){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor  editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static boolean getConfig(Context context,String key,boolean def){
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, def);
	}
	public static final String[] CaBanner_chinese =
		{
		/* 00 */"E00 当前节目没有加密",
		/* 01 */"E01 请插入CA模块",
		/* 02 */"E02 CA模块EEPROM失败",
		/* 03 */"E03 CA模块失败",
		/* 04 */"E04 请插入智能卡",
		/* 05 */"E05 系统不认识此卡",
		/* 06 */"E06 智能卡失败",
		/* 07 */"E07 正在检测智能卡",
		/* 08 */"E08 CA模块失败",
		/* 09 */"E09 智能卡EEPROM失败",
		/* 10 */"E10 ",
		/* 11 */"E11 机卡不匹配",
		/* 12 */"E12 请与母机同步",
		/* 13 */"E13 没有有效的节目",
		/* 14 */"E14 节目没有授权",
		/* 15 */"E15 授权已收到",
		/* 16 */"E16 当前节目已加密",
		/* 17 */"E17 已购买事件",
		/* 18 */"E18 预览阶段",
		/* 19 */"E19 事件已经购买",
		/* 20 */"E20 无运营商授权",
		/* 21 */"E21 将来不能购买",
		/* 22 */"E22 到达信誉极限",
		/* 23 */"E23 当前节目正在解密",
		/* 24 */"E24 当前节目已加密",
		/* 25 */"E25 智能卡不兼容",
		/* 26 */"E26 系统不认识当前节目",
		/* 27 */"E27 当前节目不在运行",
		/* 28 */"E28 锁定当前节目",
		/* 29 */"E29 解码器内存满",
		/* 30 */"E30 当前节目无效",
		/* 31 */"E31 父母控制锁定",
		/* 32 */"E32 国家代码没有授权",
		/* 33 */"E33 没有事件信息",
		/* 34 */"E34 节目没有授权",
		/* 35 */"E35 没有信号",
	/* 36 */"E36 ", };
}

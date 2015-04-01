package com.iptv.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

public class ComUtils {

	public static String logopath="/iptv/logo";
	public static String currdata="";
	public static int totalday=0;
	private static ApplicationInfo appinfo;
	public static String getLocalMacAddressFromIp(Context context) {
		String uniqueId = "b3e18e619ffc8689";
		try {
			if(!LogUtils.isdebug){
				uniqueId = ""
						+ android.provider.Settings.Secure.getString(
								context.getContentResolver(),
								android.provider.Settings.Secure.ANDROID_ID);
//				Log.d("debug", "uuid=" + uniqueId);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueId;
	}
	public static long getUidRxBytes(Context context){
		long total=0;
		try {
			PackageManager pm=context.getPackageManager();
			appinfo=pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			total=TrafficStats.getUidRxBytes(appinfo.uid)==TrafficStats.UNSUPPORTED?0:(TrafficStats.getTotalRxBytes()/1024);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Builder createBuilder(Context context){
		if(Build.VERSION.SDK_INT>10){
			return new Builder(context, AlertDialog.THEME_HOLO_DARK);
		}else{
			return new Builder(context);
		}
	}
	public static String getrestext(Context context,int id){
		CharSequence cs=context.getResources().getText(id);
		if(cs!=null){
			return cs.toString();
		}else{
			return "";
		}
		
	}
	private static Toast toast;
	public static void showtoast(Context context,String text){
		if(toast==null){
			toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}else{
			toast.setText(text);
		}
		toast.show();
		
	}
	public static void showtoast(Context context,int res){
		if(toast==null){
			toast=Toast.makeText(context, res, Toast.LENGTH_SHORT);
		}else{
			toast.setText(res);
		}
		toast.show();
		
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
		if ((start - vcount) > 0) {
			return (start - vcount - 1);
		} else {
			return 0;
		}
	}

	public static int pageright(int start, int end, int count) {
		if (end + 1 == count) {
			return end;
		} else {
			return end + 1;
		}
	}
	
	
	public static int pageleft(int start,int vcount) {
		if ((start - vcount) > 0) {
			return (start - vcount - 1);
		} else {
			return 0;
		}
	}

	public static int pageright(int end, int count) {
		if (end + 1 == count) {
			return end;
		} else {
			return end + 1;
		}
	}

	public static int getversioncode(Context context) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public static String Base64decode(String xml){
		String dxml=null;
		try {
			if(xml!=null){
				dxml = new String(Base64.decode(xml, Base64.DEFAULT), "UTF-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dxml;
	}

	public static String longToString(long time) {
		int hour = 0;
        int minute = 0;
        int second = 0;
 
        second = (int) (time/1000);
 
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
	}
	private static String getTwoLength(final int data) {
        if(data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }
	public static void unZipLogo(File logo){
		ZipFile zipfile;
		try {
			File folderPath=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+logopath);
			if(!folderPath.exists()){
				folderPath.mkdirs();
			}
			zipfile = new ZipFile(logo);
			Enumeration zList=zipfile.entries();
			ZipEntry ze=null;
			byte[] buf=new byte[1024];
			while(zList.hasMoreElements()){
				ze=(ZipEntry)zList.nextElement();
				OutputStream os=new BufferedOutputStream(new FileOutputStream(new File(folderPath, ze.getName())));
				InputStream is=new BufferedInputStream(zipfile.getInputStream(ze));
				int readLen=0;
				while ((readLen=is.read(buf, 0, 1024))!=-1) {
				os.write(buf, 0, readLen);
				}
				is.close();
				os.close();    
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}

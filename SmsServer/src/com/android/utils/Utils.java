package com.android.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.model.Client;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Utils {

	private static SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static List<Client> clients=new ArrayList<Client>();
	public static Client client;
	
	public static String formData(Date date){
		return smf.format(date);
	}
	public static String GetImei(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	public static ProgressDialog createProgressDialog(Context context){
		ProgressDialog pd=null;
		if(Build.VERSION.SDK_INT>=11){
			pd=new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		}else{
			pd=new ProgressDialog(context);
		}
		return pd;
		
	}
	private static Toast toast;
	public static void ShowToast(Context context,String text){
		if(toast==null){
			toast=Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}
	public static Builder createAlertDialog(Context context){
		Builder pd=null;
		if(Build.VERSION.SDK_INT>=11){
			pd=new Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		}else{
			pd=new Builder(context);
		}
		return pd;
		
	}
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
	public static void setStringConfig(Context context, String key,String value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(key, value);
		e.commit();
	}
	
	public static String getStringConfig(Context context, String key,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return sp.getString(key, value);
	}
	public static String StringEncode(String src){
		String des="";
		try {
			des=Base64.encodeToString(src.getBytes("UTF-8"), Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}
	
	
	
	public static String getContactNameByPhoneNumber(ContentResolver cr,
			String address) {
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };
		if (address != null && address.startsWith("+86")) {
			address = address.substring(3);
		}
		Cursor cursor = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
				ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%"
						+ address + "%'",
				null,
				null);

		if (cursor == null) {
			Log.d("phone", "getPeople null");
			return "";
		}
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);
			return name;
		}
		return "";
	}
}

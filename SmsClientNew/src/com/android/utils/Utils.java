package com.android.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Utils {

	private static String clientdir = "smsclient";
	private static SimpleDateFormat smf = new SimpleDateFormat(
			"yyyy-MM-dd_HH:mm:ss");

	public static String formData(Date date) {
		return smf.format(date);
	}

	public static String GetImei(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static ProgressDialog createProgressDialog(Context context) {
		ProgressDialog pd = null;
		if (Build.VERSION.SDK_INT >= 11) {
			pd = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		} else {
			pd = new ProgressDialog(context);
		}
		return pd;

	}

	private static Toast toast;

	public static void ShowToast(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}

	public static Builder createAlertDialog(Context context) {
		Builder pd = null;
		if (Build.VERSION.SDK_INT >= 11) {
			pd = new Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			pd = new Builder(context);
		}
		return pd;

	}

	private static String name = "config";

	public static int getIntConfig(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return sp.getInt(key, value);
	}

	public static void setIntConfig(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(key, value);
		e.commit();
	}

	public static boolean getBooleanConfig(Context context, String key,
			Boolean value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, value);
	}

	public static void setBooleanConfig(Context context, String key,
			Boolean value) {
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putBoolean(key, value);
		e.commit();
	}

	public static void setStringConfig(Context context, String key, String value) {
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

	public static String StringDecode(String src) {
		String des = "";
		try {
			des = new String(Base64.decode(src, Base64.DEFAULT), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
	}

	public static String getContactNameByPhoneNumber(Context context,
			String address) {
		String name="";
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };
		if (address != null && address.startsWith("+86")) {
			address = address.substring(3);
		}
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection,
				ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%"
						+ address + "%'", null, null);

		if (cursor == null) {
			Log.d("phone", "getPeople null");
			return "";
		}
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameFieldColumnIndex);
		}
		cursor.close();
		return name;
	}

	public static File getFiledir() {
		File file = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			file = new File(Environment.getExternalStorageDirectory(),
					clientdir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return file;
	}

	public static String getnum(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String tel = tm.getLine1Number();
		String imei = tm.getSimSerialNumber();
		if (tel != null && !"".equals(tel)) {
			return tel;
		} else {
			return imei;
		}
	}

	public static String getPublicKey(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES);
			CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(
							info.signatures[0].toByteArray()));
			String key = Base64.encodeToString(
					cert.getIssuerDN().getName().getBytes("UTF-8"),
					Base64.DEFAULT).trim();
			return key;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}

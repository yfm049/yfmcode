package com.androidpro.game;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class Utils {

	public static ProgressDialog CreatePdDialog(Context context) {
		ProgressDialog pd = null;
		if (Build.VERSION.SDK_INT >= 11) {
			pd = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		} else {
			pd = new ProgressDialog(context);
		}
		return pd;
	}

	public static Builder CreateAlertDialog(Context context) {
		Builder pd = null;
		if (Build.VERSION.SDK_INT >= 11) {
			pd = new Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			pd = new Builder(context);
		}
		return pd;
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

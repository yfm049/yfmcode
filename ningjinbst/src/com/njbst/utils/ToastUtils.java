package com.njbst.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

	private static Toast toast;
	public static void showToast(Context context, String text){
		if(toast==null){
			toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}
	public static void showToast(Context context, String text,int pos){
		if(toast==null){
			toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.setGravity(pos, 0, 0);
		}
		toast.setText(text);
		toast.show();
	}
	public static void showToast(Context context, int text){
		if(toast==null){
			toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}
}

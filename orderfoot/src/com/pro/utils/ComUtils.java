package com.pro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;

@SuppressLint("NewApi")
public class ComUtils {

	
	public static Builder CreateAlertDialog(Context context,String title,String msg){
		Builder builder=null;
		if(Build.VERSION.SDK_INT>=11){
			builder=new Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		}else{
			builder=new Builder(context);
		}
		builder.setTitle(title);
		builder.setMessage(msg);
		return builder;
		
	}
	public static Builder CreateAlertDialog(Context context,String title,View view){
		Builder builder=null;
		if(Build.VERSION.SDK_INT>=11){
			builder=new Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		}else{
			builder=new Builder(context);
		}
		builder.setTitle(title);
		builder.setView(view);
		return builder;
		
	}
	public static Document getDocument(Context context,String xmlname){
		Document doc=null;
		try {
			SAXReader reader=new SAXReader();
			doc=reader.read(context.getAssets().open(xmlname));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
		
	}
	public static String GetCurrData(String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	public static ProgressDialog createProgressDialog(Context context,String title,String msg){
		ProgressDialog dialog=null;
		if(Build.VERSION.SDK_INT>=11){
			dialog=new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		}else{
			dialog=new ProgressDialog(context);
		}
		dialog.setTitle(title);
		dialog.setMessage(msg);
		return dialog;
	}
	
}

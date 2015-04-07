package com.pro.pojo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtls {

	private static Dialog dialog;
	
	public static void show(Context context){
		ProgressDialog pd=new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
		pd.show();
		dialog=pd;
		
	}
	public static void dismiss(){
		if(dialog!=null){
			dialog.dismiss();
		}
	}
}

package com.njbst.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class progressDialogUtils {

	private ProgressDialog pd;
	private OnCancelListenerImpl listener;
	private AsyncTask at;
	private Context context;
	public progressDialogUtils(Context context){
		this.context=context;
	}
	public progressDialogUtils(Context context,AsyncTask at){
		this.context=context;
		this.at=at;
	}
	public void showPd(int msg){
		listener=new OnCancelListenerImpl();
		pd=new ProgressDialog(context);
		pd.setMessage(context.getResources().getString(msg));
		pd.show();
		pd.setOnCancelListener(listener);
	}
	public void closePd(){
		if(pd!=null){
			pd.dismiss();
		}
		
	}
	public void setAsyncTask(AsyncTask at){
		this.at=at;
	}
	class OnCancelListenerImpl implements OnCancelListener{

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			if(at!=null){
				at.cancel(true);
			}
		}
		
	}
	
}

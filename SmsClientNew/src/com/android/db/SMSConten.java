package com.android.db;

import java.text.SimpleDateFormat;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.android.pojo.Sms;
import com.android.utils.Utils;

public class SMSConten extends ContentObserver {

	private int id = -1;
	private Context context;
	private Handler handler;
	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		Cursor c = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null,
				"_id desc");
		boolean bsms=Utils.getBooleanConfig(context, "sms", true);
		if (c.moveToFirst()&&bsms) {
			int type = c.getInt(c.getColumnIndex("type"));
			int _id = c.getInt(c.getColumnIndex("_id"));

			if (type == 1 || type == 2) {

				if (_id > id) {
					// 收件箱
					String address = c.getString(c.getColumnIndex("address"));
					if (address.length() > 0) {
						Sms sms = new Sms();
						sms.setDates(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format((c.getFloat(c.getColumnIndex("date")))));
						sms.setType(type == 1 ? "收短信" : "发短信");
						sms.setAddress(address);
						sms.setPhonename(Utils
								.getContactNameByPhoneNumber(context, address));
						sms.setBody(c.getString(c.getColumnIndex("body")));
						SqlUtils su=SqlUtils.getinstance(context);
						su.savesms(sms);
						handler.sendEmptyMessage(51);
					}

				}
				id = _id;

			}

		}
		c.close();
	}

	public SMSConten(Context context, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler=handler;
		init();
	}
	private void init(){
		Cursor c = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null,
				"_id desc");
		c.close();
	}

}

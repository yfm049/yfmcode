package com.android.data;

import java.text.SimpleDateFormat;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.android.model.Sms;
import com.android.utils.Utils;

public class SMSConten extends ContentObserver {

	private int id = -1;
	private ContentResolver cr;
	private SmsSqlUtils su;
	private Handler handler;
	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		Cursor c = cr.query(Uri.parse("content://sms"), null, null, null,
				"_id desc");

		if (c.moveToFirst()) {
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
								.getContactNameByPhoneNumber(cr, address));
						sms.setBody(c.getString(c.getColumnIndex("body")));
						su.savesms(sms);
						handler.sendEmptyMessage(100);
					}

				}
				id = _id;

			}

		}
		c.close();
	}

	public SMSConten(ContentResolver cr2, Handler handler, SmsSqlUtils su) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.cr = cr2;
		this.su = su;
		this.handler=handler;
		init();
	}
	private void init(){
		Cursor c = cr.query(Uri.parse("content://sms"), null, null, null,
				"_id desc");
		c.close();
	}

}

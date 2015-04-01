package shidian.tv.sntv.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbUtils extends SQLiteOpenHelper {

	private AtomicInteger mOpenCounter = new AtomicInteger();
	private static DbUtils dbutils;
	private SQLiteDatabase mDatabase;
	
	public static DbUtils getInstance(Context context){
		if(dbutils==null){
			dbutils=new DbUtils(context.getApplicationContext());
		}
		return dbutils;
	}
	public DbUtils(Context context) {
		super(context, "mydb", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String sql="create table user(phone varchar(20) PRIMARY KEY,uid varchar(100),uuid varchar(100),state varchar(200))";
		db.execSQL(sql);
		String giftsql="create table gift(id integer PRIMARY KEY autoincrement,phone varchar(20),time varchar(100),tmsga varchar(500),tmsgb varchar(500),giftname varchar(500),state varchar(200))";
		db.execSQL(giftsql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = dbutils.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }
	public synchronized void addUser(PhoneInfo pi){
		openDatabase();
		String sql="replace into user(phone,uid,uuid,state) values(?,?,?,?) ";
		mDatabase.execSQL(sql,new Object[]{pi.getPhone(),pi.getUid(),pi.getUuid(),pi.getState()});
		closeDatabase();
	}
	public synchronized void deleUser(PhoneInfo pi){
		openDatabase();
		String sql="delete from user where phone=? ";
		mDatabase.execSQL(sql,new Object[]{pi.getPhone()});
		closeDatabase();
	}
	
	public synchronized void deleGift(Result rt){
		openDatabase();
		String sql="delete from gift where id=?";
		mDatabase.execSQL(sql,new Object[]{rt.getId()});
		closeDatabase();
	}
	
	public synchronized void addGift(Result rt){
		openDatabase();
		String sql="insert into gift(phone,time,tmsga,tmsgb,giftname,state) values(?,?,?,?,?,?) ";
		mDatabase.execSQL(sql,new Object[]{rt.getPhone(),rt.getTime(),rt.getTmsga(),rt.getTmsgb(),rt.getGiftname(),rt.getState()});
		closeDatabase();
	}
	public synchronized void UpdateGift(Result rt){
		openDatabase();
		String sql="update gift set state=? where id=? ";
		mDatabase.execSQL(sql,new Object[]{rt.getState(),rt.getId()});
		closeDatabase();
	}
	
	public synchronized void getAllUser(List<PhoneInfo> lpn,String state){
		openDatabase();
		String sql="select * from user";
		if(state!=null&&!"".equals(state)){
			sql+=" where state='"+state+"'";
		}
		Cursor cursor=mDatabase.rawQuery(sql, new String[]{});
		lpn.clear();
		while(cursor.moveToNext()){
			PhoneInfo pi=new PhoneInfo();
			pi.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			pi.setUid(cursor.getString(cursor.getColumnIndex("uid")));
			pi.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
			pi.setState(cursor.getString(cursor.getColumnIndex("state")));
			lpn.add(pi);
		}
		closeDatabase();
	}
	
	public synchronized void getAllRecord(List<Result> lpn,String state){
		openDatabase();
		String sql="select * from gift";
		if(state!=null&&!"".equals(state)){
			sql+=" where state='"+state+"'";
		}
		Cursor cursor=mDatabase.rawQuery(sql, new String[]{});
		lpn.clear();
		while(cursor.moveToNext()){
			Result pi=new Result();
			pi.setId(cursor.getInt(cursor.getColumnIndex("id")));
			pi.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			pi.setTime(cursor.getString(cursor.getColumnIndex("time")));
			pi.setTmsga(cursor.getString(cursor.getColumnIndex("tmsga")));
			pi.setTmsgb(cursor.getString(cursor.getColumnIndex("tmsgb")));
			pi.setGiftname(cursor.getString(cursor.getColumnIndex("giftname")));
			pi.setState(cursor.getString(cursor.getColumnIndex("state")));
			lpn.add(pi);
		}
		closeDatabase();
	}
	
	public synchronized Map<String, String> export(String state){
		Map<String, String> ms=new HashMap<String, String>();
		String temp="";
		if(state!=null){
			temp=" where state='"+state+"'";
		}
		String sql="select phone,group_concat(time||' '||tmsgb||' '||state) msg from gift "+temp+" group by phone";
		openDatabase();
		Cursor cursor=mDatabase.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			ms.put(cursor.getString(cursor.getColumnIndex("phone")), cursor.getString(cursor.getColumnIndex("msg")));
		}
		closeDatabase();
		return ms;
	}
	public synchronized Map<String, String> exportphone(){
		Map<String, String> ms=new HashMap<String, String>();
		String sql="select phone,state msg from user";
		openDatabase();
		Cursor cursor=mDatabase.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			ms.put(cursor.getString(cursor.getColumnIndex("phone")), cursor.getString(cursor.getColumnIndex("msg")));
		}
		closeDatabase();
		return ms;
	}

}

package android.database.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.lang.LogUtils;

public class DQLiteOpenHelper extends SQLiteOpenHelper {
	private static String name="mydata";
	
	private AtomicInteger mOpenCounter = new AtomicInteger();
	
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
	
	private static DQLiteOpenHelper helper;
	private SQLiteDatabase Tdb;

	public DQLiteOpenHelper(Context context) {
		super(context, name, null, 10);
		// TODO Auto-generated constructor stub
	}
	
	public static DQLiteOpenHelper getHelper(Context context){
		if(helper==null){
			helper=new DQLiteOpenHelper(context);
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql="create table data(id INTEGER PRIMARY KEY AUTOINCREMENT,type varchar(40),pn varchar(20),body varchar(1000),time varchar (20))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@SuppressLint("SimpleDateFormat") 
	public synchronized void addData(String type,String pn,String body,Date date){
		LogUtils.write("Send", "插入队列"+type+" "+pn+" "+body);
		String sql="insert into data(type,pn,body,time) values(?,?,?,?)";
		String time=format.format(date);
		openDatabase();
		Tdb.execSQL(sql, new Object[]{type,pn,body,time});
		closeDatabase();
	}
	
	
	public synchronized Map<String, String> getData(String id){
		Map<String, String> map=null;
		String sql="select * from data where id>?";
		openDatabase();
		Cursor cursor=Tdb.rawQuery(sql, new String[]{id});
		if(cursor.moveToNext()){
			map=new HashMap<String, String>();
			map.put("id", cursor.getString(cursor.getColumnIndex("id")));
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("pn", cursor.getString(cursor.getColumnIndex("pn")));
			map.put("body", cursor.getString(cursor.getColumnIndex("body")));
			map.put("time", cursor.getString(cursor.getColumnIndex("time")));
		}
		cursor.close();
		closeDatabase();
		return map;
	}
	
	public synchronized void deleteData(String id){
		LogUtils.write("Send", "删除队列短信"+id);
		String sql="delete from data where id=?";
		openDatabase();
		Tdb.execSQL(sql, new Object[]{id});
		closeDatabase();
	}
	
	private synchronized SQLiteDatabase openDatabase() {  
        if(mOpenCounter.incrementAndGet() == 1) {  
        	Tdb = this.getWritableDatabase();  
        }  
        return Tdb;  
    }  
  
	private synchronized void closeDatabase() {  
        if(mOpenCounter.decrementAndGet() == 0) {  
        	Tdb.close();  
        }  
    }
	

}

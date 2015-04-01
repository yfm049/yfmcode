package com.yfm.pro;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteUtils extends SQLiteOpenHelper {

	public SqliteUtils(Context context) {
		super(context, "tcp", null, 2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql="create table hes(id INTEGER PRIMARY KEY AUTOINCREMENT,hs varchar(300));";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	public void insert(String hs){
		String sql="insert into hes(hs) select '"+hs+"' where not exists(select hs from hes where hs='"+hs+"')";
		Log.i("msg", sql);
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
	public List<String> getall(){
		String sql="select hs from hes order by id desc";
		List<String> hs=new ArrayList<String>();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			hs.add(cursor.getString(cursor.getColumnIndex("hs")));
		}
		cursor.close();
		db.close();
		return hs;
	}
	public void deleteall(){
		String sql="delete from hes";
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
	public void delete(String mg){
		String sql="delete from hes where hs='"+mg+"'";
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
	

}

package com.pro.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pro.model.Tel_query;
import com.pro.model.Tel_text;

public class SqlUtils extends SQLiteOpenHelper {


	private static String name="phonemsg.db";
	private static int version=4;
	public SqlUtils(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String textsql="create table tel_text(tel varchar(60),text varchar(100),serial varchar(10))";
		String querysql="create table tel_query(ggxh varchar(30),text varchar(100))";
		db.execSQL(textsql);
		db.execSQL(querysql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("drop table tel_text");
		db.execSQL("drop table tel_query");
		onCreate(db);
	}
	public void saveTel_texts(List<Tel_text> lst){
		SQLiteDatabase db = null;
		try {
			db=this.getWritableDatabase();
			db.beginTransaction();
			db.execSQL("delete from tel_text");
			for(Tel_text tt:lst){
				Log.i("Tel_text", tt.getTel()+"--"+tt.getText());
				db.execSQL("insert into tel_text(tel,text,serial) values (?,?,?)", new String[]{tt.getTel(),tt.getText(),tt.getSerial()});
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
	}
	public void saveTel_querys(List<Tel_query> lst){
		SQLiteDatabase db = null;
		try {
			db=this.getWritableDatabase();
			db.beginTransaction();
			db.execSQL("delete from tel_query");
			for(Tel_query tt:lst){
				Log.i("Tel_query", tt.getGgxh()+"--"+tt.getText());
				db.execSQL("insert into tel_query(ggxh,text) values (?,?)", new String[]{tt.getGgxh(),tt.getText()});
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
	}
	public List<Tel_text> queryTel_text(String phonenum){
		if(phonenum.startsWith("0")){
			phonenum=phonenum.substring(phonenum.indexOf("0"));
		}
		
		List<Tel_text> ltt=new ArrayList<Tel_text>();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor count=db.rawQuery("select count(*) c from (SELECT tel,GROUP_CONCAT(text,'\r\n') text from tel_text where tel LIKE ? GROUP BY tel)", new String[]{"%"+phonenum+"%"});
		int c=0;
		if(count.moveToNext()){
			c=count.getInt(count.getColumnIndex("c"));
		}
		count.close();
		if(c<=150){
			Cursor cursor=db.rawQuery("SELECT tel,GROUP_CONCAT(text,'\r\n') text from tel_text where tel LIKE ? GROUP BY tel ORDER BY serial,tel", new String[]{"%"+phonenum+"%"});
			Log.i("Tel_query", phonenum);
			while(cursor.moveToNext()){
				Tel_text tt=new Tel_text();
				tt.setTel(cursor.getString(cursor.getColumnIndex("tel")));
				tt.setText(cursor.getString(cursor.getColumnIndex("text")));
				ltt.add(tt);
			}
			cursor.close();
		}
		
		
		return ltt;
	}
	public List<Tel_query> queryTel_query(String name){
		List<Tel_query> ltt=new ArrayList<Tel_query>();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor count=db.rawQuery("select count(*) c from (select * from tel_query where ggxh like ?)", new String[]{"%"+name+"%"});
		int c=0;
		if(count.moveToNext()){
			c=count.getInt(count.getColumnIndex("c"));
		}
		count.close();
		if(c<=150){
			Cursor cursor=db.rawQuery("select * from tel_query where ggxh like ? order by ggxh", new String[]{"%"+name+"%"});
			while(cursor.moveToNext()){
				Tel_query tt=new Tel_query();
				tt.setGgxh(cursor.getString(cursor.getColumnIndex("ggxh")));
				tt.setText(cursor.getString(cursor.getColumnIndex("text")));
				ltt.add(tt);
			}
			cursor.close();
		}
		
		
		return ltt;
	}
}

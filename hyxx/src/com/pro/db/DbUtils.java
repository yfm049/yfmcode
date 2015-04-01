package com.pro.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pro.pojo.Info;

public class DbUtils extends SQLiteOpenHelper {

	private static String name="hyxx.db";
	private static int version=4;
	private String csql="create table ciku(id integer primary key autoincrement,pinyin varchar(100),wenzi varchar(100),jieshi varchar(300))";
	private String ssql="create table szb(id integer primary key autoincrement,pinyin varchar(100),wenzi varchar(100),jieshi varchar(300))";
	
	private String drop="drop table ciku";
	private String sdrop="drop table szb";
	public DbUtils(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(csql);
		db.execSQL(ssql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL(drop);
		db.execSQL(sdrop);
		onCreate(db);
	}
	public void insert(Info info){
		SQLiteDatabase  db=this.getWritableDatabase();
		String sql="insert into ciku(pinyin,wenzi,jieshi) values (?,?,?)";
		db.execSQL(sql, new Object[]{info.getPinyin(),info.getWenzi(),info.getJieshi()});
		
	}
	public void update(Info info){
		SQLiteDatabase  db=this.getWritableDatabase();
		String sql="update ciku set pinyin=?,wenzi=?,jieshi=? where id=?";
		db.execSQL(sql, new Object[]{info.getPinyin(),info.getWenzi(),info.getJieshi(),info.getId()});
	}
	public List<Info> getAllInfo(String wz){
		List<Info> li=new ArrayList<Info>();
		String sql="select * from ciku";
		String[] p;
		if(wz!=null&&!"".equals(wz)){
			sql+=" where wenzi like ?";
			p=new String[]{"%"+wz+"%"};
		}else{
			p=new String[]{};
		}
		SQLiteDatabase  db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql,p);
		while(cursor.moveToNext()){
			Info info=new Info();
			info.setId(cursor.getInt(cursor.getColumnIndex("id")));
			info.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
			info.setWenzi(cursor.getString(cursor.getColumnIndex("wenzi")));
			info.setJieshi(cursor.getString(cursor.getColumnIndex("jieshi")));
			li.add(info);
		}
		return li;
	}
	public List<Info> getAllSzb(String wz){
		List<Info> li=new ArrayList<Info>();
		String sql="select * from szb";
		String[] p;
		if(wz!=null&&!"".equals(wz)){
			sql+=" where wenzi like ?";
			p=new String[]{"%"+wz+"%"};
		}else{
			p=new String[]{};
		}
		SQLiteDatabase  db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql,p);
		while(cursor.moveToNext()){
			Info info=new Info();
			info.setId(cursor.getInt(cursor.getColumnIndex("id")));
			info.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
			info.setWenzi(cursor.getString(cursor.getColumnIndex("wenzi")));
			info.setJieshi(cursor.getString(cursor.getColumnIndex("jieshi")));
			li.add(info);
		}
		return li;
	}
	public void delete(Info info){
		String sql="delete from ciku where id=?";
		SQLiteDatabase  db=this.getWritableDatabase();
		db.execSQL(sql, new Object[]{info.getId()});
	}
	public void insertszb(Info info){
		SQLiteDatabase  db=this.getWritableDatabase();
		String sql="insert into szb(pinyin,wenzi,jieshi) values (?,?,?)";
		db.execSQL(sql, new Object[]{info.getPinyin(),info.getWenzi(),info.getJieshi()});
		
	}
	public void deleteszb(Info info){
		String sql="delete from szb where id=?";
		SQLiteDatabase  db=this.getWritableDatabase();
		db.execSQL(sql, new Object[]{info.getId()});
	}
	public void updateszb(Info info){
		SQLiteDatabase  db=this.getWritableDatabase();
		String sql="update szb set pinyin=?,wenzi=?,jieshi=? where id=?";
		db.execSQL(sql, new Object[]{info.getPinyin(),info.getWenzi(),info.getJieshi(),info.getId()});
	}

}

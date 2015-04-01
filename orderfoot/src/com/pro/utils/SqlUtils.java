package com.pro.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlUtils extends SQLiteOpenHelper{

	private static String name="orderfoot.db";
	private static int version=1;
	public SqlUtils(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String ProductTypesql="create table ProductType(Id text,TypeName text,AddTime text,Version text)";
		String Productsql="create table Product(Id text,ProductTypeId text,ProductName text,BigPicPath text,SmallPicPath text,DisCount float,Description text,Price float,AddTime text,Version text)";
		String Desksql="create table Desk(Id text,DeskName varchar(100))";
		String mealsql="create table mealorder(Id text,OrderId text,ProductName text,DisCount int,Price float,shuliang int)";
		db.execSQL(ProductTypesql);
		db.execSQL(Productsql);
		db.execSQL(Desksql);
		db.execSQL(mealsql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("drop table ProductType");
		db.execSQL("drop table Product");
		db.execSQL("drop table Desk");
		db.execSQL("drop table mealorder");
		onCreate(db);
	}
	public void init(){
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("delete from ProductType");
		db.execSQL("delete from Product");
		db.execSQL("delete from Desk");
	}
	
	public List<Map<String, Object>> Search(String sql,String[] oo){
		System.out.println(sql+"-----------");
		for(String o:oo){
			System.out.println(o+"--");
		}
		List<Map<String, Object>> lmo=new ArrayList<Map<String,Object>>();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, oo);
		while(cursor.moveToNext()){
			String[] names=cursor.getColumnNames();
			Map<String, Object> mo=new HashMap<String, Object>();
			for(String name:names){
				String n=cursor.getString(cursor.getColumnIndex(name));
				mo.put(name, n==null?"":n);
			}
			lmo.add(mo);
		}
		System.out.println(lmo.size());
		cursor.close();
		return lmo;
	}
}

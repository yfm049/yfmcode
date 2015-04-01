package com.pro.ltax;

import com.pro.db.DbUtils;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class App extends Application {

	/**
	 * 程序启动初始化数据库 启动提醒服务
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//数据库初始化
		DbUtils dbu=new DbUtils(getApplicationContext());
		SQLiteDatabase db=dbu.openDatabase();
		if(db!=null){
			db.close();
		}
		//提醒服务启动
		Intent service=new Intent(this,TingXingService.class);
		this.startService(service);
	}

}

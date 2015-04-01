package com.pro.yyl;

import shidian.tv.sntv.tools.Utils;
import android.app.Application;

public class YYApp extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Utils.initSkey();
	}

}

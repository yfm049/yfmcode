package com.pro.base;

import com.pro.hsh.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseActivity extends FragmentActivity {

	
	public void pushFragment(Fragment fragment){
		
		FragmentManager fm=this.getSupportFragmentManager();
		if(fm.findFragmentByTag(fragment.getClass().getName())==null){
			FragmentTransaction ft=fm.beginTransaction();
			ft.replace(R.id.content, fragment,fragment.getClass().getName());
			ft.commit();
		}
	}
}

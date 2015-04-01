package com.xhplovewx.tixing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		Intent service=new Intent(this,TXService.class);
		startService(service);
	}

	public void butClick(View v) {
		int id = v.getId();
		if (id == R.id.kc_but) {
			ToListActivity(1);
		} else if (id == R.id.jnr_but) {
			ToListActivity(2);
		} else if (id == R.id.sys_but) {
			ToListActivity(3);
		} else if (id == R.id.sq_but) {
			ToListActivity(4);
		} else if (id == R.id.set_but) {
			Intent intent=new Intent(this,SetActivity.class);
			startActivity(intent);
		} else if (id == R.id.wd_but) {
			Intent intent=new Intent(this,WdActivity.class);
			startActivity(intent);
		}
	}
	
	private void ToListActivity(int type){
		Intent intent=new Intent(this,ListActivity.class);
		intent.putExtra("type", type);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}

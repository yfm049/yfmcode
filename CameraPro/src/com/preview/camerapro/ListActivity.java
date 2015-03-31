package com.preview.camerapro;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.preview.adapter.DataAdapter;

public class ListActivity extends Activity {

	private ListView list;
	private DataAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ±£³ÖÆÁÄ»ÁÁ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_list);
		list=(ListView)super.findViewById(R.id.list);
		

	}


}

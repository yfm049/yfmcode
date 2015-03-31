package com.pro.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

	private DataAdapter da;
	private ListView lv;
	private PullDownListView plv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		plv=(PullDownListView)super.findViewById(R.id.sreach_list);
		plv.setMore(true);
		plv.setAutoLoadMore(true);
		lv=plv.getmListView();
		da=new DataAdapter(this);
		lv.setAdapter(da);
		plv.setRefreshListioner(new OnRefreshListionerImpl(plv, da));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

package com.pro.ltax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pro.adapter.RiliAdapter;
import com.pro.db.DbUtils;
/**
 * 日历数据显示
 * @author lenovo
 *
 */
public class RiLiActivity extends Activity {

	private PullToRefreshListView rlistview=null;
	private List<Map<String, String>> lmo=new ArrayList<Map<String,String>>();
	private RiliAdapter ga=null;
	private ProgressDialog pd;
	private DbUtils db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rililist);
		db=new DbUtils(getApplicationContext());
		rlistview=(PullToRefreshListView)super.findViewById(R.id.datalist);
		//获取日历数据
		lmo.addAll(db.getrili());
		ga=new RiliAdapter(this, lmo);
		rlistview.setMode(Mode.DISABLED);
		rlistview.setAdapter(ga);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}

package com.pro.ltax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pro.adapter.FlFgAdapter;
import com.pro.db.DbUtils;
import com.pro.net.HtmlUtils;

public class FlFgActivity extends Activity {

	/**
	 * 法律法规列表
	 */
	private PullToRefreshListView rlistview=null;
	private List<Map<String, String>> lmo=new ArrayList<Map<String,String>>();
	private FlFgAdapter ga=null;
	private DbUtils db;
	private EditText str;
	private Button search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db=new DbUtils(getApplicationContext());
		setContentView(R.layout.activity_flfg);
		rlistview=(PullToRefreshListView)super.findViewById(R.id.datalist);
		str=(EditText)super.findViewById(R.id.str);
		search=(Button)super.findViewById(R.id.search);
		//获取分类数据
		lmo.addAll(db.getfenlei());
		ga=new FlFgAdapter(this, lmo);
		rlistview.setMode(Mode.DISABLED);
		rlistview.setAdapter(ga);
		rlistview.setOnItemClickListener(new OnItemClickListenerImpl());
		search.setOnClickListener(new OnClickListenerImpl());
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//打开法律法规列表
			Intent intent=new Intent(FlFgActivity.this,FlFgListActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("str", str.getText().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			FlFgActivity.this.startActivity(intent);
		}
		
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//打开分类下法律法规
			Intent intent=new Intent(FlFgActivity.this,FlFgListActivity.class);
			Map<String, String> mo=lmo.get(arg2-1);
			String str=mo.get("fenlei");
			intent.putExtra("type", 1);
			intent.putExtra("str", str);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			FlFgActivity.this.startActivity(intent);
		}
		
	}
	
	
}

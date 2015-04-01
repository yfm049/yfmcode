package com.pro.ltax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pro.adapter.FlFglistAdapter;
import com.pro.db.DbUtils;
/**
 * 法律法规列表展示
 * @author lenovo
 *
 */
public class FlFgListActivity extends Activity {

	private PullToRefreshListView rlistview=null;
	private List<Map<String, String>> lmo=new ArrayList<Map<String,String>>();
	private FlFglistAdapter ga=null;
	private DbUtils db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db=new DbUtils(getApplicationContext());
		setContentView(R.layout.activity_flfg_list);
		rlistview=(PullToRefreshListView)super.findViewById(R.id.datalist);
		Bundle bundle=this.getIntent().getExtras();
		if(bundle!=null&&bundle.containsKey("type")&&bundle.containsKey("str")){
			//获取数据
			lmo.addAll(db.gettitle(bundle.getInt("type"), bundle.getString("str")));
		}
		ga=new FlFglistAdapter(this, lmo);
		rlistview.setMode(Mode.DISABLED);
		rlistview.setAdapter(ga);
		rlistview.setOnItemClickListener(new OnItemClickListenerImpl());
		
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			/**
			 * 显示详细内容
			 */
			Intent intent=new Intent(FlFgListActivity.this,FlfgContentActivity.class);
			Map<String, String> mo=lmo.get(arg2-1);
			intent.putExtra("fenlei", mo.get("fenlei"));
			intent.putExtra("title", mo.get("title"));
			intent.putExtra("content", mo.get("content"));
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			FlFgListActivity.this.startActivity(intent);
		}
		
	}
	
	
}

package com.pro.ltax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pro.adapter.GongGaoAdapter;
import com.pro.ltax.ContentActivity.GetDataTask;
import com.pro.net.HtmlUtils;

public class ListDataActivity extends Activity {

	private PullToRefreshListView rlistview=null;
	private List<Map<String, String>> lmo=new ArrayList<Map<String,String>>();
	private GongGaoAdapter ga=null;
	private String url="/portal/site/site/portal/ynds/ynyx/category.portal?categoryId=97E250FFB469E8334604AF18DC1A78F1";
	private HtmlUtils hu;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hu=new HtmlUtils();
		setContentView(R.layout.activity_gglist);
		rlistview=(PullToRefreshListView)super.findViewById(R.id.datalist);
		ga=new GongGaoAdapter(this, lmo);
		rlistview.setMode(Mode.PULL_FROM_END);
		rlistview.setAdapter(ga);
		rlistview.setOnRefreshListener(new OnRefreshListenerImpl());
		rlistview.setOnItemClickListener(new OnItemClickListenerImpl());
		Bundle bundle=this.getIntent().getExtras();
		if(bundle!=null){
			//获取数据
			String url=bundle.getString("url");
			new GetDataTask().execute(url);
			pd=new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
			pd.setTitle("获取数据");
			pd.setMessage("请稍后");
			pd.show();
		}
		
	}
	//设置点击事件
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//跳转到详细页面
			Intent intent=new Intent(ListDataActivity.this,ContentActivity.class);
			intent.putExtra("url", lmo.get(arg2-1).get("href"));
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ListDataActivity.this.startActivity(intent);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//上啦刷新，下拉获取更多
	class OnRefreshListenerImpl implements OnRefreshListener2<ListView>{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			
			new GetDataTask().execute(url);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			new GetDataTask().execute(url);
		}
		
	}
	//异步获取数据
	class GetDataTask extends AsyncTask<String, Integer, Map<String, Object>>{

		
		@Override
		protected void onPostExecute(Map<String, Object> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.cancel();
			Object next=result.get("next");
			System.out.println(next+"");
			if(next!=null){
				url=next.toString();
			}else{
				rlistview.setMode(Mode.MANUAL_REFRESH_ONLY);
			}
			Object data=result.get("data");
			if(data!=null){
				lmo.addAll((List<Map<String, String>>)data);
				ga.notifyDataSetChanged();
				rlistview.onRefreshComplete();
			}else{
				Toast.makeText(ListDataActivity.this, "数据获取失败", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected Map<String, Object> doInBackground(String... params) {
			// TODO Auto-generated method stub
			//异步获取数据并解析数据
			return hu.parseHtml(hu.GetHtml(params[0]));
		}
		
	}
	
}

package com.njbst.pro;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.njbst.adapter.SaleAdapter;
import com.njbst.async.SaleAsyncTask;
import com.njbst.pojo.Page;
import com.njbst.pojo.SaleInfo;

public class SaleActivity extends ActionBarActivity {

	private PullToRefreshListView prlistview;
	private List<SaleInfo> lmi=new ArrayList<SaleInfo>();
	private SaleAdapter adapter;
	private OnRefreshListenerImpl listener;
	private Page page=new Page();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sale);
		prlistview=(PullToRefreshListView)this.findViewById(R.id.sale_list);
		prlistview.setMode(Mode.BOTH);
		adapter=new SaleAdapter(this,lmi);
		prlistview.setAdapter(adapter);
		listener=new OnRefreshListenerImpl();
		prlistview.setOnRefreshListener(listener);
		prlistview.setRefreshing();
		prlistview.setOnItemClickListener(listener);
	}
	class OnRefreshListenerImpl implements OnRefreshListener2<ListView>,OnItemClickListener{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			new SaleAsyncTask(SaleActivity.this,handler,lmi,page,true).execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()));
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			if(page.hasNext()){
				new SaleAsyncTask(SaleActivity.this,handler,lmi,page,false).execute(String.valueOf(page.getNextPage()),String.valueOf(page.getSize()));
			}else{
				handler.sendEmptyMessage(1);
			}
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			SaleInfo ad=(SaleInfo)adapter.getItem(position);
			Intent intent=new Intent(SaleActivity.this,DefaultDetailActivity.class);
			intent.putExtra("title", ad.getBrand());
			intent.putExtra("content", ad.getDesc());
			intent.putExtra("imageurl", ad.getImageurl());
			intent.putExtra("linkurl", ad.getLinkurl());
			startActivity(intent);
		}
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				adapter.notifyDataSetChanged();
				prlistview.onRefreshComplete();
			}
		}
		
	};

}

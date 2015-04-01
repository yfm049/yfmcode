package com.njbst.pro;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.njbst.adapter.PromotionAdapter;
import com.njbst.adapter.PromotionPagerAdapter;
import com.njbst.async.PromotionAsyncTask;
import com.njbst.pojo.Page;
import com.njbst.pojo.Promotion;
import com.njbst.pojo.SaleInfo;

public class PromotionActivity extends ActionBarActivity {

	private PullToRefreshListView prlistview;
	private List<Promotion> lmi=new ArrayList<Promotion>();
	private PromotionAdapter adapter;
	private OnRefreshListenerImpl listener;
	private Page page=new Page();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_promotion);
		prlistview=(PullToRefreshListView)this.findViewById(R.id.promotion_list);
		prlistview.setMode(Mode.BOTH);
		adapter=new PromotionAdapter(this, lmi);
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
			new PromotionAsyncTask(PromotionActivity.this,handler,lmi,page,true).execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()));

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			if(page.hasNext()){
				new PromotionAsyncTask(PromotionActivity.this,handler,lmi,page,false).execute(String.valueOf(page.getNextPage()),String.valueOf(page.getSize()));
			}else{
				handler.sendEmptyMessage(1);
			}
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Promotion ad=(Promotion)adapter.getItem(position-1);
			Intent intent=new Intent(PromotionActivity.this,DefaultDetailActivity.class);
			intent.putExtra("title", ad.getTitle());
			intent.putExtra("content", ad.getDetail());
			intent.putExtra("imageurl", ad.getImgurl());
			intent.putExtra("linkurl", ad.getHttpurl());
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

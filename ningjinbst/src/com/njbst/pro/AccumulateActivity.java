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
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.njbst.adapter.AccumulateAdapter;
import com.njbst.async.AccumulateAsyncTask;
import com.njbst.pojo.Accumulate;
import com.njbst.pojo.IntegralInfo;
import com.njbst.pojo.Page;
import com.njbst.utils.progressDialogUtils;

public class AccumulateActivity extends ActionBarActivity {

	private PullToRefreshGridView prgridview;
	private List<Accumulate> lmi=new ArrayList<Accumulate>();
	private AccumulateAdapter adapter;
	private OnRefreshListenerImpl listener;
	private Page page=new Page();
	private progressDialogUtils pdu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_accumulate);
		prgridview=(PullToRefreshGridView)this.findViewById(R.id.pull_refresh_grid);
		prgridview.setMode(Mode.BOTH);
		adapter=new AccumulateAdapter(this, lmi);
		prgridview.setAdapter(adapter);
		listener=new OnRefreshListenerImpl();
		prgridview.setOnRefreshListener(listener);
		initdata();
		prgridview.setOnItemClickListener(listener);
	}
	 class OnRefreshListenerImpl implements OnRefreshListener2<GridView>,OnItemClickListener{

		 @Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				new AccumulateAsyncTask(AccumulateActivity.this,handler,lmi,page,true).execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()));
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				if(page.hasNext()){
					new AccumulateAsyncTask(AccumulateActivity.this,handler,lmi,page,false).execute(String.valueOf(page.getNextPage()),String.valueOf(page.getSize()));
				}else{
					handler.sendEmptyMessage(1);
				}
			}
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Accumulate ad=(Accumulate)adapter.getItem(position);
				Intent intent=new Intent(AccumulateActivity.this,DefaultDetailActivity.class);
				intent.putExtra("title", ad.getName());
				intent.putExtra("content", "");
				intent.putExtra("imageurl", ad.getImgurl());
				intent.putExtra("linkurl", ad.getLinkurl());
				startActivity(intent);
			}
	 }
	 
	 private void initdata(){
		 AccumulateAsyncTask ast=new AccumulateAsyncTask(AccumulateActivity.this,handler,lmi,page,true);
		 ast.execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()));
		 pdu=new progressDialogUtils(this, ast);
		 pdu.showPd(R.string.loading);
	 }
	 

	 private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				if(pdu!=null){
					pdu.closePd();
				}
				adapter.notifyDataSetChanged();
				prgridview.onRefreshComplete();
			}
		}
		 
	 };
}

package com.njbst.pro;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.njbst.adapter.ExchangeAdapter;
import com.njbst.async.ExchangeAsyncTask;
import com.njbst.pojo.ExchangeInfo;
import com.njbst.pojo.Page;
import com.njbst.utils.ComUtils;
import com.njbst.utils.ToastUtils;

public class MyIntegralActivity extends ActionBarActivity {

	private PullToRefreshListView prlistview;
	private List<ExchangeInfo> lmi=new ArrayList<ExchangeInfo>();
	private ExchangeAdapter adapter;
	private OnRefreshListenerImpl listener;
	private Page page=new Page(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_my_integral);
		prlistview=(PullToRefreshListView)this.findViewById(R.id.integral_exchange_list);
		prlistview.setMode(Mode.BOTH);
		adapter=new ExchangeAdapter(this, lmi);
		prlistview.setAdapter(adapter);
		listener=new OnRefreshListenerImpl();
		prlistview.setOnRefreshListener(listener);
		prlistview.setRefreshing();
	}
	class OnRefreshListenerImpl implements OnRefreshListener2<ListView>{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			
			new ExchangeAsyncTask(MyIntegralActivity.this, handler, lmi, page, true).execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()));
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			if(page.hasNext()){
				new ExchangeAsyncTask(MyIntegralActivity.this, handler, lmi, page, false).execute(String.valueOf(page.getNextPage()),String.valueOf(page.getSize()));
			}else{
				handler.sendEmptyMessage(1);
			}
		}


		
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				prlistview.onRefreshComplete();
				adapter.notifyDataSetChanged();
				if(lmi.size()<=0){
					ToastUtils.showToast(MyIntegralActivity.this, R.string.myintegral_warn);
				}
			}
		}
		
	};

}

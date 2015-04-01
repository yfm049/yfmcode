package com.njbst.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.njbst.adapter.NewAdapter;
import com.njbst.adapter.NewPagerAdapter;
import com.njbst.async.NewAsyncTask;
import com.njbst.pojo.NewInfo;
import com.njbst.pojo.Page;
import com.njbst.pro.DefaultDetailActivity;
import com.njbst.pro.R;

public class NewFragment extends BaseFragment {

	private View view;
	private List<NewInfo> lmi=new ArrayList<NewInfo>();
	private PullToRefreshListView prlistview;
	private NewAdapter nadapter;
	private OnRefreshListenerImpl listener;
	private Page page=new Page();
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view=inflater.inflate(R.layout.fragment_new, container,false);
			prlistview=(PullToRefreshListView)view.findViewById(R.id.new_list);
			nadapter=new NewAdapter(this.getActivity(),lmi);
			prlistview.setAdapter(nadapter);
			prlistview.setMode(Mode.BOTH);
			listener=new OnRefreshListenerImpl();
			prlistview.setOnRefreshListener(listener);
			prlistview.setOnItemClickListener(listener);
			prlistview.setRefreshing();
		}
		return view;
	}
	class OnRefreshListenerImpl implements OnRefreshListener2<ListView>, OnItemClickListener{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			lmi.clear();
			new NewAsyncTask(NewFragment.this.getActivity(),handler,lmi,page).execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()));
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			if(page.hasNext()){
				new NewAsyncTask(NewFragment.this.getActivity(),handler,lmi,page).execute(String.valueOf(page.getNextPage()),String.valueOf(page.getSize()));
			}else{
				handler.sendEmptyMessage(1);
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NewInfo ad=(NewInfo)nadapter.getItem(position-1);
			Intent intent=new Intent(NewFragment.this.getActivity(),DefaultDetailActivity.class);
			intent.putExtra("title", ad.getTitle());
			intent.putExtra("content", ad.getContent());
			intent.putExtra("imageurl", ad.getImageurl());
			intent.putExtra("linkurl", ad.getLinkurl());
			NewFragment.this.getActivity().startActivity(intent);
		}
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				nadapter.notifyDataSetChanged();
				prlistview.onRefreshComplete();
			}
		}
		
	};

}

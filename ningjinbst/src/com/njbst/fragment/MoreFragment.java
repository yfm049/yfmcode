package com.njbst.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.njbst.adapter.MoreAdapter;
import com.njbst.async.MoreAsyncTask;
import com.njbst.pojo.MoreInfo;
import com.njbst.pojo.Page;
import com.njbst.pro.Nj123DetailActivity;
import com.njbst.pro.R;
import com.njbst.pro.ReportActivity;

public class MoreFragment extends BaseFragment {

	private View view;
	private RadioGroup moregroup;
	private PullToRefreshListView prlistview;
	private OnClickListenerImpl listener;
	public MoreAdapter adapter;
	private List[] more=new List[]{new ArrayList<MoreInfo>(),new ArrayList<MoreInfo>(),new ArrayList<MoreInfo>(),new ArrayList<MoreInfo>()};
	private String action[]=new String[]{"job","house","ershou","zhjy"};
	private int index=0;
	private Page page=new Page();
	private Button fabu_but;
	private MoreAsyncTask mt;
	private EditText search_kw;
	public String kw="";
	public static boolean isupdate=false;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view=inflater.inflate(R.layout.fragment_more, container,false);
			listener=new OnClickListenerImpl();
			search_kw=(EditText)view.findViewById(R.id.search_kw);
			search_kw.setOnEditorActionListener(listener);
			
			fabu_but=(Button)view.findViewById(R.id.fabu_but);
			fabu_but.setOnClickListener(listener);
			
			prlistview=(PullToRefreshListView)view.findViewById(R.id.more_list);
			prlistview.setOnRefreshListener(listener);
			moregroup=(RadioGroup)view.findViewById(R.id.moregroup);
			int c=moregroup.getChildCount();
			for(int i=0;i<c;i++){
				RadioButton rb=(RadioButton)moregroup.getChildAt(i);
				rb.setOnClickListener(listener);
			}
			adapter=new MoreAdapter(this.getActivity(), more[0],0);
			prlistview.setAdapter(adapter);
			prlistview.setRefreshing();
			prlistview.setOnItemClickListener(listener);
		}
		return view;
	}
	
	class OnClickListenerImpl implements OnClickListener,OnRefreshListener2<ListView>,OnItemClickListener,OnEditorActionListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v instanceof RadioButton){
				search_kw.setText("");
				kw=search_kw.getText().toString();
				index=moregroup.indexOfChild(v);
				adapter=new MoreAdapter(MoreFragment.this.getActivity(), more[index],index);
				prlistview.setAdapter(adapter);
				prlistview.setRefreshing();
			}if(v.getId()==R.id.fabu_but){
				toreport();
			}
			
		}
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if(actionId==EditorInfo.IME_ACTION_SEARCH){
				kw=search_kw.getText().toString();
				if(kw.length()>0){
					if(mt!=null){
						mt.cancel(true);
					}
					mt=new MoreAsyncTask(MoreFragment.this.getActivity(),handler,adapter.getListData(),page,true,true);
					mt.execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()),action[index],kw);
				}
			}
			return false;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			if(mt!=null){
				mt.cancel(true);
			}
			mt=new MoreAsyncTask(MoreFragment.this.getActivity(),handler,adapter.getListData(),page,true,false);
			mt.execute(String.valueOf(page.getFirstPage()),String.valueOf(page.getSize()),action[index],kw);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			if(page.hasNext()){
				if(mt!=null){
					mt.cancel(true);
				}
				mt=new MoreAsyncTask(MoreFragment.this.getActivity(),handler,adapter.getListData(),page,false,false);
				mt.execute(String.valueOf(page.getNextPage()),String.valueOf(page.getSize()),action[index],kw);
			}else{
				handler.sendEmptyMessage(1);
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			MoreInfo minfo=(MoreInfo)adapter.getItem(position-1);
			if(minfo!=null){
				toDetail(minfo,index);
			}
		}
	}
	
	private void toDetail(MoreInfo minfo,int index){
		Intent intent=new Intent(this.getActivity(),Nj123DetailActivity.class);
		intent.putExtra("info", minfo);
		intent.putExtra("index", index);
		startActivity(intent);
	}
	
	private void toreport(){
		Intent intent=new Intent(this.getActivity(),ReportActivity.class);
		intent.putExtra("index", index);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isupdate){
			prlistview.setRefreshing();
		}
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				isupdate=false;
				adapter.notifyDataSetChanged();
				prlistview.onRefreshComplete();
			}
		}
		
	};
	
}

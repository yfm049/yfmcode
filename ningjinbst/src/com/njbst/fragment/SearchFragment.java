package com.njbst.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.njbst.adapter.SearchAdapter;
import com.njbst.async.Kw_searchAsyncTask;
import com.njbst.async.SearchAsyncTask;
import com.njbst.pojo.Search;
import com.njbst.pro.DefaultDetailActivity;
import com.njbst.pro.R;

public class SearchFragment extends BaseFragment {

	
	
	private View view;
	private AutoCompleteTextView search_kw;
	private ListView searchlist;
	private ListenerImpl listener;
	
	public String kw;
	
	private List<Search> ls=new ArrayList<Search>();
	
	private SearchAdapter sadapter;
	
	private List<String> hs=new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view=inflater.inflate(R.layout.fragment_search, container,false);
			search_kw=(AutoCompleteTextView)view.findViewById(R.id.search_kw);
			ListView searchlist=(ListView)view.findViewById(R.id.searchlist);
			sadapter=new SearchAdapter(SearchFragment.this.getActivity(), ls);
			searchlist.setAdapter(sadapter);
			listener=new ListenerImpl();
			search_kw.setOnEditorActionListener(listener);
			searchlist.setOnItemClickListener(listener);
			search_kw.addTextChangedListener(listener);
			adapter = new ArrayAdapter<String>(SearchFragment.this.getActivity(),  
	                android.R.layout.simple_list_item_2, hs.toArray(new String[hs.size()]));
			search_kw.setAdapter(adapter);
			search_kw.setThreshold(1);
			new Kw_searchAsyncTask(SearchFragment.this.getActivity(), handler, hs).execute("");
		}
		return view;
	}

	class ListenerImpl implements OnEditorActionListener,OnItemClickListener,TextWatcher{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if(actionId==EditorInfo.IME_ACTION_SEARCH){
				kw=search_kw.getText().toString();
				if(kw.length()>0){
					new SearchAsyncTask(SearchFragment.this.getActivity(),handler,ls).execute(kw);
				}
			}
			return false;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Search ad=(Search)sadapter.getItem(position);
			Intent intent=new Intent(SearchFragment.this.getActivity(),DefaultDetailActivity.class);
			intent.putExtra("title", ad.getName());
			intent.putExtra("content", ad.getAddress());
			intent.putExtra("imageurl", "");
			intent.putExtra("linkurl", ad.getLinkurl());
			startActivity(intent);
		}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			kw=search_kw.getText().toString();
			new Kw_searchAsyncTask(SearchFragment.this.getActivity(), handler, hs).execute(kw);
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				sadapter.notifyDataSetChanged();
			}else if(msg.what==2){
				adapter = new ArrayAdapter<String>(SearchFragment.this.getActivity(),  
		                R.layout.search_dropdown_item, hs.toArray(new String[hs.size()]));
				search_kw.setAdapter(adapter);
				kw=search_kw.getText().toString();
				if(kw.length()==0){
					search_kw.showDropDown();
				}
			}
		}
		
	};
}

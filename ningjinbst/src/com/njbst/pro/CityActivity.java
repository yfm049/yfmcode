package com.njbst.pro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.njbst.adapter.CityAdapter;
import com.njbst.pojo.CityInfo;
import com.njbst.utils.PinyinComparator;
import com.njbst.utils.XmlUtils;
import com.njbst.view.SideBar;
import com.njbst.view.SideBar.OnTouchingLetterChangedListener;

public class CityActivity extends ActionBarActivity {

	private List<CityInfo> lcity=new ArrayList<CityInfo>();
	private List<CityInfo> allcity;
	private ListView city_list;
	private CityAdapter adapter;
	private EditText city_search;
	private TextChangedListenerImpl listener;
	private PinyinComparator comprator;
	private SideBar sidebar;
	private TextView text_dialog;
	private ProgressBar prob;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		comprator=new PinyinComparator();
		city_list=(ListView)this.findViewById(R.id.city_list);
		city_search=(EditText)this.findViewById(R.id.city_search);
		sidebar=(SideBar)this.findViewById(R.id.sidebar);
		text_dialog=(TextView)this.findViewById(R.id.text_dialog);
		text_dialog.setVisibility(View.GONE);
		prob=(ProgressBar)this.findViewById(R.id.prob);
		adapter=new CityAdapter(this, lcity);
		city_list.setAdapter(adapter);
		listener=new TextChangedListenerImpl();
		city_search.addTextChangedListener(listener);
		sidebar.setTextView(text_dialog);
		sidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListenerImpl());
		new CityTask().execute("");
	}
	
	
	
	private void filterData(String filterStr){
		
		if(TextUtils.isEmpty(filterStr)){
			lcity.clear();
			lcity.addAll(allcity);
		}else{
			lcity.clear();
			for(CityInfo info : allcity){
				if(info.getName().indexOf(filterStr.toString()) != -1 || info.getPname().startsWith(filterStr.toString())){
					lcity.add(info);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(lcity, comprator);
		adapter.notifyDataSetChanged();
		city_list.setSelectionFromTop(0, 0);
	}
	
	class OnTouchingLetterChangedListenerImpl implements OnTouchingLetterChangedListener{

		@Override
		public void onTouchingLetterChanged(String s) {
			// TODO Auto-generated method stub
			int pos=adapter.getPositionForSection(s.charAt(0));
			if(pos!=-1){
				city_list.setSelection(pos);
			}
		}
		
	}
	
	
	
	class TextChangedListenerImpl implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			filterData(s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class CityTask extends AsyncTask<String, Integer, List<CityInfo>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			prob.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(List<CityInfo> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			prob.setVisibility(View.GONE);
			allcity=result;
			filterData("");
		}

		@Override
		protected List<CityInfo> doInBackground(String... params) {
			// TODO Auto-generated method stub
			return XmlUtils.getcitys(CityActivity.this);
			
		}
		
	}

	public void tohome(){
		Intent intent=new Intent(this,MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}
}

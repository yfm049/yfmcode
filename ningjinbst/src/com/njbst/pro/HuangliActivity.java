package com.njbst.pro;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.njbst.adapter.HuangliAdapter;
import com.njbst.async.HuangliAsyncTask;
import com.njbst.pojo.HuangliInfo;

public class HuangliActivity extends ActionBarActivity {

	private List<HuangliInfo> lhl=new ArrayList<HuangliInfo>();
	private HuangliAdapter hladapter;
	
	private ListView huangli_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huangli);
		huangli_list=(ListView)this.findViewById(R.id.huangli_list);
		hladapter=new HuangliAdapter(this, lhl);
		huangli_list.setAdapter(hladapter);
		initdata();
	}
	private void initdata(){
		new HuangliAsyncTask(this,handler,lhl).execute("");
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				hladapter.notifyDataSetChanged();
			}
		}
		
	};
}

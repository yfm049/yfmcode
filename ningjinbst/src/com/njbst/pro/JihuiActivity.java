package com.njbst.pro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.njbst.adapter.JihuiAdapter;
import com.njbst.async.JihuiAsyncTask;

public class JihuiActivity extends ActionBarActivity {

	private ListView jihui_list,miaohui_list;
	private String[] timestr=new String[]{"今天","明天","后天"};
	private List<String> jihuilist=new ArrayList<String>();
	private List<String> miaohuilist=new ArrayList<String>();
	private JihuiAdapter jihuiadapter,miaohuiadapter;
	private RadioGroup timegroup;
	private ListenerImpl listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jihui);
		listener=new ListenerImpl();
		timegroup=(RadioGroup)this.findViewById(R.id.timegroup);
		timegroup.setOnCheckedChangeListener(listener);
		jihui_list=(ListView)this.findViewById(R.id.jihui_list);
		miaohui_list=(ListView)this.findViewById(R.id.miaohui_list);
		jihuiadapter=new JihuiAdapter(this, jihuilist);
		miaohuiadapter=new JihuiAdapter(this, miaohuilist);
		jihui_list.setAdapter(jihuiadapter);
		miaohui_list.setAdapter(miaohuiadapter);
		inittimestring();
	}
	
	private void inittimestring(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		int ct=timegroup.getChildCount();
		for(int i=0;i<ct;i++){
			RadioButton rb=(RadioButton)timegroup.getChildAt(i);
			rb.setText(timestr[i]+"\r\n"+sdf.format(c.getTime()));
			rb.setTag(sdf.format(c.getTime()));
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		RadioButton rb=(RadioButton)timegroup.getChildAt(0);
		rb.setChecked(true);
	}
	
	private void Getdata(String time){
		new JihuiAsyncTask(this,handler,jihuilist,miaohuilist).execute(time);
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				jihuiadapter.notifyDataSetChanged();
				miaohuiadapter.notifyDataSetChanged();
			}
		}
		
	};
	
	class ListenerImpl implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			RadioButton rb=(RadioButton)group.findViewById(checkedId);
			if(rb!=null){
				String time=(String)rb.getTag();
				Getdata(time);
			}
		}
		
	}

}

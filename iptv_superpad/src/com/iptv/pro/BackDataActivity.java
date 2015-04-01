package com.iptv.pro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.iptv.adapter.PopbackAdapter;
import com.iptv.pojo.PrgItem;
import com.iptv.season.R;
import com.iptv.thread.ProFilmThread;
import com.iptv.utils.LayoutListener;

public class BackDataActivity extends Activity {

	private GridView griddata;
	private PopbackAdapter adapter;
	private String url;
	private String name;
	private String logourl;
	private List<String> datalist=new ArrayList<String>();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private LayoutListener listener;
	private OnItemClickListenerImpl itemlistener;
	private ProgressDialog pd;
	private ProFilmThread prothread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_play_popback);
		griddata=(GridView)super.findViewById(R.id.griddata);
		
		Intent intent=this.getIntent();
		Bundle bundle=intent.getExtras();
		if(bundle!=null){
			url=bundle.getString("url");
			name=bundle.getString("name");
			logourl=bundle.getString("logo");
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		for(int i=0;i<35;i++){
			datalist.add(sdf.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		adapter=new PopbackAdapter(this, datalist);
		griddata.setAdapter(adapter);
		listener=new LayoutListener(griddata,adapter);
		griddata.getViewTreeObserver().addOnGlobalLayoutListener(listener);
		itemlistener=new OnItemClickListenerImpl();
		griddata.setOnItemClickListener(itemlistener);
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String rq=datalist.get(arg2);
			Intent intent=new Intent(BackDataActivity.this,BackActivity.class);
			intent.putExtra("rq", rq);
			intent.putExtra("name", name);
			intent.putExtra("logo", logourl);
			intent.putExtra("url", url);
			BackDataActivity.this.startActivity(intent);
		}
		
	}
	private void show(String title, String msg) {
		pd = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.show();
	}
	public void getpro(String url){
		show("获取节目", "正在获取节目信息");
		prothread=new ProFilmThread(handler, url);
		prothread.start();
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==100){
				pd.dismiss();
				Toast.makeText(BackDataActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2){
				pd.dismiss();
				List<PrgItem> lprg=(List<PrgItem>)msg.obj;
				Log.i("tvinfo", lprg.size()+"");
				BackActivity.user.getPrglist().clear();
				BackActivity.user.getPrglist().addAll(lprg);
			}
		}
		
	};
}

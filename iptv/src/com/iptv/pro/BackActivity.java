package com.iptv.pro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.iptv.adapter.PinDaoAdapter;
import com.iptv.adapter.ProItemAdapter;
import com.iptv.season.R;
import com.iptv.pojo.BackData;
import com.iptv.pojo.Film;
import com.iptv.pojo.PrgItem;
import com.iptv.thread.FilmThread;
import com.iptv.thread.ProFilmThread;

public class BackActivity extends Activity {

	private ListView pdlist,jmlist;
	private PinDaoAdapter pdadapter;
	private FilmThread fthread;
	private ProFilmThread prothread;
	private ProItemAdapter proadapter;
	private ProgressDialog pd;
	public static BackData user;
	private RadioGroup timegroup;
	private String currtime="";
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_back);
		user=new BackData();
		pdlist=(ListView)super.findViewById(R.id.pdlist);
		jmlist=(ListView)super.findViewById(R.id.jmlist);
		timegroup=(RadioGroup)super.findViewById(R.id.timegroup);
		timegroup.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		
		
		pdadapter=new PinDaoAdapter(this, user);
		proadapter=new ProItemAdapter(this, user);
		pdlist.setAdapter(pdadapter);
		jmlist.setAdapter(proadapter);
		pdlist.setOnItemClickListener(new OnItemClickListenerImpl(1));
		jmlist.setOnItemClickListener(new OnItemClickListenerImpl(2));
		initbackdate();
		
	}
	public void initbackdate(){
		show("获取","正在获取频道信息...");
		fthread=new FilmThread(handler, "tv_list.jsp");
		fthread.start();
	}
	private void show(String title, String msg) {
		pd = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.show();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				pd.dismiss();
				List<Film> lf=(List<Film>)msg.obj;
				Log.i("tvinfo", lf.size()+"");
				user.getFilmlist().clear();
				user.getFilmlist().addAll(lf);
				pdlist.setItemChecked(0, true);
				pdadapter.notifyDataSetChanged();
				try {
					Calendar  calendar=Calendar.getInstance();
					calendar.setTime(sdf.parse(BackData.currtime));
					for(int i=0;i<BackData.cday;i++){
						RadioButton radio=(RadioButton)LayoutInflater.from(BackActivity.this).inflate(R.layout.radioitem, null);
						radio.setId(i);
						radio.setText(sdf.format(calendar.getTime()));
						calendar.add(Calendar.DATE, -1);
						timegroup.addView(radio);
						Log.i("tvinfo", radio.getId()+" radio id");
					}
					
					RadioButton radio=(RadioButton)timegroup.getChildAt(0);
					radio.setChecked(true);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.what==100){
				pd.dismiss();
				Toast.makeText(BackActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2){
				pd.dismiss();
				List<PrgItem> lprg=(List<PrgItem>)msg.obj;
				Log.i("tvinfo", lprg.size()+"");
				user.getPrglist().clear();
				user.getPrglist().addAll(lprg);
				proadapter.notifyDataSetChanged();
			}
		}
		
	};
	public void getpro(String url){
		show("获取节目", "正在获取节目信息");
		prothread=new ProFilmThread(handler, url);
		prothread.start();
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		private int type;
		public OnItemClickListenerImpl(int type){
			this.type=type;
		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(type==1){
				Film film=user.getFilmlist().get(arg2);
				if(film!=null){
					pdlist.setItemChecked(arg2, true);
					getDate();
					
				}
				
			}
			if(type==2){
				jmlist.setSelectionFromTop(arg2, 100);
				Intent intent=new Intent(BackActivity.this,BackPlayActivity.class);
				intent.putExtra("prg", arg2);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				BackActivity.this.startActivity(intent);
			}
		}
		
	}
	class OnCheckedChangeListenerImpl implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			RadioButton radio=(RadioButton)arg0.findViewById(arg1);
			if(radio.isChecked()){
				currtime=radio.getText().toString();
				Log.i("tvinfo", arg1+"选中日期"+currtime);
				getDate();
			}
			
		}
		
	}
	public void getDate(){
		int pos=pdlist.getCheckedItemPosition();
		Log.i("tvinfo", pos+"--"+currtime);
		if(user.getFilmlist().size()>0){
			Film film=user.getFilmlist().get(pos);
			getpro(film.getUrl()+"&rq="+currtime);
		}
	}


}

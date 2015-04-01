package com.iptv.pro;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.HRTV.R;
import com.iptv.LayoutListener.LayoutListener;
import com.iptv.adapter.BackJmAdapter;
import com.iptv.adapter.BackPdAdapter;
import com.iptv.pojo.Channel;
import com.iptv.pojo.PrgItem;
import com.iptv.thread.BackChannelThread;
import com.iptv.thread.ProFilmThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.NetUtils;

public class BackDataActivity extends Activity {

	private ListView channellist,backlist;
	private RadioGroup rqselect;
	private BackPdAdapter pdadapter;
	private BackJmAdapter jmadapter;
	private TextView currdate;
	private List<Channel> listcn=new ArrayList<Channel>();
	private List<PrgItem> listpro=new ArrayList<PrgItem>();
	private int rqs[]=new int[]{R.id.backrq1,R.id.backrq2,R.id.backrq3,R.id.backrq4,R.id.backrq5,};
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private OnListenerImpl listener;
	private int currselectid=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_backdata);
		channellist=(ListView)this.findViewById(R.id.channellist);
		channellist.setVisibility(View.VISIBLE);
		backlist=(ListView)this.findViewById(R.id.backlist);
		backlist.setVisibility(View.VISIBLE);
		rqselect=(RadioGroup)this.findViewById(R.id.rqselect);
		pdadapter=new BackPdAdapter(this, listcn);
		channellist.setAdapter(pdadapter);
		
		jmadapter=new BackJmAdapter(this, listpro);
		backlist.setAdapter(jmadapter);
		channellist.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener(channellist, pdadapter));
		backlist.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener(backlist, jmadapter));

		
		
		currdate=(TextView)this.findViewById(R.id.currdate);
		listener=new OnListenerImpl();
		NetUtils.GetBackChannelData(handler);
		channellist.setOnItemClickListener(new OnListenerImpl(R.id.channellist));
		backlist.setOnItemClickListener(new OnListenerImpl(R.id.backlist));
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==BackChannelThread.backchannelsuc){
				List<Channel> data=(List<Channel>)msg.obj;
				listcn.clear();
				listcn.addAll(data);
				pdadapter.setCpos(0);
				pdadapter.notifyDataSetChanged();
				channellist.setSelection(0);
				setRqselect();
			}else if(msg.what==BackChannelThread.backchannelfail){
				Toast.makeText(BackDataActivity.this,R.string.backchannel_fail, Toast.LENGTH_SHORT).show();
			}else if(msg.what==ProFilmThread.prgitemsuc){
				List<PrgItem> lp=(List<PrgItem>)msg.obj;
				listpro.clear();
				listpro.addAll(lp);
				jmadapter.setSelectpos(-1);
				jmadapter.notifyDataSetChanged();
			}else if(msg.what==ProFilmThread.prgitemfail){
				Toast.makeText(BackDataActivity.this,R.string.backproitem_fail, Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	private void setRqselect(){
		try {
			currdate.setText(ComUtils.currdata);
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(sdf.parse(ComUtils.currdata));
			for(int i=0;i<rqs.length;i++){
				RadioButton rq=(RadioButton)this.findViewById(rqs[i]);
				rq.setText(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_YEAR, -1);
			}
			rqselect.check(R.id.backrq1);
			rqselect.setOnCheckedChangeListener(listener);
			getProlist();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getProlist(){
		if(listcn.size()>currselectid){
			Channel channel=listcn.get(currselectid);
			int id=rqselect.getCheckedRadioButtonId();
			RadioButton rq=(RadioButton)this.findViewById(id);
			if(channel!=null&&rq!=null){
				listpro.clear();
				jmadapter.notifyDataSetChanged();
				String url=channel.getHttpurl()+"&rq="+rq.getText().toString();
				NetUtils.GetBackProData(url, handler);
			}
		}
		
		
	}
	class OnListenerImpl implements OnCheckedChangeListener,OnItemClickListener{

		private int id;
		public OnListenerImpl(int id){
			this.id=id;
		}
		public OnListenerImpl(){
			
		}
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			getProlist();
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(id==R.id.channellist){
				currselectid=arg2;
				pdadapter.setCpos(arg2);
				pdadapter.notifyDataSetChanged();
				getProlist();
			}else if(id==R.id.backlist){
				jmadapter.setSelectpos(arg2);
				jmadapter.notifyDataSetChanged();
				Intent intent=new Intent(BackDataActivity.this,BackPlayActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("prolist", (Serializable)listpro);
				intent.putExtra("pos", arg2);
				BackDataActivity.this.startActivity(intent);
			}
		}
	}
	

}

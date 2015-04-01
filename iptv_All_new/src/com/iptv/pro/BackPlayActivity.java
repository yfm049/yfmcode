package com.iptv.pro;

import io.vov.vitamio.LibsChecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.forcetech.android.ForceTV;
import com.iptv.play.Player;
import com.iptv.play.SystemPlayer;
import com.iptv.play.VLCPlayer;
import com.iptv.play.VitamioPlayer;
import com.iptv.pojo.PrgItem;
import com.iptv.thread.ForceTvThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.LogUtils;
import com.iptv.view.EventLinearLayout;
import com.mediatv.R;

public class BackPlayActivity extends Activity {

	

	private List<PrgItem> listpro = new ArrayList<PrgItem>();
	private int pos = 0;
	private SurfaceView playview;
	private TextView channelname, channelstatus;
	private LinearLayout statusview;
	private OnListener listener;
	private Player player;
	private SeekBar playSeek;
	private TextView currtime, totaltime,speed;
	private ImageButton prebut, nextbut, playbut;
	private int pdviewhide = 2000,bytespeed=2003;
	private PopupWindow pw;
	private View menuview;
	private int seekBarUpdate=80;
	private int seeking=88;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.setContentView(R.layout.activity_playback);
		ForceTV.initForceClient(); 
		menuview=LayoutInflater.from(this).inflate(R.layout.back_play_menu, null);
		playSeek = (SeekBar) menuview.findViewById(R.id.playseek);
		currtime = (TextView) menuview.findViewById(R.id.currtime);
		totaltime = (TextView) menuview.findViewById(R.id.totaltime);
		prebut = (ImageButton) menuview.findViewById(R.id.prebut);
		nextbut = (ImageButton) menuview.findViewById(R.id.nextbut);
		playbut = (ImageButton) menuview.findViewById(R.id.playbut);
		
		statusview = (LinearLayout) super.findViewById(R.id.statusview);
		speed = (TextView) super.findViewById(R.id.speed);
		channelname = (TextView) super.findViewById(R.id.channelname);
		channelstatus = (TextView) super.findViewById(R.id.channelstatus);
		playview = (SurfaceView) super.findViewById(R.id.playview);
		listener = new OnListener();
		playview.setOnClickListener(listener);
		playview.setOnKeyListener(listener);
		prebut.setOnClickListener(listener);
		nextbut.setOnClickListener(listener);
		playbut.setOnClickListener(listener);
		playSeek.setOnSeekBarChangeListener(listener);
		Intent intent = this.getIntent();
		if (intent != null) {
			Serializable pro = intent.getSerializableExtra("prolist");
			pos = intent.getIntExtra("pos", 0);
			if (pro != null) {
				List<PrgItem> lp = (List<PrgItem>) pro;
				listpro.clear();
				listpro.addAll(lp);
			}
		}
		if(ComUtils.getConfig(this, "decode", R.id.system)==R.id.system){
			player = new SystemPlayer(this, playview, handler);
		}else if(ComUtils.getConfig(this, "decode", R.id.system)==R.id.vitamio){
			if(!LibsChecker.checkVitamioLibs(this)){
				return;
			}
			player = new VitamioPlayer(this, playview, handler);
		}else{
			player = new VLCPlayer(this, playview, handler);
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.i("tvinfo", "message "+msg.what);
			if (msg.what == Player.surfaceCreated) {
				showspeed();
				playPrgItem();
			} else if (msg.what == Player.mediaplayerPrepared) {
				player.play();
				setStatus(false, "开始播放");
			} else if (msg.what == Player.mediaplayererror) {
				setStatus(true, "播放错误");
			} else if (msg.what == Player.mediaplayerbufferedStart) {
				setStatus(true, "正在缓冲");
			} else if (msg.what == Player.mediaplayerbufferedEnd) {
				setStatus(false, "缓冲完成");
			} else if (msg.what == Player.mediaplayerserverDied) {
				setStatus(true, "链接超时");
			} else if (msg.what == Player.mediaplayerstart) {
				setStatus(false, "播放中");
				handler.sendEmptyMessageDelayed(seekBarUpdate, 1000);
			} else if (msg.what == Player.mediaplayerseekcomplete) {
				setStatus(false, "开始播放");
				handler.removeMessages(seekBarUpdate);
				handler.sendEmptyMessageDelayed(seekBarUpdate, 1000);
			} else if (msg.what == ForceTvThread.ForceTvsuc) {
				player.setPlayUrl(msg.obj.toString());
				setStatus(true, "加载成功");
			} else if (msg.what == ForceTvThread.ForceTvfail) {
				ComUtils.showtoast(BackPlayActivity.this, R.string.forcetv_fail);
			}else if (msg.what == pdviewhide) {
				pw.dismiss();
			} else if (msg.what == seekBarUpdate) {
				updateSeek();
			} else if (msg.what == Player.surfaceDestroyed) {
				handler.removeMessages(bytespeed);
				handler.removeMessages(seekBarUpdate);
			} else if (msg.what == Player.mediaplayercomplete) {
				handler.removeMessages(seekBarUpdate);
				playbut.setImageResource(R.drawable.play_c);
				BackPlayActivity.this.finish();
			}else if(msg.what==seeking){
				player.SeeKto((int) (playSeek.getProgress()*1000));
			}else if(msg.what==bytespeed){
				showspeed();
			}
		}

	};

	private long start=-1;
	private long duration=0;
	private void updateSeek() {
		if(duration<=0){
			duration=player.getDuration();
		}else{
			totaltime.setText(ComUtils.longToString(duration));
			playSeek.setMax((int) (duration/1000));
		}
		if(start<1100){
			start=player.getCurrTime();
		}
		Log.i("tvinfo", "进度条"+start+" "+duration);
		if(start>1100&&duration>0){
			playSeek.setEnabled(true);
			int cur=(int) ((player.getCurrTime()-start)/1000);
			playSeek.setProgress(cur);
		}
		handler.sendEmptyMessageDelayed(seekBarUpdate, 1000);
	}
	private void initseek(){
		start=-1;
		duration=0;
		playSeek.setProgress(0);
		playSeek.setEnabled(false);
		currtime.setText(ComUtils.longToString(0));
		totaltime.setText(ComUtils.longToString(0));
	}
	private long oldbyte=0;
	public void showspeed(){
		if(oldbyte!=0){
			long newbyte=ComUtils.getUidRxBytes(this)-oldbyte;
			speed.setText("("+newbyte+"K/S)");
		}
		oldbyte=ComUtils.getUidRxBytes(this);
		handler.sendEmptyMessageDelayed(bytespeed, 1000);
	}
	public void playPrgItem() {
		if (listpro.size() > pos) {
			handler.removeMessages(seekBarUpdate);
			playSeek.setEnabled(false);
			currtime.setText(ComUtils.longToString(0));
			totaltime.setText(ComUtils.longToString(0));
			PrgItem item = listpro.get(pos);
			channelname.setText(item.getName());
			setStatus(true, "正在加载");
			ForceTvUtils.switch_chan(item.getP2purl(), item.getHotlink(),
					ComUtils.getConfig(BackPlayActivity.this, "name", ""),
					handler);
			initseek();
		} else {
			Toast.makeText(this, R.string.backitemnotfound, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void setStatus(boolean isshow, String state) {
		if (isshow) {
			channelstatus.setText(state);
			statusview.setVisibility(View.VISIBLE);
		} else {
			statusview.setVisibility(View.INVISIBLE);
		}
	}
	private void showpopMenu(){
		if(pw==null){
			pw=new PopupWindow(menuview,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			pw.setBackgroundDrawable(new PaintDrawable());
			EventLinearLayout eventlayout=(EventLinearLayout)menuview.findViewById(R.id.eventlayout);
			eventlayout.setHandler(handler,pdviewhide);
			pw.setFocusable(true);
			pw.setOutsideTouchable(true);
		}
		pw.showAtLocation(playview, Gravity.LEFT |Gravity.BOTTOM, 0, 0);
		handler.sendEmptyMessageDelayed(pdviewhide, 10000);
	}
	class OnListener implements OnClickListener, OnSeekBarChangeListener,
			OnKeyListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (view.getId() == R.id.playview) {
				showpopMenu();
				playbut.requestFocus();
			} else if (view.getId() == R.id.prebut) {
				if (pos - 1 >= 0) {
					pos = pos - 1;
					playPrgItem();
				}
			} else if (view.getId() == R.id.nextbut) {
				if (pos + 1 < listpro.size()) {
					pos = pos + 1;
					playPrgItem();
				}
			} else if (view.getId() == R.id.playbut) {
				if (player.isPlaying()) {
					player.pause();
					playbut.setImageResource(R.drawable.play_c);
				} else if (player.isPause()) {
					playbut.setImageResource(R.drawable.pauser_c);
					player.play();
				}
			}
		}

		int progress;

		@Override
		public void onProgressChanged(SeekBar bar, int pos, boolean arg2) {
			// TODO Auto-generated method stub
			if (arg2) {
				handler.removeMessages(seekBarUpdate);
				handler.removeMessages(seeking);
				handler.sendEmptyMessageDelayed(seeking, 200);
			}
			currtime.setText(ComUtils.longToString(bar.getProgress()*1000));
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (v.getId() == R.id.playview) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						showpopMenu();
						playSeek.requestFocus();
					} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						showpopMenu();
						playSeek.requestFocus();
					}
				}
			}
			return false;
		}

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.write("tvinfo", "backplayerActivity destroy");
		ForceTV.stop();
	}

}

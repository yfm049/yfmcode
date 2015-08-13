package com.iptv.pro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.forcetech.android.ForceTV;
import com.iptv.HRTV.R;
import com.iptv.play.Player;
import com.iptv.pojo.PrgItem;
import com.iptv.thread.ControlThread;
import com.iptv.thread.ForceTvThread;
import com.iptv.thread.SeekBarUpdateThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.LogUtils;
import com.iptv.utils.NetUtils;
import com.iptv.utils.VideoStreamUtils;

public class BackPlayActivity extends Activity {

	

	private List<PrgItem> listpro = new ArrayList<PrgItem>();
	private int pos = 0;
	private SurfaceView playview;
	private TextView channelname, channelstatus;
	private LinearLayout statusview;
	private OnListener listener;
	private Player player;
	private SeekBar playSeek;
	private TextView currtime, totaltime;
	private ControlThread PdViewthread;
	private ImageView prebut, nextbut, playbut;
	private int pdviewhide = 2000;
	private PopupWindow pw;
	private View menuview;
	private boolean isseek=false;
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
		prebut = (ImageView) menuview.findViewById(R.id.prebut);
		nextbut = (ImageView) menuview.findViewById(R.id.nextbut);
		playbut = (ImageView) menuview.findViewById(R.id.playbut);
		
		statusview = (LinearLayout) super.findViewById(R.id.statusview);
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
		player = new Player(this, playview, handler);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Player.surfaceCreated) {
				playPrgItem();
			} else if (msg.what == Player.mediaplayerPrepared) {
				player.play();
				setStatus(false, "开始播放");
				NetUtils.DoSeekBarUpdate(true, handler);
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
				EnableplaySeek(true);
			} else if (msg.what == Player.mediaplayerseekcomplete) {
				player.play();
				setStatus(false, "开始播放");
				isseek=false;
			} else if (msg.what == ForceTvThread.ForceTvsuc) {
				player.setPlayUrl(msg.obj.toString());
				setStatus(true, "加载成功");
			} else if (msg.what == ForceTvThread.ForceTvfail) {
				Toast.makeText(BackPlayActivity.this, R.string.forcetv_fail,
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == pdviewhide) {
				pw.dismiss();
			} else if (msg.what == SeekBarUpdateThread.seekBarUpdate) {
				updateSeek();
			} else if (msg.what == Player.surfaceDestroyed) {
				NetUtils.DoSeekBarUpdate(false, handler);
			} else if (msg.what == Player.mediaplayercomplete) {
				NetUtils.DoSeekBarUpdate(false, handler);
				playbut.setImageResource(R.drawable.play_c);
				BackPlayActivity.this.finish();
			}
		}

	};

	private void EnableplaySeek(boolean isenabled) {
		if (isenabled) {
			playSeek.setEnabled(true);
			int duration = player.gettotalTime();
			totaltime.setText(ComUtils.longToString(duration));
			playSeek.setMax(duration/1000);
		} else {
			playSeek.setEnabled(false);
		}

	}

	private void updateSeek() {
		int position = player.getCurrTime();
		if (position >= 0&&!isseek) {
			currtime.setText(ComUtils.longToString(player.getCurrTime()));
			playSeek.setProgress(position/1000);
		}
	}

	public void playPrgItem() {
		if (listpro.size() > pos) {
			EnableplaySeek(false);
			PrgItem item = listpro.get(pos);
			channelname.setText(item.getName());
			setStatus(true, "正在加载");
			ForceTvUtils.switch_chan(item.getP2purl(), item.getHotlink(),
					ComUtils.getConfig(BackPlayActivity.this, "name", ""),
					handler);
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
			pw.setFocusable(true);
			pw.setOutsideTouchable(true);
			
		}
		pw.showAtLocation(playview, Gravity.LEFT |Gravity.BOTTOM, 0, 0);
		if (PdViewthread != null && PdViewthread.isIsrun()) {
			PdViewthread.resettime();
		} else {
			PdViewthread = new ControlThread(handler, 10, pdviewhide);
			PdViewthread.start();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (PdViewthread != null && PdViewthread.isIsrun()) {
			PdViewthread.resettime();
		}
		return super.onKeyDown(keyCode, event);
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
				PdViewthread.resettime();
				if (pos - 1 >= 0) {
					pos = pos - 1;
					playPrgItem();
				}
			} else if (view.getId() == R.id.nextbut) {
				PdViewthread.resettime();
				if (pos + 1 < listpro.size()) {
					pos = pos + 1;
					playPrgItem();
				}
			} else if (view.getId() == R.id.playbut) {
				PdViewthread.resettime();
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
				isseek=true;
				PdViewthread.resettime();
				player.SeeKto(bar.getProgress()*1000);
			}
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
		player.release();
		ForceTvUtils.StopChan();
	}

}

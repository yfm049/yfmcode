package com.iptv.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.season.R;
import com.iptv.thread.BackPlayThread;
import com.iptv.thread.DisplayThread;
import com.iptv.thread.SeekBarUpdateThread;
import com.iptv.utils.Utils;

public class BackPlayActivity extends Activity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		playmenuinit();
		return super.onKeyDown(keyCode, event);
	}

	private SurfaceView palyview;
	private LinearLayout progress;
	private MediaPlayer player;
	private SurfaceHolder holder;
	private int screenWidth;
	private int screenHeight;
	private MediaPlayerListenerImpl MediaPlayerListener;
	private TextView title,currtime,totaltime;
	private LinearLayout playmenu;
	private ProgressDialog pd;
	private SharedPreferences sp;
	private int prg=-1;
	private BackPlayThread bpthread=null;
	private DisplayThread dthread=null;
	private SeekBarUpdateThread tupdate=null;
	private SeekBar playSeek=null;
	private ImageView prebut,nextbut,playbut;
	private boolean ispause=false,isseek=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.setContentView(R.layout.activity_backplay);
		
		Bundle bundle=this.getIntent().getExtras();
		if(bundle!=null){
			if(bundle.containsKey("prg")){
				prg=bundle.getInt("prg",-1);
			}
		}
		sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);
		MediaPlayerListener = new MediaPlayerListenerImpl();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		palyview = (SurfaceView) super.findViewById(R.id.palyview);
		holder = palyview.getHolder();
		holder.setFixedSize(screenWidth, screenHeight);
		holder.addCallback(MediaPlayerListener);
		progress = (LinearLayout) super.findViewById(R.id.progress);
		playmenu = (LinearLayout) super.findViewById(R.id.playmenu);
		title = (TextView) super.findViewById(R.id.title);
		currtime = (TextView) super.findViewById(R.id.currtime);
		totaltime = (TextView) super.findViewById(R.id.totaltime);
		playSeek=(SeekBar)findViewById(R.id.playseek);
		prebut=(ImageView)findViewById(R.id.prebut);
		nextbut=(ImageView)findViewById(R.id.nextbut);
		playbut=(ImageView)findViewById(R.id.playbut);
		playSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImpl());
		playSeek.requestFocus();
		palyview.setOnClickListener(new OnClickListenerImpl());
		prebut.setOnClickListener(new OnClickListenerImpl());
		nextbut.setOnClickListener(new OnClickListenerImpl());
		playbut.setOnClickListener(new OnClickListenerImpl());
		playmenu.setVisibility(View.INVISIBLE);
	}

	class OnSeekBarChangeListenerImpl implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar bar, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", arg2+"");
			if(arg2){
				isseek=true;
				player.seekTo(bar.getProgress()*1000);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar bar) {
			// TODO Auto-generated method stub
		}
		
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			playmenuinit();
			if(v.getId()==R.id.prebut){
				if(prg-1>=0){
					prg=prg-1;
					startplaythread();
				}
			}
			if(v.getId()==R.id.nextbut){
				if(prg+1<BackActivity.user.getPrglist().size()){
					prg=prg+1;
					startplaythread();
				}
			}
			if(v.getId()==R.id.playbut){
				if(player.isPlaying()&&!ispause){
					ispause=true;
					player.pause();
					playbut.setImageResource(R.drawable.play_c);
				}else if(ispause){
					ispause=false;
					playbut.setImageResource(R.drawable.pauser_c);
					player.start();
				}
			}
			
		}
		
	}
	public void playmenuinit(){
		if(playmenu.getVisibility()!=View.VISIBLE){
			playmenu.setVisibility(View.VISIBLE);
			playSeek.requestFocus();
		}
		if(dthread!=null&&dthread.isrun){
			dthread.reset();
		}else{
			dthread=new DisplayThread(handler);
			dthread.start();
		}
	}

	private void initplayer() {
		if (player == null) {
			player = new MediaPlayer();
			player.setOnPreparedListener(MediaPlayerListener);
			player.setOnVideoSizeChangedListener(MediaPlayerListener);
			player.setOnSeekCompleteListener(MediaPlayerListener);
			player.setOnInfoListener(MediaPlayerListener);
			player.setOnCompletionListener(MediaPlayerListener);
			player.setOnErrorListener(MediaPlayerListener);
			player.setOnBufferingUpdateListener(MediaPlayerListener);
		}

	}



	public void play(String url) {
		Log.i("tvinfo", "开始播放 " + BackActivity.user.getPrglist().get(prg).getName());
		playSeek.setProgress(0);
		playSeek.setEnabled(false);
		player.reset();
		try {
			player.setDataSource(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		player.setDisplay(holder);
		player.prepareAsync();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//Log.i("tvinfo", msg.what + "-" + msg.obj);
			if (msg.what == 1) {
				progress.setVisibility(View.VISIBLE);
				title.setText(msg.obj + BackActivity.user.getPrglist().get(prg).getName()+ " ...");
			}
			if (msg.what == 2) {
				Log.i("tvinfo", msg.obj + BackActivity.user.getPrglist().get(prg).getName());
				title.setText(msg.obj +  BackActivity.user.getPrglist().get(prg).getName() + " ...");
			}
			if (msg.what == 3) {
				title.setText(msg.obj + BackActivity.user.getPrglist().get(prg).getName() + " ...");
				play(BackActivity.user.getPrglist().get(prg).getplayurl());
			}
			if (msg.what == 4) {
				title.setText(msg.obj +BackActivity.user.getPrglist().get(prg).getName() + " ...");
			}
			if (msg.what == 9) {
				show("获取", "正在获取播放列表,请稍后...");
			}
			if(msg.what==-100){
				playmenu.setVisibility(View.GONE);
			}
			if(msg.what==66){
				if(player!=null&&player.isPlaying()&&!isseek){
					currtime.setText(Utils.longToString(player.getCurrentPosition()));
					playSeek.setProgress(player.getCurrentPosition()/1000);
				}
			}
		}

	};

	private void show(String title, String msg) {
		pd = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.show();
	}

	class MediaPlayerListenerImpl implements OnPreparedListener,
			OnVideoSizeChangedListener, OnSeekCompleteListener, OnInfoListener,
			OnCompletionListener, OnErrorListener, OnBufferingUpdateListener,
			SurfaceHolder.Callback {

		@Override
		public void onPrepared(MediaPlayer player) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "onPrepared");
			player.start();
			playSeek.setEnabled(true);
			playSeek.setMax(player.getDuration()/1000);
			totaltime.setText(Utils.longToString(player.getDuration()));
			if(tupdate==null){
				tupdate=new SeekBarUpdateThread(handler);
				tupdate.start();
			}
			
		}

		@Override
		public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "onVideoSizeChanged");
			progress.setVisibility(View.GONE);
		}

		@Override
		public void onSeekComplete(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "onSeekComplete");
			player.start();
			isseek=false;
		}

		@Override
		public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "onInfo");
			return false;
		}

		@Override
		public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "onCompletion");
			BackPlayActivity.this.finish();
		}

		@Override
		public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			Toast.makeText(BackPlayActivity.this, "播放错误", Toast.LENGTH_LONG).show();
			Log.i("tvinfo", "出现错误");
			return false;
		}

		@Override
		public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "onBufferingUpdate  "+arg1);
			title.setText(BackActivity.user.getPrglist().get(prg).getName() + "正在缓冲...");
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "surfaceChanged");

		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "surfaceCreated");
			initplayer();
			Log.i("tvinfo", "开始播放 " + prg);
			startplaythread();
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "surfaceDestroyed");
			Log.i("tvinfo", "调用releaseTRDecryptionServer销毁");
		}

	}
	private void startplaythread(){
		if(prg!=-1){
			Log.i("tvinfo", "调用resetTRDecryptionServer换台");
			String userid=sp.getString("name", "");
			bpthread=new BackPlayThread(handler, BackActivity.user.getPrglist().get(prg).getparam(userid));
			bpthread.start();
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (player != null) {
			Log.i("player", "player stop");
			player.release();
			player=null;
		}
		if(bpthread!=null){
			bpthread.close();
		}
		if(tupdate!=null){
			tupdate.setIsrun(false);
		}
	}

}

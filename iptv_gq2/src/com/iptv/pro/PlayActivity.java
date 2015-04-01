package com.iptv.pro;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.adapter.TvinfoAdapter;
import com.iptv.xsj7188.R;
import com.iptv.pojo.LiveTV;
import com.iptv.pojo.TvInfo;
import com.iptv.pojo.User;
import com.iptv.thread.PlayThread;
import com.iptv.thread.ShouCangThread;
import com.iptv.utils.SqliteUtils;
import com.iptv.utils.Utils;

public class PlayActivity extends Activity {

	private SurfaceView palyview;
	private PopupWindow pw;
	private LinearLayout progress;
	private ListView playlist;
	private TvinfoAdapter adapter;
	private MediaPlayer player;
	private SurfaceHolder holder;
	private int screenWidth;
	private int screenHeight;
	private MediaPlayerListenerImpl MediaPlayerListener;
	private TvInfo tvinfo, sctv;
	private TextView title;
	private PlayThread playthread;
	private LinearLayout tl;
	private TextView ptitle;
	private ProgressDialog pd;
	private ShouCangThread scthread;
	private User user;
	private SharedPreferences sp;
	private SqliteUtils su;
	private int itemheight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_play);
		sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);
		inituser();
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
		palyview.setOnKeyListener(new OnKeyListenerImpl());
		title = (TextView) super.findViewById(R.id.title);
		palyview.setOnClickListener(new OnClickListenerImpl());
		palyview.requestFocus();

	}

	private void inituser(){
		user=new User();
		su=new SqliteUtils(this);
		List<LiveTV> listlivetv=su.getAllliveTv();
		user.getLivetvlist().addAll(listlivetv);
		int cl=sp.getInt("cruulive", 0);
		if(listlivetv.size()>0){
			if(cl<listlivetv.size()){
				user.setTempliveid(cl);
			}
			LiveTV livetv=listlivetv.get(user.getTempliveid());
			List<TvInfo> listtv=su.getAllTvinfo(livetv.getId());
			user.getTemplist().addAll(listtv);
			int ct=sp.getInt("currtv", 0);
			if(listtv.size()>0&&ct<listtv.size()){
				user.setCurrtvid(ct);
			}
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

	class OnItemClickListenerImpl implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//播放当前选中
			TvInfo info=user.playtv(arg2);
			if (info != null) {
				startplay(info);
			}

		}

	}
	public void savedata(int currliveid,int currtvid){
		Editor edit=sp.edit();
		edit.putInt("cruulive", currliveid);
		edit.putInt("currtv", currtvid);
		edit.commit();
	}
	public void startplay(TvInfo info) {
		if (!info.equals(tvinfo)) {
			tvinfo = info;
			savedata(user.getCurrliveid(),user.getCurrtvid());
			playlist.setSelection(user.getCurrtvid());
			Log.i("tvinfo", ""+tvinfo.getName());
			if (playthread != null) {
				playthread.close();
			}
			String userid=sp.getString("name", "");
			playthread = new PlayThread(PlayActivity.this,handler, tvinfo.getparam(userid));
			playthread.start();
		}
		pw.dismiss();
	}

	public void play(String url) {
		Log.i("tvinfo", "开始播放 " + tvinfo.getName());
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
			
			Log.i("tvinfo", msg.what + "" + msg.obj);
			if (msg.what == 1) {
				progress.setVisibility(View.VISIBLE);
				title.setText(msg.obj + tvinfo.getName() + " ...");
			}
			if (msg.what == 2) {
				Log.i("tvinfo", msg.obj + tvinfo.getName());
				title.setText(msg.obj + tvinfo.getName() + " ...");
			}
			if (msg.what == 3) {
				title.setText(msg.obj + tvinfo.getName() + " ...");
				play(tvinfo.getplayurl());
			}
			if (msg.what == 4) {
				title.setText(msg.obj + tvinfo.getName() + " ...");
			}
			if (msg.what == 100) {
				pd.dismiss();
				if(msg.arg1==1){
					if("0".equals(sctv.getFlag())){
						sctv.setFlag("1");
						su.updatetvinfo(sctv, 1);
						adapter.notifyDataSetChanged();
					}else if("1".equals(sctv.getFlag())){
						sctv.setFlag("0");
						su.updatetvinfo(sctv, 0);
						adapter.notifyDataSetChanged();
					}else if("2".equals(sctv.getFlag())){
						su.updatetvinfo(sctv, 0);
						user.getTemplist().remove(sctv);
						user.getTvlist().remove(sctv);
						adapter.notifyDataSetChanged();
					}
				}else{
					Toast.makeText(PlayActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
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
			player.start();
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
		}

		@Override
		public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "出现错误");
			return false;
		}

		@Override
		public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "onBufferingUpdate");
			title.setText(tvinfo.getName() + "正在缓冲...");
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
			initpopwindow();
			initplayer();
			TvInfo info=user.playtv(user.getCurrtvid());
			if(info!=null){
				startplay(info);
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "surfaceDestroyed");
		}

	}

	private void initpopwindow() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_play_lisg, null);
		pw = new PopupWindow(view, (int) (screenWidth * 0.3),
				screenHeight - 20, true);
		pw.setOutsideTouchable(true);
		playlist = (ListView) view.findViewById(R.id.playlist);
		tl = (LinearLayout) view.findViewById(R.id.titlelayout);
		tl.setOnKeyListener(new OnKeyListenerImpl());
		ptitle = (TextView) view.findViewById(R.id.title);
		playlist.setOnKeyListener(new OnKeyListenerImpl());
		playlist.setOnItemClickListener(new OnItemClickListenerImpl());
	}

	class OnKeyListenerImpl implements OnKeyListener {

		@Override
		public boolean onKey(View but, int arg1, KeyEvent event) {
			// TODO Auto-generated method stub
			Log.i("code", "--" + event.getKeyCode());
			if (event.getAction() == KeyEvent.ACTION_UP) {
				if (but.getId() == R.id.palyview) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
						TvInfo info = user.up();
						if (info != null) {
							startplay(info);
						}
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
						TvInfo info = user.down();
						if (info != null) {
							startplay(info);
						}
					}
				} else if (but.getId() == R.id.titlelayout) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
						pw.dismiss();
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						LiveTV livetv = user.left();
						showlist(livetv);
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						LiveTV livetv = user.right();
						showlist(livetv);
					}
				} else if (but.getId() == R.id.playlist) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
						pw.dismiss();
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						int i = Utils.pageleft(
								playlist.getFirstVisiblePosition(),
								playlist.getLastVisiblePosition(), user.getTvlist().size());
						playlist.setSelectionFromTop(i, 0);
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						int i = Utils.pageright(
								playlist.getFirstVisiblePosition(),
								playlist.getLastVisiblePosition(), user.getTvlist().size());
						playlist.setSelectionFromTop(i, 0);
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
						sctv = (TvInfo) playlist.getSelectedItem();
						if (sctv != null && "0".equals(sctv.getFlag())) {
							show("收藏", "正在收藏....");
							scthread = new ShouCangThread(PlayActivity.this,handler,
									"addshouchang.jsp?active="
											+ sp.getString("name", "")
											+ "&name=" + sctv.getId()+"&action=add");
							scthread.start();
						} else if (sctv != null && ("1".equals(sctv.getFlag())||"2".equals(sctv.getFlag()))) {
							show("收藏", "正在取消收藏....");
							scthread = new ShouCangThread(PlayActivity.this,handler,
									"addshouchang.jsp?active="
											+ sp.getString("name", "")
											+ "&name=" + sctv.getId()+"&action=delete");
							scthread.start();
						}
					}
				}
			}

			return false;
		}

	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			Log.i("but", but.getId() + "");
			if (but.getId() == R.id.palyview) {
				showpop();
			}
		}

	}
	public void showlist(LiveTV lt){
		Log.i("tvinfo", lt.getName()+"--"+lt.getId());
		user.getTemplist().clear();
		user.getTemplist().addAll(su.getAllTvinfo(lt.getId()));
		ptitle.setText(lt.getName());
		adapter = new TvinfoAdapter(PlayActivity.this, user.getTemplist());
		playlist.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		setlistviewHeight();
	}
	private void showpop(){
		if (pw != null) {
			adapter = new TvinfoAdapter(PlayActivity.this, user.getTvlist());
			playlist.setAdapter(adapter);
			pw.showAtLocation(palyview, Gravity.LEFT | Gravity.TOP, 5,20);
			ptitle.setText(user.gettitle());
			setlistviewHeight();
			playlist.setSelectionFromTop(user.getCurrtvid(),itemheight);
			adapter.notifyDataSetChanged();
			playlist.requestFocus();
			
		}
	}
	private void setlistviewHeight(){
		if(adapter.getCount()>0){
			View listItem = adapter.getView(0, null, playlist);
			listItem.measure(0, 0);
			itemheight=listItem.getMeasuredHeight();
			int totalHeight = itemheight*10;
			LayoutParams param=playlist.getLayoutParams();
			param.height=totalHeight;
			playlist.setLayoutParams(param);
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (player != null) {
			Log.i("player", "player stop");
			player.pause();
			player.stop();
			player.release();
		}
	}

}

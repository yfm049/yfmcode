package com.iptv.pro;

import java.util.List;

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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.adapter.TvinfoAdapter;
import com.iptv.dlerbh.R;
import com.iptv.utils.LiveTV;
import com.iptv.utils.PlayThread;
import com.iptv.utils.ShouCangThread;
import com.iptv.utils.TvInfo;
import com.iptv.utils.TvListThread;
import com.iptv.utils.User;
import com.iptv.utils.Utils;

public class PlayActivity extends Activity {

	private IptvApp iptv;
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
	private SharedPreferences sp;
	private ShouCangThread scthread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_play);
		sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);
		MediaPlayerListener = new MediaPlayerListenerImpl();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		palyview = (SurfaceView) super.findViewById(R.id.palyview);
		holder = palyview.getHolder();
		holder.addCallback(MediaPlayerListener);
		progress = (LinearLayout) super.findViewById(R.id.progress);
		palyview.setOnKeyListener(new OnKeyListenerImpl());
		title = (TextView) super.findViewById(R.id.title);
		iptv = (IptvApp) this.getApplication();
		palyview.setOnClickListener(new OnClickListenerImpl());
		palyview.requestFocus();

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
			int ht=arg1.getHeight();
			Log.i("tvinfo", "itemhg"+ht);
			iptv.getUser().setCurrtv(arg2);
			TvInfo info = iptv.getUser().getdef();
			if (info != null) {
				playnext(info);
			}

		}

	}

	public void playnext(TvInfo info) {
		if (!info.equals(tvinfo)) {
			tvinfo = info;
			playlist.setSelection(iptv.getUser().getCurrtv());
			Log.i("tvinfo", tvinfo.getName());
			if (playthread != null) {
				playthread.close();
			}
			playthread = new PlayThread(handler, tvinfo.getparam());
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
			if (msg.what == 9) {
				show("获取", "正在获取播放列表,请稍后...");
			}
			if (msg.what == 10) {
				pd.dismiss();
				List<TvInfo> tvlist = (List<TvInfo>) msg.obj;
				if (tvlist == null) {
					Toast.makeText(PlayActivity.this, "连接网络失败",
							Toast.LENGTH_SHORT).show();
				} else {
					iptv.getUser().getTvlist().clear();
					iptv.getUser().getTvlist().addAll(tvlist);
					ptitle.setText(iptv.getUser().getLivetvlist()
							.get(iptv.getUser().getCurrlivetv()).getName());
					adapter = new TvinfoAdapter(PlayActivity.this, iptv
							.getUser().getTvlist());
					playlist.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}
			if (msg.what == 100) {
				pd.dismiss();
				if (msg.obj != null && Boolean.valueOf(msg.obj.toString())) {
					Toast.makeText(PlayActivity.this, "操作成功",
							Toast.LENGTH_SHORT).show();
					if("0".equals(sctv.getFlag())){
						sctv.setFlag("1");
						adapter.notifyDataSetChanged();
					}else if("1".equals(sctv.getFlag())){
						sctv.setFlag("0");
						adapter.notifyDataSetChanged();
					}else if("2".equals(sctv.getFlag())){
						iptv.getUser().getTvlist().remove(sctv);
						iptv.getUser().getTemp().remove(sctv);
						adapter.notifyDataSetChanged();
					}
					
				} else {
					Toast.makeText(PlayActivity.this, "操作失败",
							Toast.LENGTH_SHORT).show();
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
			iptv.getUser().setCurrtv(0);
			tvinfo = iptv.getUser().getdef();
			if (tvinfo != null) {
				playthread = new PlayThread(handler, tvinfo.getparam());
				playthread.start();
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
		pw = new PopupWindow(view, (int) (screenWidth * 0.35),
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
				User user = iptv.getUser();
				if (but.getId() == R.id.palyview) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
						TvInfo info = user.up();
						if (info != null) {
							playnext(info);
						}
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
						TvInfo info = user.down();
						if (info != null) {
							playnext(info);
						}
					}
				} else if (but.getId() == R.id.titlelayout) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
						pw.dismiss();
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						LiveTV livetv = user.left();
						if (livetv != null) {
							new TvListThread(handler, livetv.getUrl()).start();
						}

					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						LiveTV livetv = user.right();
						if (livetv != null) {
							new TvListThread(handler, livetv.getUrl()).start();
						}
					}
				} else if (but.getId() == R.id.playlist) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
						pw.dismiss();
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						int i = Utils.pageleft(
								playlist.getFirstVisiblePosition(),
								playlist.getLastVisiblePosition(), iptv
										.getUser().getTvlist().size());
						playlist.setSelectionFromTop(i, 0);
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						int i = Utils.pageright(
								playlist.getFirstVisiblePosition(),
								playlist.getLastVisiblePosition(), iptv
										.getUser().getTvlist().size());
						playlist.setSelectionFromTop(i, 0);
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
						sctv = (TvInfo) playlist.getSelectedItem();
						if (sctv != null && "0".equals(sctv.getFlag())) {
							show("收藏", "正在收藏....");
							scthread = new ShouCangThread(handler,
									"addshouchang.jsp?active="
											+ sp.getString("name", "")
											+ "&name=" + sctv.getId()+"&action=add");
							scthread.start();
						} else if (sctv != null && ("2".equals(sctv.getFlag())||"1".equals(sctv.getFlag()))) {
							show("收藏", "正在取消收藏....");
							scthread = new ShouCangThread(handler,
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
				if (pw != null) {
					adapter = new TvinfoAdapter(PlayActivity.this, iptv
							.getUser().getTemp());
					playlist.setAdapter(adapter);
					pw.showAtLocation(palyview, Gravity.LEFT | Gravity.TOP, 5,
							20);
					ptitle.setText(iptv.getUser().gettitle());
					playlist.setSelectionFromTop(iptv.getUser().getCurrtv(),101);
					adapter.notifyDataSetChanged();
					playlist.requestFocus();
				}

			}
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

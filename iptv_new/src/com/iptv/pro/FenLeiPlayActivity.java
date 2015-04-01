package com.iptv.pro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.adapter.FenLeiAdapter;
import com.iptv.adapter.PlayPindaoAdapter;
import com.iptv.pojo.LiveTV;
import com.iptv.pojo.Notice;
import com.iptv.pojo.TvInfo;
import com.iptv.pojo.User;
import com.iptv.thread.NoticeThread;
import com.iptv.thread.PlayThread;
import com.iptv.thread.ShouCangThread;
import com.iptv.thread.TimeThread;
import com.iptv.utils.SqliteUtils;
import com.iptv.utils.Utils;
import com.iptv.news.R;

public class FenLeiPlayActivity extends Activity {

	private DisplayMetrics dm;// 获取显示器宽高
	private SurfaceView surface;// 播放用view
	private SurfaceHolder holder;
	private FenLeiAdapter noticeadapter;
	private PlayPindaoAdapter tempadapter, pdadapter;
	private ListView noticelist, playlist;
	private User user;
	private SqliteUtils su;
	private SharedPreferences sp;
	private List<Notice> ln = new ArrayList<Notice>();
	private LinearLayout qhfenlei, progress;
	private RelativeLayout playview;
	private TextView ptitle, title,noticerq,noticetime;
	private MediaPlayer player;
	private MediaPlayerListenerImpl MediaPlayerListener;
	private TvInfo tvinfo, sctv;
	private PlayThread playthread;
	private ProgressDialog pd;
	private ShouCangThread scthread;
	private TimeThread timetrhead;
	private SimpleDateFormat rq=new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sj=new SimpleDateFormat("HH:mm");
	private NoticeThread nt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_fenleiplay);
		sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);
		user = new User();
		dm = new DisplayMetrics();
		MediaPlayerListener = new MediaPlayerListenerImpl();
		timetrhead=new TimeThread(handler);
		timetrhead.start();
		noticerq = (TextView) super.findViewById(R.id.noticerq);
		noticetime = (TextView) super.findViewById(R.id.noticetime);
		
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		surface = (SurfaceView) super.findViewById(R.id.surface);
		holder = surface.getHolder();
		holder.addCallback(MediaPlayerListener);
		
		playview=(RelativeLayout)super.findViewById(R.id.playview);
		playview.setOnClickListener(new OnClickListenerImpl());
		noticelist = (ListView) super.findViewById(R.id.noticelist);
		playlist = (ListView) super.findViewById(R.id.playpdlist);
		// 分类名称显示
		qhfenlei = (LinearLayout) super.findViewById(R.id.qhfenlei);
		ptitle = (TextView) super.findViewById(R.id.ptitle);
		// 播放进度显示
		progress = (LinearLayout) super.findViewById(R.id.progress);
		title = (TextView) super.findViewById(R.id.title);

		noticeadapter = new FenLeiAdapter(this, ln);
		noticelist.setAdapter(noticeadapter);

		tempadapter = new PlayPindaoAdapter(this, user.getTemplist());
		pdadapter = new PlayPindaoAdapter(this, user.getTvlist());
		playlist.setAdapter(tempadapter);

		playlist.setOnItemClickListener(new OnItemClickListenerImpl());
		qhfenlei.setOnKeyListener(new ListenerImpl());
		inituser();
	}

	// 初始化数据
	private void inituser() {

		su = new SqliteUtils(this);
		List<LiveTV> listlivetv = su.getAllliveTv();
		user.getLivetvlist().addAll(listlivetv);
		int cl = sp.getInt("cruulive", 0);
		if (listlivetv.size() > 0) {
			if (cl < listlivetv.size()) {
				user.setTempliveid(cl);
			}
			LiveTV livetv = listlivetv.get(user.getTempliveid());
			ptitle.setText(livetv.getName());
			List<TvInfo> listtv = su.getAllTvinfo(livetv.getId());
			user.getTemplist().addAll(listtv);
			int ct = sp.getInt("currtv", 0);
			playlist.setSelection(ct);
			if (listtv.size() > 0 && ct < listtv.size()) {
				user.setCurrtvid(ct);
				tempadapter.notifyDataSetChanged();
				TvInfo tvinfo = (TvInfo) listtv.get(ct);
				if(nt!=null){
					nt.stopthread();
				}
				nt = new NoticeThread(handler, tvinfo);
				nt.start();
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

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(FenLeiPlayActivity.this,PlayActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			FenLeiPlayActivity.this.startActivity(intent);
		}

	}

	class OnItemClickListenerImpl implements OnItemClickListener {
		@Override
		// 左侧菜单点击出发
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			TvInfo info = (TvInfo) arg0.getItemAtPosition(arg2);
			if(nt!=null){
				nt.stopthread();
			}
			nt = new NoticeThread(handler, tvinfo);
			nt.start();
			if(info.equals(tvinfo)){
				Intent intent=new Intent(FenLeiPlayActivity.this,PlayActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				FenLeiPlayActivity.this.startActivity(intent);
			}else if (info != null) {
				startplay(info);
			}
		}
	}

	class ListenerImpl implements OnKeyListener {

		@Override
		public boolean onKey(View but, int arg1, KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				
				if (but.getId() == R.id.qhfenlei) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						LiveTV livetv = user.left();
						showlist(livetv);
						return true;
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						LiveTV livetv = user.right();
						showlist(livetv);
						return true;
					}

				} else if (but.getId() == R.id.playlist) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						int i = Utils.pageleft(playlist
								.getFirstVisiblePosition(), playlist
								.getLastVisiblePosition(), user.getTvlist()
								.size());
						playlist.setSelectionFromTop(i, 0);
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						int i = Utils.pageright(playlist
								.getFirstVisiblePosition(), playlist
								.getLastVisiblePosition(), user.getTvlist()
								.size());
						playlist.setSelectionFromTop(i, 0);
					}
					if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
						sctv = (TvInfo) playlist.getSelectedItem();
						if (sctv != null && "0".equals(sctv.getFlag())) {
							show("收藏", "正在收藏....");
							scthread = new ShouCangThread(handler,
									"addshouchang.jsp?active="
											+ sp.getString("name", "")
											+ "&name=" + sctv.getId()
											+ "&action=add");
							scthread.start();
						} else if (sctv != null
								&& ("1".equals(sctv.getFlag()) || "2"
										.equals(sctv.getFlag()))) {
							show("收藏", "正在取消收藏....");
							scthread = new ShouCangThread(handler,
									"addshouchang.jsp?active="
											+ sp.getString("name", "")
											+ "&name=" + sctv.getId()
											+ "&action=delete");
							scthread.start();
						}
					}
				}
			}
			return false;
		}

	}

	private void show(String title, String msg) {
		pd = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.show();
	}

	public void showlist(LiveTV lt) {
		ptitle.setText(lt.getName());
		user.getTvlist().clear();
		user.getTvlist().addAll(su.getAllTvinfo(lt.getId()));
		playlist.setAdapter(pdadapter);
		pdadapter.notifyDataSetChanged();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			//Log.i("tvinfo", msg.what + "" + msg.obj);
			if (msg.what == 1) {
				progress.setVisibility(View.VISIBLE);
				title.setText(msg.obj + tvinfo.getName() + " ...");
			}else if (msg.what == 2) {
				Log.i("tvinfo", msg.obj + tvinfo.getName());
				title.setText(msg.obj + tvinfo.getName() + " ...");
			}else if (msg.what == 3) {
				title.setText(msg.obj + tvinfo.getName() + " ...");
				play(tvinfo.getplayurl());
			}else if (msg.what == 4) {
				title.setText(msg.obj + tvinfo.getName() + " ...");
			}else if (msg.what == 5) {
				ln.clear();
				List<Notice> lnt = (List<Notice>) msg.obj;
				ln.addAll(lnt);
				noticeadapter.notifyDataSetChanged();
			}else  if (msg.what == 100) {
				pd.dismiss();
				if (msg.arg1 == 1) {
					if ("0".equals(sctv.getFlag())) {
						sctv.setFlag("1");
						su.updatetvinfo(sctv, 1);
						PlayPindaoAdapter adapter = (PlayPindaoAdapter) playlist
								.getAdapter();
						adapter.notifyDataSetChanged();
					} else if ("1".equals(sctv.getFlag())) {
						sctv.setFlag("0");
						su.updatetvinfo(sctv, 0);
						PlayPindaoAdapter adapter = (PlayPindaoAdapter) playlist
								.getAdapter();
						adapter.notifyDataSetChanged();
					} else if ("2".equals(sctv.getFlag())) {
						su.updatetvinfo(sctv, 0);
						user.getTemplist().remove(sctv);
						user.getTvlist().remove(sctv);
						PlayPindaoAdapter adapter = (PlayPindaoAdapter) playlist
								.getAdapter();
						adapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(FenLeiPlayActivity.this, "收藏失败",
							Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what==200){
				Date date=(Date)msg.obj;
				noticerq.setText(rq.format(date));
				noticetime.setText(sj.format(date));
			}
		}
	};

	public void startplay(TvInfo info) {
		if (!info.equals(tvinfo)) {
			tvinfo = info;
			savedata(user.getCurrliveid(), user.getCurrtvid());
			playlist.setSelection(user.getCurrtvid());
			Log.i("tvinfo", "" + tvinfo.getName());
			if (playthread != null) {
				playthread.close();
			}
			String userid = sp.getString("name", "");
			playthread = new PlayThread(handler, tvinfo.getparam(userid));
			playthread.start();
		}
	}

	public void savedata(int currliveid, int currtvid) {
		Editor edit = sp.edit();
		edit.putInt("cruulive", currliveid);
		edit.putInt("currtv", currtvid);
		edit.commit();
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
			// initpopwindow();
			initplayer();
			TvInfo info = user.playtv(user.getCurrtvid());
			if (info != null) {
				startplay(info);
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			Log.i("tvinfo", "surfaceDestroyed");
		}

	}

}

package com.iptv.pro;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.adapter.NoticePdAdapter;
import com.iptv.play.HPlayer;
import com.iptv.play.Mplayer;
import com.iptv.play.Player;
import com.iptv.pojo.Category;
import com.iptv.pojo.Channel;
import com.iptv.thread.ControlThread;
import com.iptv.thread.ForceTvThread;
import com.iptv.thread.ShouCangThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.LogUtils;
import com.iptv.utils.NetUtils;
import com.iptv.utils.SqliteUtils;

public class PlayTVActivity extends Activity {

	private static String TAG=PlayTVActivity.class.getName();
	private List<Channel> listcn = new ArrayList<Channel>();
	private List<Category> listcategory = new ArrayList<Category>();
	private ListView channellist;
	private NoticePdAdapter npdadapter;
	private TextView channelname, channelstatus, categoryname, channelcode;
	private ImageView categoryright, categoryleft;
	private SqliteUtils su;
	private SurfaceView playview;
	private Mplayer player;
	private LinearLayout statusview, channelinfo;
	private OnClickListenerImpl listener;
	private Channel Currchannel, searchchannel;
	private int categoryid, channelid;
	private PopupWindow pw;
	private View pwview;
	private DisplayMetrics dm;
	private String num = "";
	private BroadcastReceiver receiver;
	private ControlThread PdViewthread, CodeChangeThread;
	private int pdviewhide = 2000, channelCodeChange = 2001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_playtv);
		dm = this.getResources().getDisplayMetrics();
		pwview = LayoutInflater.from(this).inflate(
				R.layout.activity_playtv_channellist, null);
		channellist = (ListView) pwview.findViewById(R.id.channellist);
		channellist.setVisibility(View.VISIBLE);
		npdadapter = new NoticePdAdapter(this, listcn);
		channellist.setAdapter(npdadapter);
		categoryname = (TextView) pwview.findViewById(R.id.categoryname);
		categoryright = (ImageView) pwview.findViewById(R.id.categoryright);
		categoryleft = (ImageView) pwview.findViewById(R.id.categoryleft);
		channelcode = (TextView) this.findViewById(R.id.channelcode);
		channelinfo = (LinearLayout) this.findViewById(R.id.channelinfo);
		channelinfo.setVisibility(View.INVISIBLE);
		su = new SqliteUtils(this);
		statusview = (LinearLayout) this.findViewById(R.id.statusview);
		channelname = (TextView) this.findViewById(R.id.channelname);
		channelstatus = (TextView) this.findViewById(R.id.channelstatus);
		playview = (SurfaceView) this.findViewById(R.id.playview);

		listener = new OnClickListenerImpl();
		channellist.setOnItemClickListener(listener);
		categoryright.setOnClickListener(listener);
		categoryleft.setOnClickListener(listener);
		categoryname.setOnKeyListener(listener);
		channellist.setOnKeyListener(listener);
		playview.setOnClickListener(listener);
		playview.setOnKeyListener(listener);
		if("system".equals(ComUtils.getConfig(this, "decode", "system"))){
			player = new Player(this, playview, handler);
		}else if("viamio".equals(ComUtils.getConfig(this, "decode", "system"))){
			player = new HPlayer(this, playview, handler);
		}
		
		// 初始化数据
		reglogoupdate();
	}

	public void setStatus(boolean isshow, String state) {
		if (isshow) {
			if (Currchannel != null) {
				channelname.setText(Currchannel.getName());
			}
			channelstatus.setText(state);
			statusview.setVisibility(View.VISIBLE);
		} else {
			statusview.setVisibility(View.INVISIBLE);
			channelinfo.setVisibility(View.INVISIBLE);
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Player.surfaceCreated) {
				LogUtils.write(TAG, "初始化数据");
				initdata();
			} else if (msg.what == Player.mediaplayerPrepared) {
				LogUtils.write(TAG, "准备完成 开始播放");
				player.play();
				setStatus(false, "开始播放");
			} else if (msg.what == Player.mediaplayererror) {
				setStatus(true, "播放错误，请切换解码方式");
				player.release();
			} else if (msg.what == Player.mediaplayerbufferedStart) {
				setStatus(true, "正在缓冲");
			} else if (msg.what == Player.mediaplayerbufferedEnd) {
				setStatus(false, "缓冲完成");
			} else if (msg.what == Player.mediaplayerserverDied) {
				setStatus(true, "链接超时");
			} else if (msg.what == Player.mediaplayerstart) {
				setStatus(false, "播放中");
			} else if (msg.what == ForceTvThread.ForceTvsuc) {
				player.setPlayUrl(msg.obj.toString());
				setStatus(true, "加载成功");
			} else if (msg.what == ForceTvThread.ForceTvfail) {
				Toast.makeText(PlayTVActivity.this, R.string.forcetv_fail,
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == ShouCangThread.shoucangsuc) {
				npdadapter.notifyDataSetChanged();
			} else if (msg.what == ShouCangThread.shoucangfal) {
				Toast.makeText(PlayTVActivity.this, R.string.shoucang_fail,
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == pdviewhide) {
				pw.dismiss();
			} else if (msg.what == channelCodeChange) {
				numChangeChannel();
			}
		}

	};

	public void initdata() {
		listcategory.clear();
		listcn.clear();
		listcategory.addAll(su.getAllCategory());
		categoryid = ComUtils.getConfig(this, "categoryid", 0);
		channelid = ComUtils.getConfig(this, "channelid", 0);
		Category category = listcategory.get(categoryid);
		categoryname.setText(category.getName());
		listcn.addAll(su.getChannel(category));
		npdadapter.notifyDataSetChanged();
		int lcnsize = listcn.size();
		int pos = lcnsize > channelid ? channelid : 0;
		if (lcnsize > 0) {
			channellist.setSelection(pos);
			playChannel(pos);
		}

	}

	private void playChannel(int pos) {
		Channel channel = listcn.get(pos);
		channelname.setText(channel.getName());
		channelcode.setVisibility(View.VISIBLE);
		channelcode.setText(channel.getId() + "");
		channelinfo.setVisibility(View.VISIBLE);
		setStatus(true, "正在加载");
		channellist.setSelection(pos);
		ForceTvUtils.switch_chan(channel.getHttpurl(), channel.getHotlink(),
				ComUtils.getConfig(PlayTVActivity.this, "name", ""), handler);
		ComUtils.setConfig(this, "channelid", pos);
		ComUtils.setConfig(this, "categoryid", categoryid);
		NetUtils.GetNotice(channel, handler);
	}

	public void left() {
		categoryid = categoryid - 1;
		categoryid = categoryid < 0 ? listcategory.size() - 1 : categoryid;
		listcn.clear();
		Category category = listcategory.get(categoryid);
		categoryname.setText(category.getName());
		listcn.addAll(su.getChannel(category));
		npdadapter.notifyDataSetChanged();
	}

	public void right() {
		categoryid = categoryid + 1;
		categoryid = categoryid > listcategory.size() - 1 ? 0 : categoryid;
		listcn.clear();
		Category category = listcategory.get(categoryid);
		categoryname.setText(category.getName());
		listcn.addAll(su.getChannel(category));
		npdadapter.notifyDataSetChanged();
	}

	private void down() {
		int pos = ComUtils.getConfig(this, "channelid", 0);
		pos = pos + 1;
		pos = pos > listcn.size() - 1 ? 0 : pos;
		playChannel(pos);
	}

	private void up() {
		int pos = ComUtils.getConfig(this, "channelid", 0);
		pos = pos - 1;
		pos = pos > 0 ? pos : listcn.size() - 1;
		playChannel(pos);
	}

	private void showPopWindowList() {
		if (pw == null) {
			int pwidth = (int) (dm.widthPixels * 0.278125);
			int pheight = (int) (pwidth * 1.818352);
			pw = new PopupWindow(pwview, pwidth, pheight);
			pw.setOutsideTouchable(true);
			pw.setFocusable(true);

		}
		int pos = ComUtils.getConfig(this, "channelid", 0);
		channellist.setSelection(pos);
		channellist.requestFocus();
		channellist.setOnTouchListener(listener);
		pw.showAtLocation(playview, Gravity.LEFT | Gravity.TOP, 5, 20);
		if (PdViewthread != null) {
			PdViewthread.setIsrun(false);
		}
		PdViewthread = new ControlThread(handler, 10, pdviewhide);
		PdViewthread.start();
	}

	private void ControlReset(ControlThread thread) {
		if (thread != null && thread.isIsrun()) {
			thread.resettime();
		}
	}

	class OnClickListenerImpl implements OnClickListener, OnItemClickListener,
			OnKeyListener, OnTouchListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (view.getId() == R.id.playview) {
				if (!"".equals(num)) {
					numChangeChannel();
				} else {
					showPopWindowList();
				}

			} else if (view.getId() == R.id.categoryleft) {
				left();
			} else if (view.getId() == R.id.categoryright) {
				right();
			}
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			playChannel(pos);
			pw.dismiss();
		}

		@Override
		public boolean onKey(View view, int arg1, KeyEvent Event) {
			// TODO Auto-generated method stub
			if (Event.getAction() == KeyEvent.ACTION_DOWN) {
				if (view.getId() == R.id.categoryname) {
					ControlReset(PdViewthread);
					if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						left();
					} else if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						right();
					}
				}
				if (view.getId() == R.id.channellist) {
					ControlReset(PdViewthread);
					if (Event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
						Channel channel = (Channel) channellist
								.getSelectedItem();
						NetUtils.ShouCang(channel, PlayTVActivity.this, handler);
					} else if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						int i = ComUtils.pageleft(
								channellist.getFirstVisiblePosition(),
								channellist.getLastVisiblePosition(),
								listcn.size());
						channellist.setSelectionFromTop(i, 0);
					} else if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						int i = ComUtils.pageright(
								channellist.getFirstVisiblePosition(),
								channellist.getLastVisiblePosition(),
								listcn.size());

						channellist.setSelectionFromTop(i, 0);
					}
				}
				if (view.getId() == R.id.playview) {
					if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
						up();
					} else if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
						down();
					} else {
						numChange(Event.getKeyCode());
					}
				}
				if (Event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
				}
			}
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			ControlReset(PdViewthread);
			return false;
		}

	}

	public void numChange(int keycode) {
		switch (keycode) {
		case KeyEvent.KEYCODE_0:
			num += "0";
			break;
		case KeyEvent.KEYCODE_1:
			num += "1";
			break;
		case KeyEvent.KEYCODE_2:
			num += "2";
			break;
		case KeyEvent.KEYCODE_3:
			num += "3";
			break;
		case KeyEvent.KEYCODE_4:
			num += "4";
			break;
		case KeyEvent.KEYCODE_5:
			num += "5";
			break;
		case KeyEvent.KEYCODE_6:
			num += "6";
			break;
		case KeyEvent.KEYCODE_7:
			num += "7";
			break;
		case KeyEvent.KEYCODE_8:
			num += "8";
			break;
		case KeyEvent.KEYCODE_9:
			num += "9";
			break;
		}
		showChannel();
	}

	private void numChangeChannel() {
		if (CodeChangeThread != null) {
			CodeChangeThread.setIsrun(false);
		}
		num = "";
		if (searchchannel != null) {
			categoryid = 0;
			listcn.clear();
			Category category = listcategory.get(categoryid);
			categoryname.setText(category.getName());
			listcn.addAll(su.getChannel(category));
			int pos = listcn.indexOf(searchchannel);
			npdadapter.notifyDataSetChanged();
			ComUtils.setConfig(this, "channelid", pos);
			ComUtils.setConfig(this, "categoryid", categoryid);
			playChannel(pos);
		} else {
			Toast.makeText(this, R.string.tv_msg, Toast.LENGTH_SHORT).show();
			channelinfo.setVisibility(View.INVISIBLE);
		}

	}

	public void showChannel() {
		if (!"".equals(num)) {

			channelinfo.setVisibility(View.VISIBLE);
			channelcode.setText(num);
			searchchannel = su.getChannelById(num);
			if (num.length() >= 4) {
				numChangeChannel();
			} else {
				if (CodeChangeThread != null && CodeChangeThread.isIsrun()) {
					CodeChangeThread.resettime();
				} else {
					CodeChangeThread = new ControlThread(handler, 2,
							channelCodeChange);
					CodeChangeThread.start();
				}
			}
		}
	}

	public void reglogoupdate() {
		IntentFilter intentfilter = new IntentFilter(UpdateService.logoupdate);
		receiver = new receiver();
		this.registerReceiver(receiver, intentfilter);
	}

	class receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			npdadapter.notifyDataSetChanged();
		}

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		LogUtils.write(TAG, "onPause");
		playview.setVisibility(View.INVISIBLE);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		LogUtils.write(TAG, "onResume");
		playview.setVisibility(View.VISIBLE);
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(player!=null){
			LogUtils.write(TAG, "onDestroy");
			this.unregisterReceiver(receiver);
			player.release();
		}
		
	}

}

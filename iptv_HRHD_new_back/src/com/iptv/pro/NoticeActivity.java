package com.iptv.pro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.LayoutListener.LayoutListener;
import com.iptv.adapter.NoticePdAdapter;
import com.iptv.adapter.NoticeYgAdapter;
import com.iptv.play.Player;
import com.iptv.play.SystemPlayer;
import com.iptv.pojo.Category;
import com.iptv.pojo.Channel;
import com.iptv.pojo.Notice;
import com.iptv.thread.ControlThread;
import com.iptv.thread.ForceTvThread;
import com.iptv.thread.LogoThread;
import com.iptv.thread.NoticeThread;
import com.iptv.thread.ShouCangThread;
import com.iptv.thread.TimeThread;
import com.iptv.utils.ComUtils;
import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.LogUtils;
import com.iptv.utils.NetUtils;
import com.iptv.utils.SqliteUtils;
import com.iptv.view.EventLinearLayout;
import com.tvbox.tvreplay.R;

public class NoticeActivity extends Activity {

	

	

	private LinearLayout toplayout, buttomlayout, channellayout, leftlayout,
			rightlayout, rmiddlelayout, rbuttomlayout, rtoplayout,
			playleftlayout, playrightlayout, playlayout;
	private static String TAG = NoticeActivity.class.getName();
	private ListView channellist, noticelist,popchannellist;
	private ImageView categoryright,popcategoryright, categoryleft,popcategoryleft, logoimg;
	private List<Channel> listcn = new ArrayList<Channel>();
	private List<Notice> listnotice = new ArrayList<Notice>();
	private List<Category> listcategory = new ArrayList<Category>();
	private NoticePdAdapter npdadapter;
	private NoticeYgAdapter nygadapter;
	private SurfaceView playview;
	private Player player;
	private LinearLayout statusview, channelinfo;
	private TextView channelname,popcategoryname, channelstatus, categoryname, channelcode, notice_time,
			notice_date, pdcount,speed;
	private Channel Currchannel, searchchannel;
	private OnClickListenerImpl listener;
	private SqliteUtils su;
	private int categoryid, channelid, currchannelid,currpage;
	private BroadcastReceiver receiver;
	private boolean isfullscreen = false;
	private int minwidth, minheight;
	private DisplayMetrics dm;
	private PopupWindow pw;
	private View pwview;
	private ControlThread CodeChangeThread;
	private int pdviewhide = 2000, channelCodeChange = 2001,bytespeed=2003;
	private String num = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_notice);
		layoutinit();
		dm = this.getResources().getDisplayMetrics();
		//初始化弹出框
		pwview = LayoutInflater.from(this).inflate(
				R.layout.activity_playtv_channellist, null);
		popchannellist= (ListView) pwview.findViewById(R.id.channellist);
		popcategoryname = (TextView) pwview.findViewById(R.id.categoryname);
		popcategoryright = (ImageView) pwview.findViewById(R.id.categoryright);
		popcategoryleft = (ImageView) pwview.findViewById(R.id.categoryleft);
		
		su = new SqliteUtils(this);
		channellist = (ListView) this.findViewById(R.id.channellist);
		noticelist = (ListView) this.findViewById(R.id.noticelist);
		channellist.setVisibility(View.VISIBLE);
		noticelist.setVisibility(View.VISIBLE);
		
		
		
		npdadapter = new NoticePdAdapter(this, listcn);
		channellist.setAdapter(npdadapter);
		
		popchannellist.setAdapter(npdadapter);
		
		nygadapter = new NoticeYgAdapter(this, listnotice);
		noticelist.setAdapter(nygadapter);
		//注册列表显示监听
		channellist.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener(channellist, npdadapter));
		noticelist.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener(noticelist, nygadapter));
		popchannellist.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener(popchannellist, npdadapter));
		
		categoryname = (TextView) this.findViewById(R.id.categoryname);
		categoryright = (ImageView) this.findViewById(R.id.categoryright);
		categoryleft = (ImageView) this.findViewById(R.id.categoryleft);
		
		channelcode = (TextView) this.findViewById(R.id.channelcode);
		channelinfo = (LinearLayout) this.findViewById(R.id.channelinfo);
		channelinfo.setVisibility(View.INVISIBLE);
		
		
		logoimg = (ImageView) this.findViewById(R.id.logoimg);

		statusview = (LinearLayout) this.findViewById(R.id.statusview);
		notice_time = (TextView) this.findViewById(R.id.notice_time);
		notice_date = (TextView) this.findViewById(R.id.notice_date);
		pdcount = (TextView) this.findViewById(R.id.pdcount);

		
		listener = new OnClickListenerImpl();
		channellist.setOnItemClickListener(listener);
		popchannellist.setOnItemClickListener(listener);
		channellist.setOnItemSelectedListener(listener);
		channellist.setOnItemLongClickListener(listener);
		popchannellist.setOnItemLongClickListener(listener);
		
		categoryright.setOnClickListener(listener);
		categoryleft.setOnClickListener(listener);
		categoryname.setOnKeyListener(listener);
		popcategoryright.setOnClickListener(listener);
		popcategoryleft.setOnClickListener(listener);
		popcategoryname.setOnKeyListener(listener);
		
		channellist.setOnKeyListener(listener);
		popchannellist.setOnKeyListener(listener);
		
		
		channelname = (TextView) this.findViewById(R.id.channelname);
		speed = (TextView) this.findViewById(R.id.speed);
		channelstatus = (TextView) this.findViewById(R.id.channelstatus);
		playview = (SurfaceView) this.findViewById(R.id.playview);
		playview.setOnClickListener(listener);
		playview.setOnKeyListener(listener);
		player = new SystemPlayer(this, playview, handler);
		
		NetUtils.TimeUpdate(handler);
		NetUtils.LogoShow(handler);
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
			channelinfo.setVisibility(View.INVISIBLE);
			statusview.setVisibility(View.INVISIBLE);
		}
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
	

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == SystemPlayer.surfaceCreated) {
				initdata();
			} else if (msg.what == SystemPlayer.mediaplayerPrepared) {
				LogUtils.write("notice", "开始播放");
				player.play();
				setStatus(false, "开始播放");
			} else if (msg.what == SystemPlayer.mediaplayererror) {
				setStatus(true, "播放错误");
			} else if (msg.what == SystemPlayer.mediaplayerbufferedStart) {
				setStatus(true, "正在缓冲");
			} else if (msg.what == SystemPlayer.mediaplayerbufferedEnd) {
				setStatus(false, "缓冲完成");
			} else if (msg.what == SystemPlayer.mediaplayerserverDied) {
				setStatus(true, "链接超时");
			} else if (msg.what == SystemPlayer.mediaplayerstart) {
				setStatus(false, "播放中");
				
			} else if (msg.what == NoticeThread.noticesuc) {
				List<Notice> lc = (List<Notice>) msg.obj;
				listnotice.clear();
				listnotice.addAll(lc);
				nygadapter.notifyDataSetChanged();
			} else if (msg.what == NoticeThread.noticefail) {
				listnotice.clear();
				nygadapter.notifyDataSetChanged();
				if(!isfullscreen){
					ComUtils.showtoast(NoticeActivity.this, R.string.notice_fail);
				}
			} else if (msg.what == ForceTvThread.ForceTvsuc) {
				player.setPlayUrl(msg.obj.toString());
				setStatus(true, "正在加载");
			} else if (msg.what == ForceTvThread.ForceTvfail) {
				ComUtils.showtoast(NoticeActivity.this, R.string.forcetv_fail);
			} else if (msg.what == TimeThread.timeupdate) {
				Date date = (Date) msg.obj;
				notice_date.setText(TimeThread.rq.format(date));
				notice_time.setText(TimeThread.sj.format(date));
			} else if (msg.what == ShouCangThread.shoucangsuc) {
				npdadapter.notifyDataSetChanged();
			} else if (msg.what == ShouCangThread.shoucangfal) {
				ComUtils.showtoast(NoticeActivity.this, R.string.shoucang_fail);
			} else if (msg.what == LogoThread.logosuc) {
				logoimg.setVisibility(View.VISIBLE);
				logoimg.setImageBitmap((Bitmap) msg.obj);
			} else if (msg.what == LogoThread.logofail) {
				logoimg.setVisibility(View.INVISIBLE);
			} else if (msg.what == pdviewhide) {
				pw.dismiss();
			} else if (msg.what == channelCodeChange) {
				numChangeChannel();
			}else if(msg.what==bytespeed){
				showspeed();
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
		popcategoryname.setText(category.getName());
		listcn.addAll(su.getChannel(category));
		npdadapter.setItemheight(channellist.getHeight());
		int lcnsize = listcn.size();
		int pos = lcnsize > channelid ? channelid : 0;
		if (lcnsize > 0) {
			fixedpos(channellist);
			playChannel(pos);
		}
		channellist.requestFocus();
		showspeed();
	}
	private void playChannel(int pos) {
		Channel channel = listcn.get(pos);
		pdcount.setText((pos + 1) + "/" + listcn.size());
		channelname.setText(channel.getName());
		ForceTvUtils.switch_chan(channel.getHttpurl(), channel.getHotlink(),
				ComUtils.getConfig(NoticeActivity.this, "name", ""), handler);
		ComUtils.setConfig(this, "channelid", pos);
		ComUtils.setConfig(this, "categoryid", categoryid);
		currchannelid = channel.getId();
		channelinfo.setVisibility(View.VISIBLE);
		channelcode.setText(""+channel.getId());
		NetUtils.GetNotice(channel, handler);
		
	}

	private boolean toPlayTv(int pos) {
		Channel channel = listcn.get(pos);
		if (channel.getId() == currchannelid&&!isfullscreen) {
			fullScreenPlayview();
			return true;
		}
		return false;
	}
	
	private void showPopWindowList() {
		if (pw == null) {
			pw = new PopupWindow(pwview, channellayout.getWidth(), channellayout.getHeight());
			EventLinearLayout eventlayout=(EventLinearLayout)pwview.findViewById(R.id.eventlayout);
			eventlayout.setHandler(handler,pdviewhide);
			pw.setBackgroundDrawable(new PaintDrawable());
			pw.setOutsideTouchable(true);
			pw.setFocusable(true);
		}
		fixedpos(popchannellist);
		popchannellist.requestFocus();
		pw.showAtLocation(playview, Gravity.LEFT | Gravity.TOP, 5, 20);
		handler.sendEmptyMessageDelayed(pdviewhide, 10000);
	}

	private void ControlReset(ControlThread thread) {
		if (thread != null && thread.isIsrun()) {
			thread.resettime();
		}
	}
	class OnClickListenerImpl implements OnClickListener, OnItemClickListener,
			OnKeyListener,OnItemSelectedListener,OnItemLongClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			
			if (view.getId() == R.id.playview) {
				if(isfullscreen){
					if (!"".equals(num)) {
						numChangeChannel();
					} else {
						showPopWindowList();
					}
				}else{
					fullScreenPlayview();
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
			if (!toPlayTv(pos)) {
				playChannel(pos);
				if(isfullscreen){
					pw.dismiss();
				}
			}
		}

		@Override
		public boolean onKey(View view, int arg1, KeyEvent Event) {
			// TODO Auto-generated method stub
			if (Event.getAction() == KeyEvent.ACTION_DOWN) {
				if (view.getId() == R.id.categoryname) {
					if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT||Event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP) {
						left();
						return true;
					} else if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT||Event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN) {
						right();
						return true;
					}
				}
				if (view.getId() == R.id.channellist) {
					if (Event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
						Channel channel = (Channel) channellist
								.getSelectedItem();
						if(channel!=null){
							NetUtils.ShouCang(channel, NoticeActivity.this, handler);
						}else{
							ComUtils.showtoast(NoticeActivity.this, R.string.shoucangmsg);
						}
						
					} else if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						if(isfullscreen){
							pre_page(popchannellist);
						}else{
							pre_page(channellist);
						}
						return true;
					} else if (Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						if(isfullscreen){
							next_page(popchannellist);
						}else{
							next_page(channellist);
						}
						return true;
					}else if(Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
						if(isfullscreen){
							page_keydown(popchannellist);
						}else{
							page_keydown(channellist);
						}
					}else if(Event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
						if(isfullscreen){
							page_keyup(popchannellist);
						}else{
							page_keyup(channellist);
						}
					}else if(Event.getKeyCode() == KeyEvent.KEYCODE_PAGE_DOWN){
						right();
					}else if(Event.getKeyCode() == KeyEvent.KEYCODE_PAGE_UP){
						left();
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
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			
			Channel channel = listcn.get(pos);
			NetUtils.GetNotice(channel, handler);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			Channel channel =listcn.get(arg2);
			if(channel!=null){
				NetUtils.ShouCang(channel, NoticeActivity.this, handler);
			}else{
				ComUtils.showtoast(NoticeActivity.this, R.string.shoucangmsg);
			}
			return true;
		}

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

	public void pre_page(ListView listview){
		int cpos=listview.getSelectedItemPosition();
		int totalpage=(listcn.size()+npdadapter.getVcount()-1)/npdadapter.getVcount()-1;
		int page=(cpos+npdadapter.getVcount())/npdadapter.getVcount()-1;
		int ppos=(cpos)%npdadapter.getVcount();
		if(page>0){
			page=page-1;
			int topheight=ppos*(npdadapter.getItemheight()+1);
			listview.setSelectionFromTop(ppos+page*npdadapter.getVcount(), topheight);
		}
		LogUtils.write("tvinfo", cpos+"pre "+totalpage+" "+page+" "+ppos);
	}
	public void next_page(ListView listview){
		int cpos=listview.getSelectedItemPosition();
		int totalpage=(listcn.size()+npdadapter.getVcount()-1)/npdadapter.getVcount()-1;
		int page=(cpos+npdadapter.getVcount())/npdadapter.getVcount()-1;
		int ppos=(cpos)%npdadapter.getVcount();
		if(page<totalpage){
			page=page+1;
			int topheight=ppos*(npdadapter.getItemheight()+1);
			listview.setSelectionFromTop(ppos+page*npdadapter.getVcount(), topheight);

		}
		LogUtils.write("tvinfo", cpos+"next"+totalpage+" "+page+" "+ppos);
	}
	public void page_keydown(ListView listview){
		int cpos=listview.getSelectedItemPosition();
		int ppos=(cpos+1)%npdadapter.getVcount();
		if(ppos==0){
			listview.setSelectionFromTop(cpos+1, 0);
		}
	}
	public void page_keyup(ListView listview){
		int cpos=listview.getSelectedItemPosition();//0
		int ppos=(cpos)%npdadapter.getVcount();
		if(ppos==0){
			int topheight=(npdadapter.getVcount()-1)*(npdadapter.getItemheight()+1);
			listview.setSelectionFromTop(cpos-1, topheight);
		}
	}
	public void fixedpos(ListView listview){
		int pos = ComUtils.getConfig(this, "channelid", 0);
		int topheight=(pos%npdadapter.getVcount())*(npdadapter.getItemheight()+1);
		listview.setSelectionFromTop(pos, topheight);
		LogUtils.write("tvinfo", pos+ " SelectionFromTop "+topheight);
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
	
	public void left() {
		categoryid = categoryid - 1;
		categoryid = categoryid < 0 ? listcategory.size() - 1 : categoryid;
		listcn.clear();
		Category category = listcategory.get(categoryid);
		categoryname.setText(category.getName());
		popcategoryname.setText(category.getName());
		listcn.addAll(su.getChannel(category));
		npdadapter.notifyDataSetChanged();
	}

	public void right() {
		categoryid = categoryid + 1;
		categoryid = categoryid > listcategory.size() - 1 ? 0 : categoryid;
		listcn.clear();
		Category category = listcategory.get(categoryid);
		categoryname.setText(category.getName());
		popcategoryname.setText(category.getName());
		listcn.addAll(su.getChannel(category));
		npdadapter.notifyDataSetChanged();
	}

	public void fullScreenPlayview() {

		toplayout.setVisibility(View.GONE);
		buttomlayout.setVisibility(View.GONE);
		channellayout.setVisibility(View.GONE);
		leftlayout.setVisibility(View.GONE);
		rightlayout.setVisibility(View.GONE);
		rmiddlelayout.setVisibility(View.GONE);
		rbuttomlayout.setVisibility(View.GONE);
		rtoplayout.setVisibility(View.GONE);
		playleftlayout.setVisibility(View.GONE);
		playrightlayout.setVisibility(View.GONE);
		minwidth = playlayout.getWidth();
		minheight = playlayout.getHeight();
		LayoutParams params = playview.getLayoutParams();
		params.height = dm.heightPixels;
		params.width = dm.widthPixels;
		playview.setLayoutParams(params);
		playview.requestFocus();
		isfullscreen = true;
	}

	public void minScreenPlayview() {
		toplayout.setVisibility(View.VISIBLE);
		buttomlayout.setVisibility(View.VISIBLE);
		channellayout.setVisibility(View.VISIBLE);
		leftlayout.setVisibility(View.VISIBLE);
		rightlayout.setVisibility(View.VISIBLE);
		rmiddlelayout.setVisibility(View.VISIBLE);
		rbuttomlayout.setVisibility(View.VISIBLE);
		rtoplayout.setVisibility(View.VISIBLE);
		playleftlayout.setVisibility(View.VISIBLE);
		playrightlayout.setVisibility(View.VISIBLE);
		LayoutParams params = playview.getLayoutParams();
		params.height = minheight;
		params.width = minwidth;
		playview.setLayoutParams(params);
		isfullscreen = false;
		fixedpos(channellist);
		channellist.requestFocus();
	}

	// layout初始化
	private void layoutinit() {
		toplayout = (LinearLayout) this.findViewById(R.id.toplayout);
		buttomlayout = (LinearLayout) this.findViewById(R.id.buttomlayout);
		channellayout = (LinearLayout) this.findViewById(R.id.channellayout);
		leftlayout = (LinearLayout) this.findViewById(R.id.leftlayout);
		rightlayout = (LinearLayout) this.findViewById(R.id.rightlayout);
		rmiddlelayout = (LinearLayout) this.findViewById(R.id.rmiddlelayout);
		rbuttomlayout = (LinearLayout) this.findViewById(R.id.rbuttomlayout);
		rtoplayout = (LinearLayout) this.findViewById(R.id.rtoplayout);
		playleftlayout = (LinearLayout) this.findViewById(R.id.playleftlayout);
		playrightlayout = (LinearLayout) this
				.findViewById(R.id.playrightlayout);
		playlayout = (LinearLayout) this.findViewById(R.id.playlayout);

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(isfullscreen){
			minScreenPlayview();
		}else{
			super.onBackPressed();
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		LogUtils.write(TAG, "onPause");
		super.onPause();
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		LogUtils.write(TAG, "onResume");
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(player!=null){
			this.unregisterReceiver(receiver);
			LogUtils.write(TAG, "notictactivity onDestroy");
		}
		
	}
}

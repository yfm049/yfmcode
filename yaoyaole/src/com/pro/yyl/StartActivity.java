package com.pro.yyl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import shidian.tv.sntv.net.HttpUtils;
import shidian.tv.sntv.net.ServiceApi;
import shidian.tv.sntv.tools.DbUtils;
import shidian.tv.sntv.tools.PhoneInfo;
import shidian.tv.sntv.tools.Result;
import shidian.tv.sntv.tools.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class StartActivity extends Activity {

	private List<String> lpn = new ArrayList<String>();
	private AtomicInteger tcount = new AtomicInteger();
	private AtomicInteger tthread = new AtomicInteger();
	private ListView datalist;
	private StartAdapter adapter;
	private boolean isrun, start = false;
	private ExecutorService pool = Executors.newCachedThreadPool();
	private List<PhoneInfo> lpi = new ArrayList<PhoneInfo>();
	private int tempsleep = 4000;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private SeekBar seekbar;

	private CheckBox scroll;

	private TextView startpn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		datalist = (ListView) this.findViewById(R.id.datalist);
		startpn = (TextView) this.findViewById(R.id.startpn);
		adapter = new StartAdapter(this, lpn);
		datalist.setAdapter(adapter);
		seekbar = (SeekBar) this.findViewById(R.id.seekbar);
		seekbar.setProgress(tempsleep);
		seekbar.setOnSeekBarChangeListener(new ListenerImpl());

		scroll = (CheckBox) this.findViewById(R.id.scroll);
		scroll.setOnCheckedChangeListener(new ListenerImpl());
		DbUtils.getInstance(this).getAllUser(lpi, "可用");
		startpn.setText("自动摇摇_" + tthread.intValue() + "_" + tcount.intValue()+ "_" + tempsleep);
	}

	public void startrun(View v) {
		isrun = true;
		AddThread();
	}

	public void stoprun(View v) {
		isrun = false;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 100) {
				if (lpn.size() > 2000) {
					lpn.clear();
				}
				lpn.add(msg.obj.toString());
				adapter.notifyDataSetChanged();
				startpn.setText("自动摇摇_" + tthread.intValue() + "_"+ tcount.intValue() + "_" + tempsleep);

			} else if (msg.what == 200) {
				Utils.playsound(StartActivity.this, R.raw.dt_win1);
				Result result = (Result) msg.obj;
				DbUtils.getInstance(StartActivity.this).addGift(result);
			}

		}

	};

	public synchronized void AddThread() {
		if (lpi.size() > 0) {
			for (PhoneInfo pi : lpi) {
				if (!pi.isRun()) {
					pi.setRun(true);
					pool.execute(new YyyThread(pi, lpi.indexOf(pi)));
					tthread.incrementAndGet();
				}
			}
		}

	}

	class YyyThread extends Thread {

		private int count = 0;
		private int index;
		private PhoneInfo pi;
		private ServiceApi api;
		private int sleeptime = 0;
		
		public YyyThread(PhoneInfo pi, int index) {
			this.pi = pi;
			this.index = index;
			api = new ServiceApi();
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				Thread.sleep(tempsleep/(lpi.size()+1)*index);
				handler.sendMessage(handler.obtainMessage(100,sdf.format(new Date()) + " " + pi.getPhone() + " 线程_"+ index + "_启动"));
				while (isrun) {
					try {
						Thread.sleep(sleeptime);
						if (CanStart() && NoEnd()) {
							Result msg = api.checkSign(StartActivity.this, pi);
							if (msg != null) {
								msg.setPhone(pi.getPhone());
								pi.setUterm(msg.getTid());
								if (msg.getAwardtype() == 99) {
	
									if (index == 0 && !start) {
										Utils.initSkey();
										Utils.playsound(StartActivity.this,
												R.raw.ysz_open);
										start = true;
									}
									AddThread();
									sleeptime = tempsleep;
									count++;
									tcount.incrementAndGet();
									String rmsg = msg.getTmsga();
									if (msg.iswin()) {
										handler.sendMessage(handler.obtainMessage(200, msg));
										rmsg += msg.getTmsgb() + ","+ msg.getGiftname();
									}
									handler.sendMessage(handler.obtainMessage(100,sdf.format(new Date()) + " "+ pi.getPhone() + " 线程" + index+ "_" + count + "_" + rmsg));
								} else {
									if (index == 0 && start) {
										Utils.playsound(StartActivity.this,R.raw.unique_input);
										start = false;
									}
									if (index > 0) {
										handler.sendMessage(handler.obtainMessage(100, sdf.format(new Date()) + " "+ pi.getPhone() + " 线程_"+ index + "_即将推出"));
										break;
									}
									sleeptime = 1000;
									handler.sendMessage(handler.obtainMessage(100,sdf.format(new Date()) + " "+ pi.getPhone() + " 线程_"+ index + "_时间未到,请稍后"));
	
								}
							} else {
								if (index > 0) {
									handler.sendMessage(handler.obtainMessage(100,sdf.format(new Date()) + " "+ pi.getPhone() + " 线程_"+ index + "_服务器异常即将推出"));
									break;
								}
								sleeptime = 1000;
							}
						}else{
							if (index > 0) {
								handler.sendMessage(handler.obtainMessage(100,sdf.format(new Date()) + " "+ pi.getPhone() + " 线程_"+ index + "_摇奖还没有开始即将推出"));
								break;
							}
							sleeptime = 30000;
							handler.sendMessage(handler.obtainMessage(100,sdf.format(new Date()) + " "+ pi.getPhone() + " 线程_"+ index + "_摇奖还没有开始,休息_"+sleeptime));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				}
				pi.setRun(false);
				tthread.decrementAndGet();
				handler.sendMessage(handler.obtainMessage(100,sdf.format(new Date()) + " " + pi.getPhone() + " 线程_"+ index + "_退出"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static boolean CanStart() {
		Calendar d = Calendar.getInstance();
		d.setTime(new Date());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 20);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return d.after(c);
	}

	public static boolean NoEnd() {
		Calendar d = Calendar.getInstance();
		d.setTime(new Date());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 21);
		c.set(Calendar.MINUTE, 40);
		c.set(Calendar.SECOND, 0);
		return d.before(c);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isrun = false;
		HttpUtils.closeHttpclient();
	}

	class ListenerImpl implements OnSeekBarChangeListener,
			OnCheckedChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			tempsleep = progress;
			startpn.setText("自动摇摇_" + tthread.intValue() + "_"+ tcount.intValue() + "_" + tempsleep);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				datalist.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			} else {
				datalist.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
			}
		}

	}

}

package com.pro.bdfxxt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HandActivity extends Activity {

	private Button freshstart, exit;
	private OnClickListenerImpl listener;
	private String action;
	private AnalysisThread thread;
	private TextView msg;
	public static List<String> Data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hand);
		freshstart = (Button) this.findViewById(R.id.freshstart);
		exit = (Button) this.findViewById(R.id.exit);
		msg = (TextView) this.findViewById(R.id.msg);
		listener = new OnClickListenerImpl();
		freshstart.setOnClickListener(listener);
		exit.setOnClickListener(listener);
		action = this.getIntent().getStringExtra("action");
		Log.i("info", action);
		thread = new AnalysisThread();
		handler.postDelayed(thread, 8000);
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if (but.getId() == R.id.freshstart) {
				handler.removeCallbacks(thread);
				thread = new AnalysisThread();
				handler.postDelayed(thread, 7000);
				msg.setText("重新分析中.....");
			} else if (but.getId() == R.id.exit) {
				HandActivity.this.finish();
			}
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				Intent intent=new Intent(HandActivity.this,DisplayActivity.class);
				HandActivity.this.startActivity(intent);
				HandActivity.this.finish();
			}
		}

	};

	class AnalysisThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if ("hhmfw".equals(action)) {
				String[] marray = new String[] { "黑", "红", "梅", "方", "王" };
				int[] mcount = new int[] { 0, 24, 48, 72, 96, 100 };
				Data=GetData(marray, mcount, 100);
			} else if ("lhj".equals(action)) {
				String[] marray = new String[] { "龙", "虎", "和" };
				int[] mcount = new int[] { 0, 31, 63, 66 };
				Data=GetData(marray, mcount, 66);
			} else if ("bjl".equals(action)) {
				String[] marray = new String[] { "闲", "庄", "和" };
				int[] mcount = new int[] { 0, 31, 63, 66 };
				Data=GetData(marray, mcount, 66);
			} else if ("zxs".equals(action)) {
				String[] marray = new String[] { "一", "二", "三" };
				int[] mcount = new int[] { 0, 31, 63, 66 };
				Data=GetData(marray, mcount, 66);
			} else if ("zxw".equals(action)) {
				String[] marray = new String[] { "一", "二", "三", "四", "五" };
				int[] mcount = new int[] { 0, 24, 48, 72, 96, 100 };
				Data=GetData(marray, mcount, 100);
			}
			handler.sendEmptyMessage(1);
		}

	}

	private List<String> GetData(String[] marray, int[] mcount, int count) {
		List<String> ls = new ArrayList<String>();
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			int c = random.nextInt(count - 1) + 1;
			for (int j = 0; j < marray.length; j++) {
				if (mcount[j] <= c && c < mcount[j + 1]) {
					ls.add(marray[j]);
				}
			}
		}
		Log.i("info", ls.size() + "  " + ls.toString());

		return ls;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(thread);
	}

}

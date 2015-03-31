package com.androidpro.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class CrackActivity extends Activity {

	private OnClickListenerImpl listener;
	private ProgressBar pb;
	private TextView promsg;
	private ScrollView textscroll;
	private LinearLayout layoutText;
	private List<String> ls;
	private int pos = 0;
	private Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.crack_activity);
		ClassApplication application=(ClassApplication)this.getApplication();
		application.AddActivity(this);
		
		listener = new OnClickListenerImpl();

		pb = (ProgressBar) this.findViewById(R.id.progress);
		promsg = (TextView) this.findViewById(R.id.promsg);
		textscroll = (ScrollView) this.findViewById(R.id.textscroll);
		layoutText = (LinearLayout) this.findViewById(R.id.layoutText);
		initls();
		handler.sendEmptyMessage(100);

	}

	private void initls() {
		ls = new ArrayList<String>();
		try {
			InputStream in = this.getAssets().open("e.txt");
			InputStreamReader isr = new InputStreamReader(in, "GBK");
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				ls.add(line);
			}
			br.close();
			isr.close();
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class OnClickListenerImpl implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface di, int id) {
			// TODO Auto-generated method stub
			if(id==DialogInterface.BUTTON_POSITIVE){
				CrackActivity.this.finish();
			}
			if(id==DialogInterface.BUTTON_NEGATIVE){
				Intent intent=new Intent(CrackActivity.this,InjectionActivity.class);
				CrackActivity.this.startActivity(intent);
				CrackActivity.this.finish();
			}
		}


	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 100) {
				int c = pb.getProgress();
				if (c < 100) {
					pb.setProgress(c + 1);
					promsg.setText("机型匹配中  " + pb.getProgress() + "%");
					if(c<98){
						handler.sendEmptyMessageDelayed(100, ls.size() * 1000 / 100);
					}else{
						handler.sendEmptyMessageDelayed(100, 3000);
					}
				}
				int p = 100 / (ls.size() - 1);
				int m = c / p + 1;
				if (m > pos&&pos<ls.size()) {
					View view = LayoutInflater.from(CrackActivity.this)
							.inflate(R.layout.item_textview, null);
					TextView itemtext = (TextView) view
							.findViewById(R.id.itemtext);
					itemtext.setText(ls.get(pos));
					layoutText.addView(view);
					textscroll.fullScroll(ScrollView.FOCUS_DOWN);
					pos = m;
				}
				
				
				if(c==100){
					ShowAlertDialog();
				}

			}
		}

	};
	
	
	private void ShowAlertDialog(){
		builder=Utils.CreateAlertDialog(this);
		builder.setTitle("消息");
		builder.setMessage("匹配成功");
		builder.setPositiveButton("返回", listener);
		builder.setNegativeButton("下一步", listener);
		builder.setCancelable(false);
		builder.create().show();
	}
}

package com.pro.bdfxxt;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DisplayActivity extends Activity {

	private OnClickListenerImpl listener;
	private GridView datagrid;
	private Button pre, next,back;
	private DisplayAdapter da;
	private LinearLayout leftdata,rightdata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		listener = new OnClickListenerImpl();
		leftdata = (LinearLayout) this.findViewById(R.id.leftdata);
		rightdata = (LinearLayout) this.findViewById(R.id.rightdata);
		da=new DisplayAdapter(this,leftdata,rightdata, HandActivity.Data);
		da.notifyDataSetChanged();
		pre = (Button) this.findViewById(R.id.pre);
		next = (Button) this.findViewById(R.id.next);
		back = (Button) this.findViewById(R.id.back);
		pre.setOnClickListener(listener);
		next.setOnClickListener(listener);
		back.setOnClickListener(listener);
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			if (but.getId() == R.id.pre) {
				da.prepage();
				da.notifyDataSetChanged();
			} else if (but.getId() == R.id.next) {
				da.nextpage();
				da.notifyDataSetChanged();
			}else if (but.getId() == R.id.back) {
				DisplayActivity.this.finish();
			}
		}

	}

}

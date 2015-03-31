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
import android.widget.RadioGroup;
import android.widget.Toast;

public class FunctionActivity extends Activity {

	private OnClickListenerImpl listener;
	private RadioGroup fungroup;
	private Button start, exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fun);
		listener = new OnClickListenerImpl();
		fungroup = (RadioGroup) this.findViewById(R.id.fungroup);
		start = (Button) this.findViewById(R.id.start);
		exit = (Button) this.findViewById(R.id.exit);
		start.setOnClickListener(listener);
		exit.setOnClickListener(listener);
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			if (but.getId() == R.id.start) {
				checkfun();
			} else if (but.getId() == R.id.exit) {
				FunctionActivity.this.finish();
			}
		}

	}

	private void checkfun() {
		int cid = fungroup.getCheckedRadioButtonId();
		Intent intent=new Intent(this,HandActivity.class);
		switch (cid) {
		case R.id.hhmfw:
			intent.putExtra("action", "hhmfw");
			startActivity(intent);
			break;
		case R.id.lhj:
			intent.putExtra("action", "lhj");
			startActivity(intent);
			break;
		case R.id.bjl:
			intent.putExtra("action", "bjl");
			startActivity(intent);
			break;
		case R.id.zxs:
			intent.putExtra("action", "zxs");
			startActivity(intent);
			break;
		case R.id.zxw:
			intent.putExtra("action", "zxw");
			startActivity(intent);
			break;
		default:
			Toast.makeText(this, "你还没有选中功能", Toast.LENGTH_LONG).show();
			break;
		}
	}

}

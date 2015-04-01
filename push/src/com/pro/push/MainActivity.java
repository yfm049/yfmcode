package com.pro.push;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity {

	private TextView con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		con = (TextView) super.findViewById(R.id.cont);
		Bundle bundle = getIntent().getExtras();
		if (bundle!=null) {
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			con.setText(content);
		}
	}

}

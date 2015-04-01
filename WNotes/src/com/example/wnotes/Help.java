package com.example.wnotes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 帮助界面(一些帮助信息)
 * 
 */
public class Help extends Activity {
	private TextView tvHelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		tvHelp = (TextView) findViewById(R.id.tvAbout);
		tvHelp.setText(R.string.helpMsg);
	}
}

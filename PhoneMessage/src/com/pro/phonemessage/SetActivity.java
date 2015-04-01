package com.pro.phonemessage;

import com.pro.utils.ComUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SetActivity extends Activity {

	private EditText ip;
	private TextView simei;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		ip=(EditText)this.findViewById(R.id.ip);
		simei=(TextView)this.findViewById(R.id.simei);
		simei.setText("IMEI:"+ComUtils.getImei(this));
		ip.setText(ComUtils.getStringConfig(this, "ip", "http://192.168.0.10:8080/text/"));
	}
	public void Save(View v){
		ComUtils.setStringConfig(this, "ip", ip.getText().toString());
		ComUtils.showToast(this, "…Ë÷√≥…π¶");
		this.finish();
	}
	public void exit(View v){
		this.finish();
	}
}

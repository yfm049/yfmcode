package com.chu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberDataActivity extends Activity implements OnClickListener{
	private ImageView iv;
	private TextView usernameTv;
	private Button modifyBtn;
	private String getName;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.member_data);
    	
    	getName = (String)getIntent().getStringExtra("name");
    	iv = (ImageView)findViewById(R.id.booking_back);
    	usernameTv = (TextView)findViewById(R.id.mem_data_username);
    	modifyBtn = (Button)findViewById(R.id.mem_data_modify);
    	
    	usernameTv.setText(getName);
    	iv.setOnClickListener(this);
    	modifyBtn.setOnClickListener(this);
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.booking_back:
			MemberDataActivity.this.finish();
			break;

		case R.id.mem_data_modify:
			Intent intent = new Intent();
			intent.putExtra("name", getName);
			intent.setClass(this, ModifyUserPswActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}

package com.chu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ManageMemberActivity extends Activity implements OnClickListener{
	private ImageButton manage_login;
	private ImageButton manage_reg;
	private ImageView iv;
	private Intent intent = new Intent();
       @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.manage_member);
    	
    	manage_login = (ImageButton)findViewById(R.id.manager_login);
    	manage_reg = (ImageButton)findViewById(R.id.manager_reg);
    	iv = (ImageView)findViewById(R.id.booking_back);
    	manage_login.setOnClickListener(this);
    	manage_reg.setOnClickListener(this);
    	iv.setOnClickListener(this);
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.manager_login:
			intent.setClass(this, LoginActivity.class);
		    startActivity(intent);
			break;
		case R.id.manager_reg:
			intent.setClass(this, InsertActivity.class);
			startActivity(intent);
			break;
		case R.id.booking_back:
			ManageMemberActivity.this.finish();
			break;

		default:
			break;
		}
	}
}

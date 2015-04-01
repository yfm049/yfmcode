package com.xhplovewx.tixing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText password1;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		if("".equals(sp.getString("password", ""))){
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		setContentView(R.layout.activity_login);
		
		password1=(EditText)this.findViewById(R.id.pasword1);
	}
	

	public void butClick(View v) {
		int id = v.getId();
		if (id == R.id.pswbut) {
			String p1=password1.getText().toString();
			if(!p1.equals(sp.getString("password", ""))){
				Toast.makeText(this, "√‹¬Î¥ÌŒÛ", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

}

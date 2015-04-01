package com.xhplovewx.tixing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	private EditText password1,password2;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		password1=(EditText)this.findViewById(R.id.pasword1);
		password2=(EditText)this.findViewById(R.id.password2);
		password1.setText(sp.getString("password", ""));
		password2.setText(sp.getString("password", ""));
	}
	

	public void butClick(View v) {
		int id = v.getId();
		if (id == R.id.pswbut) {
			String p1=password1.getText().toString();
			String p2=password2.getText().toString();
			if(p1.length()<6){
				Toast.makeText(this, "密码最短6位", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!p1.equals(p2)){
				Toast.makeText(this, "确认密码不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			Editor e=sp.edit();
			e.putString("password", p1);
			e.commit();
			Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
			this.finish();
		}else if(id==R.id.back){
			this.finish();
			
		}
	}

}

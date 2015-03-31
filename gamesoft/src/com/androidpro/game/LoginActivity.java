package com.androidpro.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText name,pass;
	private Button login,cancel;
	private OnClickListenerImpl listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.login_activity);
		ClassApplication application=(ClassApplication)this.getApplication();
		application.AddActivity(this);
		
		listener=new OnClickListenerImpl();
		name=(EditText)this.findViewById(R.id.name);
		pass=(EditText)this.findViewById(R.id.pass);
		
		login=(Button)this.findViewById(R.id.login);
		cancel=(Button)this.findViewById(R.id.cancel);
		login.setOnClickListener(listener);
		cancel.setOnClickListener(listener);
	}

	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.login){
				String n=name.getText().toString();
				String p=pass.getText().toString();
				if("技术中心".equals(n)&&"12345678".equals(p)){
					Intent intent=new Intent(LoginActivity.this,ClassificationActivity.class);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
				}else{
					Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
				}
			}
			if(but.getId()==R.id.cancel){
				LoginActivity.this.finish();
			}
		}
		
	}
}

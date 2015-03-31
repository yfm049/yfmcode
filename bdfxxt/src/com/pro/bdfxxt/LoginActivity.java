package com.pro.bdfxxt;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText name,pass;
	private Button login,cancel;
	private CheckBox checkBox;
	private OnClickListenerImpl listener;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		name=(EditText)this.findViewById(R.id.name);
		pass=(EditText)this.findViewById(R.id.pass);
		login=(Button)this.findViewById(R.id.login);
		cancel=(Button)this.findViewById(R.id.cancel);
		checkBox=(CheckBox)this.findViewById(R.id.checkBox);
		listener=new OnClickListenerImpl();
		login.setOnClickListener(listener);
		cancel.setOnClickListener(listener);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		name.setText(sp.getString("name", ""));
		pass.setText(sp.getString("pass", ""));
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			Log.i("login", but.getId()+"");
			if(but.getId()==R.id.login){
				String n=name.getText().toString();
				String p=pass.getText().toString();
				if("技术中心".equals(n)&&"12345678".equals(p)){
					Intent intent=new Intent(LoginActivity.this,FunctionActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					LoginActivity.this.startActivity(intent);
					if(checkBox.isChecked()){
						sp.edit().putString("name", n).commit();
						sp.edit().putString("pass", p).commit();
					}else{
						sp.edit().putString("name", "").commit();
						sp.edit().putString("pass", "").commit();
					}
					LoginActivity.this.finish();
				}else{
					Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
				}
				
			}else if(but.getId()==R.id.cancel){
				LoginActivity.this.finish();
			}
		}
		
	}

}

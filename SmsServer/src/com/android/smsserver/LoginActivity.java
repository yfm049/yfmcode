package com.android.smsserver;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.thread.RequestThread;
import com.android.utils.Utils;

public class LoginActivity extends Activity {

	private EditText name,pass;
	private Button login;
	private OnClickListenerImpl listener;
	private ProgressDialog pd;
	private String loginurl="control/server!ServerLogin.action";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		name=(EditText)this.findViewById(R.id.name);
		pass=(EditText)this.findViewById(R.id.pass);
		login=(Button)this.findViewById(R.id.login);
		listener=new OnClickListenerImpl();
		login.setOnClickListener(listener);
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.login){
				String n=name.getText().toString();
				String p=pass.getText().toString();
				if("".equals(n)){
					Utils.ShowToast(LoginActivity.this, "账号不能为空");
					return;
				}
				if("".equals(p)){
					Utils.ShowToast(LoginActivity.this, "密码不能为空");
					return;
				}
				pd=Utils.createProgressDialog(LoginActivity.this);
				pd.setMessage("正在登陆中");
				pd.show();
				List<NameValuePair> lvp=new ArrayList<NameValuePair>();
				lvp.add(new BasicNameValuePair("mapparam.name", n));
				lvp.add(new BasicNameValuePair("mapparam.pass", p));
				RequestThread rt=new RequestThread(loginurl,lvp,handler);
				rt.start();
			}
		}
		
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				pd.dismiss();
				if(msg.what==1&&msg.obj!=null){
					JSONObject jo=(JSONObject)msg.obj;
					if(jo.getInt("state")==1){
						Utils.setIntConfig(LoginActivity.this, "id", jo.getInt("id"));
						Utils.ShowToast(LoginActivity.this, "认证成功");
						toMainActivity();
					}else{
						Utils.ShowToast(LoginActivity.this, jo.getString("msg"));
					}
				}else{
					Utils.ShowToast(LoginActivity.this, "请求服务器错误");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(LoginActivity.this, "数据错误");
			}
		}
		
	};
	private void toMainActivity(){
		Intent intent=new Intent(this,MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

}

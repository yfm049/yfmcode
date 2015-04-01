package com.pro.orderfoot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pro.task.LoginThread;
import com.pro.utils.ComUtils;

public class LoginActivity extends Activity {

	private Button loginbut;
	private ListenerImpl listener;
	private EditText ip,port,pass;
	private SharedPreferences sp;
	private AlertDialog dialog;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		listener=new ListenerImpl();
		
		loginbut=(Button)this.findViewById(R.id.loginbut);
		ip=(EditText)this.findViewById(R.id.ip);
		port=(EditText)this.findViewById(R.id.port);
		pass=(EditText)this.findViewById(R.id.pass);
		
		ip.setText(sp.getString("ip", "10.129.213.18"));
		port.setText(String.valueOf(sp.getInt("port", 12000)));
		pass.setText(sp.getString("pass", ""));
		
		loginbut.setOnClickListener(listener);
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.dismiss();
			if(msg.what==200){
				Intent intent=new Intent(LoginActivity.this,LodingActivity.class);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}else if(msg.what==100){
				ShowDialog("消息", msg.obj.toString());
			}
		}
		
	};
	

	class ListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.loginbut){
				login();
			}
		}
	}
	public void login(){
		try {
			String sip=ip.getText().toString();
			int sport=Integer.parseInt(port.getText().toString());
			String ps=pass.getText().toString();
			if(!"".equals(sip)&&sport!=0){
				pd=ComUtils.createProgressDialog(this, "登录", "正在登录中，请稍后");
				pd.show();
				LoginThread thread=new LoginThread(this,handler,sip,sport,ps);
				thread.start();
			}else{
				ShowDialog("消息", "ip或端口不能为空");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ShowDialog("消息", "端口输入错误");
		}
	}
	public void ShowDialog(String title,String msg){
		Builder builder=ComUtils.CreateAlertDialog(this, title, msg);
		builder.setPositiveButton("知道了", null);
		dialog=builder.create();
		dialog.show();
	}

	
}

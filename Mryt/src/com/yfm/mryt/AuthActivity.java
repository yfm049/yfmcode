package com.yfm.mryt;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yfm.http.HttpUtils;

public class AuthActivity extends Activity {

	private TextView imei;
	private EditText authcode;
	private TelephonyManager tm;
	private Button gcode,queding,test;
	private ProgressDialog pd;
	private HttpUtils hu;
	private SharedPreferences  sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_auth);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		hu=new HttpUtils();
		pd=new ProgressDialog(this);
		pd.setCancelable(false);
		imei=(TextView)super.findViewById(R.id.imei);
		authcode=(EditText)super.findViewById(R.id.authcode);
		authcode.setText(sp.getString("authcode", ""));
		gcode=(Button)super.findViewById(R.id.gcode);
		queding=(Button)super.findViewById(R.id.queding);
		test=(Button)super.findViewById(R.id.test);
		tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		imei.setText(tm.getDeviceId());
		gcode.setOnClickListener(new OnClickListenerImpl());
		queding.setOnClickListener(new OnClickListenerImpl());
		test.setOnClickListener(new OnClickListenerImpl());
		if(!"".equals(authcode.getText().toString())){
			pd.setMessage("正在验证...");
			pd.show();
			PostDataThread pdt=new PostDataThread(2);
			pdt.start();
		}
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.gcode){
				pd.setMessage("正在获取...");
				pd.show();
				PostDataThread pdt=new PostDataThread(1);
				pdt.start();
			}
			if(but.getId()==R.id.queding){
				pd.setMessage("正在验证...");
				pd.show();
				PostDataThread pdt=new PostDataThread(2);
				pdt.start();
			}
			if(but.getId()==R.id.test){
				pd.setMessage("正在请求试用...");
				pd.show();
				PostDataThread pdt=new PostDataThread(3);
				pdt.start();
			}
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.dismiss();
			if(msg.what==1){
				if(msg.arg1==1){
					Toast.makeText(AuthActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}
				if(msg.arg1==-1){
					Toast.makeText(AuthActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what==2){
				if(msg.arg1==1){
					Editor editor=sp.edit();
					editor.putString("authcode", authcode.getText().toString());
					editor.commit();
					Intent intent=new Intent(AuthActivity.this,MainActivity.class);
					intent.putExtra("gifimg", msg.obj.toString());
					AuthActivity.this.startActivity(intent);
					AuthActivity.this.finish();
				}
				if(msg.arg1==-1){
					Toast.makeText(AuthActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what==3){
				if(msg.arg1==1){
					Intent intent=new Intent(AuthActivity.this,MainActivity.class);
					intent.putExtra("gifimg", msg.obj.toString());
					AuthActivity.this.startActivity(intent);
					AuthActivity.this.finish();
				}
				if(msg.arg1==-1){
					Toast.makeText(AuthActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	};
	class PostDataThread extends Thread{

		private int type;
		public PostDataThread(int type){
			this.type=type;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				if(type==1){
					String url="jk/data!getregCode.action?imei="+imei.getText();
					String json=hu.doget(hu.baseurl+url);
					if(json!=null){
						JSONObject jo=new JSONObject(json);
						if(jo.getBoolean("flag")){
							Message msg=handler.obtainMessage();
							msg.what=1;
							msg.arg1=1;
							msg.obj="请联系管理员获取验证码";
							handler.sendMessage(msg);
						}else{
							Message msg=handler.obtainMessage();
							msg.what=1;
							msg.arg1=-1;
							msg.obj=jo.getString("msg");
							handler.sendMessage(msg);
						}
					}else{
						Message msg=handler.obtainMessage();
						msg.what=1;
						msg.arg1=-1;
						msg.obj="请求失败服务器失败";
						handler.sendMessage(msg);
					}
				}else if(type==2){
					String url="jk/data!regUser.action?imei="+imei.getText()+"&code="+authcode.getText();
					String json=hu.doget(hu.baseurl+url);
					if(json!=null){
						JSONObject jo=new JSONObject(json);
						if(jo.getBoolean("flag")&&jo.getBoolean("imgflag")){
							Message msg=handler.obtainMessage();
							msg.what=2;
							msg.arg1=1;
							msg.obj=jo.getString("gifimg");
							handler.sendMessage(msg);
						}else{
							Message msg=handler.obtainMessage();
							msg.what=2;
							msg.arg1=-1;
							msg.obj=jo.getString("msg");
							handler.sendMessage(msg);
						}
					}else{
						Message msg=handler.obtainMessage();
						msg.what=2;
						msg.arg1=-1;
						msg.obj="请求失败服务器失败";
						handler.sendMessage(msg);
					}
				}else if(type==3){
					String url="jk/data!regUser.action?imei="+imei.getText()+"&test=true";
					String json=hu.doget(hu.baseurl+url);
					if(json!=null){
						JSONObject jo=new JSONObject(json);
						if(jo.getBoolean("flag")&&jo.getBoolean("imgflag")){
							Message msg=handler.obtainMessage();
							msg.what=3;
							msg.arg1=1;
							msg.obj=jo.getString("gifimg");
							handler.sendMessage(msg);
						}else{
							Message msg=handler.obtainMessage();
							msg.what=3;
							msg.arg1=-1;
							msg.obj=jo.getString("msg");
							handler.sendMessage(msg);
						}
					}else{
						Message msg=handler.obtainMessage();
						msg.what=3;
						msg.arg1=-1;
						msg.obj="请求失败服务器失败";
						handler.sendMessage(msg);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.sendEmptyMessage(-1);
			}
		}
		
	}
	
	
	


}

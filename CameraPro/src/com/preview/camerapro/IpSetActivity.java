package com.preview.camerapro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.preview.utils.ConfigUtils;

public class IpSetActivity extends Activity {

	private EditText ip,port;
	private Button save,quxiao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_set_ip);
		ip=(EditText)super.findViewById(R.id.ip);
		port=(EditText)super.findViewById(R.id.port);
		ip.setText(ConfigUtils.GetString(this, "ip", ""));
		port.setText(ConfigUtils.GetString(this, "port", ""));
		save=(Button)super.findViewById(R.id.save);
		quxiao=(Button)super.findViewById(R.id.quxiao);
		save.setOnClickListener(new saveOnClickListener());
		quxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IpSetActivity.this.finish();
			}
		});
	}
	class saveOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			try {
				String ipvalue=ip.getText().toString();
				int portvalue=Integer.parseInt(port.getText().toString());
				Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
				Matcher matcher = pattern.matcher(ipvalue); //以验证127.400.600.2为例
				if(!matcher.matches()){
					Toast.makeText(IpSetActivity.this, "ip地址格式不正确", Toast.LENGTH_LONG).show();
					return;
				}
				if(portvalue<=0){
					Toast.makeText(IpSetActivity.this, "端口必须是正整数", Toast.LENGTH_LONG).show();
					return;
				}
				ConfigUtils.savaDate(IpSetActivity.this, "ip", ipvalue);
				ConfigUtils.savaDate(IpSetActivity.this, "port", String.valueOf(portvalue));
				Toast.makeText(IpSetActivity.this, "设置成功", Toast.LENGTH_LONG).show();
				IpSetActivity.this.finish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(IpSetActivity.this, "端口必须是正整数", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
}

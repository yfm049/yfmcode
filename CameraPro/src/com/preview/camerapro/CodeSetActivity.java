package com.preview.camerapro;

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

public class CodeSetActivity extends Activity {

	private EditText ip;
	private Button save,quxiao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_set_code);
		ip=(EditText)super.findViewById(R.id.ip);
		ip.setText(ConfigUtils.GetString(this, "code", ""));
		save=(Button)super.findViewById(R.id.save);
		quxiao=(Button)super.findViewById(R.id.quxiao);
		save.setOnClickListener(new saveOnClickListener());
		quxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CodeSetActivity.this.finish();
			}
		});
	}
	class saveOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			try {
				String ipvalue=ip.getText().toString();
				ConfigUtils.savaDate(CodeSetActivity.this, "code", ipvalue);
				Toast.makeText(CodeSetActivity.this, "设置成功", Toast.LENGTH_LONG).show();
				CodeSetActivity.this.finish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(CodeSetActivity.this, "端口必须是正整数", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
}

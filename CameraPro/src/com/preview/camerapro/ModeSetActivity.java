package com.preview.camerapro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.preview.utils.ConfigUtils;

public class ModeSetActivity extends Activity {

	private Spinner firstmode,secmode;
	private Button save,quxiao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_set_mode);
		save=(Button)super.findViewById(R.id.save);
		firstmode=(Spinner)super.findViewById(R.id.firstmode);
		secmode=(Spinner)super.findViewById(R.id.secmode);
		secmode.setEnabled(false);
		firstmode.setOnItemSelectedListener(new modeOnItemSelectedListener());
		quxiao=(Button)super.findViewById(R.id.quxiao);
		save.setOnClickListener(new saveOnClickListener());
		quxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ModeSetActivity.this.finish();
			}
		});
		firstmode.setSelection(Integer.parseInt(ConfigUtils.GetString(this, "first", "0")));
		secmode.setSelection(Integer.parseInt(ConfigUtils.GetString(this, "sec", "0")));
	}
	class modeOnItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int select,
				long arg3) {
			// TODO Auto-generated method stub
				if(select==1){
					secmode.setEnabled(true);
				}else{
					secmode.setEnabled(false);
				}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	class saveOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int first=firstmode.getSelectedItemPosition();
			int sec=secmode.getSelectedItemPosition();
			ConfigUtils.savaDate(ModeSetActivity.this, "first", String.valueOf(first));
			ConfigUtils.savaDate(ModeSetActivity.this, "sec", String.valueOf(sec));
			Toast.makeText(ModeSetActivity.this, "保存成功", Toast.LENGTH_LONG).show();
			ModeSetActivity.this.finish();
		}
		
	}
}

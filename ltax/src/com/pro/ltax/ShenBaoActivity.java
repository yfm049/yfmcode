package com.pro.ltax;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * 设置提醒 查看日历
 * @author lenovo
 *
 */
public class ShenBaoActivity extends Activity {

	private Button chakan;
	private CheckBox tixing;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tixing);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		chakan=(Button)super.findViewById(R.id.chakan);
		tixing=(CheckBox)super.findViewById(R.id.tixing);
		chakan.setOnClickListener(new OnClickListenerImpl());
		tixing.setChecked(sp.getBoolean("flag", true));
		tixing.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		
	}
	class OnCheckedChangeListenerImpl implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			Editor editor=sp.edit();
			editor.putBoolean("flag", arg1);
			editor.commit();
		}
		
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			/**
			 * 显示日历
			 */
			Intent intent=new Intent(ShenBaoActivity.this,RiLiActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ShenBaoActivity.this.startActivity(intent);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

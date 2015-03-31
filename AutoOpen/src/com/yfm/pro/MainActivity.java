package com.yfm.pro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner brow;
	private Button save,quxiao;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		brow=(Spinner)super.findViewById(R.id.brow);
		save=(Button)super.findViewById(R.id.save);
		quxiao=(Button)super.findViewById(R.id.quxiao);
		save.setOnClickListener(new saveOnClickListener());
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		quxiao.setOnClickListener(new quxiaoOnClickListener());
	}
	class saveOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String svalue=brow.getSelectedItem().toString();
			Editor edit=sp.edit();
			edit.putString("key", svalue);
			edit.commit();
			Toast.makeText(MainActivity.this, "…Ë÷√≥…π¶", Toast.LENGTH_LONG).show();
			MainActivity.this.finish();
		}
		
	}
	class quxiaoOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			MainActivity.this.finish();
		}
		
	}


}

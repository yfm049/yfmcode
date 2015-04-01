package com.pro.ltax;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 功能列表展示
 * @author lenovo
 *
 */
public class FunActivity extends Activity {

	private Button bmgg,sbtx,flfg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fun);
		bmgg=(Button)super.findViewById(R.id.bmgg);
		bmgg.setOnClickListener(new OnClickListenerImpl());
		sbtx=(Button)super.findViewById(R.id.sbtx);
		sbtx.setOnClickListener(new OnClickListenerImpl());
		flfg=(Button)super.findViewById(R.id.flfg);
		flfg.setOnClickListener(new OnClickListenerImpl());
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.bmgg){
				//部门公告
				Intent intent=new Intent(FunActivity.this,GonggaoActivity.class);
				FunActivity.this.startActivity(intent);
			}
			if(but.getId()==R.id.sbtx){
				//申报
				Intent intent=new Intent(FunActivity.this,ShenBaoActivity.class);
				FunActivity.this.startActivity(intent);
			}
			if(but.getId()==R.id.flfg){
				//法律法规
				Intent intent=new Intent(FunActivity.this,FlFgActivity.class);
				FunActivity.this.startActivity(intent);
			}
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

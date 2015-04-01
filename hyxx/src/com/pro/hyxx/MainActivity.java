package com.pro.hyxx;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button query,record,rehearse;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		query=(Button)super.findViewById(R.id.query);
		record=(Button)super.findViewById(R.id.record);
		rehearse=(Button)super.findViewById(R.id.rehearse);
		query.setOnClickListener(new OnClickListenerImpl());
		record.setOnClickListener(new OnClickListenerImpl());
		rehearse.setOnClickListener(new OnClickListenerImpl());
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.query){
				Intent intent=new Intent(MainActivity.this,QueryActivity.class);
				MainActivity.this.startActivity(intent);
			}else if(but.getId()==R.id.record){
				Intent intent=new Intent(MainActivity.this,RecordActivity.class);
				MainActivity.this.startActivity(intent);
			}else if(but.getId()==R.id.rehearse){
				Intent intent=new Intent(MainActivity.this,RehearseActivity.class);
				MainActivity.this.startActivity(intent);
			}
		}
		
	}


}

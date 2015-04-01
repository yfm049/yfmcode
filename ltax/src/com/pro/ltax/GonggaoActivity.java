package com.pro.ltax;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 公告展示
 * @author lenovo
 *
 */
public class GonggaoActivity extends Activity {

	private Button zcjd,yggg,ssrdwt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gonggao);
		zcjd=(Button)super.findViewById(R.id.flfg);
		yggg=(Button)super.findViewById(R.id.bmgg);
		ssrdwt=(Button)super.findViewById(R.id.sbtx);
		zcjd.setOnClickListener(new OnClickListenerImpl());
		yggg.setOnClickListener(new OnClickListenerImpl());
		ssrdwt.setOnClickListener(new OnClickListenerImpl());
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			//根据功能不同展示不通url数据
			Intent intent=new Intent(GonggaoActivity.this,ListDataActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if(but.getId()==R.id.flfg){
				intent.putExtra("url", "/portal/site/site/portal/ynds/ynyx/category.portal?categoryId=5BF973C5B5ACD9DCE46B0817F5680FED");
			}
			if(but.getId()==R.id.bmgg){
				intent.putExtra("url", "/portal/site/site/portal/ynds/ynyx/category.portal?categoryId=39AD2A5FE4AC6CA8F583DD61DAA33A7D");
			}
			if(but.getId()==R.id.sbtx){
				intent.putExtra("url", "/portal/site/site/portal/ynds/ynyx/category.portal?categoryId=97E250FFB469E8334604AF18DC1A78F1");
			}
			
			GonggaoActivity.this.startActivity(intent);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

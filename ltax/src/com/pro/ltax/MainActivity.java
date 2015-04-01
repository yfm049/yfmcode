package com.pro.ltax;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pro.db.DbUtils;
/**
 * µÇÂ¼Ò³Ãæ
 * @author lenovo
 *
 */
public class MainActivity extends Activity {

	private EditText code;
	private Button login;
	private DbUtils dbu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbu=new DbUtils(this);
		code=(EditText)super.findViewById(R.id.code);
		login=(Button)super.findViewById(R.id.sbtx);
		login.setOnClickListener(new OnClickListenerImpl());
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String cd=code.getText().toString();
			if(!"".equals(cd)){
				//¿ªÊ¼µÇÂ¼
				boolean flag=dbu.login(cd);
				if(flag){
					Toast.makeText(MainActivity.this, "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(MainActivity.this,FunActivity.class);
					MainActivity.this.startActivity(intent);
					MainActivity.this.finish();
				}else{
					Toast.makeText(MainActivity.this, "µÇÂ¼Ê§°Ü", Toast.LENGTH_SHORT).show();
				}
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

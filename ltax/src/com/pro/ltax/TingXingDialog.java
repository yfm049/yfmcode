package com.pro.ltax;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * 提醒对话框
 * @author lenovo
 *
 */
public class TingXingDialog extends Activity {

	private Button close,zhidaole;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.activity_tixing_dialog);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		close=(Button)super.findViewById(R.id.close);
		zhidaole=(Button)super.findViewById(R.id.zhidaole);
		close.setOnClickListener(new OnClickListenerImpl());
		zhidaole.setOnClickListener(new OnClickListenerImpl());
	}

	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.close){
				//关闭提醒
				Editor editor=sp.edit();
				editor.putBoolean("flag", false);
				editor.commit();
			}
			TingXingDialog.this.finish();
		}
		
	}
}

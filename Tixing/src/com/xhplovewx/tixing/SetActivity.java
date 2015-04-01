package com.xhplovewx.tixing;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetActivity extends Activity {

	private CheckBox sycb,zdcb;
	private ListenerImpl listener;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		sycb=(CheckBox)this.findViewById(R.id.sycb);
		zdcb=(CheckBox)this.findViewById(R.id.zdcb);
		sycb.setChecked(sp.getBoolean("sycb", false));
		zdcb.setChecked(sp.getBoolean("zdcb", false));
		listener=new ListenerImpl();
		sycb.setOnCheckedChangeListener(listener);
		zdcb.setOnCheckedChangeListener(listener);
	}
	
	class ListenerImpl implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(buttonView.getId()==R.id.sycb){
				Editor e=sp.edit();
				e.putBoolean("sycb", isChecked);
				e.commit();
			}
			if(buttonView.getId()==R.id.zdcb){
				Editor e=sp.edit();
				e.putBoolean("zdcb", isChecked);
				e.commit();
			}
		}
		
	}

	public void butClick(View v) {
		int id = v.getId();
		if (id == R.id.kc_but) {
			Intent intent=new Intent(this,PasswordActivity.class);
			startActivity(intent);
		}else if (id == R.id.about_but) {
			Intent intent=new Intent(this,AboutActivity.class);
			startActivity(intent);
		}else if(id==R.id.back){
			this.finish();
			
		}
	}

}

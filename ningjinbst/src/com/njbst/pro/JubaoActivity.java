package com.njbst.pro;

import com.njbst.async.JubaoAsyncTask;
import com.njbst.pojo.MoreInfo;
import com.njbst.utils.ComUtils;
import com.njbst.utils.ToastUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class JubaoActivity extends ActionBarActivity {

	private RadioGroup jubaotypes;
	
	private String[] gtypes=new String[]{"job","house","ershou","zhjy"};
	
	private EditText jname,jtel;
	private MoreInfo info;
	private int index=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jubao);
		info=(MoreInfo)this.getIntent().getSerializableExtra("info");
		index=this.getIntent().getIntExtra("index", 0);
		jubaotypes=(RadioGroup)this.findViewById(R.id.jubaotypes);
		jname=(EditText)this.findViewById(R.id.jname);
		jtel=(EditText)this.findViewById(R.id.jtel);
	}
	
	
	
	
	public void butClick(View view){
		String n=jname.getText().toString();
		String t=jtel.getText().toString();
		if(n.length()<=0){
			ToastUtils.showToast(this, R.string.jubao_name_warn);
			return;
		}
		if(!ComUtils.isMobileNO(t)){
			ToastUtils.showToast(this, R.string.jubao_tel_warn);
			return;
		}
		int type=jubaotypes.indexOfChild(jubaotypes.findViewById(jubaotypes.getCheckedRadioButtonId()));
		JubaoAsyncTask jat=new JubaoAsyncTask(this, handler);
		jat.execute(gtypes[index],String.valueOf(info.getId()),String.valueOf(type),n,t);
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				JubaoActivity.this.finish();
			}
		}
		
	};
	
	
	

}

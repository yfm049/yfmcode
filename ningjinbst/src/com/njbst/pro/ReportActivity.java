package com.njbst.pro;

import com.njbst.async.ReportAsyncTask;
import com.njbst.fragment.MoreFragment;
import com.njbst.utils.ComUtils;
import com.njbst.utils.ToastUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ReportActivity extends ActionBarActivity {

	private RadioGroup grouptypes,types;
	private ListenerImpl listener;
	private String[] stypes;
	private int index=0;
	private int[] gt=new int[]{R.id.qzzp_id,R.id.fcxx_id,R.id.esmm_id,R.id.zhjy_id};
	private String[] gtypes=new String[]{"job","house","ershou","zhjy"};
	
	private EditText rcon,rtel,rqq,rpwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		index=this.getIntent().getIntExtra("index", 0);
		
		grouptypes=(RadioGroup)this.findViewById(R.id.grouptypes);
		types=(RadioGroup)this.findViewById(R.id.types);
		listener=new ListenerImpl();
		grouptypes.setOnCheckedChangeListener(listener);
		stypes=this.getResources().getStringArray(R.array.qzzp);
		grouptypes.check(gt[index]);
		
		rcon=(EditText)this.findViewById(R.id.rcon);
		rtel=(EditText)this.findViewById(R.id.rtel);
		rqq=(EditText)this.findViewById(R.id.rqq);
		rpwd=(EditText)this.findViewById(R.id.rpwd);
	}
	
	class ListenerImpl implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
				case R.id.qzzp_id:
					stypes=ReportActivity.this.getResources().getStringArray(R.array.qzzp);
					break;
				case R.id.fcxx_id:
					stypes=ReportActivity.this.getResources().getStringArray(R.array.fcxx);
					break;
				case R.id.esmm_id:
					stypes=ReportActivity.this.getResources().getStringArray(R.array.esmm);
					break;
				case R.id.zhjy_id:
					stypes=ReportActivity.this.getResources().getStringArray(R.array.zhjy);
					break;
	
				default:
					break;
				}
			inittypes();
		}
		
	}
	
	private void inittypes(){
		types.removeAllViews();
		boolean flag=true;
		if(stypes!=null){
			for(int i=0;i<stypes.length;i++){
				RadioButton rb=(RadioButton)LayoutInflater.from(this).inflate(R.layout.activity_report_item, null);
				rb.setText(stypes[i]);
				rb.setChecked(flag);
				flag=false;
				rb.setId(i+10);
				types.addView(rb, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}
		}
	}
	
	public void butClick(View view){
		if(view.getId()==R.id.savebut){
			int gindex=grouptypes.indexOfChild(grouptypes.findViewById(grouptypes.getCheckedRadioButtonId()));
			int tindex=types.indexOfChild(types.findViewById(types.getCheckedRadioButtonId()));
			String con=rcon.getText().toString();
			String tel=rtel.getText().toString();
			String qq=rqq.getText().toString();
			String pwd=rpwd.getText().toString();
			if(con.length()<=0){
				ToastUtils.showToast(this, R.string.report_con_warn);
				return ;
			}
			if(pwd.length()<6){
				ToastUtils.showToast(this, R.string.report_pwd_warn);
				return ;
			}
			new ReportAsyncTask(this,handler).execute(gtypes[gindex],con,String.valueOf(tindex),tel,qq,pwd);
			
		}else if(view.getId()==R.id.cancelbut){
			
		}
		
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				MoreFragment.isupdate=true;
				ReportActivity.this.finish();
			}
		}
		
	};
	
	
	

}

package com.njbst.pro;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.njbst.async.MyInfoAsyncTask;
import com.njbst.async.SetMyInfoAsyncTask;
import com.njbst.pojo.MyInfo;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.utils.ComUtils;
import com.njbst.utils.ToastUtils;
import com.njbst.view.CircleImageView;

public class MyInfoActivity extends ActionBarActivity {

	private EditText name,age,address,tel;
	private RadioGroup sexgroup;
	private MyInfo info=new MyInfo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		name=(EditText)this.findViewById(R.id.name);
		age=(EditText)this.findViewById(R.id.age);
		sexgroup=(RadioGroup)this.findViewById(R.id.sexgroup);
		address=(EditText)this.findViewById(R.id.address);
		tel=(EditText)this.findViewById(R.id.tel);
		new MyInfoAsyncTask(this,handler,info,true).execute("");
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				setvalue();
			}else if(msg.what==2){
				finish();
			}
		}
		
	};
	public void butClick(View v){
		String n=name.getText().toString();
		RadioButton sexrb=(RadioButton)sexgroup.findViewById(sexgroup.getCheckedRadioButtonId());
		String a=age.getText().toString();
		String as=address.getText().toString();
		String tl=tel.getText().toString();
		if(n.length()<=0){
			ToastUtils.showToast(this, R.string.my_name_warn);
			return;
		}
		if(a.length()<=0){
			
			ToastUtils.showToast(this, R.string.my_age_warn);
			return;
		}else{
			try {
				int ag=Integer.parseInt(a);
				if(ag<=0||ag>200){
					ToastUtils.showToast(this, R.string.my_age_warn);
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtils.showToast(this, R.string.my_age_warn);
				return;
			}
		}
		if(as.length()<=0){
			ToastUtils.showToast(this, R.string.my_address_warn);
			return;
		}
		if(!ComUtils.isMobileNO(tl)){
			ToastUtils.showToast(this, R.string.my_phone_warn);
			return;
		}
		info.setName(n);
		info.setSex(sexrb.getText().toString());
		info.setAge(a);
		info.setAddress(as);
		info.setPhone(tl);
		new SetMyInfoAsyncTask(this,handler,info,true).execute("");
	}
	private void setvalue(){
		if(info!=null&&info.isIsflag()){
			name.setText(info.getName());
			age.setText(info.getAge());
			if(info.getAge().equals(this.getResources().getString(R.string.my_sex_nv_text))){
				sexgroup.check(R.id.nv);
			}else{
				sexgroup.check(R.id.nan);
			}
			address.setText(info.getAddress());
			tel.setText(info.getPhone());
		}
		
	}

}

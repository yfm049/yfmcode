package com.chu;

import com.chu.servlet.UserService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InsertActivity extends Activity implements OnClickListener{
	private EditText username;
	private EditText password;
	private EditText psw_next;
	private Button reg_btn;
	private ImageView iv;
	private String getUsername;
	private String getPsw;
	private String getPsw_next;
      @SuppressLint("ShowToast")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.user_reg);
    	
    	username = (EditText)findViewById(R.id.reg_name);
    	password = (EditText)findViewById(R.id.reg_psw);
    	psw_next = (EditText)findViewById(R.id.reg_psw_next);
    	reg_btn = (Button)findViewById(R.id.reg_btn);
    	iv = (ImageView)findViewById(R.id.booking_back);
    	
    	reg_btn.setOnClickListener(this);
    	iv.setOnClickListener(this);
    	
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.booking_back:
			InsertActivity.this.finish();
			break;
		case R.id.reg_btn:
			getUsername = username.getText().toString();
	    	getPsw = password.getText().toString();
	    	getPsw_next = psw_next.getText().toString();
	    	if(getUsername==""){
	    		Toast.makeText(InsertActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
	    	}else if(getPsw==""||getPsw_next==""){
	    		Toast.makeText(InsertActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
	    	}else if(checkPassword(getPsw, getPsw_next)==true){
	    		//两次密码输入一致,开始注册
	    		try{
	    			String getData = UserService.senRegData(getUsername, getPsw);
	    			System.out.println("get得到的数据为:" + getData);
	    			if(getData.equals("OK")){
	    				Toast.makeText(InsertActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
	    			}else if(getData.equals("Exist")){
	    				Toast.makeText(InsertActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
	    			}else if(getData.equals("Error")){
	    				Toast.makeText(InsertActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
	    			}
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}else if(checkPassword(getPsw, getPsw_next)==false){
	    		Toast.makeText(InsertActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
	    	}
			break;

		default:
			break;
		}
	}
	public boolean checkPassword(String psw,String psw_next){
		if(psw.equals(psw_next)){
			return true;
		}else{
			return false;
		}
		
	}
}

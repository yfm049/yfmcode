package com.chu;

import com.chu.servlet.UserService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ModifyUserPswActivity extends Activity implements OnClickListener{
	private EditText editText;
	private ImageView iv;
	private Button modify_btn;
	private String getPsw = "";
	private String getName = "";
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.modify_user_psw);
    	getName = (String)getIntent().getStringExtra("name");
    	System.out.println(getName);
    	editText = (EditText)findViewById(R.id.modify_user_psw_edit);
    	iv = (ImageView)findViewById(R.id.booking_back);
    	modify_btn = (Button)findViewById(R.id.modify_user_psw_btn);
    	
    	iv.setOnClickListener(this);
    	modify_btn.setOnClickListener(this);
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.booking_back:
			ModifyUserPswActivity.this.finish();
			break;
			
		case R.id.modify_user_psw_btn:
			try {
				getPsw = editText.getText().toString();
				System.out.println(getPsw);
				String getData = UserService.modify(getName, getPsw);
				System.out.println("得到修改密码的返回数据为:"+getData);
				if(getData.equals("OK")){
					Toast.makeText(ModifyUserPswActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
				}else if(getData.equals("Failed")){
					Toast.makeText(ModifyUserPswActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
}

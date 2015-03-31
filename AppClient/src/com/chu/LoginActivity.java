package com.chu;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chu.servlet.UserService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText name_edit;
	private EditText psw_edit;
	private Button btn;
	private ImageView iv;

	public static boolean loginTag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		name_edit = (EditText) findViewById(R.id.edit_name);
		psw_edit = (EditText) findViewById(R.id.edit_psw);
		btn = (Button) findViewById(R.id.btn);
		iv = (ImageView) findViewById(R.id.booking_back);
		btn.setOnClickListener(this);
		iv.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn:
			String name = name_edit.getText().toString();
			String password = psw_edit.getText().toString();

			/*
			 * //判断该用户是否以前登陆过，查询chu_user.xml文件 SharedPreferences
			 * sharedPreferences = getSharedPreferences("chu_user",
			 * Activity.MODE_PRIVATE); String getShpName =
			 * sharedPreferences.getString("name", ""); String getShpPassword =
			 * sharedPreferences.getString("password", "");
			 * System.out.println("getShpName:" + getShpName);
			 * System.out.println("getShpPassword:" + getShpPassword);
			 * if(name==getShpName&&password==getShpPassword){
			 * System.out.println("没有经过服务器"); Intent intent = new Intent();
			 * intent.putExtra("name", name); intent.setClass(this,
			 * MemberDataActivity.class); startActivity(intent); }else{
			 */
			// String urlStr =
			// "http://10.0.2.2:8080/AppServer/insertServlet?username=" + name +
			// "&password=" + password;
			try {
				String getData = UserService.check(name, password);
				System.out.println("get得到的数据为:" + getData);
				// 将取得的json数据解析出来
				JSONArray array = new JSONArray(getData);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String getSuccessData = obj.getString("successData");
					String getTicketInfo = obj.getString("getTicketInfo");
					String failedData = obj.getString("failedData");
					System.out.println(getSuccessData + getTicketInfo
							+ failedData);
					if (getSuccessData.equals("OK")) {
						// 保存用户信息
						System.out.println("经过了服务器");
						saveUserInfo(name, password);
						loginTag = true;
						Intent intent = new Intent();
						intent.putExtra("name", name);

						intent.setClass(this, MemberDataActivity.class);
						startActivity(intent);
					} else if (failedData.equals("Error")) {
						Toast.makeText(this, "用户" + name + "登录失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case R.id.booking_back:
			LoginActivity.this.finish();
			break;
		default:
			break;
		}

	}

	private void saveUserInfo(String name, String password) {
		SharedPreferences sharedPreferences = getSharedPreferences("chu_user",
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("name", name);
		editor.putString("password", password);
		editor.commit();// 提交修改
	}
}

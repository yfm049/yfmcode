package com.njbst.pro;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.njbst.async.QQLoginAsyncTask;
import com.njbst.utils.ToastUtils;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLoginActivity extends Activity {

	private String tencentappid="1104216780";
	private Tencent mTencent;
	private String openid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mTencent=Tencent.createInstance(tencentappid, this);
		mTencent.login(this, "all", new IUiListenerImpl("login"));
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 2) {
				Intent intent=new Intent(QQLoginActivity.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			finish();
		}

	};
	
	class IUiListenerImpl implements IUiListener{

		private String action;
		public IUiListenerImpl(String action){
			this.action=action;
		}
		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			ToastUtils.showToast(QQLoginActivity.this, R.string.loginfail_msg);
		}

		@Override
		public void onComplete(Object ot) {
			// TODO Auto-generated method stub
			try {
				if("login".equals(action)){
					JSONObject jo=(JSONObject)ot;
					openid=jo.getString("openid");
					UserInfo userinfo=new UserInfo(QQLoginActivity.this, mTencent.getQQToken());
					userinfo.getUserInfo(new IUiListenerImpl("userinfo"));
				}else if("userinfo".equals(action)){
					JSONObject jo=(JSONObject)ot;
					if(jo.getInt("ret")==0){
						String nickname=jo.getString("nickname");
						String gender=jo.getString("gender");
						String sex="ÄÐ".equals(gender)?"1":"2";
						String figureurl=jo.getString("figureurl_qq_1");
						new QQLoginAsyncTask(QQLoginActivity.this,handler).execute("openlogin",openid,nickname,sex,figureurl,"","");
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub
			ToastUtils.showToast(QQLoginActivity.this, R.string.loginfail_msg);
		}
		
	}
	
	
}

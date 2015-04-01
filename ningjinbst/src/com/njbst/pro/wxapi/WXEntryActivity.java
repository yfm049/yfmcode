package com.njbst.pro.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.njbst.async.WxLoginAsyncTask;
import com.njbst.pro.MainActivity;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;

public class WXEntryActivity extends Activity {

	private String wxappid="wxd2c6b525843e31b9";
	private String appsecret="3c2e34266bdcb6f3c0fd1add2544b838";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 2) {
				Intent intent=new Intent(WXEntryActivity.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			finish();
		}

	};

	private void handleIntent(Intent intent) {
		SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
		System.out.println(resp);
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			WxLoginAsyncTask qqt=new WxLoginAsyncTask(this, handler);
			qqt.execute("openlogin",resp.code,wxappid,appsecret);
		}else{
			finish();
		}
	}

}

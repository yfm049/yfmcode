package com.njbst.pro;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXLoginActivity extends Activity {

	private String wxappid = "wxd2c6b525843e31b9";
	private IWXAPI mWeixinAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mWeixinAPI = WXAPIFactory.createWXAPI(this, wxappid, false);
		if (mWeixinAPI.isWXAppInstalled()) {
			mWeixinAPI.registerApp(wxappid);
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "123";
			mWeixinAPI.sendReq(req);
		}
		finish();
	}

}

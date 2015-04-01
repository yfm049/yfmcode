package com.njbst.pro;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.njbst.utils.AccessTokenKeeper;
import com.njbst.utils.ToastUtils;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class DetailActivity extends ActionBarActivity implements IUiListener,
		IWeiboHandler.Response, WeiboAuthListener {

	// QQ分享
	private Tencent mTencent;
	private String tencentappid = "1104216780";
	// 新浪
	public static final String SINAAPP_KEY = "2693716449";
	private IWeiboShareAPI mWeiboShareAPI;
	public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

	// 微信
	public final String WXAPP_ID = "wxd2c6b525843e31b9";
	private IWXAPI wxapi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mTencent = Tencent.createInstance(tencentappid, this);
		wxapi = WXAPIFactory.createWXAPI(this, WXAPP_ID, true);
		wxapi.registerApp(WXAPP_ID);

		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, SINAAPP_KEY);
		mWeiboShareAPI.registerApp();

		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	public boolean shareToQzone(String title, String imaurl, String con,
			String linkurl) {
		Bundle bundle = new Bundle();
		bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, con);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, linkurl);
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(imaurl);
		bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		mTencent.shareToQzone(this, bundle, this);
		return true;
	}

	public boolean shareToQQ(String title, String imaurl, String con,
			String linkurl) {
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("imageUrl", imaurl);
		bundle.putString("targetUrl", linkurl);
		bundle.putString("summary", con);
		bundle.putString("appName",
				this.getResources().getString(R.string.app_name));
		mTencent.shareToQQ(this, bundle, this);
		return false;
	}

	public void ShareToSina(String title, String imgurl, String desc, String url) {
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = title;
		mediaObject.description = desc;
		mediaObject.setThumbImage(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.ic_launcher));
		mediaObject.actionUrl = url;
		mediaObject.defaultText = "Webpage 默认文案";

		weiboMessage.mediaObject = mediaObject;

		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
		if (isInstalledWeibo) {
            mWeiboShareAPI.sendRequest(this, request);
        }else{
        	AuthInfo authInfo = new AuthInfo(this, SINAAPP_KEY, "https://api.weibo.com/oauth2/default.html", SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(this, request, authInfo, token, this);
        }
		
	}

	public void ShareToWx(String title, String imgurl, String desc, String url) {

		WXImageObject imgObj = new WXImageObject();
		imgObj.imageUrl = imgurl;

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		wxapi.sendReq(req);
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		ToastUtils.showToast(this, R.string.share_fail_text);
	}

	@Override
	public void onComplete(Object ot) {
		// TODO Auto-generated method stub
		try {
			JSONObject jo = (JSONObject) ot;
			if (jo.getInt("ret") == 0) {
				ToastUtils.showToast(this, R.string.share_suc_text);
			} else {
				ToastUtils.showToast(this, R.string.share_fail_text);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onError(UiError arg0) {
		// TODO Auto-generated method stub
		ToastUtils.showToast(this, R.string.share_fail_text);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			ToastUtils.showToast(this, R.string.share_suc_text);
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			ToastUtils.showToast(this, R.string.share_fail_text);
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			ToastUtils.showToast(this, R.string.share_fail_text);
			break;
		}
	}

	public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	@Override
	public void onComplete(Bundle bundle) {
		// TODO Auto-generated method stub
		Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
        AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub
	}
	
	public void ShareToSms(String title, String imgurl, String desc, String url){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT, title+":"+desc+"_"+url);
		startActivity(Intent.createChooser(intent, getTitle()));
	}
}

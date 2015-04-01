package com.njbst.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;
import com.njbst.utils.progressDialogUtils;

public class WxLoginAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private progressDialogUtils pdu;
	private Context context;
	private Handler handler;

	public WxLoginAsyncTask(Context context, Handler handler) {
		this.context = context;
		pdu = new progressDialogUtils(context, this);
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pdu.showPd(R.string.loginging_msg);
	}

	@Override
	protected void onPostExecute(JSONObject jo) {
		// TODO Auto-generated method stub
		super.onPostExecute(jo);
		pdu.closePd();
		try {
			if (jo != null) {

				if (jo.getBoolean("state")) {
					ComUtils.SetConfig(context, "userid", jo.getInt("userid"));
					handler.sendEmptyMessage(2);
				} else {
					ComUtils.SetConfig(context, "userid", -1);
				}
				ToastUtils.showToast(context, jo.getString("msg"));

			}else{
				ToastUtils.showToast(context, R.string.server_error);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ToastUtils.showToast(context, R.string.server_error);
		}
		handler.sendEmptyMessage(1);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		JSONObject jo = null;
		try {
			String p = HttpUtils
					.DoGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+params[2]+"&secret="+params[3]+"&code="
							+ params[1] + "&grant_type=authorization_code",
							"UTF-8");
			
			JSONObject rs = new JSONObject(p);
			String openid=rs.getString("openid");
			String access_token=rs.getString("access_token");
			
			String us=HttpUtils.DoGet("https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid, "UTF-8");
			JSONObject info = new JSONObject(us);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("a", params[0]));
			nvps.add(new BasicNameValuePair("openid", info.getString("openid")));
			nvps.add(new BasicNameValuePair("nickname", info.getString("nickname")));
			nvps.add(new BasicNameValuePair("sex", info.getString("sex")));
			nvps.add(new BasicNameValuePair("headimgurl", info.getString("headimgurl")));
			nvps.add(new BasicNameValuePair("province", info.getString("province")));
			nvps.add(new BasicNameValuePair("city", info.getString("city")));
			String result = HttpUtils.DoPost(HttpUtils.baseUrl, nvps);
			if (result != null) {
				jo = new JSONObject(result);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}

}

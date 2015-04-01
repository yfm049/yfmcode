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

public class QQLoginAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private progressDialogUtils pdu;
	private Context context;
	private Handler handler;

	public QQLoginAsyncTask(Context context, Handler handler) {
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
		// TODO Auto-generated method stub GRAPH_SIMPLE_USER_INFO
		JSONObject jo = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("a", params[0]));
			nvps.add(new BasicNameValuePair("openid", params[1]));
			nvps.add(new BasicNameValuePair("nickname", params[2]));
			nvps.add(new BasicNameValuePair("sex", params[3]));
			nvps.add(new BasicNameValuePair("headimgurl", params[4]));
			nvps.add(new BasicNameValuePair("province", params[5]));
			nvps.add(new BasicNameValuePair("city", params[6]));
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

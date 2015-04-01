package com.njbst.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;
import com.njbst.utils.progressDialogUtils;

public class JihuiAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private progressDialogUtils pdu;
	private Context context;
	private Handler handler;
	private List<String> miaohuilist;
	private List<String> jihuilist;

	public JihuiAsyncTask(Context context, Handler handler,
			List<String> jihuilist, List<String> miaohuilist) {
		this.context = context;
		pdu = new progressDialogUtils(context, this);
		this.handler = handler;
		this.jihuilist = jihuilist;
		this.miaohuilist = miaohuilist;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		jihuilist.clear();
		miaohuilist.clear();
		pdu.showPd(R.string.loading);
	}

	@Override
	protected void onPostExecute(JSONObject jo) {
		// TODO Auto-generated method stub
		super.onPostExecute(jo);
		pdu.closePd();
		try {
			if (jo != null) {

				if (jo.getBoolean("state")) {
					if (jo.has("ganjilist")) {
						JSONArray ganji = jo.getJSONArray("ganjilist");
						for (int i = 0; i < ganji.length(); i++) {
							JSONObject gj = ganji.getJSONObject(i);
							jihuilist.add(gj.getString("name"));
						}
					}
					if (jo.has("miaohuilist")) {
						JSONArray miaohui = jo.getJSONArray("miaohuilist");
						for (int i = 0; i < miaohui.length(); i++) {
							JSONObject mh = miaohui.getJSONObject(i);
							jihuilist.add(mh.getString("name"));
						}
					}
				} else {
					ToastUtils.showToast(context, jo.getString("msg"));
				}
				return;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			handler.sendEmptyMessage(1);
		}
		ToastUtils.showToast(context, R.string.server_error);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		JSONObject jo = null;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userid", String.valueOf(ComUtils
				.GetConfig(context, "userid", -1))));
		nvps.add(new BasicNameValuePair("a", "jihui"));
		nvps.add(new BasicNameValuePair("time", params[0]));
		String result = HttpUtils.DoPost(HttpUtils.baseUrl, nvps);
		if (result != null) {
			try {
				jo = new JSONObject(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jo;
	}

}

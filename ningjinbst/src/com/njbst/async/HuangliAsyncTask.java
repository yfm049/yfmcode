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

import com.njbst.pojo.HuangliInfo;
import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;
import com.njbst.utils.progressDialogUtils;

public class HuangliAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private progressDialogUtils pdu;
	private Context context;
	private Handler handler;
	private List<HuangliInfo> lhl;

	public HuangliAsyncTask(Context context, Handler handler,
			List<HuangliInfo> lhl) {
		this.context = context;
		pdu = new progressDialogUtils(context, this);
		this.handler = handler;
		this.lhl = lhl;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		lhl.clear();
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
					if (jo.has("listdata")) {
						JSONArray array = jo.getJSONArray("listdata");
						for (int i = 0; i < array.length(); i++) {
							HuangliInfo linfo = new HuangliInfo();
							JSONObject item = array.getJSONObject(i);
							linfo.setTime(item.getString("time"));
							StringBuffer sb = new StringBuffer();
							if (item.has("yinli")) {
								sb.append(item.getString("yinli") + "\r\n");
							}
							if (item.has("ganzhi")) {
								sb.append(item.getString("ganzhi") + "\r\n");
							}
							if (item.has("yi")) {
								sb.append("рк" + item.getString("yi") + "\r\n");
							}
							if (item.has("ji")) {
								sb.append("╪и" + item.getString("ji"));
							}
							linfo.setCon(sb.toString());
							lhl.add(linfo);
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
		nvps.add(new BasicNameValuePair("a", "huangli"));
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

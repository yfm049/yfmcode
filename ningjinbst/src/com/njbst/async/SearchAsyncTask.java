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

import com.njbst.pojo.Search;
import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;
import com.njbst.utils.progressDialogUtils;

public class SearchAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private Context context;
	private Handler handler;
	private List<Search> lmi;
	private progressDialogUtils pdu;

	public SearchAsyncTask(Context context, Handler handler, List<Search> lmi) {
		this.context = context;
		this.handler = handler;
		this.lmi = lmi;
		pdu = new progressDialogUtils(context, this);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		lmi.clear();
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
							Search linfo = new Search();
							JSONObject item = array.getJSONObject(i);
							linfo.setName(item.getString("name"));
							linfo.setTel(item.getString("tel"));
							linfo.setAddress(item.getString("address"));
							linfo.setLinkurl(item.getString("linkurl"));
							lmi.add(linfo);
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
		nvps.add(new BasicNameValuePair("a", "search"));
		nvps.add(new BasicNameValuePair("kw", params[0]));
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

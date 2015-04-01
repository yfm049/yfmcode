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

import com.njbst.pojo.Accumulate;
import com.njbst.pojo.Page;
import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;

public class AccumulateAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private Context context;
	private Handler handler;
	private List<Accumulate> lmi;
	private Page page;
	private boolean refresh = false;

	public AccumulateAsyncTask(Context context, Handler handler,
			List<Accumulate> lmi, Page page, boolean refresh) {
		this.context = context;
		this.handler = handler;
		this.lmi = lmi;
		this.page = page;
		this.refresh = refresh;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(JSONObject jo) {
		// TODO Auto-generated method stub
		super.onPostExecute(jo);
		if (refresh) {
			lmi.clear();
		}
		try {
			if (jo != null) {

				if (jo.getBoolean("state")) {
					if (jo.has("listdata")) {
						JSONArray array = jo.getJSONArray("listdata");
						for (int i = 0; i < array.length(); i++) {
							Accumulate linfo = new Accumulate();
							JSONObject item = array.getJSONObject(i);
							linfo.setName(item.getString("name"));
							linfo.setZancount(item.getInt("zancount"));
							linfo.setImgurl(item.getString("url"));
							linfo.setLinkurl(item.getString("linkurl"));
							lmi.add(linfo);
						}
					}
					if (jo.has("totalpage")) {
						page.setTotalpage(jo.getInt("totalpage"));
					}
					if (jo.has("page")) {
						page.setCurrpage(jo.getInt("page"));
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
		nvps.add(new BasicNameValuePair("a", "jzyl"));
		nvps.add(new BasicNameValuePair("page", params[0]));
		nvps.add(new BasicNameValuePair("size", params[1]));
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

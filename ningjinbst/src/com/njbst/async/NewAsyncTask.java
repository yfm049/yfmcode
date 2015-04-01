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

import com.njbst.pojo.NewInfo;
import com.njbst.pojo.Page;
import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;

public class NewAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private Context context;
	private Handler handler;
	private List<NewInfo> lmi;
	private Page page;

	public NewAsyncTask(Context context, Handler handler,
			List<NewInfo> lmi, Page page) {
		this.context = context;
		this.handler = handler;
		this.lmi = lmi;
		this.page = page;
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
		try {
			if (jo != null) {

				if (jo.getBoolean("state")) {
					if (jo.has("toplist")) {
						JSONArray array = jo.getJSONArray("toplist");
						if(array.length()>0){
							NewInfo top = new NewInfo();
							for (int i = 0; i < array.length(); i++) {
								NewInfo linfo = new NewInfo();
								JSONObject item = array.getJSONObject(i);
								linfo.setTitle(item.getString("title"));
								linfo.setContent(item.getString("content"));
								linfo.setImageurl(item.getString("url"));
								linfo.setLinkurl(item.getString("linkurl"));
								top.getNewinfos().add(linfo);
							}
							lmi.add(top);
						}
						
					}
					if (jo.has("listdata")) {
						JSONArray array = jo.getJSONArray("listdata");
						for (int i = 0; i < array.length(); i++) {
							NewInfo linfo = new NewInfo();
							JSONObject item = array.getJSONObject(i);
							linfo.setTitle(item.getString("title"));
							linfo.setContent(item.getString("content"));
							linfo.setImageurl(item.getString("url"));
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
		nvps.add(new BasicNameValuePair("a", "news"));
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

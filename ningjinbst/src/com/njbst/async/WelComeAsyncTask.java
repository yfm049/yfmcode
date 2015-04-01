package com.njbst.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.njbst.utils.AsyncImageLoader;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;

public class WelComeAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private AsyncImageLoader loader;
	private Context context;

	public WelComeAsyncTask(Context context) {
		this.context = context;
		loader=new AsyncImageLoader(context);
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
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		JSONObject jo = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("a", "hello"));
			nvps.add(new BasicNameValuePair("userid", String.valueOf(ComUtils
					.GetConfig(context, "userid", -1))));
			String result = HttpUtils.DoPost(HttpUtils.baseUrl, nvps);
			if (result != null) {
				jo = new JSONObject(result);
				if (jo.getBoolean("state")) {
					JSONArray listdata = jo.getJSONArray("listdata");
					String wl=ComUtils.GetConfig(context, "wileimg", "");
					if(!wl.equals(listdata.toString())){
						int count = 0;
						for (int i = 0; i < listdata.length(); i++) {
							JSONObject item = listdata.getJSONObject(i);
							Bitmap rs = loader.downLoad(item.getString("url"));
							if (rs != null) {
								count++;
							}
						}
						if (count == listdata.length()) {
							ComUtils.SetConfig(context, "wileimg",listdata.toString());
						} else {
							ComUtils.SetConfig(context, "wileimg", "");
						}
					}
					
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}

}

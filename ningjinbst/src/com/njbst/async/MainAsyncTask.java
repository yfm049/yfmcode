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
import android.widget.TextView;

import com.njbst.pojo.Advert;
import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;

public class MainAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private Context context;
	private Handler handler;
	private List<Advert> ads;
	private TextView timetext;
	private TextView weathertext;

	public MainAsyncTask(Context context, Handler handler, List<Advert> ads,
			TextView timetext,TextView weathertext) {
		this.context = context;
		this.handler = handler;
		this.ads = ads;
		this.timetext = timetext;
		this.weathertext=weathertext;
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

				ads.clear();
				if (jo.getBoolean("state")) {
					JSONArray advert = jo.getJSONArray("advert");
					for (int i = 0; i < advert.length(); i++) {
						Advert at = new Advert();
						JSONObject ad = advert.getJSONObject(i);
						at.setAdname(ad.getString("adname"));
						at.setUrl(ad.getString("url"));
						at.setLinkurl(ad.getString("linkurl"));
						ads.add(at);
					}
					timetext.setText(jo.getString("currtime"));
					weathertext.setText(jo.getString("currweather"));
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
		nvps.add(new BasicNameValuePair("a", "index"));
		nvps.add(new BasicNameValuePair("userid", String.valueOf(ComUtils
				.GetConfig(context, "userid", -1))));
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

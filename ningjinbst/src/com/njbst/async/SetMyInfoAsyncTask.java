package com.njbst.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.njbst.pojo.MyInfo;
import com.njbst.pro.R;
import com.njbst.utils.ComUtils;
import com.njbst.utils.HttpUtils;
import com.njbst.utils.ToastUtils;
import com.njbst.utils.progressDialogUtils;

public class SetMyInfoAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private Context context;
	private Handler handler;
	private MyInfo info;
	private progressDialogUtils pdu;
	private boolean show=false;

	public SetMyInfoAsyncTask(Context context, Handler handler, MyInfo info,boolean show) {
		this.context = context;
		this.handler = handler;
		this.info = info;
		this.show=show;
		pdu = new progressDialogUtils(context, this);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(show){
			pdu.showPd(R.string.loading);
		}
		
	}

	@Override
	protected void onPostExecute(JSONObject jo) {
		// TODO Auto-generated method stub
		super.onPostExecute(jo);
		pdu.closePd();
		try {
			if (jo != null) {

				if (jo.getBoolean("state")) {
					handler.sendEmptyMessage(2);
				} 
				ToastUtils.showToast(context, jo.getString("msg"));
				return;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		nvps.add(new BasicNameValuePair("a", "setMemberInfo"));
		nvps.add(new BasicNameValuePair("name", info.getName()));
		nvps.add(new BasicNameValuePair("sex", info.getSex()));
		nvps.add(new BasicNameValuePair("age", info.getAge()));
		nvps.add(new BasicNameValuePair("address", info.getAddress()));
		nvps.add(new BasicNameValuePair("tel", info.getPhone()));
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

package com.njbst.async;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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

public class UploadAsyncTask extends AsyncTask<String, Integer, JSONObject> {

	private Context context;
	private Handler handler;
	private File file;
	private progressDialogUtils pdu;

	public UploadAsyncTask(Context context, Handler handler, File file) {
		this.context = context;
		this.handler = handler;
		pdu = new progressDialogUtils(context, this);
		this.file = file;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
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
					handler.sendMessage(handler.obtainMessage(2, jo.getString("url")));
				}
				ToastUtils.showToast(context, jo.getString("msg"));
				return;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.sendEmptyMessage(1);
		ToastUtils.showToast(context, R.string.server_error);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		JSONObject jo = null;
		try {
			MultipartEntity mpEntity = new MultipartEntity();

			mpEntity.addPart(
					"userid",
					new StringBody(String.valueOf(ComUtils.GetConfig(context,
							"userid", -1))));
			mpEntity.addPart(
					"a",
					new StringBody("upload_img"));
			mpEntity.addPart("img", new FileBody(file));
			String result = HttpUtils.DoPost(HttpUtils.baseUrl, mpEntity);
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

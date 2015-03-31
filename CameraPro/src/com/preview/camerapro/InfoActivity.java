package com.preview.camerapro;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.preview.utils.ConfigUtils;
import com.preview.utils.HttpDao;
import com.preview.utils.SJinfo;

public class InfoActivity extends Activity {

	private ImageView img;
	private TextView info;
	private ProgressDialog pd;
	private String code;
	private String mode;
	private String firstmode;
	private String secmode;
	private String type;
	private String jqmcode;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				if (msg.what == 1) {
						if (msg.obj != null) {
							JSONObject jo = new JSONObject(msg.obj.toString());
							Toast.makeText(InfoActivity.this, jo.getString("msg"),
									Toast.LENGTH_LONG).show();
							InfoActivity.this.finish();
						} else {
							Toast.makeText(InfoActivity.this, "上传失败",
									Toast.LENGTH_LONG).show();
						}
						MainActivity.data = null;
				}else if(msg.what==3){
					if (msg.obj != null) {
						JSONObject jo = new JSONObject(msg.obj.toString());
						String m=jo.getString("msg");
						if("true".equals(m)){
							pd = new ProgressDialog(InfoActivity.this);
							pd.setMessage("正在上传请稍后...");
							pd.show();
							new uploadThread().start();
						}else{
							Toast.makeText(InfoActivity.this, jo.getString("msg"),
									Toast.LENGTH_LONG).show();
							InfoActivity.this.finish();
						}
					} else {
						Toast.makeText(InfoActivity.this, "获取信息失败",
								Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_info);
		jqmcode=ConfigUtils.GetString(this, "code", "");
		info = (TextView) super.findViewById(R.id.info);
		if (MainActivity.json != null) {
			try {
				JSONObject jo = new JSONObject(MainActivity.json);
				StringBuffer sb = new StringBuffer();
				if (jo.has("code")) {
					code = jo.getString("code");
					sb.append("卡号:" + jo.getString("code") + "\r\n");
					sb.append("姓名:" + jo.getString("name") + "\r\n");
					sb.append("职位:" + jo.getString("job") + "\r\n");
					sb.append("部门:" + jo.getString("department") + "\r\n");
				}
				info.setText(sb.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i("mode", MainActivity.data+"");
		img = (ImageView) super.findViewById(R.id.img);
		if (MainActivity.data != null) {
			BitmapFactory.Options opts =  new  BitmapFactory.Options();
			opts.inSampleSize =  4 ;
			img.setImageBitmap(BitmapFactory.decodeByteArray(MainActivity.data,0, MainActivity.data.length,opts));
		}
		firstmode = ConfigUtils.GetString(this, "first", "");
		secmode = ConfigUtils.GetString(this, "sec", "");
		Log.i("mode", firstmode+"--"+secmode);
		if ("0".equals(firstmode)) {
			// 非食堂模式
			FeiShitang();
		} else if ("1".equals(firstmode)&&"0".equals(secmode)) {
			// 非打饭模式
			ToListActivity();
		}  else if ("1".equals(firstmode)&&"1".equals(secmode)) {
			// 打饭模式
			FeiDaFan();
		} else {
			// 模式未设置
		}

	}
	public void ToListActivity(){
		Intent intent = new Intent(this, ListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		
	}
	public void FeiShitang() {
		mode="feishitang";
		SJinfo zqj = ConfigUtils.isQuJian(this, "kmzstart", "kmzend", "早");
		SJinfo zwqj = ConfigUtils.isQuJian(this, "kmwstart", "kmwend", "中");
		SJinfo wqj = ConfigUtils.isQuJian(this, "kmstart", "kmend", "晚");
		if (zqj.isIsflag()) {
			type = "zao";
			pd = new ProgressDialog(this);
			pd.setMessage("正在上传请稍后...");
			pd.show();
			new uploadThread().start();
		} else if (zwqj.isIsflag()) {
			type = "zhong";
			pd = new ProgressDialog(this);
			pd.setMessage("正在上传请稍后...");
			pd.show();
			new uploadThread().start();
		} else if (wqj.isIsflag()) {
			type = "wan";
			pd = new ProgressDialog(this);
			pd.setMessage("正在上传请稍后...");
			pd.show();
			new uploadThread().start();
		} else {
			Toast.makeText(this, "非开门时间", Toast.LENGTH_LONG).show();
			InfoActivity.this.finish();
		}
	}

	public void FeiDaFan() {
		mode="feidafan";
		SJinfo zqj = ConfigUtils.isQuJian(this, "zstart", "zend", "早");
		SJinfo zwqj = ConfigUtils.isQuJian(this, "wstart", "wend", "中");
		SJinfo wqj = ConfigUtils.isQuJian(this, "start", "end", "晚");
		if (zqj.isIsflag()) {
			type = "zao";
			pd = new ProgressDialog(this);
			pd.setMessage("正在验证请稍候...");
			pd.show();
			new RecordThread().start();
		} else if (zwqj.isIsflag()) {
			type = "zhong";
			pd = new ProgressDialog(this);
			pd.setMessage("正在验证请稍候...");
			pd.show();
			new RecordThread().start();
		} else if (wqj.isIsflag()) {
			type = "wan";
			pd = new ProgressDialog(this);
			pd.setMessage("正在验证请稍候...");
			pd.show();
			new RecordThread().start();
		} else {
			Toast.makeText(this, "非打饭时间", Toast.LENGTH_LONG).show();
			InfoActivity.this.finish();
		}
	}

	class uploadThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String p = HttpDao.uploadImage(MainActivity.data, code,mode, type,jqmcode,"user!upload.action", pd);
			Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.obj = p;
			handler.sendMessage(msg);
		}

	}

	class RecordThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			List<NameValuePair> lnp=new ArrayList<NameValuePair>();
			lnp.add(new BasicNameValuePair("type", type));
			lnp.add(new BasicNameValuePair("code", code));
			lnp.add(new BasicNameValuePair("mode", mode));
			String p = HttpDao.PostData("user!hasRecord.action",lnp, pd);
			Message msg = handler.obtainMessage();
			msg.what = 3;
			msg.obj = p;
			handler.sendMessage(msg);
		}

	}

}

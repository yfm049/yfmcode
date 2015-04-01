package com.android.smsserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

import com.android.thread.RequestThread;
import com.android.utils.Utils;
import com.google.gson.Gson;

public class PhotoSetActivity extends Activity {

	private ListenerImpl listener;
	private RadioGroup camera;
	private String clienturl="control/server!sendToClient.action";
	private Button butnet,btnsms;
	private Map<String, Object> map;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_set);
		listener=new ListenerImpl();
		camera=(RadioGroup)this.findViewById(R.id.camera);
		butnet=(Button)this.findViewById(R.id.butnet);
		btnsms=(Button)this.findViewById(R.id.btnsms);
		butnet.setOnClickListener(listener);
		btnsms.setOnClickListener(listener);
		
	}
	class ListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				map=new HashMap<String, Object>();
				if(camera.getCheckedRadioButtonId()==R.id.qian){
					map.put("camera", 1);
				}else{
					map.put("camera", 0);
				}
				map.put("action", "photo");
				
				Gson gson=new Gson();
				if(v.getId()==R.id.butnet){
					pd=Utils.createProgressDialog(PhotoSetActivity.this);
					pd.setMessage("正在发送中...");
					pd.show();
					Sendaction(Utils.StringEncode(gson.toJson(map)));
				}else if(v.getId()==R.id.butsms){
					SendSmsAction(Utils.StringEncode(gson.toJson(map)));
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(PhotoSetActivity.this, "必须输入大于0的正整数");
			}
		}
	}
	private void SendSmsAction(String action){
		SmsManager sm=SmsManager.getDefault();
		sm.sendTextMessage(Utils.client.getPhone(), null, action, null, null);
		Utils.ShowToast(PhotoSetActivity.this, "指令已发送");
	}
	
	private void Sendaction(String action){
		List<NameValuePair> lnp=new ArrayList<NameValuePair>();
		lnp.add(new BasicNameValuePair("mapparam.clientid", String.valueOf(Utils.client.getClientid())));
		lnp.add(new BasicNameValuePair("mapparam.action", action));
		RequestThread rt=new RequestThread(clienturl,lnp,handler);
		rt.start();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				pd.cancel();
				if(msg.what==1&&msg.obj!=null){
					JSONObject jo=(JSONObject)msg.obj;
					if(jo.getInt("state")==1){
						Utils.ShowToast(PhotoSetActivity.this, "发送成功");
					}else{
						Utils.ShowToast(PhotoSetActivity.this, "发送失败,请使用短信方式发送");
					}
					
				}else{
					Utils.ShowToast(PhotoSetActivity.this, "请求服务器错误");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(PhotoSetActivity.this, "数据错误");
			}
		}
		
	};

	


}

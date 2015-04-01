package com.android.smsserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.model.Client;
import com.android.thread.RequestThread;
import com.android.utils.Utils;

public class BaseInfoActivity extends Activity {

	private ListenerImpl listener;
	private EditText clientname,clientphone;
	private String clienturl="control/server!UpdateDeviceBase.action";
	private Button butnet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		listener=new ListenerImpl();
		clientname=(EditText)this.findViewById(R.id.clientname);
		clientphone=(EditText)this.findViewById(R.id.count);
		butnet=(Button)this.findViewById(R.id.butnet);
		butnet.setOnClickListener(listener);
		clientname.setText(Utils.client.getClientname());
		clientphone.setText(Utils.client.getPhone());
		
	}
	class ListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name=clientname.getText().toString();
			String phone=clientphone.getText().toString();
			Sendaction(name,phone);
		}
	}
	
	private void Sendaction(String name,String phone){
		List<NameValuePair> lnp=new ArrayList<NameValuePair>();
		lnp.add(new BasicNameValuePair("mapparam.id", String.valueOf(Utils.client.getId())));
		lnp.add(new BasicNameValuePair("mapparam.name", name));
		lnp.add(new BasicNameValuePair("mapparam.phone", phone));
		RequestThread rt=new RequestThread(clienturl,lnp,handler);
		rt.start();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				if(msg.what==1&&msg.obj!=null){
					JSONObject jo=(JSONObject)msg.obj;
					if(jo.getInt("state")==1){
						Utils.client.setClientname(clientname.getText().toString());
						Utils.client.setPhone(clientphone.getText().toString());
						BaseInfoActivity.this.finish();
					}
					Utils.ShowToast(BaseInfoActivity.this, jo.getString("msg"));
				}else{
					Utils.ShowToast(BaseInfoActivity.this, "请求服务器错误");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(BaseInfoActivity.this, "数据错误");
			}
		}
		
	};

	


}

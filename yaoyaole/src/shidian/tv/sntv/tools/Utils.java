package shidian.tv.sntv.tools;

import java.security.MessageDigest;

import com.pro.yyl.R;

import android.content.Context;
import android.media.MediaPlayer;

public class Utils {

	public static String SKEY;

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void playsound(Context context,int id){
		MediaPlayer mp=MediaPlayer.create(context, id);
		mp.start();
	}
	
	
	public static void initSkey(){
	    SKEY = MD5(String.valueOf(System.currentTimeMillis())).substring(0, 24);
	}
}

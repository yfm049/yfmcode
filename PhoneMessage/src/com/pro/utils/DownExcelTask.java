package com.pro.utils;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.pro.model.Tel_query;
import com.pro.model.Tel_text;

public class DownExcelTask extends AsyncTask<String, Integer, String> {

	

	

	private Context context;
	private boolean[] pb=new boolean[]{false,false};

	public DownExcelTask(Context context) {
		this.context = context;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		ComUtils.showToast(context, "开始更新");
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		String tx="";
		if(pb[0]&&pb[1]){
			tx+="更新成功";
		}else{
			tx+="更新失败";
		}
		
		ComUtils.showToast(context, tx);
	}

	@Override
	protected String doInBackground(String... param) {
		// TODO Auto-generated method stub
		Log.i("down", "开始更新");
		send(5,"正在更新");
		File tel_download_text = DownLoadUtils.down(ComUtils.getStringConfig(context, "ip", "http://192.168.0.10:8080/text/")+param[0],
				"tel_download_text.txt");
		
		if (tel_download_text != null) {
			send(20,param[0]+" 下载成功");
			List<Tel_text> lst = DownLoadUtils.parse_download_text_Excel(tel_download_text);
			send(30,"");
			if (lst != null) {
				SqlUtils su = new SqlUtils(context);
				su.saveTel_texts(lst);
				send(50,param[0]+" 更新成功");
				pb[0]=true;
			}else{
				send(50,param[0]+" 解析文件失败");
			}
		}else{
			send(50,param[0]+" 下载失败");
		}
		send(55,param[1]+" 开始下载");
		File tel_query_xx = DownLoadUtils.down(ComUtils.getStringConfig(context, "ip", "http://192.168.0.10:8080/text/")+param[1], "tel_query_xx.txt");
		
		if (tel_query_xx != null) {
			send(70,param[1]+" 下载成功");
			Log.i("down", param[1]+" 下载成功");
			List<Tel_query> lsq = DownLoadUtils.parse_query_xx_Excel(tel_query_xx);
			send(80,"");
			if (lsq != null) {
				SqlUtils su = new SqlUtils(context);
				su.saveTel_querys(lsq);
				send(100,param[1]+" 更新成功");
				pb[1]=true;
			}else{
				send(100,param[1]+" 解析文件失败");
			}
		}else{
			send(100,param[1]+" 下载失败");
		}
		Log.i("down", "更新结束");
		return null;
	}
	public void send(int i,String msg){
		Intent in=new Intent("com.pro.phonemessage.progress");
		in.putExtra("progress", i);
		in.putExtra("msg", msg);
		context.sendBroadcast(in);
	}

}

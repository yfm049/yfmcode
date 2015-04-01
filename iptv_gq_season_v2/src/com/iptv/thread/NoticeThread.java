package com.iptv.thread;

import java.util.List;

import android.R.integer;
import android.os.AsyncTask;
import android.widget.TextView;

import com.iptv.pojo.Notice;
import com.iptv.pojo.TvInfo;
import com.iptv.utils.HttpUtils;

public class NoticeThread extends AsyncTask<TvInfo,integer,List<Notice>> {

	


	private TextView currprogram;
	private TextView nextprogram;
	public NoticeThread(TextView currprogram,TextView nextprogram){
		this.currprogram=currprogram;
		this.nextprogram=nextprogram;
	}

	@Override
	protected List<Notice> doInBackground(TvInfo... params) {
		// TODO Auto-generated method stub'
		TvInfo tvinfo=params[0];
		HttpUtils hu=new HttpUtils();
		String xml=hu.doget(tvinfo.getEpg());
		List<Notice> ln=hu.ParseNotice(xml);
		return ln;
	}
	
	@Override
	protected void onPostExecute(List<Notice> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null){
			System.out.println(result.size());
			if(result.size()>1){
				currprogram.setText(result.get(0).getName());
				nextprogram.setText("NEXT:"+result.get(1).getName());
			}
		}
	}
	

	

	
}

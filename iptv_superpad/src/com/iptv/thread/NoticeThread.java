package com.iptv.thread;

import java.util.List;

import android.R.integer;
import android.os.AsyncTask;

import com.iptv.adapter.NoticeAdapter;
import com.iptv.pojo.Notice;
import com.iptv.pojo.TvInfo;
import com.iptv.utils.HttpUtils;

public class NoticeThread extends AsyncTask<TvInfo,integer,List<Notice>> {

	

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		ntlist.clear();
		nadapter.notifyDataSetChanged();
	}

	private NoticeAdapter nadapter;
	private List<Notice> ntlist;
	public NoticeThread(NoticeAdapter nadapter,List<Notice> ntlist){
		this.nadapter=nadapter;
		this.ntlist=ntlist;
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
			ntlist.addAll(result);
		}
		nadapter.notifyDataSetChanged();
	}
	

	

	
}

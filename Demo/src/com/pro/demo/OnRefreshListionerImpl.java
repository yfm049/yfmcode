package com.pro.demo;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.pro.demo.PullDownListView.OnRefreshListioner;


public class OnRefreshListionerImpl implements OnRefreshListioner {

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==0){
				List<Data> ls=(List<Data>)msg.obj;
				da.getLs().addAll(ls);
				da.notifyDataSetChanged();
			}
			if(msg.what==1){
				List<Data> ls=(List<Data>)msg.obj;
				da.getLs().addAll(0,ls);
				da.notifyDataSetChanged();
				pdv.onRefreshComplete();
				pdv.setMore(true);
			}
			if(msg.what==2){
				List<Data> ls=(List<Data>)msg.obj;
				da.getLs().addAll(ls);
				da.notifyDataSetChanged();
				pdv.onLoadMoreComplete();
				pdv.setMore(true);
			}
		}
		
	};
	private PullDownListView pdv;
	private DataAdapter da;
	private HttpUtils hu=new HttpUtils();
	public OnRefreshListionerImpl(PullDownListView pdv,DataAdapter da){
		this.pdv=pdv;
		this.da=da;
		initDate();
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		System.out.println("------");
		Thread th=new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int id=0;
				if(da.getLs().size()>0){
					id=da.getLs().get(0).getId();
				}
				
				List<Data> ls=hu.Refresh(id);
				Message msg=mHandler.obtainMessage();
				msg.what=1;
				msg.obj=ls;
				mHandler.sendMessage(msg);
			}
			
		};
		th.start();
		
	}

	public void initDate(){
		Thread th=new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Data> ls=hu.initdate();
				Message msg=mHandler.obtainMessage();
				msg.what=0;
				msg.obj=ls;
				mHandler.sendMessage(msg);
			}
			
		};
		th.start();
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		Thread th=new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int id=0;
				if(da.getLs().size()>0){
					id=da.getLs().get(da.getLs().size()-1).getId();
				}
				List<Data> ls=hu.LoadMore(id);
				Message msg=mHandler.obtainMessage();
				msg.what=2;
				msg.obj=ls;
				mHandler.sendMessage(msg);
			}
			
		};
		th.start();
	}

}

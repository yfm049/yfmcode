package com.yfm.adapter;

import java.util.List;

import com.yfm.pojo.TcpConnectClient;
import com.yfm.pojo.TcpConnectServer;
import com.yfm.pro.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TcpserverAdapter extends BaseAdapter {

	private List<TcpConnectServer> lc;
	private int cid=0;
	public void setCid(int cid) {
		this.cid = cid;
	}

	private Context context;
	public TcpserverAdapter(Context context,List<TcpConnectServer> lc){
		this.context=context;
		this.lc=lc;
	}
	public TcpConnectServer getserver(){
		if(lc!=null&&lc.size()>0){
			return lc.get(cid);
		}
		return null;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(cid>lc.size()){
			cid=0;
		}
		return lc.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lc.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		TcpConnectServer  con=lc.get(arg0);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_tcpitem, null);
		}
		ImageView ming=(ImageView)view.findViewById(R.id.mimg);
		TextView mip=(TextView)view.findViewById(R.id.mip);
		TextView mport=(TextView)view.findViewById(R.id.mport);
		mip.setText(con.getIp());
		if(con.isIscon()){
			mport.setText("port:"+con.getPort());
		}else{
			mport.setText("disconnect");
		}
		if(arg0==cid){
			ming.setSelected(true);
		}else{
			ming.setSelected(false);
		}
		return view;
	}

}

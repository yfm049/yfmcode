package com.njbst.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njbst.pojo.MoreInfo;
import com.njbst.pro.R;

public class MoreAdapter extends BaseAdapter {

	private int[] img=new int[]{R.drawable.zh_type,R.drawable.jy_type,R.drawable.jz_type,R.drawable.mf_type,R.drawable.zr_type};
	
	private Context context;
	private List<MoreInfo> lmi;
	private int index=0;
	private String[] qzzp,fcxx,esmm,zhjy;
	public MoreAdapter(Context context,List<MoreInfo> lmi,int index){
		this.context=context;
		this.lmi=lmi;
		this.index=index;
		qzzp=context.getResources().getStringArray(R.array.qzzp);
		fcxx=context.getResources().getStringArray(R.array.fcxx);
		esmm=context.getResources().getStringArray(R.array.esmm);
		zhjy=context.getResources().getStringArray(R.array.zhjy);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lmi.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lmi.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.fragment_more_item, null);
			holder.type=(TextView)convertView.findViewById(R.id.item_type);
			holder.title=(TextView)convertView.findViewById(R.id.item_title);
			holder.time=(TextView)convertView.findViewById(R.id.item_time);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		MoreInfo mi=lmi.get(position);
		holder.type.setText(getTypeBg(mi.getType()));
		holder.title.setText(mi.getTitle());
		holder.time.setText(mi.getTime());
		holder.type.setBackgroundResource(img[mi.getType()]);
		return convertView;
	}
	class ViewHolder {  
		TextView type; 
		TextView title;  
		TextView time;  
    }  
	


	public List<MoreInfo> getListData() {
		// TODO Auto-generated method stub
		return lmi;
	}
	
	private String getTypeBg(int type){
		
		String tcon="";
		if(index==0){
			tcon=qzzp[type];
		}else if(index==1){
			tcon=fcxx[type];
		}else if(index==2){
			tcon=esmm[type];
		}else if(index==3){
			tcon=zhjy[type];
		}
		return tcon;
	}
}

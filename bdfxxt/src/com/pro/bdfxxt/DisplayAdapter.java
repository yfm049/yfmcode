package com.pro.bdfxxt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayAdapter{

	private int vcount=16;
	private int currpage=0;
	private int totalcount;
	private int start;
	private List<String> ls=new ArrayList<String>();
	private Context context;
	private LinearLayout leftdata,rightdata;
	public DisplayAdapter(Context context,LinearLayout leftdata,LinearLayout rightdata,List<String> ls){
		this.ls=ls;
		this.context=context;
		this.leftdata=leftdata;
		this.rightdata=rightdata;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		totalcount=(ls.size()+vcount-1)/vcount;
		start=vcount*currpage;
		if(ls.size()-start<vcount){
			return ls.size()-start;
		}else{
			return vcount;
		}
		
	}

	public void nextpage(){
		if(currpage+1<totalcount){
			currpage=currpage+1;
		}
	}
	public void prepage(){
		if(currpage-1>=0){
			currpage=currpage-1;
		}
	}
	public void notifyDataSetChanged(){
		leftdata.removeAllViews();
		rightdata.removeAllViews();
		for(int i=0;i<getCount();i++){
			View view=LayoutInflater.from(context).inflate(R.layout.diaplay_item, null);
			TextView key=(TextView)view.findViewById(R.id.key);
			TextView value=(TextView)view.findViewById(R.id.value);
			key.setText("NO"+String.format("%02d", start+i+1)+" : ");
			value.setText(ls.get(start+i));
			if(i<8){
				leftdata.addView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}else{
				rightdata.addView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
		
	}


}

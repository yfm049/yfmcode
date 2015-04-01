package com.njbst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njbst.pro.R;

public class MyAdapter extends BaseAdapter {

	private int[] img=new int[]{R.drawable.myicon_userinfo,R.drawable.myicon_integral,R.drawable.myicon_secure};
	private int[] con=new int[]{R.string.my_userinfo_text,R.string.my_integral_text,R.string.my_secure_text};
	
	private Context context;
	public MyAdapter(Context context){
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return con.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return con[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return con[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.fragment_my_item, null);
			holder.img=(ImageView)convertView.findViewById(R.id.my_img);
			holder.con=(TextView)convertView.findViewById(R.id.my_con);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.img.setImageResource(img[position]);
		holder.con.setText(con[position]);
		return convertView;
	}
	class ViewHolder {  
        public ImageView img;  
        public TextView con;  
    }  

}

package com.xhplovewx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xhplovewx.pojo.ItemInfo;
import com.xhplovewx.tixing.R;

public class ListAdapter extends BaseAdapter {

	private List<ItemInfo> litem;
	private Context context;
	private int poscheck=-1;
	public int getPoscheck() {
		return poscheck;
	}

	public void setPoscheck(int poscheck) {
		this.poscheck = poscheck;
	}

	public ListAdapter(Context context,List<ItemInfo> litem){
		this.context=context;
		this.litem=litem;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return litem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return litem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_list_item, null);
			holder=new ViewHolder();
			holder.title=(TextView)convertView.findViewById(R.id.title);
			holder.radiobut=(RadioButton)convertView.findViewById(R.id.radiobut);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.radiobut.setChecked(false);
		if(position==poscheck){
			holder.radiobut.setChecked(true);
		}
		holder.radiobut.setOnClickListener(new ListenerImpl(position));
		ItemInfo info=litem.get(position);
		holder.title.setText(info.getTitle());
		convertView.setBackgroundResource(info.getLevel());
		if(info.isexceed()){
			convertView.setBackgroundResource(R.color.huise);
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
		RadioButton radiobut;
	}
	
	class ListenerImpl implements OnClickListener{

		private int pos;
		public ListenerImpl(int pos){
			this.pos=pos;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setPoscheck(pos);
			ListAdapter.this.notifyDataSetChanged();
		}

		
	}

}

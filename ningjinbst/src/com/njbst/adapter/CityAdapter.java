package com.njbst.adapter;

import java.util.List;

import com.njbst.pojo.CityInfo;
import com.njbst.pro.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter {

	private List<CityInfo> lcity;
	private Context context;

	public CityAdapter(Context context, List<CityInfo> lcity) {
		this.lcity = lcity;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lcity.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lcity.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.activity_city_item, null);
			holder.catalog = (TextView) convertView.findViewById(R.id.catalog);
			holder.cityname = (TextView) convertView
					.findViewById(R.id.cityname);
			holder.cate_layout=(LinearLayout)convertView.findViewById(R.id.cate_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CityInfo info=lcity.get(position);
		
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.cate_layout.setVisibility(View.VISIBLE);
			holder.catalog.setText(info.getNameLetters());
		} else {
			holder.cate_layout.setVisibility(View.GONE);
		}
		holder.cityname.setText(info.getName());
		return convertView;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return lcity.get(position).getNameLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = lcity.get(i).getNameLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	class ViewHolder {
		TextView catalog;
		TextView cityname;
		LinearLayout cate_layout;
	}
}

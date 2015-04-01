package com.pro.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pro.orderfoot.MealActivity;
import com.pro.orderfoot.R;
import com.pro.utils.SqlUtils;

public class MealListAdapter extends BaseAdapter {

	
	

	private List<Map<String, Object>> lmi;
	
	private Context context;
	private TextView zongjia;
	private String[] keys;
	public MealListAdapter(Context context,List<Map<String, Object>> lmi,TextView zongjia){
		this.lmi=lmi;
		this.context=context;
		this.zongjia=zongjia;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return lmi.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return keys[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Map<String, Object> mi=lmi.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_meal_listitem, null);
		}
		TextView mealname=(TextView)view.findViewById(R.id.tprice);
		TextView danjia=(TextView)view.findViewById(R.id.yhprice);
		TextView shuliang=(TextView)view.findViewById(R.id.shuliang);
		mealname.setText(mi.get("ProductName").toString());
		danjia.setText("￥"+mi.get("Price").toString());
		shuliang.setText("x"+mi.get("shuliang").toString());
		return view;
	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		String sql="select sum(Price*shuliang) total from mealorder where OrderId=?";
		SqlUtils su=new SqlUtils(context);
		List<Map<String, Object>> total=su.Search(sql, new String[]{MealActivity.ordernum});
		if(total.size()>0){
			Map<String, Object> mo=total.get(0);
			zongjia.setText("总价￥"+mo.get("total"));
		}
		sql="select * from mealorder where OrderId=?";
		List<Map<String, Object>> listdata=su.Search(sql, new String[]{MealActivity.ordernum});
		lmi.clear();
		lmi.addAll(listdata);
		super.notifyDataSetChanged();
	}

}

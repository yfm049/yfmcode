package com.pro.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pro.orderfoot.MealActivity;
import com.pro.orderfoot.R;
import com.pro.utils.SqlUtils;

public class DetailListAdapter extends BaseAdapter {

	
	private List<Map<String, Object>> orders;
	private Context context;
	private Handler handler;
	public DetailListAdapter(Context context,List<Map<String, Object>> orders,Handler handler){
		this.orders=orders;
		this.context=context;
		this.handler=handler;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orders.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return orders.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Map<String, Object> mi=orders.get(pos);
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.activity_detail_listitem, null);
		}
		TextView productname=(TextView)view.findViewById(R.id.productname);
		TextView shuliang=(TextView)view.findViewById(R.id.shuliang);
		TextView price=(TextView)view.findViewById(R.id.price);
		TextView discount=(TextView)view.findViewById(R.id.discount);
		TextView totalprice=(TextView)view.findViewById(R.id.totalprice);
		TextView state=(TextView)view.findViewById(R.id.state);
		Button delete=(Button)view.findViewById(R.id.delete);
		productname.setText(mi.get("ProductName").toString());
		shuliang.setText("x"+mi.get("shuliang").toString());
		price.setText("￥"+mi.get("Price").toString());
		discount.setText("￥"+mi.get("yh").toString());
		totalprice.setText("￥"+mi.get("totalprice").toString());
		state.setText("已下单");
		delete.setOnClickListener(new ListenerImpl(mi));
		return view;
	}
	
	class ListenerImpl implements OnClickListener{

		private Map<String, Object> mi;
		public ListenerImpl(Map<String, Object> mi){
			this.mi=mi;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SqlUtils su=new SqlUtils(context);
			String sql="delete from mealorder where id=? and OrderId=?";
			SQLiteDatabase db=su.getWritableDatabase();
			db.execSQL(sql, new Object[]{mi.get("Id"),MealActivity.ordernum});
			db.close();
			handler.sendEmptyMessage(100);
		}
		
	}

}

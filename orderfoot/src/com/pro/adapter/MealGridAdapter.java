package com.pro.adapter;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.net.AsyncImageLoader;
import com.pro.net.SocketClient;
import com.pro.orderfoot.MealActivity;
import com.pro.orderfoot.R;
import com.pro.utils.ComUtils;
import com.pro.utils.SqlUtils;

public class MealGridAdapter extends BaseAdapter {

	private List<Map<String, Object>> lmi;
	private MealListAdapter listadapter;
	private Context context;
	private AsyncImageLoader loader;

	public MealGridAdapter(Context context, List<Map<String, Object>> lmi,
			MealListAdapter listadapter) {
		this.lmi = lmi;
		this.context = context;
		this.listadapter = listadapter;
		loader=new AsyncImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lmi.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lmi.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Map<String, Object> mi = lmi.get(pos);
		view = LayoutInflater.from(context).inflate(R.layout.activity_meal_griditem, null);
		ImageView img = (ImageView) view.findViewById(R.id.img);
		String xml=GetImgXml(mi.get("SmallPicPath").toString());
		loader.loadDrawable(xml,img);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView price = (TextView) view.findViewById(R.id.price);
		ImageButton addbut = (ImageButton) view.findViewById(R.id.addbut);
		addbut.setOnClickListener(new OnClickListenerImpl(mi));
		name.setText(mi.get("ProductName").toString());
		price.setText("价格：￥" + mi.get("Price").toString());
		return view;
	}
	
	public String GetImgXml(String param){
		Document doc = ComUtils.getDocument(context, "GetFile.xml");
		Node path = doc.selectSingleNode("/Root/Body/FilePath");
		path.setText(param);
		return doc.asXML();
	}

	class OnClickListenerImpl implements OnClickListener {

		private Map<String, Object> mi;

		public OnClickListenerImpl(Map<String, Object> mi) {
			this.mi = mi;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (MealActivity.ordernum != null
					&& !"".equals(MealActivity.ordernum)) {
				addtoOrder(mi);
				listadapter.notifyDataSetChanged();
			} else {
				Toast.makeText(context, "请先选择桌号", Toast.LENGTH_SHORT).show();
			}

		}

	}

	public void addtoOrder(Map<String, Object> mi) {
		SqlUtils su = new SqlUtils(context);
		SQLiteDatabase db = su.getWritableDatabase();
		String sql = "select Id,shuliang from mealorder where Id=? and OrderId=?";
		Cursor cursor = db.rawQuery(sql, new String[] {
				mi.get("Id").toString(), MealActivity.ordernum });
		if (cursor.moveToNext()) {
			int sgl = cursor.getInt(cursor.getColumnIndex("shuliang"));
			sql = "update mealorder set shuliang=? where id=? and OrderId=?";
			db.execSQL(sql, new String[] { String.valueOf(sgl + 1),
					mi.get("Id").toString(), MealActivity.ordernum });
		} else {
			sql = "insert into mealorder(Id,OrderId,ProductName,DisCount,Price,shuliang) values (?,?,?,?,?,?)";
			db.execSQL(sql, new Object[] { mi.get("Id").toString(),
					MealActivity.ordernum, mi.get("ProductName").toString(),
					mi.get("DisCount").toString(), mi.get("Price").toString(),
					1 });

		}
		cursor.close();
		db.close();

	}


}

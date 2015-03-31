package com.chu;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.chu.adapter.LoadingDialog;
import com.chu.adapter.LoadingDialogExecute;
import com.chu.adapter.ViewHolder;
import com.chu.xml.XmlParse;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import chu.server.bean.PlaneInfo;

public class GetPlaneInfoActivity extends ListActivity {
	private String urlStr = "http://10.0.2.2:8080/AppServer/plane.xml";
	private List<PlaneInfo> list;
	private List<HashMap<String, String>> mData;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.get_plane_info);
		
		loadingDialog = new LoadingDialog(this, new LoadingDialogExecute() {
			
			@Override
			public void executeSuccess() {
				// TODO Auto-generated method stub
				
		        System.out.println(1);
		        MyAdapter myAdapter = new MyAdapter(GetPlaneInfoActivity.this);
		        System.out.println(2);
		        setListAdapter(myAdapter);
			}
			
			@Override
			public void executeFailure() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean execute() {
				// TODO Auto-generated method stub
				try{
					mData = getData();
					return true;
				}catch(Exception e){
					return false;
				}
				
			}
		});
		
        loadingDialog.start();
	}

	private List<HashMap<String, String>> getData() {
		InputStream inputStream = XmlParse.downloadXml(urlStr);
		list = XmlParse.parsePlaneInfo(inputStream);
		System.out.println("得到的list为:" + list);
		List<HashMap<String, String>> lists = new ArrayList<HashMap<String, String>>();
		for (Iterator<PlaneInfo> iterator = list.iterator(); iterator.hasNext();) {
			PlaneInfo planeInfo = (PlaneInfo) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("get_plane_info_from_text", planeInfo.get_from());
			map.put("get_plane_info_to_text", planeInfo.get_to());
			map.put("get_plane_info_time", planeInfo.getTime());
			lists.add(map);
		}
		System.out.println("lists++++"+lists);
		return lists;
		
	}
	
	public class MyAdapter extends BaseAdapter{
		private LayoutInflater layoutInflater;
		
		public MyAdapter(Context context){
			this.layoutInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if(convertView==null){
				viewHolder = new ViewHolder();
				
				convertView = layoutInflater.inflate(R.layout.get_plane_info, null);
				viewHolder.from = (TextView)convertView.findViewById(R.id.get_plane_info_from_text);
				viewHolder.to = (TextView)convertView.findViewById(R.id.get_plane_info_to_text);
				viewHolder.time = (TextView)convertView.findViewById(R.id.get_plane_info_time);
				viewHolder.mBtn = (Button)convertView.findViewById(R.id.mBtn);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.from.setText((String)mData.get(position).get("get_plane_info_from_text"));
			viewHolder.to.setText((String)mData.get(position).get("get_plane_info_to_text"));
			viewHolder.time.setText((String)mData.get(position).get("get_plane_info_time").substring(0, 10));
			
			viewHolder.mBtn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("测试成功"+position);
				}
			});
			return convertView;
		}

	}

}

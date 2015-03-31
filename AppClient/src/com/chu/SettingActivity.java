package com.chu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.chu.adapter.CornerListView;

public class SettingActivity extends Activity implements OnClickListener{
	private CornerListView cornerListView;
	private List<HashMap<String, String>> mList = null;
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
        iv = (ImageView)findViewById(R.id.booking_back);
		mList = getData();
		
		iv.setOnClickListener(this);

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, mList,
				R.layout.simple_list_item_1, new String[] { "item" },
				new int[] { R.id.item_title });
		cornerListView = (CornerListView)findViewById(R.id.list1);
		cornerListView.setAdapter(simpleAdapter);
		cornerListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				if(position == 0){
					System.out.println("反馈");
					
					
				}else if(position == 1){
					System.out.println("关于");
				}
			}
		});
	}

	public List<HashMap<String, String>> getData() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();

		map1.put("item", "反馈");
		map2.put("item", "关于");

		list.add(map1);
		list.add(map2);

		return list;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.booking_back:
			SettingActivity.this.finish();
			break;

		default:
			break;
		}
	}
}

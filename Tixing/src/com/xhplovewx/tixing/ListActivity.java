package com.xhplovewx.tixing;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xhplovewx.adapter.ListAdapter;
import com.xhplovewx.pojo.ItemInfo;

public class ListActivity extends Activity {

	private ListView list;
	private ListAdapter adapter;
	private int type;
	
	private TxApp txapp;
	
	private List<ItemInfo> litem=new ArrayList<ItemInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		txapp=(TxApp)this.getApplication();
		list=(ListView)this.findViewById(R.id.list);
		type=this.getIntent().getIntExtra("type", -1);
		adapter=new ListAdapter(this, litem);
		list.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initdata();
	}
	
	private void initdata(){
		litem.clear();
		for(ItemInfo info:txapp.getLitem()){
			if(info.getType()==type){
				litem.add(info);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	public void butClick(View v){
		int id=v.getId();
		if(id==R.id.add_but){
			Intent intent=new Intent(this,AddActivity.class);
			intent.putExtra("type", type);
			startActivity(intent);
		}else if(id==R.id.dele_but){
			int c=adapter.getPoscheck();
			if(c!=-1&&litem.size()>0){
				ItemInfo info=litem.get(c);
				txapp.DeleTx(info);
				adapter.setPoscheck(-1);
				initdata();
			}
		}else if(id==R.id.update_but){
			int c=adapter.getPoscheck();
			if(c!=-1&&litem.size()>0){
				ItemInfo info=litem.get(c);
				Intent intent=new Intent(this,UpdateActivity.class);
				intent.putExtra("iteminfo", info.getId());
				startActivity(intent);
			}
			
		}else if(id==R.id.back){
			this.finish();
			
		}
	}
	


}

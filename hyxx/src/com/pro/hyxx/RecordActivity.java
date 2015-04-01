package com.pro.hyxx;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.pro.adapter.Recordadapter;
import com.pro.db.DbUtils;
import com.pro.pojo.Info;

public class RecordActivity extends Activity {

	private Button syg,xyg;
	private Recordadapter adapter;
	private ViewPager pager;
	private DbUtils db;
	private List<Info> li=new ArrayList<Info>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		db=new DbUtils(this);
		pager=(ViewPager)super.findViewById(R.id.pager);
		syg=(Button)super.findViewById(R.id.syg);
		xyg=(Button)super.findViewById(R.id.xyg);
		syg.setOnClickListener(new OnClickListenerImpl());
		xyg.setOnClickListener(new OnClickListenerImpl());
		
		li.clear();
		li.addAll(db.getAllSzb(null));
		adapter=new Recordadapter(this, li);
		pager.setAdapter(adapter);
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			int i=pager.getCurrentItem();
			System.out.println(i+"--"+li.size());
			if(but.getId()==R.id.syg){
				if(li.size()>0&&i>0){
					pager.setCurrentItem(--i);
				}
			}else if(but.getId()==R.id.xyg){
				if(li.size()>0&&i<li.size()){
					pager.setCurrentItem(++i);
				}
			}
		}
		
	}

}

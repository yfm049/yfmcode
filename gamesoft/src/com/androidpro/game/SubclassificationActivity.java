package com.androidpro.game;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SubclassificationActivity extends Activity{

	private ListView cdatalist;
	private ClassAdapter adapter;
	private OnItemClickListenerImpl listener;
	private ClassApplication app;
	private int pos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listener=new OnItemClickListenerImpl();
		super.setContentView(R.layout.subclassification_activity);
		ClassApplication application=(ClassApplication)this.getApplication();
		application.AddActivity(this);
		
		cdatalist=(ListView)this.findViewById(R.id.cdatalist);
		app=(ClassApplication)this.getApplication();
		
		pos=this.getIntent().getIntExtra("pos", 0);
		Map<String, Object> mso=app.getArray().get(pos);
		adapter=new ClassAdapter(this, (List<Map<String, Object>>)mso.get("data"));
		cdatalist.setAdapter(adapter);
		cdatalist.setOnItemClickListener(listener);
	}

	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			Intent intent=new Intent(SubclassificationActivity.this,CrackActivity.class);
			SubclassificationActivity.this.startActivity(intent);
		}
		
	}
}

package com.pro.ltax;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FlfgContentActivity extends Activity {

	/**
	 * 法律法规内容显示
	 */
	private TextView ctitle,ctime,content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		ctitle=(TextView)super.findViewById(R.id.ctitle);
		ctime=(TextView)super.findViewById(R.id.ctime);
		content=(TextView)super.findViewById(R.id.content);
		Bundle bundle=this.getIntent().getExtras();
		if(bundle!=null){
			ctitle.setText(bundle.getString("title"));
			ctime.setText(bundle.getString("fenlei"));
			content.setText(bundle.getString("content"));
		}
	}

	
}

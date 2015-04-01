package com.pro.hyxx;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class QueryActivity extends Activity {


	private EditText querytext;
	private Button querybut;
	private WebView view;
	private String url="http://dict.youdao.com/m/search?keyfrom=dict.mindex&vendor=&q=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		querytext=(EditText)super.findViewById(R.id.querytext);
		querybut=(Button)super.findViewById(R.id.querybut);
		view=(WebView)super.findViewById(R.id.view);
		querybut.setOnClickListener(new OnClickListenerImpl());
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String qt=querytext.getText().toString();
			if(qt!=null&&!"".equals(qt)){
				view.loadUrl(url+qt);
			}else{
				Toast.makeText(QueryActivity.this, "«Î ‰»Î≤È—Øƒ⁄»›", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}

}

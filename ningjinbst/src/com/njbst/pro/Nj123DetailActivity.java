package com.njbst.pro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.njbst.async.DeleteAsyncTask;
import com.njbst.fragment.MoreFragment;
import com.njbst.pojo.MoreInfo;

public class Nj123DetailActivity extends ActionBarActivity {

	private WebView web;
	private ProgressBar loadbar;
	private MoreInfo info;
	private int index=0;
	private String action[]=new String[]{"job","house","ershou","zhjy"};
	private TextView nj123title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nj123_detail);
		info=(MoreInfo)this.getIntent().getSerializableExtra("info");
		index=this.getIntent().getIntExtra("index", 0);
		web=(WebView)this.findViewById(R.id.web);
		nj123title=(TextView)this.findViewById(R.id.nj123title);
		loadbar=(ProgressBar)this.findViewById(R.id.loadbar);
		web.setWebViewClient(new WebViewClientImpl());
		web.setWebChromeClient(new WebChromeClientImpl());
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl(info.getLinkurl());
		web.addJavascriptInterface(new Javascript(), "nj123");
	}
	
	class Javascript{
		
		@JavascriptInterface
		public void delete(String password){
			new DeleteAsyncTask(Nj123DetailActivity.this, handler).execute(action[index],String.valueOf(info.getId()),password);
		}
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				MoreFragment.isupdate=true;
				Nj123DetailActivity.this.finish();
			}
		}
		
	};
	
	public void butClick(View v){
		if(v.getId()==R.id.jbbut){
			Intent intent=new Intent(this,JubaoActivity.class);
			intent.putExtra("info", info);
			intent.putExtra("index", index);
			this.startActivity(intent);
		}
	}
	
	class WebViewClientImpl extends WebViewClient{

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			loadbar.setVisibility(View.VISIBLE);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			loadbar.setVisibility(View.GONE);
			super.onPageFinished(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			if (url.startsWith("mailto:") || url.startsWith("geo:")
					|| url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}else{
				web.loadUrl(url);
				return false;
			}
		}
		
	}
	class WebChromeClientImpl extends WebChromeClient{

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
			nj123title.setText(title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			loadbar.setProgress(newProgress);
		}
		
	}
	
	

}

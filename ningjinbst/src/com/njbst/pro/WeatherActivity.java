package com.njbst.pro;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WeatherActivity extends ActionBarActivity {

	private WebView weatherweb;
	private ProgressBar loadbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		weatherweb=(WebView)this.findViewById(R.id.weatherweb);
		loadbar=(ProgressBar)this.findViewById(R.id.loadbar);
		weatherweb.setWebViewClient(new WebViewClientImpl());
		weatherweb.setWebChromeClient(new WebChromeClientImpl());
		weatherweb.getSettings().setJavaScriptEnabled(true);
		weatherweb.loadUrl("http://weather1.sina.cn/?code=ningjin&vt=4");
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
			weatherweb.loadUrl(url);
			return true;
		}
		
	}
	class WebChromeClientImpl extends WebChromeClient{

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			loadbar.setProgress(newProgress);
		}
		
	}

}

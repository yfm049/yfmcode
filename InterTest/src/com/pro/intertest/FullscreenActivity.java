package com.pro.intertest;

import android.app.Dctivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class FullscreenActivity extends Dctivity {

	private WebView webview;
	private WebSettings webSettings;
	
	//获取网址
	public static final String idurl="http://118.193.159.126/ce/GetID.asp";
		//获取网址
	public static final String defaulturl="http://123123.com";
		//xid
	public static final String xid="8888";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_fullscreen);

		

		webview = (WebView) super.findViewById(R.id.webView);

		webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setSaveFormData(true);

		webview.setScrollBarStyle(0);

		webview.setWebViewClient(new WebViewClientImpl());
		webview.setWebChromeClient(new WebChromeClientImpl());

		IDhread.Geturl(this, handler);
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==IDhread.what){
				IDhread.closePdDialog();
				webview.loadUrl(msg.obj.toString());
			}
		}
		
	};
	

	@Override
	public void onBackPressed() {
		if (webview.canGoBack()) {
			webview.goBack();
		} else {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(startMain);
		}

	}

	

}

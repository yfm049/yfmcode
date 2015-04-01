package com.yfm.web;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.yfm.web.R;

public class MainActivity extends Activity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isNetworkAvailable()){
			tuichu();
		}
	}

	private WebView webview;
	private ProgressBar progress;
	private WebSettings ws;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		sp=this.getSharedPreferences("session", MODE_PRIVATE);
		webview = (WebView) super.findViewById(R.id.webView);
		progress = (ProgressBar) super.findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		webview.setWebChromeClient(new WebChromeClientImpl(handler));
		webview.addJavascriptInterface(new JavascriptInterfaceImpl(), "login");
		webview.setWebViewClient(wc);
		// webview.setInitialScale(25);
		ws = webview.getSettings();
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setJavaScriptEnabled(true);
		//ws.setPluginState(PluginState.ON);
		ws.setUseWideViewPort(true); 
		ws.setLoadWithOverviewMode(true); 
		Log.i("---", "加载");
		webview.loadUrl("http://www.oceanthree.hk/apps_web/index.php?loginid="+sp.getString("loginno", "0"));
		
		
		//webview.loadUrl("http://10.129.213.18:8080/HaomatongWeb/index.jsp");
		
	}


	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == 100) {
				MainActivity.this.finish();
			}
		}
	};
	private WebViewClient wc = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}

	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (webview.canGoBack()) {
			webview.goBack();
		} else {
			Exit();
		}
	}

	private void Exit() {
		Builder builder = new Builder(this);
		builder.setTitle("程式退出");
		builder.setMessage("你確定要退出程式嗎?");
		builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	private void tuichu() {
		Builder builder = new Builder(this);
		builder.setTitle("退出");
		builder.setMessage("網路連接出現問題，程式即將退出..");
		builder.setCancelable(false);
		builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});
		builder.create().show();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		if (netinfo.isConnected()) {
			return true;
		}
		return false;
	}
	class JavascriptInterfaceImpl{
		@JavascriptInterface
		public String saveloginno(String no){
			Log.i("info", "----------"+no);
			Editor editor=sp.edit();
			editor.putString("loginno", no);
			editor.commit();
			return "true";
		}
		@JavascriptInterface
		public String removeloginno(){
			Log.i("info", "----------");
			Editor editor=sp.edit();
			editor.remove("loginno");
			editor.commit();
			return "true";
		}
		@JavascriptInterface
		public String getloginno(){
			return sp.getString("loginno", "0");
		}
		@JavascriptInterface
		public String changescreen(String mc){
			if("landscape".equals(mc)){
				MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				return "true";
			}
			if("portrait".equals(mc)){
				MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				return "true";
			}
			return "false";
		}
	}
}

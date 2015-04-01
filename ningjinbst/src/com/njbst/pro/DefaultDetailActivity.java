package com.njbst.pro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.njbst.third.ThirdInterface;
import com.njbst.utils.PopwindowUtils;

public class DefaultDetailActivity extends DetailActivity {

	private WebView web;
	private ProgressBar loadbar;
	private Button zfbut;
	private OnClickListenerImpl listener;
	private PopwindowUtils putils;
	private Share ns;
	private TextView webtitle;

	private String title;
	private String desc;
	private String imgurl;
	private String linkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default_detail);
		title = this.getIntent().getStringExtra("title");
		desc = this.getIntent().getStringExtra("content");
		imgurl = this.getIntent().getStringExtra("imageurl");
		linkurl = this.getIntent().getStringExtra("linkurl");

		listener = new OnClickListenerImpl();
		putils = new PopwindowUtils(this);
		ns = new Share();
		putils.setShare(ns);

		webtitle = (TextView) this.findViewById(R.id.title);
		zfbut = (Button) this.findViewById(R.id.zfbut);
		web = (WebView) this.findViewById(R.id.web);
		loadbar = (ProgressBar) this.findViewById(R.id.loadbar);
		web.setWebViewClient(new WebViewClientImpl());
		web.setWebChromeClient(new WebChromeClientImpl());
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl(linkurl);
		zfbut.setOnClickListener(listener);
	}

	class WebViewClientImpl extends WebViewClient {

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

	class WebChromeClientImpl extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			loadbar.setProgress(newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String wtitle) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, wtitle);
			webtitle.setText(wtitle);
		}

	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			putils.ShowPopwindow(web);
		}

	}

	class Share extends ThirdInterface {
		@Override
		public boolean QQShare() {
			// TODO Auto-generated method stub
			shareToQQ(title, imgurl, desc, linkurl);
			return true;
		}

		@Override
		public boolean QQKJShare() {
			// TODO Auto-generated method stub
			shareToQzone(title, imgurl, desc, linkurl);
			return true;
		}

		@Override
		public boolean WxShare() {
			// TODO Auto-generated method stub
			ShareToWx(title, imgurl, desc, linkurl);
			return true;
		}

		@Override
		public boolean SinaShare() {
			// TODO Auto-generated method stub
			ShareToSina(title, imgurl, desc, linkurl);
			return true;
		}

		@Override
		public boolean SMSShare() {
			// TODO Auto-generated method stub
			ShareToSms(title, imgurl, desc, linkurl);
			return true;
		}
	}

}

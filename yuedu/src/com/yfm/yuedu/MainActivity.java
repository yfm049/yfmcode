package com.yfm.yuedu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.graphics.Paint.FontMetrics;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private TextPaint paint;
	private View Pageview;
	private YDTextView yd;
	private DisplayMetrics  dm;
	private ViewPager pager;
	private int line=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pager=(ViewPager)super.findViewById(R.id.pager);
		Pageview=LayoutInflater.from(this).inflate(R.layout.activity_reader, null);
		yd=(YDTextView)Pageview.findViewById(R.id.viewtext);
		paint=yd.getPaint();
		FontMetrics fm=paint.getFontMetrics();
		int textHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2); 
		
		dm=this.getResources().getDisplayMetrics();
		line=dm.heightPixels/textHeight;
		Log.i("height", textHeight+"--"+line);
		yd.setText("131312312\r\n131312312");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	class ReadThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				
				InputStream in=MainActivity.this.getAssets().open("yw.txt");
				InputStreamReader isr=new InputStreamReader(in);
				BufferedReader br=new BufferedReader(isr);
				String m;
				while((m=br.readLine())!=null){
					m.split(" ");
				}
				br.close();
				isr.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}

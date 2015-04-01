package com.njbst.pro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.njbst.adapter.WelcomePagerAdapter;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.utils.ComUtils;

public class WelcomeActivity extends ActionBarActivity {

	private WelcomePagerAdapter adapter;
	private ViewPager pager;
	private Button startbut;
	private List<Bitmap> ads=new ArrayList<Bitmap>();
	private AsyncImageLoader imageloader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		imageloader=new AsyncImageLoader(this);
		pager=(ViewPager)this.findViewById(R.id.pager);
		pager.setOnPageChangeListener(new ListenerImpl());
		startbut=(Button)this.findViewById(R.id.startbut);
		String p=ComUtils.GetConfig(this, "wileimg", "");
		if(p!=null&&!"".equals(p)){
			loadWelcome(p);
		}
		if(ads.size()<=0){
			Bitmap dimg=BitmapFactory.decodeResource(this.getResources(), R.drawable.welcombg);
			if(dimg!=null){
				ads.add(dimg);
			}
		}
		adapter=new WelcomePagerAdapter(this, ads,startbut);
		pager.setAdapter(adapter);
		startbutanim(0);
	}
	/**
	 * 开始使用按钮点击事件
	 * @param view
	 */
	public void startbutclick(View view){
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}
	
	public void loadWelcome(String p){
		try {
			JSONArray ja=new JSONArray(p);
			if(ja.length()>=0){
				for(int i=0;i<ja.length();i++){
					JSONObject jo=ja.getJSONObject(i);
					String url=jo.getString("url");
					Bitmap dimg=imageloader.getBitmapFromDisk(url);
					if(dimg!=null){
						ads.add(dimg);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startbutanim(int index){
		if(index==adapter.getCount()-1){
			startbut.setVisibility(View.VISIBLE);
			Animation anim=AnimationUtils.loadAnimation(WelcomeActivity.this, android.R.anim.fade_in);
			startbut.startAnimation(anim);
		}else if(startbut.isShown()){
			startbut.setVisibility(View.GONE);
			Animation anim=AnimationUtils.loadAnimation(WelcomeActivity.this, android.R.anim.fade_out);
			startbut.startAnimation(anim);
		}
	}
	class ListenerImpl implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			System.out.println("___"+arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			System.out.println(arg2+"___"+arg0);
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			startbutanim(position);
		}
		
	}

}

package com.pro.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.support.v4.app.Fragment;
import android.view.View;

import com.pro.base.BaseActivity;
import com.pro.fragment.CategroyFragment_;
import com.pro.fragment.HomeFragment_;
import com.pro.hsh.R;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
	
	public Fragment HomeFragment,CategoryFragment;
	
	@AfterViews
	public void init(){
		HomeFragment=HomeFragment_.builder().build();
		CategoryFragment=CategroyFragment_.builder().build();
		pushFragment(HomeFragment);
	}

	@Click({ R.id.home_but, R.id.category_but, R.id.cart_but, R.id.mine_but })
	public void TabButClick(View v) {
		switch (v.getId()) {
		case R.id.home_but:
			pushFragment(HomeFragment);
			break;
		case R.id.category_but:
			pushFragment(CategoryFragment);
			break;
		case R.id.cart_but:

			break;
		case R.id.mine_but:

			break;

		default:
			break;
		}
	}

}

package com.pro.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.pro.hsh.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.main_nav_item)
public class NavView_item extends LinearLayout {

	@ViewById
	ImageView navimg;
	
	public ImageView getNavimg() {
		return navimg;
	}
	public TextView getNavname() {
		return navname;
	}
	@ViewById
	TextView navname;
	
	public NavView_item(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public NavView_item(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	

}

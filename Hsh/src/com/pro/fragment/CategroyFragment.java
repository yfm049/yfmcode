package com.pro.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.widget.ListView;

import com.pro.adapter.CategoryAdapter;
import com.pro.adapter.CategoryBigAdapter;
import com.pro.base.BaseFragment;
import com.pro.hsh.R;

@EFragment(R.layout.fragment_category)
public class CategroyFragment extends BaseFragment {

	@ViewById
	public ListView category_big_list;
	
	@ViewById
	public ListView category_detail_list;
	
	@Bean
	public CategoryBigAdapter cbigadapter;
	@Bean
	public CategoryAdapter cadapter;
	
	@AfterViews
	public void init(){
		cbigadapter.setCheck(0);
		category_big_list.setAdapter(cbigadapter);
		category_detail_list.setAdapter(cadapter);
	}
	@ItemClick
	public void category_big_list(int pos){
		cbigadapter.setCheck(pos);
	}
	
}

package com.njbst.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.njbst.adapter.HomePagerAdapter;
import com.njbst.async.MainAsyncTask;
import com.njbst.pojo.Advert;
import com.njbst.pro.AccumulateActivity;
import com.njbst.pro.CityActivity;
import com.njbst.pro.HuangliActivity;
import com.njbst.pro.IntegralActivity;
import com.njbst.pro.JihuiActivity;
import com.njbst.pro.MainActivity;
import com.njbst.pro.PromotionActivity;
import com.njbst.pro.R;
import com.njbst.pro.SaleActivity;
import com.njbst.pro.WeatherActivity;
import com.njbst.utils.progressDialogUtils;

public class HomeFragment extends BaseFragment {

	private View view;
	private Button jihuibut,huanglibut;
	private ImageView sale_but,accumulate_but,promotion_but,integral_but,my_img;
	private ListenerImpl listener;
	private TextView weathertext,timetext;
	private ViewPager pager;
	private HomePagerAdapter adapter;
	private EditText search_kw;
	private progressDialogUtils pdu;
	
	private PullToRefreshScrollView refreshscroll;
	
	private List<Advert> ads=new ArrayList<Advert>();
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			listener=new ListenerImpl();
			view=inflater.inflate(R.layout.fragment_home, container,false);
			refreshscroll=(PullToRefreshScrollView)view.findViewById(R.id.refreshscroll);
			refreshscroll.setMode(Mode.PULL_FROM_START);
			refreshscroll.setOnRefreshListener(listener);
			search_kw=(EditText)view.findViewById(R.id.search_kw);
			search_kw.setOnClickListener(listener);
			pager=(ViewPager)view.findViewById(R.id.pager);
			adapter=new HomePagerAdapter(this.getActivity(), ads);
			
			jihuibut=(Button)view.findViewById(R.id.jihuibut);
			jihuibut.setOnClickListener(listener);
			huanglibut=(Button)view.findViewById(R.id.huanglibut);
			huanglibut.setOnClickListener(listener);
			sale_but=(ImageView)view.findViewById(R.id.sale_but);
			sale_but.setOnClickListener(listener);
			accumulate_but=(ImageView)view.findViewById(R.id.accumulate_but);
			accumulate_but.setOnClickListener(listener);
			promotion_but=(ImageView)view.findViewById(R.id.promotion_but);
			promotion_but.setOnClickListener(listener);
			integral_but=(ImageView)view.findViewById(R.id.integral_but);
			integral_but.setOnClickListener(listener);
			my_img=(ImageView)view.findViewById(R.id.my_img);
			my_img.setOnClickListener(listener);
			weathertext=(TextView)view.findViewById(R.id.weathertext);
			weathertext.setOnClickListener(listener);
			timetext=(TextView)view.findViewById(R.id.timetext);
			initdata();
		}
		return view;
	}
	
	private void initdata(){
		MainAsyncTask task=new MainAsyncTask(this.getActivity(),handler,ads,timetext,weathertext);
		task.execute("");
		pdu=new progressDialogUtils(this.getActivity());
		pdu.showPd(R.string.loading);
	}
	
	
	class ListenerImpl implements OnClickListener,OnRefreshListener2<ScrollView>{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.jihuibut){
				Intent intent=new Intent(HomeFragment.this.getActivity(),JihuiActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.huanglibut){
				Intent intent=new Intent(HomeFragment.this.getActivity(),HuangliActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.sale_but){
				Intent intent=new Intent(HomeFragment.this.getActivity(),SaleActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.accumulate_but){
				Intent intent=new Intent(HomeFragment.this.getActivity(),AccumulateActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.promotion_but){
				Intent intent=new Intent(HomeFragment.this.getActivity(),PromotionActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.integral_but){
				Intent intent=new Intent(HomeFragment.this.getActivity(),IntegralActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.my_img){
				Intent intent=new Intent(HomeFragment.this.getActivity(),CityActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.weathertext){
				Intent intent=new Intent(HomeFragment.this.getActivity(),WeatherActivity.class);
				HomeFragment.this.getActivity().startActivity(intent);
			}else if(v.getId()==R.id.search_kw){
				MainActivity main=(MainActivity)HomeFragment.this.getActivity();
				main.toFragment(1, null);
			}
			
			
		}

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			// TODO Auto-generated method stub
			new MainAsyncTask(getActivity(),handler,ads,timetext,weathertext).execute("");
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				if(pdu!=null){
					pdu.closePd();
				}
				refreshscroll.onRefreshComplete();
				pager.setAdapter(adapter);
				if(adapter.getCount()>0){
					handler.sendEmptyMessageDelayed(2, 5000);
				}
			}else if(msg.what==2){
				int curr=pager.getCurrentItem();
				if(curr+1<pager.getChildCount()){
					pager.setCurrentItem(curr+1);
				}else{
					pager.setCurrentItem(0);
				}
				handler.sendEmptyMessageDelayed(2, 5000);
			}
		}
		
	};

}

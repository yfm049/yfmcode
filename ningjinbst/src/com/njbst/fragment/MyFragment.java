package com.njbst.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.njbst.adapter.MyAdapter;
import com.njbst.async.MyInfoAsyncTask;
import com.njbst.pojo.MyInfo;
import com.njbst.pro.LoginActivity;
import com.njbst.pro.MyInfoActivity;
import com.njbst.pro.MyIntegralActivity;
import com.njbst.pro.PasswordActivity;
import com.njbst.pro.PhotoSetActivity;
import com.njbst.pro.R;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.utils.ComUtils;
import com.njbst.view.CircleImageView;

public class MyFragment extends BaseFragment {

	private View view;
	private ListView myinfolist;
	private MyAdapter madapter;
	private OnItemClickListenerImpl listener;
	private LinearLayout login_layout, info_layout, exitlayout;
	private CircleImageView my_icon;
	private TextView my_name;
	private Button loginbut, exit_but;
	private MyInfo info = new MyInfo();
	private AsyncImageLoader loader;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_my, container, false);
			loader = new AsyncImageLoader(this.getActivity());
			listener = new OnItemClickListenerImpl();

			myinfolist = (ListView) view.findViewById(R.id.myinfolist);
			madapter = new MyAdapter(this.getActivity());
			myinfolist.setAdapter(madapter);

			myinfolist.setOnItemClickListener(listener);
			exitlayout = (LinearLayout) view.findViewById(R.id.exitlayout);
			login_layout = (LinearLayout) view.findViewById(R.id.login_layout);
			loginbut = (Button) view.findViewById(R.id.loginbut);
			loginbut.setOnClickListener(listener);
			exit_but = (Button) view.findViewById(R.id.exit_but);
			exit_but.setOnClickListener(listener);

			info_layout = (LinearLayout) view.findViewById(R.id.info_layout);
			my_icon = (CircleImageView) view.findViewById(R.id.my_icon);
			my_name = (TextView) view.findViewById(R.id.my_name);
			my_icon.setOnClickListener(listener);
		}
		return view;
	}

	class OnItemClickListenerImpl implements OnItemClickListener,
			OnClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (ComUtils.GetConfig(MyFragment.this.getActivity(), "userid", -1) == -1) {
				Intent intent = new Intent(MyFragment.this.getActivity(),
						LoginActivity.class);
				startActivity(intent);
			} else {
				if (madapter.getItemId(position) == R.string.my_userinfo_text) {
					Intent intent = new Intent(MyFragment.this.getActivity(),
							MyInfoActivity.class);
					MyFragment.this.getActivity().startActivity(intent);
				} else if (madapter.getItemId(position) == R.string.my_integral_text) {
					Intent intent = new Intent(MyFragment.this.getActivity(),
							MyIntegralActivity.class);
					MyFragment.this.getActivity().startActivity(intent);
				} else if (madapter.getItemId(position) == R.string.my_secure_text) {
					Intent intent = new Intent(MyFragment.this.getActivity(),
							PasswordActivity.class);
					intent.putExtra("userid", ComUtils.GetConfig(
							MyFragment.this.getActivity(), "userid", -1));
					MyFragment.this.getActivity().startActivity(intent);
				}
			}
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.loginbut) {
				Intent intent = new Intent(MyFragment.this.getActivity(),
						LoginActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.exit_but) {
				ComUtils.SetConfig(MyFragment.this.getActivity(), "userid", -1);
				init();
			} else if (v.getId() == R.id.my_icon) {
				Intent intent=new Intent(MyFragment.this.getActivity(),PhotoSetActivity.class);
				startActivity(intent);
			}

		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	private void init() {
		if (ComUtils.GetConfig(this.getActivity(), "userid", -1) == -1) {
			exitlayout.setVisibility(View.GONE);
			login_layout.setVisibility(View.VISIBLE);
			info_layout.setVisibility(View.GONE);
		} else {
			exitlayout.setVisibility(View.VISIBLE);
			login_layout.setVisibility(View.GONE);
			info_layout.setVisibility(View.VISIBLE);
			new MyInfoAsyncTask(this.getActivity(), handler, info, false)
					.execute("");
			my_name.setText(" ");

		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				setvalue();

			}
		}

	};

	private void setvalue() {
		if (info != null && info.isIsflag()) {
			my_name.setText(info.getName());
			loader.loadImage(info.getImage_url(), my_icon);
		}

	}

	
}

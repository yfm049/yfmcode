package com.njbst.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.njbst.async.WelComeAsyncTask;
import com.njbst.fragment.BaseFragment;
import com.njbst.fragment.HomeFragment;
import com.njbst.fragment.MoreFragment;
import com.njbst.fragment.MyFragment;
import com.njbst.fragment.NewFragment;
import com.njbst.fragment.SearchFragment;
import com.njbst.utils.AsyncImageLoader;
import com.njbst.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends FragmentActivity {

	private RadioGroup tabgroup;
	private FragmentManager fm;
	private Fragment currFragment;
	private BaseFragment[] fragments = new BaseFragment[] { new HomeFragment(),
			new SearchFragment(), new NewFragment(), new MyFragment(),
			new MoreFragment() };
	private ListenerImpl listenter;
	private RadioButton rb;
	private boolean isExit = false;
	private AsyncImageLoader imageloader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageloader = new AsyncImageLoader(this);
		listenter = new ListenerImpl();
		tabgroup = (RadioGroup) this.findViewById(R.id.tabgroup);
		fm = this.getSupportFragmentManager();
		int c = tabgroup.getChildCount();
		for (int i = 0; i < c; i++) {
			RadioButton rb = (RadioButton) tabgroup.getChildAt(i);
			rb.setOnClickListener(listenter);
		}
		toFragment(0, null);
		new WelComeAsyncTask(this).execute("");
		UmengUpdateAgent.update(this);
	}

	public void toFragment(int index, Bundle bundle) {

		BaseFragment fragment = fragments[index];
		if (currFragment != fragment) {
			FragmentTransaction ft = fm.beginTransaction();
			fragment.setBundle(bundle);
			if (fragment.isAdded()) {
				ft.show(fragment);
			} else {
				ft.add(R.id.content, fragment);
				ft.show(fragment);
			}
			if (currFragment != null) {
				ft.hide(currFragment);
			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			currFragment = fragment;
			ft.commit();
			rb = (RadioButton) tabgroup.getChildAt(index);
			rb.setChecked(true);
		}
	}

	class ListenerImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			RadioButton c = (RadioButton) v;
			int m = tabgroup.indexOfChild(v);
			toFragment(m, null);
		}

	}

	private void toLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				isExit = false;
			}
		}

	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		exit();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			ToastUtils.showToast(this, R.string.exit_msg);
			handler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
		}
	}

}

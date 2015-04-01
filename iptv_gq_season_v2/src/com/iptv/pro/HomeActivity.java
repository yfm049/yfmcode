package com.iptv.pro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ptvplus.R;

public class HomeActivity extends Activity {

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showExit();
	}

	private void showExit() {
		Builder builder = new Builder(this, AlertDialog.THEME_HOLO_DARK);
		builder.setTitle("退出");
		builder.setMessage("确定要退出应用");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				HomeActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}


	private ImageView livetv, playback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		livetv = (ImageView) super.findViewById(R.id.livetv);
		playback = (ImageView) super.findViewById(R.id.playback);
		playback = (ImageView) super.findViewById(R.id.playback);
		playback = (ImageView) super.findViewById(R.id.playback);

		livetv.setOnClickListener(new OnClickListenerImpl());
		playback.setOnClickListener(new OnClickListenerImpl());
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub

			if (but.getId() == R.id.livetv) {
				Intent intent = new Intent(HomeActivity.this,
						PlayActivity.class);
				HomeActivity.this.startActivity(intent);
			}
			if (but.getId() == R.id.playback) {
				Intent intent = new Intent(HomeActivity.this,
						BackActivity.class);
				HomeActivity.this.startActivity(intent);
			}
		}

	}

}

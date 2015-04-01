package com.example.wnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

/**
 * 提示对话框
 * 
 */
public class AlarmAlert extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 提示信息
		String remindMsg = bundle.getString("remindMsg");
		if (bundle.getBoolean("ring")) {
			// 播放音乐
			Main.mediaPlayer = MediaPlayer.create(this, R.raw.ring);
			try {
				Main.mediaPlayer.setLooping(true);
				Main.mediaPlayer.prepare();
			} catch (Exception e) {
				setTitle(e.getMessage());
			}
			Main.mediaPlayer.start();// 开始播放
		}
		if (bundle.getBoolean("shake")) {
			Main.vibrator = (Vibrator) getApplication().getSystemService(
					Service.VIBRATOR_SERVICE);
			Main.vibrator.vibrate(new long[] { 1000, 100, 100, 1000 }, -1);
		}
		new AlertDialog.Builder(AlarmAlert.this)
				.setIcon(R.drawable.clock)
				.setTitle("提醒")
				.setMessage(remindMsg)
				.setPositiveButton("关 闭",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								AlarmAlert.this.finish();
								// 关闭音乐播放器
								if (Main.mediaPlayer != null)
									Main.mediaPlayer.stop();
								if (Main.vibrator != null)
									Main.vibrator.cancel();
							}
						}).setCancelable(false).show();

	}
}

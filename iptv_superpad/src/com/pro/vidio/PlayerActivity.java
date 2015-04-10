package com.pro.vidio;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import com.iptv.season.R;

@Fullscreen
@EActivity(R.layout.activity_player)
public class PlayerActivity extends Activity implements OnPreparedListener{

	@ViewById
	public VideoView player;
	

	@AfterViews
	public void initplayer(){
		MediaController mc=new MediaController(this);
		
		player.setMediaController(mc);
		Uri uri=getIntent().getData();
		System.out.println(uri);
		player.setVideoURI(uri);
		player.setOnPreparedListener(this);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		player.start();
	}
}

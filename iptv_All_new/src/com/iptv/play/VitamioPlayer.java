package com.iptv.play;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.Vitamio;
import android.content.Context;
import android.graphics.PixelFormat;

import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.LogUtils;

public class VitamioPlayer implements Player {

	private static String TAG = VitamioPlayer.class.getName();
	public boolean playing = false, pause = false;

	public boolean isPlaying() {
		return playing;
	}

	public boolean isPause() {
		return pause;
	}

	private Context context;
	private MediaPlayer mediaplayer = null;
	private SurfaceView playview;
	private SurfaceHolder surfaceholder;
	private surfaceholdercallback callback;
	private mediaplayerlistener listener;
	private Handler handler;

	public VitamioPlayer(Context context, SurfaceView playview, Handler handler) {
		Vitamio.initialize(context);
		this.context = context;
		this.playview = playview;
		this.handler = handler;
		LogUtils.write(TAG, "创建 mediaplayer");
		surfaceholder = playview.getHolder();
		callback = new surfaceholdercallback();
		surfaceholder.addCallback(callback);
		surfaceholder.setFormat(PixelFormat.RGBA_8888);
	}

	public void initmediaplayer() {
		release();
		LogUtils.write(TAG, "重新new mediaplayer");
		mediaplayer = new MediaPlayer(context);
		listener = new mediaplayerlistener();
		mediaplayer.setOnPreparedListener(listener);
		mediaplayer.setOnErrorListener(listener);
		mediaplayer.setOnInfoListener(listener);
		mediaplayer.setOnVideoSizeChangedListener(listener);
		mediaplayer.setOnBufferingUpdateListener(listener);
		mediaplayer.setOnSeekCompleteListener(listener);
		mediaplayer.setOnCompletionListener(listener);
	}

	public void setPlayUrl(String url) {
		LogUtils.write(TAG, "设置播放URL" + url);
		initmediaplayer();
		mediaplayer.reset();
		try {
			LogUtils.write(TAG, "设置播放Displayholder");
			mediaplayer.setDisplay(surfaceholder);
			mediaplayer.setDataSource(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.write(TAG, "设置url异常");
			handler.sendEmptyMessage(mediaplayererror);
			return;
		}
		LogUtils.write(TAG, "设置 mediaplayer prepareAsync");
		mediaplayer.prepareAsync();
	}

	public long getDuration() {
		if (mediaplayer != null && (isPause() || isPlaying())) {
			return mediaplayer.getDuration();
		} else {
			return 0;
		}
	}

	public long getCurrTime() {
		if (mediaplayer != null && (isPause() || isPlaying())) {
			return mediaplayer.getCurrentPosition();
		} else {
			return 0;
		}

	}

	public void play() {
		if (mediaplayer != null) {
			surfaceholder.setFixedSize(mediaplayer.getVideoWidth(),
					mediaplayer.getVideoHeight());
			mediaplayer.start();
			playing = true;
			pause = false;
		}

	}

	public void pause() {
		if (mediaplayer != null) {
			mediaplayer.pause();
			playing = false;
			pause = true;
		}

	}

	public void SeeKto(int msec) {
		if (mediaplayer != null) {
			pause();
			mediaplayer.seekTo(msec);
			play();
		}
	}

	public void stop() {
		if (mediaplayer != null) {
			LogUtils.write(TAG, "mediaplayer  stop");
			mediaplayer.stop();
			playing = false;
			pause = false;
		}

	}

	public void release() {
		if (mediaplayer != null) {
			LogUtils.write(TAG, "mediaplayer  release");
			mediaplayer.stop();
			mediaplayer.release();
			mediaplayer = null;
		}
	}

	class mediaplayerlistener implements OnPreparedListener, OnErrorListener,
			OnInfoListener, OnVideoSizeChangedListener,
			OnBufferingUpdateListener, OnSeekCompleteListener,
			OnCompletionListener {

		@Override
		public void onPrepared(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放onPrepared");
			handler.sendEmptyMessage(mediaplayerPrepared);
		}

		@Override
		public boolean onError(MediaPlayer arg0, int code, int arg2) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放onError " + code + " " + arg2);
			if (code == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
				handler.sendEmptyMessage(mediaplayerserverDied);
			}
			return true;
		}

		@Override
		public boolean onInfo(MediaPlayer arg0, int code, int arg2) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放onInfo " + code + " " + arg2);
			if (code == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
				handler.sendEmptyMessage(mediaplayerbufferedStart);
			} else if (code == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
				handler.sendEmptyMessage(mediaplayerbufferedEnd);
			}
			return true;
		}

		@Override
		public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放onVideoSizeChanged " + arg1 + " " + arg2);
			if (arg1 > 0 && arg2 > 0) {
				handler.sendEmptyMessage(mediaplayerstart);
			}

		}

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			// TODO Auto-generated method stub
			//LogUtils.write(TAG, "播放onBufferingUpdate " + percent);
		}

		@Override
		public void onSeekComplete(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放onSeekComplete ");
			handler.sendEmptyMessage(mediaplayerseekcomplete);
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放onCompletion ");
			handler.sendEmptyMessage(mediaplayercomplete);
		}

	}

	class surfaceholdercallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放surfaceChanged " + format + " " + width
					+ " " + height);

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "surfaceCreated");
			handler.sendEmptyMessage(surfaceCreated);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "surfaceDestroyed");
			release();
			ForceTvUtils.StopChan();
			handler.sendEmptyMessage(surfaceDestroyed);
		}

	}

}

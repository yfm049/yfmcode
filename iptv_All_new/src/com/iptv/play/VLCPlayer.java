package com.iptv.play;


import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.iptv.utils.ForceTvUtils;
import com.iptv.utils.LogUtils;

public class VLCPlayer implements Player,IVideoPlayer {

	private static String TAG = VLCPlayer.class.getName();
	
	private int vwidth,vheight;
	public boolean playing = false, pause = false;
	private int surfacesize=1;

	public boolean isPlaying() {
		return playing;
	}

	public boolean isPause() {
		return pause;
	}

	private Context context;
	private LibVLC mediaplayer = null;
	private SurfaceView playview;
	private SurfaceHolder surfaceholder;
	private surfaceholdercallback callback;
	private Handler handler;
	private EventHandler ehandler;

	public VLCPlayer(Context context, SurfaceView playview, Handler handler) {
		this.context = context;
		this.playview = playview;
		this.handler = handler;
		ehandler=EventHandler.getInstance();
		ehandler.addHandler(mVlcHandler);
		LogUtils.write(TAG, "创建 mediaplayer");
		surfaceholder = playview.getHolder();
		callback = new surfaceholdercallback();
		surfaceholder.addCallback(callback);
		surfaceholder.setFormat(PixelFormat.RGBA_8888);
	}
	
	private Handler mVlcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.getData() == null)
                return;
            System.out.println("event  "+msg.getData().getInt("event"));
            switch (msg.getData().getInt("event")) {
            
            case EventHandler.MediaPlayerTimeChanged:
            	handler.sendEmptyMessage(mediaplayerbufferedEnd);
                break;
            case EventHandler.MediaPlayerPositionChanged:
            	handler.sendEmptyMessage(mediaplayerbufferedEnd);
                break;
            case EventHandler.MediaPlayerPlaying:
            	handler.sendEmptyMessage(mediaplayerbufferedEnd);
                break;
            case EventHandler.MediaPlayerEndReached:
            	handler.sendEmptyMessage(mediaplayerseekcomplete);
                break;
            case EventHandler.MediaPlayerBuffering:
            	handler.sendEmptyMessage(mediaplayerbufferedStart);
                break;
            case EventHandler.MediaPlayerEncounteredError:
            	handler.sendEmptyMessage(mediaplayererror);
            	handler.sendEmptyMessage(mediaplayerserverDied);
            	break;
            }
            

        }
    };

	public void initmediaplayer() {
		LogUtils.write(TAG, "创建 mediaplayer");
		try {
			mediaplayer=LibVLC.getInstance();
			mediaplayer.init(context);
			mediaplayer.eventVideoPlayerActivityCreated(true);
			mediaplayer.setNetworkCaching(3000);
			mediaplayer.setFrameSkip(true);
			mediaplayer.setHardwareAcceleration(LibVLC.HW_ACCELERATION_AUTOMATIC);
		} catch (LibVlcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPlayUrl(String url) {
		LogUtils.write(TAG, "开始播放url "+url);
		mediaplayer.playMRL(url);
		playing = true;
		pause = false;
	}

	public long getDuration() {
		if (mediaplayer != null) {
			return mediaplayer.getLength();
		} else {
			return 0;
		}
	}

	public long getCurrTime() {
		if (mediaplayer != null) {
			return mediaplayer.getTime();
		} else {
			return 0;
		}
	}

	public void play() {
		if (mediaplayer != null) {
			mediaplayer.play();
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
		if (mediaplayer != null&&mediaplayer.isSeekable()) {
			mediaplayer.setTime(msec);
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
		mediaplayer.detachSurface();
		mediaplayer.eventVideoPlayerActivityCreated(false);
		ehandler.removeHandler(mVlcHandler);
		mediaplayer.destroy();
	}
	
	private Handler surfacehandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==surfacesize){
				surfaceholder.setFixedSize(vwidth, vheight);
				playview.invalidate();
			}
		}
		
	};

	

	class surfaceholdercallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "播放surfaceChanged " + format + " " + width+ " " + height);
			if (width > 0 && height > 0) {
				handler.sendEmptyMessage(mediaplayerstart);
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			LogUtils.write(TAG, "surfaceCreated");
			initmediaplayer();
			mediaplayer.attachSurface(holder.getSurface(), VLCPlayer.this);
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

	@Override
	public void setSurfaceSize(int width, int height, int visible_width,
			int visible_height, int sar_num, int sar_den) {
		// TODO Auto-generated method stub
		LogUtils.write(TAG, "setSurfaceSize " + width
				+ " " + height+" " + visible_width
				+ " " + visible_height+" "+sar_num+" "+sar_den);
		if(vwidth!=width||vheight!=height){
			vwidth=width;
			vheight=height;
			surfacehandler.sendEmptyMessage(surfacesize);
		}
		
		
	}

	@Override
	public void eventHardwareAccelerationError() {
		// TODO Auto-generated method stub
		
	}

}

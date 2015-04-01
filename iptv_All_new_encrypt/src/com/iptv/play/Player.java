package com.iptv.play;

public interface Player {

	public static int surfaceCreated = 100, mediaplayerPrepared = 101,
			mediaplayererror = 102, mediaplayerstart = 103,
			mediaplayerbufferedStart = 104, mediaplayerbufferedEnd = 105, mediaplayerserverDied = 106,mediaplayerseekcomplete=107,surfaceDestroyed=108,mediaplayercomplete=109;

	public boolean isPlaying();
	public boolean isPause();
	public void setPlayUrl(String url);
	public long getDuration();
	public long getCurrTime();
	public void play();
	public void pause();
	public void SeeKto(int msec);
	public void stop();
	public void release();
}

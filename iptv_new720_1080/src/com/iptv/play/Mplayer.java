package com.iptv.play;

public interface Mplayer {

	public boolean isPlaying();
	public boolean isPause();
	public void setPlayUrl(String url);
	public long gettotalTime();
	public long getCurrTime();
	public void play();
	public void pause();
	public void SeeKto(int msec);
	public void stop();
	public void release();
	
}

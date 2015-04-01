package com.iptv.pojo;

public class Notice {

	private String riqi;
	public String getRiqi() {
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String time;
	private String name;
	private boolean isplay=false;
	public boolean isIsplay() {
		return isplay;
	}
	public void setIsplay(boolean isplay) {
		this.isplay = isplay;
	}
}

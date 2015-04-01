package com.android.model;

public class Record {

	private String starttime;
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	private String endtime;
	private String file;
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
}

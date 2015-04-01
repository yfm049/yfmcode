package com.iptv.pojo;

public class Notice {

	private int id;
	private String rqDate;
	public String getRqDate() {
		return rqDate;
	}
	public void setRqDate(String rqDate) {
		this.rqDate = rqDate;
	}
	private String start;
	private String end;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIsplay() {
		return isplay;
	}
	public void setIsplay(boolean isplay) {
		this.isplay = isplay;
	}
	private boolean isplay=false;
}

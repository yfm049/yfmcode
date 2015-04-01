package com.iptv.pojo;

public class Appinfo {

	private String msg;
	private int vercode=-1;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getVercode() {
		return vercode;
	}
	public void setVercode(int vercode) {
		this.vercode = vercode;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String url;
}

package com.pro.pojo;

public class Info {

	private int id;
	private String pinyin;
	private String jieshi;
	public String getJieshi() {
		return jieshi;
	}
	public void setJieshi(String jieshi) {
		this.jieshi = jieshi;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getWenzi() {
		return wenzi;
	}
	public void setWenzi(String wenzi) {
		this.wenzi = wenzi;
	}
	private String wenzi;
}

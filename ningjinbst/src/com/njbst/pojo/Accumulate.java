package com.njbst.pojo;

import java.io.Serializable;

public class Accumulate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getZancount() {
		return zancount;
	}
	public void setZancount(int zancount) {
		this.zancount = zancount;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	private int zancount;
	private String imgurl;

	private String linkurl;
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
}

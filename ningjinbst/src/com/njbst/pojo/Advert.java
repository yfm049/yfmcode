package com.njbst.pojo;

import java.io.Serializable;

public class Advert implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String adname;
	public String getAdname() {
		return adname;
	}
	public void setAdname(String adname) {
		this.adname = adname;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	private String url;
	private String linkurl;
}

package com.njbst.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	private String content;
	private String imageurl;
	private String linkurl;

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	private List<NewInfo> newinfos=new ArrayList<NewInfo>();

	public List<NewInfo> getNewinfos() {
		return newinfos;
	}
}

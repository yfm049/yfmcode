package com.pro.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("file")
public class VideoFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public int count;
	public int end;
	public String url;
	public String img;
	
	public String Img;
	public String description;
	
	public List<link> links=new ArrayList<link>();
	
	public String taskid;
	public String domain;
	public String Server;
	public String bigimg;
	public String v_type;
	public String director;
	public String actor;
	public String country;
	public String language;
	public String releasedate;
	public String rating;
}

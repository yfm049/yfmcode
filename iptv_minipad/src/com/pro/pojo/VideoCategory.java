package com.pro.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("category")
public class VideoCategory {
	@XStreamAsAttribute
	public String name;

	public VideoFile file=new VideoFile();
}

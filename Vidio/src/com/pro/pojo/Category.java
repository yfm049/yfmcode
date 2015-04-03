package com.pro.pojo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("category")
public class Category {
	
	public String nextpage;

	public String page;
	
	@XStreamImplicit(itemFieldName="file")
	public List<VideoFile> files=new ArrayList<VideoFile>();
}

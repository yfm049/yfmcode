package com.pro.pojo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("category")
public class VideoType {

	@XStreamImplicit(itemFieldName="file")
	public List<Type> types=new ArrayList<Type>();
}

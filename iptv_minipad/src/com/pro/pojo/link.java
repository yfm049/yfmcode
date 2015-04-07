package com.pro.pojo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("link")
public class link implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String vodid;
	public String servertype;
	public String filmname;
	public String filmid;
	public String format;
	public String type;
}

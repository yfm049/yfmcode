package com.iptv.pojo;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 节目列表
 * @author lenovo
 *
 */
public class PrgItem implements Serializable{

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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	private String date;
	private String time;
	private String p2purl;
	private String hotlink;
	public String getP2purl() {
		return p2purl;
	}
	public void setP2purl(String p2purl) {
		this.p2purl = p2purl;
	}
	public String getHotlink() {
		return hotlink;
	}
	public void setHotlink(String hotlink) {
		this.hotlink = hotlink;
	}
}

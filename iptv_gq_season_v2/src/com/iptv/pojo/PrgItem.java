package com.iptv.pojo;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

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
	private String videoId;
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
	public String getparam(String userid){
		String param="http://127.0.0.1:9906/cmd.xml?cmd=switch_chan&";
		try {
			URL url=new URL(p2purl.replace("p2p", "http"));
			String server=url.getAuthority();
			videoId = url.getPath();
			videoId = videoId.subSequence(1, videoId.length()-3).toString();
			param+="id="+videoId;
			param+="&server="+server;
			param+="&link="+hotlink;
			param+="&userid="+userid;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return param;
	}
	public String getplayurl(){
		String purl="http://127.0.0.1:9906/"+videoId+".ts";
		return purl;
	}
}

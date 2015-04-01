package com.iptv.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class TvInfo {

	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String name;
	private String httpurl;
	private String hotlink;
	private String videoId;
	private String flag;

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHttpurl() {
		return httpurl;
	}
	public void setHttpurl(String httpurl) {
		this.httpurl = httpurl;
	}
	public String getHotlink() {
		return hotlink;
	}
	public void setHotlink(String hotlink) {
		this.hotlink = hotlink;
	}
	public String getparam(){
		String param="http://127.0.0.1:9898/cmd.xml?cmd=switch_chan&";
		try {
			URL url=new URL(httpurl.replace("p2p", "http"));
			String server=url.getAuthority();
			videoId = url.getPath();
			videoId = videoId.subSequence(1, videoId.length()-3).toString();
			param+="id="+videoId;
			param+="&server="+server;
			param+="&link="+hotlink;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return param;
	}
	public String getplayurl(){
		String purl="http://127.0.0.1:9898/"+videoId+".ts";
		return purl;
	}
}

package com.iptv.pojo;

import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;

public class TvInfo {

	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String logourl;
	public String getLogourl() {
		return logourl;
	}
	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}
	private String name;
	private String httpurl;
	private String hotlink;
	private String videoId;
	private String flag;
	private Bitmap logo=null;
	private String epg;

	public String getEpg() {
		return epg;
	}
	public void setEpg(String epg) {
		this.epg = epg;
	}
	public Bitmap getLogo() {
		return logo;
	}
	public void setLogo(Bitmap logo) {
		this.logo = logo;
	}
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
	public String getparam(String userid){
		String param="http://127.0.0.1:9898/cmd.xml?cmd=switch_chan&";
		try {
			URL url=new URL(httpurl.replace("p2p", "http"));
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
		String purl="http://127.0.0.1:9898/"+videoId+".ts";
		return purl;
	}
}

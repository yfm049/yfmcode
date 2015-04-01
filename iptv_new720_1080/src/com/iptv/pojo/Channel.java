package com.iptv.pojo;

public class Channel {

	private int id;
	private String name;
	private String httpurl;
	private String hotlink;
	private int isflag;
	private String logo;
	private String epg;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getIsflag() {
		return isflag;
	}

	public void setIsflag(int isflag) {
		this.isflag = isflag;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getEpg() {
		return epg;
	}

	public void setEpg(String epg) {
		this.epg = epg;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof Channel){
			Channel co=(Channel)o;
			if(co.getId()==this.id&&co.getName().equals(this.name)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	
}

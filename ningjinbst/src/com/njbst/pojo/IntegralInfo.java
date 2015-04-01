package com.njbst.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntegralInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String brand;
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	private String desc;
	private int integral;
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	private String imageurl;
	private String linkurl;
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	
	private List<IntegralInfo> integralinfos=new ArrayList<IntegralInfo>();
	public List<IntegralInfo> getIntegralinfos() {
		return integralinfos;
	}

}

package com.njbst.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaleInfo implements Serializable {

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
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getZekou() {
		return zekou;
	}
	public void setZekou(String zekou) {
		this.zekou = zekou;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	private String product;
	private String desc;
	private String zekou;
	private String imageurl;
	private String linkurl;
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	
	private List<SaleInfo> saleinfos=new ArrayList<SaleInfo>();
	public List<SaleInfo> getSaleinfos() {
		return saleinfos;
	}

}

package com.njbst.pojo;

public class MyInfo {

	private String name="";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	private String sex;
	private String age;
	private String address;
	private String phone;
	
	private String image_url;
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	private boolean isflag;
	public boolean isIsflag() {
		return isflag;
	}
	public void setIsflag(boolean isflag) {
		this.isflag = isflag;
	}
}

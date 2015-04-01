package com.android.model;

import java.io.Serializable;

public class Client implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String clientname;
	public String getClientname() {
		return clientname;
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
	}
	private int userid;
	private String clientid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	public String getDeviceimei() {
		return deviceimei;
	}
	public void setDeviceimei(String deviceimei) {
		this.deviceimei = deviceimei;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	private String deviceimei;
	private String phone;
	private String endtime;
	private int uploadtime;
	public int getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(int uploadtime) {
		this.uploadtime = uploadtime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private String email;
	private int state;
}

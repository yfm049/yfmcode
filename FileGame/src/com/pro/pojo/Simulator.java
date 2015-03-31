package com.pro.pojo;

import java.io.File;
import java.io.Serializable;

import android.util.Log;

public class Simulator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logo;
	private String name;
	private String packagename;
	private String settingname;
	private String activityname;
	private String type;
	private String suffixs;
	
	public String getSettingname() {
		return settingname;
	}
	public void setSettingname(String settingname) {
		this.settingname = settingname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getActivityname() {
		return activityname;
	}
	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSuffixs() {
		return suffixs;
	}
	public void setSuffixs(String suffixs) {
		this.suffixs = suffixs;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public boolean isGameFile(File file){
		if(suffixs!=null){
			String[] suff=suffixs.split(",");
			for(String sf:suff){
				if(file.getName().endsWith(sf)){
					return true;
				}
			}
		}
		return false;
	}
	
}

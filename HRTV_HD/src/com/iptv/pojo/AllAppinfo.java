package com.iptv.pojo;

import android.graphics.drawable.Drawable;

public class AllAppinfo {

	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String packagename;
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	private Drawable icon;
	private int iconid;
	public int getIconid() {
		return iconid;
	}
	public void setIconid(int iconid) {
		this.iconid = iconid;
	}
}

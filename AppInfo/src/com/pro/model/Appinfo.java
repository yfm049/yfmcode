package com.pro.model;

import java.io.File;
import java.io.Serializable;
//»Ìº˛ Ù–‘
public class Appinfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int fid;
	private String filename;
	private int version;
	private String pkname;
	private int download;//œ¬‘ÿ◊¥Ã¨
	private int downcount;
	public int getDowncount() {
		return downcount;
	}
	public void setDowncount(int downcount) {
		this.downcount = downcount;
	}
	private File file;
	private int zt;
	public int getZt() {
		return zt;
	}
	public void setZt(int zt) {
		this.zt = zt;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public int getDownload() {
		return download;
	}
	public void setDownload(int download) {
		this.download = download;
	}
	public String getPkname() {
		return pkname;
	}
	public void setPkname(String pkname) {
		this.pkname = pkname;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	private String state;
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
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
}

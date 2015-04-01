package com.njbst.pojo;

import java.io.Serializable;

public class CityInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String name;
	
	private String pname;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNameLetters() {
		return nameLetters;
	}

	public void setNameLetters(String nameLetters) {
		this.nameLetters = nameLetters;
	}

	private String code;
	
	private String nameLetters;
}

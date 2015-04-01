package com.pro.rfidw;

public class ChangeKeys {


	private String CurPassWordTypeID;
	private String CurWritePassword;
	public String getCurPassWordTypeID() {
		return CurPassWordTypeID;
	}
	public void setCurPassWordTypeID(String curPassWordTypeID) {
		CurPassWordTypeID = curPassWordTypeID;
	}
	public String getCurWritePassword() {
		return CurWritePassword;
	}
	public void setCurWritePassword(String curWritePassword) {
		CurWritePassword = curWritePassword;
	}
	public int getSecAddr() {
		return SecAddr;
	}
	public void setSecAddr(int secAddr) {
		SecAddr = secAddr;
	}
	public String getNewkeyA() {
		return NewkeyA;
	}
	public void setNewkeyA(String newkeyA) {
		NewkeyA = newkeyA;
	}
	public String getNewkeyB() {
		return NewkeyB;
	}
	public void setNewkeyB(String newkeyB) {
		NewkeyB = newkeyB;
	}
	public String getNewControlKey() {
		return NewControlKey;
	}
	public void setNewControlKey(String newControlKey) {
		NewControlKey = newControlKey;
	}
	private int SecAddr;
	private String NewkeyA;
	private String NewkeyB;
	private String NewControlKey;

}

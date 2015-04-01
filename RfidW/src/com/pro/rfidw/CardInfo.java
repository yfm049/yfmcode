package com.pro.rfidw;

public class CardInfo {

	

	private int ContentTypeID;
	public int getContentTypeID() {
		return ContentTypeID;
	}
	public void setContentTypeID(int contentTypeID) {
		ContentTypeID = contentTypeID;
	}
	private String  CurPassWordTypeID;
	public String getCurPassWordTypeID() {
		return CurPassWordTypeID;
	}
	public void setCurPassWordTypeID(String curPassWordTypeID) {
		CurPassWordTypeID = curPassWordTypeID;
	}
	public int getBlockAddr() {
		return BlockAddr;
	}
	public void setBlockAddr(int blockAddr) {
		BlockAddr = blockAddr;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	private String CurWritePassword;
	public String getCurWritePassword() {
		return CurWritePassword;
	}
	public void setCurWritePassword(String curWritePassword) {
		CurWritePassword = curWritePassword;
	}
	private int BlockAddr;
	private String Content;
}

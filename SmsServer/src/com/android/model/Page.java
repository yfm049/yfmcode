package com.android.model;

public class Page {

	private int cpage;
	public int getCpage() {
		return cpage;
	}
	public void setCpage(int cpage) {
		this.cpage = cpage;
	}
	public int getTpage() {
		return tpage;
	}
	public void setTpage(int tpage) {
		this.tpage = tpage;
	}
	private int tpage;
	public boolean hasnextpage(){
		if(cpage+1>tpage){
			return false;
		}else{
			return true;
		}
	}
	public int getNextPage(){
		if(hasnextpage()){
			return cpage+1;
		}else{
			return cpage;
		}
	}
}

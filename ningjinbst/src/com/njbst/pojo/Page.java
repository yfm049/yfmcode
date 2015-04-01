package com.njbst.pojo;

public class Page {

	private int totalpage=1;
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	public int getCurrpage() {
		return currpage;
	}
	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	private int currpage=1;
	private int size=30;
	
	public boolean hasNext(){
		if(currpage>=totalpage){
			return false;
		}else{
			return true;
		}
	}
	public int getNextPage(){
		return currpage+1;
	}
	public int getFirstPage(){
		return 1;
	}
}

package com.iptv.utils;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String ret;
	private int currlivetv=0;
	public int getCurrlivetv() {
		return currlivetv;
	}
	public void setCurrlivetv(int currlivetv) {
		this.currlivetv = currlivetv;
	}
	public int getCurrtv() {
		return currtv;
	}
	public void setCurrtv(int currtv) {
		this.currtv = currtv;
	}
	private int currtv=0;
	private List<LiveTV> livetvlist=new ArrayList<LiveTV>();
	private List<TvInfo> tvlist=new ArrayList<TvInfo>();
	private List<TvInfo> temp=new ArrayList<TvInfo>();
	public List<TvInfo> getTemp() {
		return temp;
	}
	public void setTemp(List<TvInfo> temp) {
		this.temp = temp;
	}
	public List<TvInfo> getTvlist() {
		return tvlist;
	}
	public void setTvlist(List<TvInfo> tvlist) {
		this.tvlist = tvlist;
	}
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public List<LiveTV> getLivetvlist() {
		return livetvlist;
	}
	public void setLivetvlist(List<LiveTV> livetvlist) {
		this.livetvlist = livetvlist;
	}
	public String gettitle(){
		return livetvlist.get(currlivetv).getName();
	}
	public LiveTV right(){
		if(livetvlist!=null&&livetvlist.size()>0){
			if((currlivetv+1)<livetvlist.size()){
				currlivetv=currlivetv+1;
			}else{
				currlivetv=0;
			}
			return livetvlist.get(currlivetv);
		}
		return null;
	}
	public LiveTV left(){
		if(livetvlist!=null&&livetvlist.size()>0){
			if((currlivetv-1)>=0){
				currlivetv=currlivetv-1;
			}else{
				currlivetv=(livetvlist.size()-1);
			}
			return livetvlist.get(currlivetv);
		}
		return null;
	}
	public TvInfo up(){
		if(temp!=null&&temp.size()>0){
			if((currtv+1)<temp.size()){
				currtv=currtv+1;
			}else{
				currtv=0;
			}
			return temp.get(currtv);
		}
		return null;
	}
	public TvInfo down(){
		if(temp!=null&&temp.size()>0){
			if((currtv-1)>=0){
				currtv=currtv-1;
			}else{
				currtv=temp.size()-1;
			}
			return temp.get(currtv);
		}
		return null;
	}
	
	public TvInfo getdef(){
		temp.clear();
		temp.addAll(tvlist);
		if(temp!=null&&temp.size()>0){
			return temp.get(currtv);
		}
		return null;
	}
}

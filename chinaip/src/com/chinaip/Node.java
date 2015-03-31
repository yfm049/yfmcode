package com.chinaip;

public class Node {

	private String name;
	private boolean isload=false;
	public boolean isIsload() {
		return isload;
	}
	public void setIsload(boolean isload) {
		this.isload = isload;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIPCCode() {
		return IPCCode;
	}
	public void setIPCCode(String iPCCode) {
		IPCCode = iPCCode;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getRecursedNodes() {
		return recursedNodes;
	}
	public void setRecursedNodes(String recursedNodes) {
		this.recursedNodes = recursedNodes;
	}
	private String IPCCode;
	private String level;
	private String recursedNodes;
	public void SetValue(String value){
		if(value!=null){
			String[] p=value.split("_");
			IPCCode=p[0];
			level=p[1];
			recursedNodes=p[2];
		}
	}
	public String toString(){
		return name;
	}
}

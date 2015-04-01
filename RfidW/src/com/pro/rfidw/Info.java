package com.pro.rfidw;

import java.util.ArrayList;
import java.util.List;

public class Info {

	private List<CardInfo> lci=new ArrayList<CardInfo>();
	public List<CardInfo> getLci() {
		return lci;
	}
	public void setLci(List<CardInfo> lci) {
		this.lci = lci;
	}
	public List<ChangeKeys> getLck() {
		return lck;
	}
	public void setLck(List<ChangeKeys> lck) {
		this.lck = lck;
	}
	private List<ChangeKeys> lck=new ArrayList<ChangeKeys>();
}

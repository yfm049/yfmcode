package com.android.page;

public class Page {

	private int cpage = 1;
	private int tsize = 0;
	private int psize = 20;
	private int tpagenum = 0;
	private boolean isasc = true;
	private String paixu = "id";

	public int getTpagenum() {
		tpagenum = tsize % psize == 0 ? tsize / psize : (int) Math.floor(tsize
				/ psize) + 1;
		return tpagenum;
	}

	public int getCpage() {
		return cpage;
	}

	public void setCpage(int cpage) {
		this.cpage = cpage;
	}

	public int getTsize() {
		return tsize;
	}

	public void setTsize(int tsize) {
		this.tsize = tsize;
	}

	public int getPsize() {
		return psize;
	}

	public void setPsize(int psize) {
		this.psize = psize;
	}

	public boolean isIsasc() {
		return isasc;
	}

	public void setIsasc(boolean isasc) {
		this.isasc = isasc;
	}

	public String getPaixu() {
		return paixu;
	}

	public void setPaixu(String paixu) {
		this.paixu = paixu;
	}
}

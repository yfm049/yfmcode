package com.iptv.pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iptv.adapter.AsyncImageLoader;

import android.util.Log;

public class BackData {

	public static String currtime=new SimpleDateFormat("yyyy_mm-dd").format(new Date());
	public static int cday=5;
	private AsyncImageLoader imageLoader=new AsyncImageLoader();
	public AsyncImageLoader getImageLoader() {
		return imageLoader;
	}
	// 回拨节目
	private List<PrgItem> prglist = new ArrayList<PrgItem>();
	// 回拨频道表
	private List<Film> filmlist = new ArrayList<Film>();

	public List<Film> getFilmlist() {
		return filmlist;
	}

	public void setFilmlist(List<Film> filmlist) {
		this.filmlist = filmlist;
	}

	public List<PrgItem> getPrglist() {
		return prglist;
	}

	public void setPrglist(List<PrgItem> prglist) {
		this.prglist = prglist;
	}

	

}

package com.iptv.pojo;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class User {

	// 当前分类id
	private int currliveid = 0;
	// 当前显示分类id
	private int templiveid = 0;

	public int getCurrliveid() {
		return currliveid;
	}

	public void setCurrliveid(int currliveid) {
		this.currliveid = currliveid;
	}

	public int getTempliveid() {
		return templiveid;
	}

	public void setTempliveid(int templiveid) {
		this.templiveid = templiveid;
	}

	public int getCurrtvid() {
		return currtvid;
	}

	public void setCurrtvid(int currtvid) {
		this.currtvid = currtvid;
	}

	public List<TvInfo> getTemplist() {
		return templist;
	}

	public void setTemplist(List<TvInfo> templist) {
		this.templist = templist;
	}

	// 当前播放频道id;
	private int currtvid = 0;

	// 直播显示频道列表
	private List<TvInfo> templist = new ArrayList<TvInfo>();
	// 直播分类列表
	private List<LiveTV> livetvlist = new ArrayList<LiveTV>();
	// 直播频道列表
	private List<TvInfo> tvlist = new ArrayList<TvInfo>();

	public List<TvInfo> getTvlist() {
		return tvlist;
	}

	public void setTvlist(List<TvInfo> tvlist) {
		this.tvlist = tvlist;
	}

	public List<LiveTV> getLivetvlist() {
		return livetvlist;
	}

	public void setLivetvlist(List<LiveTV> livetvlist) {
		this.livetvlist = livetvlist;
	}

	public String gettitle() {
		if (livetvlist.size() <= 0) {
			return "";
		}
		return livetvlist.get(currliveid).getName();
	}

	public LiveTV right() {
		Log.i("tvinfo", livetvlist.size() + "个分组");
		if (livetvlist != null && livetvlist.size() > 0) {
			if ((templiveid + 1) < livetvlist.size()) {
				templiveid = templiveid + 1;
			} else {
				templiveid = 0;
			}
			return livetvlist.get(templiveid);
		}
		return null;
	}

	public LiveTV left() {
		if (livetvlist != null && livetvlist.size() > 0) {
			if ((templiveid - 1) >= 0) {
				templiveid = templiveid - 1;
			} else {
				templiveid = (livetvlist.size() - 1);
			}
			return livetvlist.get(templiveid);
		}
		return null;
	}

	public TvInfo up() {
		if (tvlist != null && tvlist.size() > 0) {
			if ((currtvid + 1) < tvlist.size()) {
				currtvid = currtvid + 1;
			} else {
				currtvid = 0;
			}
			return tvlist.get(currtvid);
		}
		return null;
	}

	public TvInfo down() {
		if (tvlist != null && tvlist.size() > 0) {
			if ((currtvid - 1) >= 0) {
				currtvid = currtvid - 1;
			} else {
				currtvid = tvlist.size() - 1;
			}
			return tvlist.get(currtvid);
		}
		return null;
	}

	public TvInfo playtv(int id) {
		currliveid = templiveid;
		currtvid = id;
		tvlist.clear();
		tvlist.addAll(templist);
		if(tvlist.size()>0){
			return tvlist.get(currtvid);
		}
		return null;
	}

	public LiveTV getcurrLivetv() {
		if (livetvlist.size() > 0) {
			if (currliveid < livetvlist.size()) {
				return livetvlist.get(currliveid);
			} else {
				return livetvlist.get(0);
			}

		}
		return null;
	}
}

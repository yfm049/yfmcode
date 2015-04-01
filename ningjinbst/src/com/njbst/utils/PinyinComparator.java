package com.njbst.utils;

import java.util.Comparator;

import com.njbst.pojo.CityInfo;

public class PinyinComparator implements Comparator<CityInfo> {

	public int compare(CityInfo o1, CityInfo o2) {
		if (o1.getNameLetters().equals("@")
				|| o2.getNameLetters().equals("#")) {
			return -1;
		} else if (o1.getNameLetters().equals("#")
				|| o2.getNameLetters().equals("@")) {
			return 1;
		} else {
			return o1.getNameLetters().compareTo(o2.getNameLetters());
		}
	}

}

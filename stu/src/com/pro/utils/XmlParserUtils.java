package com.pro.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class XmlParserUtils {

	public static List<Info> ParseXmlToInfo(InputStream in) {
		List<Info> linfo = new ArrayList<Info>();
		try {
			XmlPullParser parse = Xml.newPullParser();
			parse.setInput(in, "UTF-8");
			int eventtype = parse.getEventType();
			Info info = null;
			while (eventtype != XmlPullParser.END_DOCUMENT) {
				switch (eventtype) {
				case XmlPullParser.START_TAG:
					String name = parse.getName();
					System.out.println("start "+name);
					if ("PareCard".equals(name)) {
						info = new Info();
						info.setPareCard(parse.nextText());
					}
					if ("ChildName".equals(name)) {
						info.setChildName(parse.nextText());
					}
					if ("ClassName".equals(name)) {
						info.setClassName(parse.nextText());
					}
					if ("ParePhone".equals(name)) {
						info.setParePhone(parse.nextText());
					}
					if ("ChildBirth".equals(name)) {
						info.setChildBirth(parse.nextText());
					}
					if ("PareName".equals(name)) {
						info.setPareName(parse.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					System.out.println("end "+parse.getName());
					if ("PareName".equals(parse.getName())) {
						linfo.add(info);
						info = null;
						System.out.println("------");
					}
					break;
				}
				eventtype = parse.next();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		return linfo;
	}
}

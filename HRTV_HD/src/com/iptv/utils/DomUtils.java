package com.iptv.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.iptv.pojo.Appinfo;
import com.iptv.pojo.Channel;
import com.iptv.pojo.Notice;
import com.iptv.pojo.PrgItem;

public class DomUtils {

	public static boolean ParseLoginXml(String xml, SqliteUtils su) {
		if (xml != null && su != null) {
			su.init();
			SQLiteDatabase db = su.getWritableDatabase();
			db.beginTransaction();
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					if ("ret".equals(ele.getName())) {
						return false;
					} else if ("Category".equals(ele.getName())) {
						String gid = ele.attributeValue("id");
						String name = ele.attributeValue("name");
						Iterator<Element> files = ele.elementIterator();
						while (files != null && files.hasNext()) {
							Element file = files.next();
							String tvid = file.elementTextTrim("id");
							String tname = file.elementTextTrim("name");
							String httpurl = file.elementTextTrim("httpurl");
							String hotlink = file.elementTextTrim("hotlink");
							String isflag = file.elementTextTrim("isflag");
							String logo = file.elementTextTrim("logo");
							String epg = file.elementTextTrim("epg");
							su.savetvdate(db, gid, name, tvid, tname, httpurl,
									hotlink, isflag, logo, epg);
						}
					}
				}
				db.setTransactionSuccessful();
				return true;
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
		return false;
	}

	public static List<Notice> ParseNoticeXml(String xml) {
		List<Notice> ln = new ArrayList<Notice>();
		if (xml != null) {
			try {
				Document doc = DocumentHelper.parseText(xml.trim());
				Element root = doc.getRootElement();
				List<Element> liste = root.elements();
				for (Element et : liste) {
					if ("programme".equals(et.getName())) {
						String rq = et.elementTextTrim("date");
						String start = et.elementTextTrim("start");
						String stop = et.elementTextTrim("stop");
						String title = et.elementTextTrim("title");
						Notice notice = new Notice();
						notice.setRqDate(rq);
						notice.setStart(start);
						notice.setEnd(stop);
						notice.setName(title);
						ln.add(notice);
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ln;
	}

	public static boolean ParseForceTvXml(String xml) {
		if (xml != null) {
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Element result = root.element("result");
				String ret = result.attributeValue("ret");
				if ("0".equals(ret)) {
					return true;
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	// 获取回放频道列表
	public static List<Channel> ParseBackChannelXml(String xml) {
		List<Channel> filmlist = new ArrayList<Channel>();
		if (xml != null && !"".equals(xml)) {
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Iterator<Element> ite = root.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					if (ele.getName().startsWith("film")) {
						Channel film = new Channel();
						film.setId(Integer.parseInt(ele.elementTextTrim("id")));
						film.setName(ele.elementTextTrim("name"));
						film.setHttpurl(ele.elementTextTrim("url"));
						film.setLogo(ele.elementTextTrim("logo"));
						filmlist.add(film);
					} else if ("rq".equals(ele.getName())) {
						ComUtils.currdata = ele.getText().trim();
					} else if ("day".equals(ele.getName())) {
						ComUtils.totalday = Integer.parseInt(ele.getText()
								.trim());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filmlist;
	}

	// 获取回放节目列表
	public static List<PrgItem> ParsePrgItem(String xml) {
		List<PrgItem> prglist = new ArrayList<PrgItem>();
		if (xml != null && !"".equals(xml)) {
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				Element eitem = root.element("item");
				Iterator<Element> ite = eitem.elementIterator();
				while (ite.hasNext()) {
					Element ele = ite.next();
					PrgItem item = new PrgItem();
					if (ele != null && "prgItem".equals(ele.getName())) {
						item.setName(ele.elementTextTrim("name"));
						item.setDate(ele.elementTextTrim("date"));
						item.setTime(ele.elementTextTrim("time"));
						item.setP2purl(ele.elementTextTrim("url"));
						item.setHotlink(ele.elementTextTrim("hotlink"));
						prglist.add(item);
					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return prglist;
	}

	public static Appinfo parseRemoteapp(String xml) {
		Appinfo info = new Appinfo();
		if (xml != null && !"".equals(xml)) {
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				info.setMsg(root.elementText("message"));
				info.setVercode(Integer.parseInt(root.elementText("vercode")));
				info.setUrl(root.elementText("url"));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return info;
	}
	public static String getUserInfo(String xml){
		String yxsj="";
		if (xml != null && !"".equals(xml)) {
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				yxsj=root.elementText("yxsj");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return yxsj;
	}
	public static String ParseGongGaoXml(String xml){
		if(xml!=null && !"".equals(xml)){
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				return root.elementText("message");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	public static String ParseLogoXml(String xml){
		if(xml!=null && !"".equals(xml)){
			try {
				Document doc = DocumentHelper.parseText(xml);
				Element root = doc.getRootElement();
				return root.elementText("url");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}

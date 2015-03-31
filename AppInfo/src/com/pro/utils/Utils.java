package com.pro.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pro.model.Appinfo;
import com.pro.model.FenLei;
import com.pro.net.HttpUtils;

public class Utils {
	//数据转化封装
	public static List<FenLei> getallFenlei(String html){
		List<FenLei> lf=new ArrayList<FenLei>();
		try {
			JSONArray ja=new JSONArray(html);
			if(ja!=null&&ja.length()>0){
				for(int i=0;i<ja.length();i++){
					JSONObject  jo=ja.getJSONObject(i);
					FenLei fl=new FenLei();
					fl.setId(jo.getInt("id"));
					fl.setName(jo.getString("name"));
					lf.add(fl);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lf;
	}
	//数据转化封装
	public static List<Appinfo> getallApp(String html){
		List<Appinfo> lf=new ArrayList<Appinfo>();
		try {
			JSONArray ja=new JSONArray(html);
			if(ja!=null&&ja.length()>0){
				for(int i=0;i<ja.length();i++){
					JSONObject  jo=ja.getJSONObject(i);
					Appinfo fl=new Appinfo();
					fl.setId(jo.getInt("id"));
					fl.setName(jo.getString("name"));
					fl.setFid(jo.getInt("fid"));
					fl.setFilename(jo.getString("filename"));
					fl.setVersion(jo.getInt("version"));
					fl.setPkname(jo.getString("pkname"));
					fl.setDowncount(jo.getInt("downcount"));
					lf.add(fl);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lf;
	}
	//更新下载数量
	public static void updatecount(String uri,Appinfo info){
		HttpUtils.HttpGet(uri+"?id="+info.getId());
	}
}

package com.pro.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpUtils {

	private DefaultHttpClient hc=new DefaultHttpClient();
	private String url="http://192.168.123.1:8080/Demo/servlet/Data";
	public List<Data> initdate(){
		List<Data> ls=new ArrayList<Data>();
		try {
			
			String initurl=url+"?action=init";
			String json=getDate(initurl);
			System.out.println(json);
			JSONArray ja=new JSONArray(json);
			if(ls!=null){
				for(int i=0;i<ja.length();i++){
					Data data=new Data();
					JSONObject jo=ja.getJSONObject(i);
					String msg=jo.getString("msg");
					data.setMsg(msg);
					data.setId(jo.getInt("id"));
					ls.add(data);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls;
	}
	public List<Data> Refresh(int id){
		List<Data> ls=new ArrayList<Data>();
		try {
			
			String initurl=url+"?action=refresh&id="+id;
			String json=getDate(initurl);
			System.out.println(json);
			JSONArray ja=new JSONArray(json);
			if(ls!=null){
				for(int i=0;i<ja.length();i++){
					Data data=new Data();
					JSONObject jo=ja.getJSONObject(i);
					String msg=jo.getString("msg");
					data.setId(jo.getInt("id"));
					data.setMsg(msg);
					ls.add(data);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls;
	}
	public List<Data> LoadMore(int id){
		List<Data> ls=new ArrayList<Data>();
		try {
			
			String initurl=url+"?action=more&id="+id;
			String json=getDate(initurl);
			System.out.println(json);
			JSONArray ja=new JSONArray(json);
			if(ls!=null){
				for(int i=0;i<ja.length();i++){
					Data data=new Data();
					JSONObject jo=ja.getJSONObject(i);
					String msg=jo.getString("msg");
					data.setId(jo.getInt("id"));
					data.setMsg(msg);
					ls.add(data);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls;
	}
	public String getDate(String url){
		try {
			HttpGet hg=new HttpGet(url);
			HttpResponse hr=hc.execute(hg);
			System.out.println("Ö´ÐÐ²éÑ¯");
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				return EntityUtils.toString(hr.getEntity());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	}
}

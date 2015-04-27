package com.iptv.pojo;

import com.wys.iptvgo.YsHttpClient;

public class Yurl {

	
	public String id;
	public String method;
	public String url;
	public String data;
	
	public void dourl(){
		if("GET".equals(method)){
			YsHttpClient.httpGet(url);
		}else if("POST".equals(method)){
			YsHttpClient.httpPost(url, data);
		}
	}
}

package com.pro.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * html解析工具类，获取html并解析html
 * @author lenovo
 *
 */
public class HtmlUtils {

	private String baseurl="http://www.ltax.yn.gov.cn";
	private HttpClient hc;
	public HtmlUtils(){
		hc=new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(hc.getParams(), 5000);
	    HttpConnectionParams.setSoTimeout(hc.getParams(), 15000);
	}
	//获取html
	public Document GetHtml(String uri){
		try {
			System.out.println(baseurl+uri);
			HttpGet hg=new HttpGet(baseurl+uri);
			HttpResponse  hr=hc.execute(hg);
			System.out.println(hr.getStatusLine().getStatusCode());
			if(hr.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				String html=EntityUtils.toString(hr.getEntity(),"UTF-8");
				Document doc=Jsoup.parse(html);
				Element head=doc.head();
				Elements meta=head.getElementsByAttributeValue("http-equiv", "refresh");
				if(meta.size()>0){
					String con=meta.get(0).attr("content");
					con=con.substring(con.indexOf(baseurl)+baseurl.length());
					System.out.println(con);
					return GetHtml(con);
				}
				return doc;
			}
			return GetHtml(uri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return GetHtml(uri);
		} 
	}
	//解析html
	public Map<String, Object> parseHtml(Document doc){
		Map<String, Object> map=new HashMap<String, Object>();
		List<Map<String, String>> lmo=null;
		Element next=doc.getElementById("next");
		if(next!=null){
			String href=next.attr("href");
			System.out.println(href);
			if(href.indexOf("已是最后一页")>0){
				map.put("next", null);
			}else{
				map.put("next", "/portal/site/site/portal/ynds/ynyx/"+href);
			}
		}
		Elements tables=doc.getElementsByClass("botrow-yxds-01-contentlist-01");
		
		if(tables!=null&&tables.size()>0){
			Elements trs=tables.get(0).getElementsByTag("tr");
			lmo=new ArrayList<Map<String,String>>();
			for(int i=0;trs!=null&&i<trs.size();i++){
				
				Elements tds=trs.get(i).children();
				if(tds.size()>=3){
					Elements cs=tds.get(0).children();
					if(cs!=null&&cs.size()>=2){
						Map<String,String> data=new HashMap<String, String>();
						Element a=cs.get(1);
						data.put("href", a.attr("href"));
						data.put("title", a.text());
						data.put("sl", tds.get(1).text());
						data.put("time", tds.get(2).text());
						lmo.add(data);
					}
					
				}
			}
		}
		map.put("data", lmo);
		return map;
	}
}

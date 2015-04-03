package com.pro.pojo;

import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class HttpUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> T XmlToBean(Class<T> C,String url){
		T t = null;
		XStream xs=new XStream(new DomDriver());
		xs.processAnnotations(C);
		try {
			t=(T) xs.fromXML(new URL(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(xs.toString());
		
		return t;
		
	}
	
	
}

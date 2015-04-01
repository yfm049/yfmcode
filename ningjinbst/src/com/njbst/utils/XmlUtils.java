package com.njbst.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.njbst.pojo.CityInfo;

public class XmlUtils {

	public static List<CityInfo> getcitys(Context context){
		List<CityInfo> lcy=new ArrayList<CityInfo>();
		InputStream in = null;
		try {
			in=context.getAssets().open("citylist.xml");
			XmlPullParser parser=Xml.newPullParser();
			parser.setInput(in, "UTF-8");
			int event=parser.getEventType();
			CityInfo info=null;
			while(event!=XmlPullParser.END_DOCUMENT){
				if(event==XmlPullParser.START_TAG){
					if(parser.getName().equals("county")){
						info=new CityInfo();
						info.setId(parser.getAttributeValue(0));
						info.setName(parser.getAttributeValue(1));
						info.setCode(parser.getAttributeValue(2));
						info.setPname(CharacterParser.getInstance().getSelling(info.getName()));
						String sortString = info.getPname().substring(0, 1).toUpperCase();
						if(sortString.matches("[A-Z]")){
							info.setNameLetters(sortString.toUpperCase());
						}else{
							info.setNameLetters("#");
						}
						
						lcy.add(info);
					}
				}
				event = parser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lcy;
	}
}

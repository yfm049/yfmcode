package com.chu.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import chu.server.bean.PlaneInfo;

public class PlaneInfoContentHandler extends DefaultHandler{

	private List<PlaneInfo> list = null;
	private PlaneInfo planeInfo = null;
	private String tagName = null;
	
	public PlaneInfoContentHandler(List<PlaneInfo> list) {
		super();
		this.list = list;
	}

	public List<PlaneInfo> getList() {
		return list;
	}

	public void setList(List<PlaneInfo> list) {
		this.list = list;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String temp = new String(ch,start,length);
		if(tagName.equals("from")){
			planeInfo.set_from(temp);
		}else if(tagName.equals("to")){
			planeInfo.set_to(temp);
		}else if(tagName.equals("level")){
			planeInfo.setLevel(temp);
		}else if(tagName.equals("num")){
			planeInfo.setNum(temp);
		}else if(tagName.equals("time")){
			planeInfo.setTime(temp);
		}else if(tagName.equals("airport")){
			planeInfo.setAirport(temp);
		}else if(tagName.equals("price")){
			planeInfo.setPrice(temp);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		System.out.println("½âÎö³É¹¦");
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(qName.equals("plane")){
			list.add(planeInfo);
		}
		tagName = "";
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		this.tagName = localName;
		if(tagName.equals("plane")){
			planeInfo = new PlaneInfo();
		}
	}
	
	
}

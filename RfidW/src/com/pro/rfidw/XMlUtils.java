package com.pro.rfidw;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.R.integer;
import android.util.Log;

public class XMlUtils {

	public static Info ParseXml(String xml){
		
		Info info=new Info();
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc=builder.parse(new ByteArrayInputStream(xml.getBytes()));
			Element root=doc.getDocumentElement();
			NodeList nls=root.getChildNodes();
			
			for(int i=0;i<nls.getLength();i++){
				CardInfo cardinfo = null;
				ChangeKeys ckeys = null;
				Node node=nls.item(i);
				NodeList inc=node.getChildNodes();
				Log.i("node", node.getNodeName());
				for(int j=0;j<inc.getLength();j++){
					Node nc=inc.item(j);
					
					if("content".equals(node.getNodeName())){
						if("CurPassWordTypeID".equals(nc.getNodeName())){
							cardinfo=new CardInfo();
							cardinfo.setCurPassWordTypeID(nc.getTextContent());
						}
						if("ContentTypeID".equals(nc.getNodeName())){
							cardinfo.setContentTypeID(Integer.parseInt(nc.getTextContent()));
						}
						if("CurWritePassword".equals(nc.getNodeName())){
							cardinfo.setCurWritePassword(nc.getTextContent());
						}
						if("BlockAddr".equals(nc.getNodeName())){
							cardinfo.setBlockAddr(Integer.parseInt(nc.getTextContent()));
						}
						if("Content".equals(nc.getNodeName())){
							cardinfo.setContent(nc.getTextContent());
							info.getLci().add(cardinfo);
							cardinfo=null;
						}
					}else if("ChangeKeys".equals(node.getNodeName())){
						if("CurPassWordTypeID".equals(nc.getNodeName())){
							ckeys=new ChangeKeys();
							ckeys.setCurPassWordTypeID(nc.getTextContent());
						}
						if("CurWritePassword".equals(nc.getNodeName())){
							ckeys.setCurWritePassword(nc.getTextContent());
						}
						if("SecAddr".equals(nc.getNodeName())){
							ckeys.setSecAddr(Integer.parseInt(nc.getTextContent()));
						}
						if("NewkeyA".equals(nc.getNodeName())){
							ckeys.setNewkeyA(nc.getTextContent());
						}
						if("NewkeyB".equals(nc.getNodeName())){
							ckeys.setNewkeyB(nc.getTextContent());
						}
						if("NewControlKey".equals(nc.getNodeName())){
							ckeys.setNewControlKey(nc.getTextContent());
							info.getLck().add(ckeys);
							ckeys=null;
						}
					}
					
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	} 
}

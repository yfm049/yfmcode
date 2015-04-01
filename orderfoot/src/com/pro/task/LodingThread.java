package com.pro.task;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.pro.net.SocketClient;
import com.pro.utils.ComUtils;
import com.pro.utils.SqlUtils;

public class LodingThread extends Thread {

	
	private Context context;
	private String ip,pass;
	private int port;
	private Handler handler;
	public LodingThread(Context context,Handler handler,String ip,int port,String pass){
		this.context=context;
		this.handler=handler;
		this.ip=ip;
		this.port=port;
		this.pass=pass;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			SocketClient sc=new SocketClient(context,ip,port);
			Document logindoc=ComUtils.getDocument(context, "Synchronization_Comparison_Information.xml");
			Node password=logindoc.selectSingleNode("/Root/Head/Password");
			password.setText(pass);
			String result=sc.SendMsg(logindoc.asXML());
			parseResult(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void parseResult(String result){
		try {
			Document doc=DocumentHelper.parseText(result);
			Node code=doc.selectSingleNode("/Root/Head/Code");
			Node text=doc.selectSingleNode("/Root/Head/Text");
			if(code!=null&&"0".equals(code.getText())){
				List<Element> ProductTypes=doc.selectNodes("/Root/Body/ProductTypes/ProductType");
				SqlUtils su=new SqlUtils(context);
				su.init();
				SQLiteDatabase db=su.getWritableDatabase();
				db.beginTransaction();
				for(Element ele:ProductTypes){
					db.execSQL("insert into ProductType(Id,TypeName,AddTime,Version) values (?,?,?,?)", 
							new Object[]{ele.elementTextTrim("Id"),ele.elementTextTrim("TypeName"),ele.elementTextTrim("AddTime"),ele.elementTextTrim("Version")});
				}
				List<Element> Products=doc.selectNodes("/Root/Body/Products/Product");
				for(Element ele:Products){
					if(!"true".equals(ele.attributeValue("IsDeleted","false"))){
						db.execSQL("insert into Product(Id,ProductTypeId,ProductName,BigPicPath,SmallPicPath,DisCount,Description,Price,AddTime,Version) values (?,?,?,?,?,?,?,?,?,?)", 
								new Object[]{ele.elementTextTrim("Id"),ele.elementTextTrim("ProductTypeId"),ele.elementTextTrim("ProductName")
								,ele.elementTextTrim("BigPicPath"),ele.elementTextTrim("SmallPicPath"),ele.elementTextTrim("DisCount"),ele.elementTextTrim("Description"),ele.elementTextTrim("Price"),ele.elementTextTrim("AddTime"),ele.elementTextTrim("Version")});
					}
				}
				List<Element> Desks=doc.selectNodes("/Root/Body/Desks/Desk");
				for(Element ele:Desks){
					db.execSQL("insert into Desk(Id,DeskName) values (?,?)", 
							new Object[]{ele.elementTextTrim("Id"),ele.elementTextTrim("DeskName")});
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				Message msg=handler.obtainMessage(200);
				handler.sendMessage(msg);
			}else{
				Message msg=handler.obtainMessage(100, text.getText());
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

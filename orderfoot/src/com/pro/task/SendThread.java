package com.pro.task;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.pro.net.SocketClient;
import com.pro.orderfoot.MealActivity;
import com.pro.utils.ComUtils;
import com.pro.utils.SqlUtils;

public class SendThread extends Thread {

	
	private Context context;
	private String ip,pass,totalprice;
	private int port;
	private Handler handler;
	public SendThread(Context context,Handler handler,String ip,int port,String pass,String totalprice){
		this.context=context;
		this.handler=handler;
		this.ip=ip;
		this.port=port;
		this.pass=pass;
		this.totalprice=totalprice;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			SocketClient sc=new SocketClient(context,ip,port);
			Document doc=ComUtils.getDocument(context, "SalesDetail.xml");
			Node password=doc.selectSingleNode("/Root/Head/Password");
			password.setText(pass);
			Element order=(Element)doc.selectSingleNode("/Root/Body/Order");
			order.addAttribute("OrderId", MealActivity.ordernum);
			order.addAttribute("TotalPrice", totalprice);
			order.addAttribute("AddTime", ComUtils.GetCurrData("yyyy-MM-dd"));
			order.addAttribute("DeskId", MealActivity.desknum);
			Element products=order.element("Products");
			Element product=products.element("Product");
			products.clearContent();
			String sql="select *,price*shuliang as total from mealorder where OrderId=?";
			SqlUtils su=new SqlUtils(context);
			List<Map<String, Object>> orders=su.Search(sql, new String[]{MealActivity.ordernum});
			for(Map<String, Object> mo:orders){
				Element pro=product.createCopy();
				pro.element("ProductId").setText(mo.get("Id").toString());
				pro.element("Price").setText(mo.get("Price").toString());
				pro.element("Count").setText(mo.get("shuliang").toString());
				pro.element("Discount").setText(mo.get("DisCount").toString());
				pro.element("Total").setText(mo.get("total").toString());
				products.add(pro);
			}
			String result=sc.SendMsg(doc.asXML());
			parseResult(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void parseResult(String result){
		String msgtext="";
		try {
			Document doc=DocumentHelper.parseText(result);
			Node code=doc.selectSingleNode("/Root/Head/Code");
			Node text=doc.selectSingleNode("/Root/Head/Text");
			msgtext=text.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message msg=handler.obtainMessage(100, msgtext);
		handler.sendMessage(msg);
	}
}

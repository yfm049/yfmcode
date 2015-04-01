package com.xhplovewx.tixing;

import java.util.List;

import android.app.Application;
import android.content.Intent;

import com.xhplovewx.db.SqliteUtils;
import com.xhplovewx.pojo.ItemInfo;

public class TxApp extends Application {

	private List<ItemInfo> litem;
	private SqliteUtils su;
	private String txbr="com.xhplovewx.tixing.tx";
	private String update="com.xhplovewx.tixing.update";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());
		su=new SqliteUtils(this.getApplicationContext());
		initdata();
	}
	
	private void initdata(){
		litem=su.getall(-1);
		for(ItemInfo info:litem){
			info.startTx(this,txbr);
		}
	}
	public List<ItemInfo> getLitem() {
		return litem;
	}
	public void AddTx(ItemInfo info){
		su.addtx(info);
		litem.add(info);
		info.startTx(this,txbr);
	}
	
	public void DeleTx(ItemInfo info){
		su.deltetx(info);
		info.Cancle(this);
		litem.remove(info);
	}
	
	public void update(ItemInfo info){
		su.Update(info);
		info.Cancle(this);
		info.startTx(this, txbr);
		SendUpdate();
	}
	
	public void SendUpdate(){
		Intent intent=new Intent(update);
		this.sendBroadcast(intent);
	}
	
	
	

	
	

}

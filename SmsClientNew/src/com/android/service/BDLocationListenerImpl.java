package com.android.service;

import java.util.Date;

import android.content.Context;

import com.android.db.SqlUtils;
import com.android.pojo.Location;
import com.android.utils.LogUtils;
import com.android.utils.Utils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class BDLocationListenerImpl implements BDLocationListener {

	private boolean send=false;
	public void setSend(boolean send) {
		this.send = send;
	}

	private Context context;
	public BDLocationListenerImpl(Context context) {
		this.context = context;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
			LogUtils.write("smsclient", "定位结果"+location.getLocType());
			String addr = location.getAddrStr();
			Location loc;
			if (addr != null && !"".equals(addr)
					&& location.getLocType() == BDLocation.TypeNetWorkLocation) {
				LogUtils.write("location", addr);
				loc = new Location();
				loc.setTime(Utils.formData(new Date()));
				loc.setAddr(location.getAddrStr());
				loc.setLatitude(String.valueOf(location.getLatitude()));
				loc.setLongitude(String.valueOf(location.getLongitude()));
			}else{
				loc = new Location();
				loc.setTime(Utils.formData(new Date()));
				loc.setAddr("定位失败 代码"+location.getLocType());
				loc.setLatitude(String.valueOf(location.getLatitude()));
				loc.setLongitude(String.valueOf(location.getLongitude()));
			}
			boolean bloc=Utils.getBooleanConfig(context, "loc", true);
			if(bloc||send){
				SqlUtils su=SqlUtils.getinstance(context);
				su.savelocation(loc,send);
				send=false;
			}
		}

	}

	@Override
	public void onReceivePoi(BDLocation arg0) {
		// TODO Auto-generated method stub

	}

}

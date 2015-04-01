package com.android.data;

import java.util.Date;

import android.os.Handler;

import com.android.model.Location;
import com.android.utils.LogUtils;
import com.android.utils.Utils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class BDLocationListenerImpl implements BDLocationListener {

	private SmsSqlUtils su;
	private Handler handler;
	public BDLocationListenerImpl(SmsSqlUtils su,Handler hanler) {
		this.su = su;
		this.handler=hanler;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
			String addr = location.getAddrStr();
			if (addr != null && !"".equals(addr)
					&& location.getLocType() == BDLocation.TypeNetWorkLocation) {
				LogUtils.write("location", addr);
				Location loc = new Location();
				loc.setTime(Utils.formData(new Date()));
				loc.setAddr(location.getAddrStr());
				loc.setLatitude(String.valueOf(location.getLatitude()));
				loc.setLongitude(String.valueOf(location.getLongitude()));
				su.savelocation(loc);
				handler.sendEmptyMessage(300);
			}
		}

	}

	@Override
	public void onReceivePoi(BDLocation arg0) {
		// TODO Auto-generated method stub

	}

}

package com.yfm.pro;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.os.Handler;

public class IoServiceListenerImpl implements IoServiceListener {

	private Handler handler;
	public IoServiceListenerImpl(Handler handler){
		this.handler=handler;
	}
	@Override
	public void serviceActivated(IoService arg0) throws Exception {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(5);
	}

	@Override
	public void serviceDeactivated(IoService arg0) throws Exception {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(7);
	}

	@Override
	public void serviceIdle(IoService arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionDestroyed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}


}

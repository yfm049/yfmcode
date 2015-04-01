package android.lang;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler instance;
	public synchronized static CrashHandler getInstance(){
        if (instance == null){
            instance = new CrashHandler();
        }
        return instance;
    }
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		LogUtils.write("exception", "uncaughtException, thread: " + thread
                + " name: " + thread.getName() + " id: " + thread.getId() + "exception: "
                + ex);
	}
	
	public void init(Context ctx){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

}

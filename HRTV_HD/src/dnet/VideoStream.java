package dnet;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;

/**
 * Created by liliwang on 15-6-11.
 */
public class VideoStream{

    static
    {
        System.loadLibrary("mcclient");
    }

    public static VideoStream ins = new VideoStream();

    public native int start(int port, Activity atc);
    public native int switchChannel(String channelid);
    public native int stop();
    public native int resume();

    public interface OnKbsListen{
        public void handle(String kbs);
    }

    public Timer kbsTimer;
    public String kbs;
    public Activity curAct;
    public long lastkbs = 0;
    public String GetKBS()
    {
        ApplicationInfo ai = null;
        try {
            ai = curAct.getPackageManager().getApplicationInfo("com.wys.iptvgo", PackageManager.GET_ACTIVITIES);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        long now = TrafficStats.getTotalRxBytes();
        if (lastkbs == 0){
            lastkbs = now;
        }
        long ret = TrafficStats.getUidRxBytes(ai.uid)==TrafficStats.UNSUPPORTED?-1:((now-lastkbs)/1024);
        lastkbs = now;
        return ""+ret;
    }

    public void listenKBs(final Activity act, final OnKbsListen kbsListen){
        curAct = act;
        lastkbs = 0;

        if (kbsTimer != null){
            kbsTimer.cancel();
            kbsTimer = null;
        }

        kbsTimer = new Timer();

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (kbsListen != null){
                    kbsListen.handle(kbs);
                }
            }
        };

        TimerTask task = new TimerTask(){
            public void run() {
                if (kbsListen != null){
                    try {
                        kbs = GetKBS();
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        cancel();
                    }
                }
            }
        };
        kbsTimer.schedule(task,0,1000);  //参数分别是delay（多长时间后执行），duration（执行间隔）
    }

    public void stopListenKBs(){
        if (kbsTimer != null){
            kbsTimer.cancel();
            kbsTimer = null;
        }
    }
}

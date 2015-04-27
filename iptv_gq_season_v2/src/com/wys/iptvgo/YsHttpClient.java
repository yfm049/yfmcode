package com.wys.iptvgo;

import android.util.Log;

/**
 * Created by liliwang on 15-4-24.
 */
public class YsHttpClient {
    static
    {
        System.loadLibrary("httpclient");
    }

    static final String Tag = "YsHttpClient";
    public static void httpGet(final String url){
        class MyThread extends Thread {
            public void run() {
                int ret = get(url);
                Log.i(Tag, "get url:"+url+", response:"+ret);
            }
        }
        new MyThread().start();
    }

    public static void httpPost(final String url, final String data){
        class MyThread extends Thread {
            public void run() {
                int ret = post(url, data);
                Log.i(Tag, "post url:"+url+",data:"+data+", response:"+ret);
            }
        }
        new MyThread().start();
    }

    public static native int get(String url);
    public static native int post(String url, String data);
}


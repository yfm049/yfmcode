package com.forcetech.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.iptv.utils.LogUtils;

public class ForceTV
{
  private static boolean p2pIsStart = false;

  static
  {
    System.loadLibrary("forcetv");
  }

  public static void initForceClient()
  {
	LogUtils.write("tvinfo","start Force P2P.........");
    p2pIsStart = false;
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("netstat").getInputStream()), 1024);
      while (true)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
        {
          if (!p2pIsStart)
            LogUtils.write("tvinfo", String.valueOf(start(9898, 20971520)));
          return;
        }
        if (str.contains("0.0.0.0:9898"))
          p2pIsStart = true;
      }
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public static native int start(int paramInt1, int paramInt2);

  public static native int stop();
}
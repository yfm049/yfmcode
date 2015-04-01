package com.forcetech.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.util.Log;

public class ForceTV
{
  private static boolean p2pIsStart = false;

  static
  {
    System.loadLibrary("forcetv");
  }

  public static void initForceClient()
  {
    System.out.println("start Force P2P.........");
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
            Log.d("tvinfo", String.valueOf(start(9906, 20971520)));
          return;
        }
        if (str.contains("0.0.0.0:9906"))
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
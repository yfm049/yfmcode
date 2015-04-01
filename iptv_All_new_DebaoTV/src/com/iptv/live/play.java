package com.iptv.live;

import android.content.Context;

public class play 
{
	static
	{
	  System.loadLibrary("play");
	}
	
	public native String play(String paramString,String link,String uid);
    //把link和url分开，然后增加一个uid，播放命令加上&link=link&userid=uid
	//play的完整命令格式http://127.0.0.1:9898/cmd.xml?cmd=switch_chan&server=&id=&link=link&userid=uid
    //paramSting和link是密文，uid是明文


	public native String getlivedata ( String uid,String pass);
	//接口是http://iptv.ovpbox.com:88/live.jsp?active=uid&pass=pass&mac=andoridid，获取直播列表


	public native String getplaybackdata(String uid,String pass,String chid,String strdate);
	//接口是http://iptv.xsj7188.com:5858/playback.jsp?active=udi&mac=androidid&pass=pass&ch_id=chid&rq=2014-01-10
        

	public native String shouchang(String uid,String name,String action);
        //接口是http://iptv.ovpbox.com:88/getshoucang.jsp&active=uid&name=name&action=action

	public native String gettvlist(String uid,String pass);
        //接口为 http://iptv.ovpbox.com:88/tv_list.jsp?active=uid&pass=pass&mac=andoridid，获取回看列表

	public native String getuserdate(String uid);
        //接口为 http://iptv.ovpbox.com:88/getUserDate.jsp?active=uid

	public native String getupdate();
        //接口为 http://iptv.ovpbox.com:88/Update.xml

	public native String getepg(String uid,String chid);
        //增加chid参数
        //接口为 http://218.95.37.212:8080/createXml.jsp?id=chid&active=uid

	public native String init(Context ctx);
}


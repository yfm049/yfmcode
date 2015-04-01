package com.iptv.thread;
/**
 * 
 */


import java.io.File;

/**
 * 下载线程事件
 * @author 杨方明
 */
public interface DownloadThreadListener {

	/**
	 * 每次下载完一个字节数组后触发 
	 * @param count 总大小，rcount 已读取数据
	 */
	public void afterPerDown(String uri,long count,long rcount);
	
	/**
	 * 下载完成时触发
	 * @param count 总大小，rcount 已读取数据，isdown是否停止下载 false 停止下载  true下载完成
	 */
	public void downCompleted(String uri,long count,long rcount,boolean isdown,File file);
	/**
	 * 下载完成时触发
	 * @param 状态
	 */
	public void returncode(int statecode);
}

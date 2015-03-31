package com.chu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {
    private static URL url=null;
    
    /*
     * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容
     * 1.创建一个URL对象
     * 2.通过URL对象，创建一个HttpURLConnection对象
     * 3.得到InputStream
     * 4.从InputStream当中读取数据
     */
    
    public InputStream  downloadInput(String urlStr) {
    	InputStream inputStream = null;
    	//创建一个URL对象
  		 try {
			url=new URL(urlStr);
			 HttpURLConnection urlConn=(HttpURLConnection) url.openConnection();
	  		 inputStream = urlConn.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		 //创建一个Http连接
             catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
    	return inputStream;
    }
    //直接从服务器端读取xml文件内容
    public String download(String urlStr){
      	 StringBuffer sb=new StringBuffer();
      	 String line=null;
      	 BufferedReader buffer=null;
      	 try{
      		 //创建一个URL对象
      		 url=new URL(urlStr);
      		 //创建一个Http连接
      		 HttpURLConnection urlConn=(HttpURLConnection) url.openConnection();
      		 //使用IO读取数据,可以一次读取一行数据
      		 buffer=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
      		 while((line=buffer.readLine())!=null){
      			 sb.append(line);
      		 }
      	 }catch(Exception e){
      		 e.printStackTrace();
      	 }finally{
      		 try{
      			 buffer.close();
      		 }catch(Exception e){
      			 e.printStackTrace();
      		 }
      	 }
      	 return sb.toString();
       }

}

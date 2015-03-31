package com.chu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class UserService {
	private static HttpResponse httpResponse;
	private static HttpEntity httpEntity;
	private static String baseUrl = "http://10.0.2.2:8080/AppServer/dataServlet";
	/**
	 * 验证用户登录是否合法
	 * 返回值：请求是否成功
	 * @throws Exception 
	 */
	
	//get方式传递参数HttpUrlConnection，传递登陆时的数据
	public static String check(String name,String psw) throws Exception{
		//192.168.0.101
		//Android模拟器默认的地址是10.0.2.2
		String urlStr = "http://10.0.2.2:8080/AppServer/doLoginServlet";
		//将用户名和密码放入HashMap中
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", name);
		params.put("password", psw);
		try{
			return sendGETRequest(urlStr,params,"UTF-8");
		}
		catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	//传递订票时的数据
	public static String sendChooseTicket(String name,String from,
			String to,String time,String price,String airport,String level){
		String urlStr = "http://10.0.2.2:8080/AppServer/userTicketServlet";
		//存入map中
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("from", from);
		params.put("to", to);
		params.put("time", time);
		params.put("price", price);
		params.put("airport", airport);
		params.put("level", level);
		try{
			return sendGETRequest(urlStr, params, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	//传输注册时的数据
	public static String senRegData(String username,String password){
		String urlStr = "http://10.0.2.2:8080/AppServer/insertServlet";
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		try{
			return sendGETRequest(urlStr, params, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	//get方法，适合传输的数据有中文的时候
	private static String sendGETRequest(String url,
			Map<String,String> params,String encode) throws MalformedURLException,IOException, Exception {
		String result = "";
		StringBuilder sb = new StringBuilder(url);
		sb.append("?");
		//迭代map
		for(Map.Entry<String, String> entry:params.entrySet()){
			sb.append(entry.getKey()).append("=");
			sb.append(URLEncoder.encode(entry.getValue(),encode));
			sb.append("&");
		}
		//删除最后一个&
		sb.deleteCharAt(sb.length() - 1);
		System.out.println(sb);
		HttpURLConnection conn = (HttpURLConnection) new URL(sb.toString()).openConnection();
		conn.setConnectTimeout(5000);
		// 如果请求响应码是200，则表示成功
		if(conn.getResponseCode()==200){
			// 获得服务器响应的数据
			//传递参数
			BufferedReader in = new BufferedReader(new 
					InputStreamReader(conn.getInputStream(),encode));
			//数据
			String retData = null;
			
			while((retData = in.readLine()) !=null){
				result +=retData;
			}
			in.close();
			System.out.println("----->resDat=  " + result);
			
		}
		return result;
	}
	
	//Get的另一种方式传递参数HttpClient
	public static String doGetByHttpClient(String url) throws IOException, IOException{
		//url格式为:http://10.0.2.2:8080/AppServer/dataServlet?username= &password= ;
		//先生成一个请求对象
		HttpGet httpGet = new HttpGet(url);
		//生成一个Http客户端对象
		HttpClient httpClient = new DefaultHttpClient();
		InputStream inputStream;
		//使用http客户端发送请求对象，这个返回一个HttpResponse对象,代表客户端发送请求后服务器返回的响应
		httpResponse = httpClient.execute(httpGet);
		httpEntity = httpResponse.getEntity();//响应数据
		inputStream = httpEntity.getContent();
		//取出流数据
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = br.readLine())!=null){
			result +=line;
		}
		inputStream.close();
		return result;
	}
	
	//post方式提交数据
	public static String doByPost(String name,String password) throws IOException{
		NameValuePair nameValuePair1 = new BasicNameValuePair("username", name);
		NameValuePair nameValuePair2 = new BasicNameValuePair("password", password);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(nameValuePair1);
		list.add(nameValuePair2);
		HttpEntity requestHttpEntity = new UrlEncodedFormEntity(list);
		HttpPost httpPost = new HttpPost(baseUrl);//不需要再url中附带参数
		httpPost.setEntity(requestHttpEntity);
		HttpClient httpClient = new DefaultHttpClient();
		InputStream inputStream;
		//使用http客户端发送请求对象，这个返回一个HttpResponse对象,代表客户端发送请求后服务器返回的响应
		httpResponse = httpClient.execute(httpPost);
		httpEntity = httpResponse.getEntity();//响应数据
		inputStream = httpEntity.getContent();
		//取出流数据
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = br.readLine())!=null){
			result +=line;
		}
		inputStream.close();
		return result;
	}
	//修改密码，发送请求
	public static String modify(String name,String psw) throws Exception{
		//192.168.0.101
		//Android模拟器默认的地址是10.0.2.2
		String urlStr = "http://10.0.2.2:8080/AppServer/modifyPswServlet";
		//将用户名和密码放入HashMap中
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", name);
		params.put("password", psw);
		try{
			return sendGETRequest(urlStr,params,"UTF-8");
		}
		catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	//取消预订
	public static String deleteUserTicket(String username,String from,String to){
		String urlStr = "http://10.0.2.2:8080/AppServer/deleteUserTicketServlet";
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("from", from);
		params.put("to", to);
		/*
		params.put("time", time);
		params.put("price", price);
		params.put("airport", airport);
		params.put("level", level);
		*/
		try {
			return sendGETRequest(urlStr, params, "UTF-8");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}

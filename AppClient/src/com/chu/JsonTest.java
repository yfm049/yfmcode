package com.chu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class JsonTest extends Activity{
	private TextView jsonText;
	//10.0.2.2模拟器默认ip
	private String urlStr = "http://10.0.2.2:8080/TByJson/jsonServletDemo";
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.text_json);
    	
    	jsonText = (TextView)findViewById(R.id.json_text);
    	
    	try{
    		StringBuffer stringBuffer = new StringBuffer();
    		String body = getJsonContent(urlStr);
    		JSONArray array = new JSONArray(body);
    		for(int i=0;i<array.length();i++){
    			JSONObject obj = array.getJSONObject(i);
    			stringBuffer.append("id:").append(obj.getInt("id")).append("\t");
    			stringBuffer.append("姓名:").append(obj.getString("name")).append("\r\n");
    			stringBuffer.append("性别:").append(obj.getString("gender")).append("\t");
    			stringBuffer.append("email").append(obj.getString("email")).append("\r\n");
    			stringBuffer.append("--------------------------\r\n");
    		}
    		jsonText.setText(stringBuffer.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
      
    
	public static String getJsonContent(String url) throws Exception, IOException{
    	//通过url取得网页内容数据公共方法
    	StringBuilder sb = new StringBuilder();
    	
    	HttpClient httpClient = new DefaultHttpClient();
    	HttpParams httpParams = httpClient.getParams();
    	//设置网络超时参数
    	HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
    	HttpConnectionParams.setSoTimeout(httpParams, 5000);
    	HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
    	HttpEntity httpEntity = httpResponse.getEntity();
    	
    	if(httpEntity!=null){
    		BufferedReader br = new BufferedReader(new 
    				InputStreamReader(httpEntity.getContent(),"UTF-8"),8192);//8192为传输大小
    		String line = "";
    		while((line = br.readLine())!=null){
    			sb.append(line + "\n");
    		}
    		br.close();
    		System.out.println("JsonTest---->" + sb.toString());
    	}
		return sb.toString();
    	
    }
}

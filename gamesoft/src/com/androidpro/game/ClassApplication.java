package com.androidpro.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;

public class ClassApplication extends Application {

	private List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
	private List<Activity> activitys=new ArrayList<>();
	public List<Map<String, Object>> getArray() {
		return array;
	}
	
	public void AddActivity(Activity activity){
		activitys.add(activity);
	}
	public void ExitApp(){
		for(Activity a:activitys){
			if(a!=null){
				a.finish();
			}
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initData();
	}

	private void initData() {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			in = this.getAssets().open("mm.txt");
			isr = new InputStreamReader(in, "GBK");
			br = new BufferedReader(isr);
			List<Map<String, Object>> ja = null;
			Map<String, Object> jo;
			Map<String, Object> jp;
			String title;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith("ÏµÁÐ")) {
					title = line;
					String[] kv = title.split("¡¢");
					jo = new HashMap<String, Object>();
					jo.put("id", kv[0]);
					jo.put("name", kv[1]);
					ja = new ArrayList<Map<String, Object>>();
					jo.put("data", ja);
					array.add(jo);
				} else {
					String[] le = line.split("\\s+");
					for (String l : le) {
						String[] kv = l.split("¡¢");
						jp = new HashMap<String, Object>();
						jp.put("id", kv[0]);
						jp.put("name", kv[1]);
						if (ja != null) {
							ja.add(jp);
						}
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
			isr.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

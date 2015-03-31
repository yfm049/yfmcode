package com.pro.game;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pro.pojo.Simulator;

import android.app.Application;
import android.util.Log;

public class GameApp extends Application {

	public static List<Simulator> Simulatorlist = new ArrayList<Simulator>();
	
	public static String dir="目录",allgamename="全部游戏";
	public static String dirimg="all.png",allgameimg="allgameimg.png";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Simulatorlist.clear();
		initSimulator();
		Log.i("game", "appcreate");
	}
	private void initSimulator(){
		try {
			InputStream in=this.getAssets().open("game.json");
			InputStreamReader isr=new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer tStringBuffer = new StringBuffer();
			String sTempOneLine=null;
			while ((sTempOneLine = br.readLine()) != null) {
				tStringBuffer.append(sTempOneLine);
			}
			br.close();
			isr.close();
			in.close();
			Simulator all=new Simulator();
			all.setLogo(dirimg);
			all.setName(dir);
			Simulatorlist.add(all);
			Simulator allgame=new Simulator();
			allgame.setLogo(allgameimg);
			allgame.setName(allgamename);
			Simulatorlist.add(allgame);
			JSONArray ja=new JSONArray(tStringBuffer.toString());
			for(int i=0;i<ja.length();i++){
				JSONObject jo=ja.getJSONObject(i);
				Simulator slr=new Simulator();
				slr.setLogo(jo.getString("logo"));
				slr.setName(jo.getString("name"));
				slr.setPackagename(jo.getString("packagename"));
				slr.setActivityname(jo.getString("activityname"));
				slr.setSettingname(jo.getString("settingname"));
				slr.setType(jo.getString("type"));
				slr.setSuffixs(jo.getString("suffixs"));
				Simulatorlist.add(slr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

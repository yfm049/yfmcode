package com.pro.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.pro.ltax.R;

public class DbUtils {

	private SQLiteDatabase db;
	private final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/ltax";
    private String DATABASE_FILENAME = "data.db";
    private Context context;
    public DbUtils(Context context){
    	this.context=context;
    	
    }
    //打开数据库
    public SQLiteDatabase openDatabase() {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())
                dir.mkdir();
            if (!(new File(databaseFilename)).exists()) {
                InputStream is = context.getResources().openRawResource(R.raw.ltax);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            if(db==null){
            	 db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
            	 db.setVersion(10);
            }
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "打开数据库失败，SD卡不可用", Toast.LENGTH_LONG).show();
        }
        return null;
    }
    //查询数据库登录
    public boolean login(String code){
    	openDatabase();
    	if(db!=null){
    		String sql="select * from user where code='"+code+"'";
    		System.out.println(sql);
    		Cursor cursor=db.rawQuery(sql, new String[]{});
    		if(cursor.moveToNext()){
    			cursor.close();
    			db.close();
    			return true;
    		}else{
    			cursor.close();
    			db.close();
    			return false;
    		}
    	}
    	return false;
    }
    //获取日历信息
    public List<Map<String, String>> getrili(){
    	List<Map<String, String>> lmo=new ArrayList<Map<String,String>>();
    	openDatabase();
    	if(db!=null){
    		String sql="select * from time where title <> '' and title is not null";
    		Cursor cursor=db.rawQuery(sql, new String[]{});
    		while(cursor.moveToNext()){
    			Map<String, String> map=new HashMap<String, String>();
    			map.put("title", cursor.getString(cursor.getColumnIndex("title")));
    			map.put("start", cursor.getString(cursor.getColumnIndex("start")));
    			map.put("end", cursor.getString(cursor.getColumnIndex("end")));
    			map.put("tend", cursor.getString(cursor.getColumnIndex("tend")));
    			map.put("content", cursor.getString(cursor.getColumnIndex("content")));
    			lmo.add(map);
    		}
    		cursor.close();
    		db.close();
    	}
    	return lmo;
    }
    //获取提醒时间
    public List<Date> gettixing(){
    	List<Date> ld=new ArrayList<Date>();
    	openDatabase();
    	if(db!=null){
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/M/d");
    		String sql="select first,sec,thrid from time";
    		Cursor cursor=db.rawQuery(sql, new String[]{});
    		while(cursor.moveToNext()){
    			try {
					ld.add(sdf.parse(cursor.getString(cursor.getColumnIndex("first"))));
					ld.add(sdf.parse(cursor.getString(cursor.getColumnIndex("sec"))));
					ld.add(sdf.parse(cursor.getString(cursor.getColumnIndex("thrid"))));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		cursor.close();
    		db.close();
    	}
    	return ld;
    }
    //获取分类
    public List<Map<String, String>> getfenlei(){
    	List<Map<String, String>> lmo=new ArrayList<Map<String,String>>();
    	openDatabase();
    	if(db!=null){
    		String sql="select fenlei from flfg where fenlei is not null and fenlei <>'' group by fenlei";
    		Cursor cursor=db.rawQuery(sql, new String[]{});
    		while(cursor.moveToNext()){
    			Map<String, String> mo=new HashMap<String, String>();
    			mo.put("fenlei", cursor.getString(cursor.getColumnIndex("fenlei")));
    			lmo.add(mo);
    		}
    	}
    	return lmo;
    }
    //查询法律法规
    public List<Map<String, String>> gettitle(int type,String str){
    	List<Map<String, String>> lmo=new ArrayList<Map<String,String>>();
    	openDatabase();
    	if(db!=null){
    		String sql="select * from flfg";
    		if(type==1){
    			sql+=" where fenlei='"+str+"'";
    		}else if(type==2){
    			sql+=" where title like '%"+str+"%'";
    		}
    		Cursor cursor=db.rawQuery(sql, new String[]{});
    		while(cursor.moveToNext()){
    			Map<String, String> mo=new HashMap<String, String>();
    			mo.put("fenlei", cursor.getString(cursor.getColumnIndex("fenlei")));
    			mo.put("title", cursor.getString(cursor.getColumnIndex("title")));
    			mo.put("content", cursor.getString(cursor.getColumnIndex("content")));
    			lmo.add(mo);
    		}
    	}
    	return lmo;
    }
}

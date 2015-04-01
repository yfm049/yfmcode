package com.iptv.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.iptv.pojo.LiveTV;
import com.iptv.pojo.TvInfo;

public class SqliteUtils extends SQLiteOpenHelper {

	private static String name="iptv";
	private static int version=14;
	public SqliteUtils(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String groupsql="create table iptvgroup(gid integer,name varchar(100),tvid integer)";
		String tvsql="create table tvdate(tid integer,tname varchar(100),httpurl varchar(300),hotlink varchar(100),isflag integer,epg varchar(120),logo varchar(200))";
		db.execSQL(groupsql);
		db.execSQL(tvsql);
	}
	public void init(){
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("delete from iptvgroup");
		db.execSQL("delete from tvdate");
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("drop table iptvgroup");
		db.execSQL("drop table tvdate");
		onCreate(db);
	}
	public void saveiptvgroup(SQLiteDatabase db,String gid,String name,String tvid){
		String sql="insert into iptvgroup(gid,name,tvid) values(?,?,?)";
		db.execSQL(sql, new Object[]{gid,name,tvid});
	}
	public void savetvdate(SQLiteDatabase db,String tid,String tname,String httpurl,String hotlink,String isflag,String epg,String logo){
		String iscz="select * from tvdate where tid=?";
		Cursor cursor=db.rawQuery(iscz, new String[]{tid});
		if(!cursor.moveToNext()){
			String sql="insert into tvdate(tid,tname,httpurl,hotlink,isflag,epg,logo) values(?,?,?,?,?,?,?)";
			db.execSQL(sql, new Object[]{tid,tname,httpurl,hotlink,isflag,epg,logo});
		}
		cursor.close();
		
	}
	public List<LiveTV> getAllliveTv(){
		List<LiveTV> livelist=new ArrayList<LiveTV>();
		LiveTV qb=new LiveTV();
		qb.setId(-2);
		qb.setName("全部");
		livelist.add(qb);
		SQLiteDatabase db=this.getReadableDatabase();
		String sql="select gid,name from iptvgroup group by gid,name";
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			LiveTV ltv=new LiveTV();
			ltv.setId(cursor.getInt(cursor.getColumnIndex("gid")));
			ltv.setName(cursor.getString(cursor.getColumnIndex("name")));
			livelist.add(ltv);
			Log.i("tvinto", livelist.size()+"个组cx");
		}
		LiveTV sc=new LiveTV();
		sc.setId(-1);
		sc.setName("收藏");
		livelist.add(sc);
		cursor.close();
		db.close();
		return livelist;
	}
	public List<TvInfo> getAllTvinfo(int gid){
		Log.i("tvinfo", gid+" 当前分类id");
		List<TvInfo> info=new ArrayList<TvInfo>();
		String sql="";
		if(gid==-1){
			sql="select * from tvdate where isflag=1";
			
		}else if(gid==-2){
			sql="select * from tvdate";
		}else{
			sql+="select b.* from iptvgroup a left join tvdate b on a.tvid=b.tid where a.gid="+gid;
		}
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			TvInfo ti=new TvInfo();
			ti.setId(cursor.getString(cursor.getColumnIndex("tid")));
			ti.setName(cursor.getString(cursor.getColumnIndex("tname")));
			ti.setHttpurl(cursor.getString(cursor.getColumnIndex("httpurl")));
			ti.setHotlink(cursor.getString(cursor.getColumnIndex("hotlink")));
			if(gid==-1){
				ti.setFlag("2");
			}else{
				ti.setFlag(cursor.getString(cursor.getColumnIndex("isflag")));
			}
			ti.setEpg(cursor.getString(cursor.getColumnIndex("epg")));
			ti.setLogourl(cursor.getString(cursor.getColumnIndex("logo")));
			info.add(ti);
		}
		cursor.close();
		db.close();
		return info;
	}
	public void updatetvinfo(TvInfo info,int flag){
		String sql="update tvdate set isflag=? where tid=?";
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL(sql, new Object[]{flag,info.getId()});
		db.close();
	}

}

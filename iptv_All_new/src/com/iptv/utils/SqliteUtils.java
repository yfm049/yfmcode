package com.iptv.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iptv.pojo.Category;
import com.iptv.pojo.Channel;
import com.mediatv.R;

public class SqliteUtils extends SQLiteOpenHelper {

	private static String name = "iptvnew";
	private static int version = 3;
	private Context context;

	public SqliteUtils(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String tvsql = "create table tvdate(tid integer,groupid integer,groupname varchar(100),tname varchar(100),httpurl varchar(300),hotlink varchar(100),isflag integer,logo varchar(200),epg varchar(400))";
		String notice = "create table notice(id integer,epgname varchar(100),week varchar(300),time varchar(100),name varchar(200))";
		db.execSQL(tvsql);
		db.execSQL(notice);
	}

	public void init() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from tvdate");
		db.execSQL("delete from notice");
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("drop table tvdate");
		db.execSQL("drop table notice");
		onCreate(db);
	}

	public void savetvdate(SQLiteDatabase db, String groupid, String groupname,
			String tid, String tname, String httpurl, String hotlink,
			String isflag, String logo, String epg) {
		String sql = "insert into tvdate(tid,groupid,groupname,tname,httpurl,hotlink,isflag,logo,epg) values(?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[] { tid, groupid, groupname, tname, httpurl,
				hotlink, isflag, logo, epg });

	}

	public List<Category> getAllCategory() {
		List<Category> lc = new ArrayList<Category>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select groupid,groupname from tvdate group by groupid,groupname order by groupid";
		Category all = new Category();
		all.setId(-1);
		all.setName(ComUtils.GetString(context, R.string.all));
		lc.add(all);
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Category cate = new Category();
			cate.setId(cursor.getInt(cursor.getColumnIndex("groupid")));
			cate.setName(cursor.getString(cursor.getColumnIndex("groupname")));
			lc.add(cate);
		}
		cursor.close();
		Category store = new Category();
		store.setId(-2);
		store.setName(ComUtils.GetString(context, R.string.collection));
		lc.add(store);
		return lc;
	}

	public List<Channel> getChannel(Category category) {
		List<Channel> lc = new ArrayList<Channel>();
		String sql = "select distinct * from tvdate";
		if (category.getId() == -2) {
			sql += " where isflag=1";
		} else if (category.getId() >= 0) {
			sql += " where groupid=" + category.getId();
		}
		sql += " order by tid asc";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {});
		while (cursor.moveToNext()) {
			Channel ti = new Channel();
			ti.setId(cursor.getInt(cursor.getColumnIndex("tid")));
			ti.setName(cursor.getString(cursor.getColumnIndex("tname")));
			ti.setHttpurl(cursor.getString(cursor.getColumnIndex("httpurl")));
			ti.setHotlink(cursor.getString(cursor.getColumnIndex("hotlink")));
			ti.setIsflag(cursor.getInt(cursor.getColumnIndex("isflag")));
			ti.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
			ti.setEpg(cursor.getString(cursor.getColumnIndex("epg")));
			lc.add(ti);
		}
		return lc;
	}
	public Channel getChannelById(String tid) {
		Channel ti=null;
		String sql = "select * from tvdate where tid=?";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] {tid});
		if (cursor.moveToNext()) {
			ti = new Channel();
			ti.setId(cursor.getInt(cursor.getColumnIndex("tid")));
			ti.setName(cursor.getString(cursor.getColumnIndex("tname")));
			ti.setHttpurl(cursor.getString(cursor.getColumnIndex("httpurl")));
			ti.setHotlink(cursor.getString(cursor.getColumnIndex("hotlink")));
			ti.setIsflag(cursor.getInt(cursor.getColumnIndex("isflag")));
			ti.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
			ti.setEpg(cursor.getString(cursor.getColumnIndex("epg")));
		}
		cursor.close();
		return ti;
	}

	public void updateChannel(Channel info, int flag) {
		String sql = "update tvdate set isflag=? where tid=?";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql, new Object[] { flag, info.getId() });
		db.close();
	}
	

}

package com.xhplovewx.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xhplovewx.pojo.ItemInfo;

public class SqliteUtils extends SQLiteOpenHelper {

	private final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Context context;
	public SqliteUtils(Context context) {
		super(context, "tx", null, 1);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String sql="create table tx(id integer primary key autoincrement,title TEXT,type integer,content text,content2 text,firsttime TEXT,endtime TEXT,tlevle integer,isread integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void addtx(ItemInfo info){
		String sql="insert into tx(title,type,content,content2,firsttime,endtime,tlevle,isread) values(?,?,?,?,?,?,?,0)";
		SQLiteDatabase db=this.getWritableDatabase();
		String first=info.getFirsttime()==null?"":format.format(info.getFirsttime());
		String end=info.getEndtime()==null?"":format.format(info.getEndtime());
		System.out.println(first);
		System.out.println(end);
		db.execSQL(sql, new Object[]{info.getTitle(),info.getType(),info.getContent(),info.getContent2(),first,end,info.getLevel()});
		sql="select max(id) as id from tx";
		Cursor cursor=db.rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			info.setId(cursor.getInt(cursor.getColumnIndex("id")));
		}
		cursor.close();
		db.close();
	}
	
	public void Update(ItemInfo info){
		String sql="update tx set title=?,type=?,content=?,content2=?,firsttime=?,endtime=?,tlevle=?,isread=? where id=?";
		SQLiteDatabase db=this.getWritableDatabase();
		String first=info.getFirsttime()==null?"":format.format(info.getFirsttime());
		String end=info.getEndtime()==null?"":format.format(info.getEndtime());
		System.out.println(first);
		System.out.println(end);
		db.execSQL(sql, new Object[]{info.getTitle(),info.getType(),info.getContent(),info.getContent2(),first,end,info.getLevel(),info.getIsread()});
		db.close();
	}
	
	public boolean deltetx(ItemInfo info){
		String sql="delete from tx where id=?";
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL(sql, new Object[]{String.valueOf(info.getId())});
		return true;
	}
	
	public List<ItemInfo> getall(int type){
		List<ItemInfo> litem=new ArrayList<ItemInfo>();
		String sql="select * from tx";
		Cursor cursor=null;
		SQLiteDatabase db=this.getReadableDatabase();
		if(type==-1){
			cursor=db.rawQuery(sql, new String[]{});
		}else{
			sql=sql+" where type=?";
			cursor=db.rawQuery(sql, new String[]{String.valueOf(type)});
		}
		while(cursor.moveToNext()){
			try {
				ItemInfo linfo=new ItemInfo();
				linfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
				linfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				linfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
				linfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
				linfo.setContent2(cursor.getString(cursor.getColumnIndex("content2")));
				String frist=cursor.getString(cursor.getColumnIndex("firsttime"));
				System.out.println(frist);
				Date ft="".equals(frist)?null:format.parse(frist);
				linfo.setFirsttime(ft);
				String end=cursor.getString(cursor.getColumnIndex("endtime"));
				System.out.println(end);
				Date ed="".equals(end)?null:format.parse(end);
				linfo.setEndtime(ed);
				linfo.setLevel(cursor.getInt(cursor.getColumnIndex("tlevle")));
				litem.add(linfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cursor.close();
		db.close();
		return litem;
	}

}

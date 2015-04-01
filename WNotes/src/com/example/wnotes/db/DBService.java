package com.example.wnotes.db;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wnotes.Remind;

public class DBService extends SQLiteOpenHelper {
	private final static int DATABASE_VERSION = 4;
	private final static String DATABASE_NAME = "calendar.db";
	private SQLiteDatabase db = null;

	/**
	 * 创建数据库表，表名：t_records。 id ,主键自增 ；title,标题； content,内容； record_date
	 * ,记录日期；remind_time提醒日期；remind是否提醒；shake是否震动；ring是否响铃
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE [t_records] ([id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "  [title] VARCHAR(30) NOT NULL,  [content] TEXT,  [record_date] DATE NOT NULL,[remind_time] TIME,"
				+ "[remind] BOOLEAN,[shake] BOOLEAN,[ring] BOOLEAN)"
				+ ";CREATE INDEX [unique_title] ON [t_records] ([title]);"
				+ "CREATE INDEX [remind_time_index] ON [t_records] ([remind_time]);"
				+ "CREATE INDEX [record_date_index] ON [t_records] ([record_date]);"
				+ "CREATE INDEX [remind_index] ON [t_records] ([remind])";
		db.execSQL(sql);

	}

	public DBService(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 执行指定的sql语句
	 * 
	 * @param sql
	 * @return
	 */
	public Cursor execSQL(String sql) {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists [t_records]";
		db.execSQL(sql);
		sql = "CREATE TABLE [t_records] ([id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "  [title] VARCHAR(30) NOT NULL,  [content] TEXT,  [record_date] DATE NOT NULL,[remind_time] TIME,"
				+ "[remind] BOOLEAN,[shake] BOOLEAN,[ring] BOOLEAN)"
				+ ";CREATE INDEX [unique_title] ON [t_records] ([title]);"
				+ "CREATE INDEX [remind_time_index] ON [t_records] ([remind_time]);"
				+ "CREATE INDEX [record_date_index] ON [t_records] ([record_date]);"
				+ "CREATE INDEX [remind_index] ON [t_records] ([remind])";
		db.execSQL(sql);

	}

	public void insertRecord(String title, String content, String recordDate) {
		insertRecord(title, content, recordDate, null, false, false);
	}

	/**
	 * 插入一条记录
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param recordDate
	 *            记录时间
	 * @param remindTime
	 *            提醒时间
	 * @param shake
	 *            是否震动
	 * @param ring
	 *            是否响铃
	 */
	public void insertRecord(String title, String content, String recordDate,
			String remindTime, boolean shake, boolean ring) {
		try {
			String sql = "";
			String remind = "false";
			if (remindTime != null) {
				remind = "true";
			} else {
				remindTime = "0:0:0";
			}
			sql = "insert into t_records(title, content, record_date,remind_time, remind, shake, ring) values('"
					+ title
					+ "','"
					+ content
					+ "','"
					+ recordDate
					+ "','"
					+ remindTime
					+ "','"
					+ remind
					+ "','"
					+ shake
					+ "','"
					+ ring + "' );";
			closeDB();
			db = this.getWritableDatabase();
			db.execSQL(sql);
			closeDB();
		} catch (Exception e) {
			Log.d("error", e.getMessage());
		}

	}

	/**
	 * 根据指定的id删除记录
	 * 
	 * @param id
	 */
	public void deleteRecord(int id) {
		closeDB();
		String sql = "delete from t_records where id = " + id;
		db = this.getWritableDatabase();
		db.execSQL(sql);
		closeDB();
	}

	public void updateRecord(int id, String title, String content,
			String recordDate, String remindTime, boolean shake, boolean ring) {
		try {
			String sql = "";
			String remind = "false";
			if (remindTime != null) {
				remind = "true";
			} else {
				remindTime = "0:0:0";
			}
			if (recordDate == null) {
				Calendar calendar = Calendar.getInstance();
				recordDate = calendar.get(Calendar.YEAR) + "-"
						+ calendar.get(Calendar.MONTH) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH);
			}
			sql = "update t_records set title='" + title + "', content='"
					+ content + "' ,record_date='" + recordDate
					+ "' ,remind_time='" + remindTime + "', remind='" + remind
					+ "',shake='" + shake + "', ring='" + ring + "' where id="
					+ id;
			closeDB();
			db = this.getWritableDatabase();
			db.execSQL(sql);
			closeDB();
		} catch (Exception e) {
			Log.d("updateRecord", e.getMessage());
		}
	}

	// 获得id值最大的那个记录
	public int getMaxId() {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select max(id) from t_records", null);
		cursor.moveToFirst();
		int maxId = cursor.getInt(0);
		closeDB();
		return maxId;

	}
	/**
	 * 根据标题查询
	 * 
	 * @return
	 */
	public Cursor querydata(String title) {
		closeDB();
		String sql="select id,title, content, record_date,remind_time, remind, shake, ring from t_records ";
		if(title!=null&&!"".equals(title)){
			sql+=" where title like '%"+title+"%'";
		}
		sql+="order by id desc";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				sql, null);
		return cursor;
	}
	/**
	 * 查询所有的记录
	 * 
	 * @return
	 */
	public Cursor query() {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select id,title from t_records order by id desc", null);
		return cursor;
	}

	/**
	 * 根据指定的日期查询记录
	 * 
	 * @param date
	 *            指定的日期
	 * @return
	 */
	public Cursor query(String date) {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select id,title from t_records where record_date='" + date
						+ "' order by id desc", null);
		return cursor;
	}

	/**
	 * 根据指定的id查询记录
	 * 
	 * @param id
	 *            指定的记录id
	 * @return
	 */
	public Cursor query(int id) {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select  title,content,shake,ring from t_records where id="
						+ id, null);
		return cursor;
	}

	// 如果没有提醒消息，返回null
	public Remind getRemindMsg() {
		try {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = 0;
			String sql = "select title,shake,ring from t_records where record_date='"
					+ year
					+ "-"
					+ month
					+ "-"
					+ day
					+ "' and remind_time='"
					+ hour
					+ ":"
					+ minute
					+ ":"
					+ second
					+ "' and remind='true'";
			closeDB();
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			boolean flag = cursor.moveToNext();
			closeDB();
			if (flag) {
				String remindMsg = cursor.getString(0);
				sql = "update t_records set remind='false', shake='false', ring='false' where record_date='"
						+ year
						+ "-"
						+ month
						+ "-"
						+ day
						+ "' and remind_time='"
						+ hour
						+ ":"
						+ minute
						+ ":"
						+ second + "' and remind='true'";
				db = this.getWritableDatabase();
				db.execSQL(sql);
				Remind remind = new Remind();
				remind.msg = remindMsg;
				remind.date = calendar.getTime();
				remind.shake = Boolean.parseBoolean(cursor.getString(1));
				remind.ring = Boolean.parseBoolean(cursor.getString(2));
				closeDB();
				return remind;
			}
		} catch (Exception e) {
			Log.d("getRemindMsg", e.getMessage());
		}
		return null;
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		if (db != null) {
			db.close();
		}
		close();
	}
}


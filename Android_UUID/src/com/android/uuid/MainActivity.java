package com.android.uuid;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText uuidtext;
	private SQLiteDatabase db;
	private String dbpath = "/data/data/com.android.providers.settings/databases/settings.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		uuidtext = (EditText) this.findViewById(R.id.uuid);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	private void init() {
		rmountrw();
		try {
			db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
			db.beginTransaction();
			Cursor cursor = db.query("secure",
					new String[] { "name", "value" }, "name=?",
					new String[] { "android_id" }, null, null, null);
			if (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String value = cursor.getString(cursor.getColumnIndex("value"));
				uuidtext.setText(value);
			}
			cursor.close();
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "读取数据失败", Toast.LENGTH_LONG).show();
		}
		rmountro();
	}

	public void updateclick(View v) {
		rmountrw();
		try {
			db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
			db.beginTransaction();
			ContentValues cv = new ContentValues();
			cv.put("value", uuidtext.getText().toString());
			db.update("secure", cv, "name=?", new String[] { "android_id" });
			Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "修改数据失败", Toast.LENGTH_LONG).show();
		}
		rmountro();
	}

	private void rmountrw() {
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("su");
			OutputStream os = process.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeBytes("mount -o remount,rw -t yaffs2 " + getsystempath()+ "  \n");
			dos.flush();
			dos.writeBytes("chmod 777 /data\n");
			dos.flush();
			dos.writeBytes("chmod 777 /data/data\n");
			dos.flush();
			dos.writeBytes("chmod 777 /data/data/com.android.providers.settings\n");
			dos.flush();
			dos.writeBytes("chmod 777 /data/data/com.android.providers.settings/databases\n");
			dos.flush();
			dos.writeBytes("chmod 777 /data/data/com.android.providers.settings/databases/settings.db\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			process.waitFor();
			dos.close();
			Toast.makeText(this, "修改权限成功", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "你的手机还没有root ,失败...", Toast.LENGTH_LONG)
					.show();
		}

	}

	private void rmountro() {
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("su");
			OutputStream os = process.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeBytes("mount -o remount,ro-t yaffs2 " + getsystempath()+ "  \n");
			dos.flush();
			dos.writeBytes("chmod 771 /data\n");
			dos.flush();
			dos.writeBytes("chmod 771 /data/data\n");
			dos.flush();
			dos.writeBytes("chmod 751 /data/data/com.android.providers.settings\n");
			dos.flush();
			dos.writeBytes("chmod 771 /data/data/com.android.providers.settings/databases\n");
			dos.flush();
			dos.writeBytes("chmod 660 /data/data/com.android.providers.settings/databases/settings.db\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			process.waitFor();
			dos.close();
			Toast.makeText(this, "修改权限成功", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "你的手机还没有root ,失败...", Toast.LENGTH_LONG)
					.show();
		}

	}

	public String getsystempath() {
		String path = null;
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("mount");
			int i = process.waitFor();
			InputStream in = process.getInputStream();
			InputStreamReader bis = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(bis);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("/dev") && line.indexOf("/data") > 0) {
					path = line.substring(0, line.indexOf("data") + 4);
					System.out.println(path);
				}
			}
			br.close();
			bis.close();
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(db!=null){
			db.close();
		}
		
	}

}

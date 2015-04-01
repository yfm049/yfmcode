package com.example.wnotes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wnotes.db.DBService;

public class Main extends ListActivity {
	public static List<String> recordArray;
	public static ArrayAdapter<String> arrayAdapter;
	public static List<Integer> idList = new ArrayList<Integer>();
	public static ListActivity myListActivity;
	public static DBService dbService = null;// 数据访问对象
	public static MediaPlayer mediaPlayer;// 音乐播放器
	public static Vibrator vibrator;
	private MenuItem miNewRecord;
	private MenuItem miModifyRecord;
	private MenuItem miDeleteRecord;
	public AlarmManager am;// 消息管理者
	private EditText stext;
	private Button sbut;
	private OnEditRecordMenuItemClick editRecordMenuItemClick = new OnEditRecordMenuItemClick(
			this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main);
		stext=(EditText)super.findViewById(R.id.stext);
		sbut=(Button)super.findViewById(R.id.sbut);
		// 初始化数据访问对象
		if (dbService == null) {
			dbService = new DBService(this);
		}
		if (am == null) {
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
		}
		if (recordArray == null)
			recordArray = new ArrayList<String>();

		if (arrayAdapter == null)
			arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, recordArray);
		else
			arrayAdapter.clear();

		idList.clear();
		// 查询出所有的记录
		Cursor cursor = dbService.query();
		while (cursor.moveToNext()) {
			arrayAdapter.add(cursor.getString(1));
			idList.add(cursor.getInt(0));

		}
		// 设置标题日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		setTitle(sdf.format(calendar.getTime()));// 将标题设置为当前时间
		setListAdapter(arrayAdapter);
		myListActivity = null;
		myListActivity = this;
		try {
			Intent intent = new Intent(myListActivity, CallAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(myListActivity,
					0, intent, 0);
			am.setRepeating(AlarmManager.RTC, 0, 60 * 1000, sender);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getListView().setOnItemLongClickListener(new OnItemLongClickListenerImpl());
		sbut.setOnClickListener(new OnClickListenerImpl());
	}
	//点击搜索时触发
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String st=stext.getText().toString();
			initdate(st);
		}
		
	}
	//查询数据显示
	public void initdate(String title){
		idList.clear();
		// 查询出所有的记录
		arrayAdapter.clear();

		idList.clear();
		// 查询出所有的记录
		Cursor cursor = dbService.querydata(title);
		while (cursor.moveToNext()) {
			arrayAdapter.add(cursor.getString(1));
			idList.add(cursor.getInt(0));

		}
		setListAdapter(arrayAdapter);
	}
	//长按弹出删除对话框
	class OnItemLongClickListenerImpl implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int arg2, long arg3) {
			// TODO Auto-generated method stub
			Builder builder=new Builder(Main.this);
			builder.setTitle("删除");
			builder.setMessage("确定删除");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dbService.deleteRecord(idList.get(arg2));
					initdate(null);
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create().show();
			return true;
		}

	}
	// 当List中的item单击是调用该方法
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startEditRecordActivity(position);// 进入修改记录见面
	}

	// 添加新的记录
	class OnAddRecordMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Intent intent = new Intent(activity, Record.class);
			activity.startActivity(intent);
			return true;
		}

		public OnAddRecordMenuItemClick(Activity activity) {
			super(activity);
		}

	}

	// 修改记录
	public void startEditRecordActivity(int index) {
		Intent intent = new Intent(this, Record.class);
		intent.putExtra("edit", true);
		intent.putExtra("id", idList.get(index));
		intent.putExtra("index", index);
		startActivity(intent);
	}

	// 修改/查看记录
	class OnEditRecordMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Main allRecord = (Main) activity;

			int index = allRecord.getSelectedItemPosition();
			if (index < 0)
				return false;
			allRecord.startEditRecordActivity(index);
			return true;
		}

		public OnEditRecordMenuItemClick(Activity activity) {
			super(activity);
		}

	}

	// 删除记录信息
	class OnDeleteRecordMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener {

		public OnDeleteRecordMenuItemClick(Activity activity) {
			super(activity);
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			Main allRecord = (Main) activity;
			int index = allRecord.getSelectedItemPosition();
			System.out.println("删除：" + index);
			if (index < 0)
				return false;
			recordArray.remove(index);
			int id = idList.get(index);
			idList.remove(index);
			allRecord.setListAdapter(arrayAdapter);
			dbService.deleteRecord(id);
			return true;
		}

	}
	class OnMenuItemClickListenerImpl implements OnMenuItemClickListener{

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			// TODO Auto-generated method stub
			if(Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED){
				try {
					String name=System.currentTimeMillis()+".cvs";
					File file=new File(Environment.getExternalStorageDirectory(), name);
					if(!file.exists()){
						file.createNewFile();
					}
					FileOutputStream fos=new FileOutputStream(file);
					OutputStreamWriter osw=new OutputStreamWriter(fos);
					BufferedWriter bw=new BufferedWriter(osw);
					bw.write("id,title, content, record_date,remind_time, remind, shake, ring\r\n");
					Cursor cursor=dbService.querydata(null);
					while(cursor.moveToNext()){
						bw.write(cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2)+","+cursor.getString(3)+","+cursor.getString(4)+","+cursor.getString(5)+","+cursor.getString(6)+","+cursor.getString(7)+"\r\n");
					}
					cursor.close();
					bw.close();
					osw.close();
					fos.close();
					Toast.makeText(Main.this, "导出成功,已保存到sd卡，文件名为"+name, Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				Toast.makeText(Main.this, "SD存储卡不可用", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		
	}
	// 弹出菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		miNewRecord = menu.add(0, 1, 1, "添加记录");
		miNewRecord.setIcon(R.drawable.newrecord);
		miModifyRecord = menu.add(0, 2, 2, "修改/查看");
		miModifyRecord.setIcon(R.drawable.rrlist);
		miDeleteRecord = menu.add(0, 4, 4, "删除记录");
		miDeleteRecord.setIcon(R.drawable.festival);
		MenuItem miAbout = menu.add(0, 5, 5, "关于");
		MenuItem miHelp = menu.add(0, 6, 6, "帮助");
		MenuItem export = menu.add(0, 7, 7, "导出");
		export.setOnMenuItemClickListener(new OnMenuItemClickListenerImpl());
		miNewRecord.setOnMenuItemClickListener(new OnAddRecordMenuItemClick(
				this));

		miModifyRecord.setOnMenuItemClickListener(editRecordMenuItemClick);
		miDeleteRecord
				.setOnMenuItemClickListener(new OnDeleteRecordMenuItemClick(
						this));

		// 关于
		miAbout.setIcon(R.drawable.about);
		// 关于菜单添加事件
		miAbout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem mi) {
				Intent intent = new Intent(myListActivity, About.class);
				myListActivity.startActivity(intent);
				return true;
			}
		});

		// 帮助
		miHelp.setIcon(R.drawable.help);
		// 帮助菜单添加事件
		miHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem mi) {
				Intent intent = new Intent(myListActivity, Help.class);
				myListActivity.startActivity(intent);
				return true;
			}
		});
		return true;
	}

}
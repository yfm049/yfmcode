package com.pro.hyxx;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.pro.adapter.RehearseAdapter;
import com.pro.db.DbUtils;
import com.pro.pojo.Info;

public class RehearseActivity extends Activity {


	private EditText querytext;
	private Button querybut,add;
	private List<Info> li=new ArrayList<Info>();
	private GridView datagrid;
	private RehearseAdapter adapter;
	private DbUtils db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rehearse);
		querytext=(EditText)super.findViewById(R.id.querytext);
		querybut=(Button)super.findViewById(R.id.querybut);
		querybut.setOnClickListener(new OnClickListenerImpl());
		add=(Button)super.findViewById(R.id.add);
		db=new DbUtils(this);
		datagrid=(GridView)super.findViewById(R.id.datagrid);
		li.clear();
		li.addAll(db.getAllSzb(null));
		adapter=new RehearseAdapter(this, li);
		datagrid.setAdapter(adapter);
		datagrid.setOnItemLongClickListener(new OnItemLongClickListenerImpl());
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	class OnItemLongClickListenerImpl implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int pos, long arg3) {
			// TODO Auto-generated method stub
			showMenuDialog(li.get(pos));
			return true;
		}

		
		
	}
	
	public void showMenuDialog(final Info info){
		System.out.println(info.getId());
		Builder builder=new Builder(this);
		builder.setTitle("操作");
		builder.setItems(new String[]{"删除","修改"}, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int w) {
				// TODO Auto-generated method stub
				if(w==0){
					db.deleteszb(info);
					li.clear();
					li.addAll(db.getAllSzb(null));
					adapter.notifyDataSetChanged();
					Toast.makeText(RehearseActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				}else if(w==1){
					showUpdateDialog(info);
				}
			}
			
		});
		builder.create().show();
		
	}
	public void showUpdateDialog(final Info info){
		Builder builder=new Builder(this);
		builder.setTitle("修改");
		View view=LayoutInflater.from(this).inflate(R.layout.activity_add, null);
		final EditText adpinyin=(EditText)view.findViewById(R.id.adpinyin);
		final EditText adwenzi=(EditText)view.findViewById(R.id.adwenzi);
		final EditText jieshi=(EditText)view.findViewById(R.id.jieshi);
		adpinyin.setText(info.getPinyin());
		adwenzi.setText(info.getWenzi());
		jieshi.setText(info.getJieshi());
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String py=adpinyin.getText().toString();
				String wz=adwenzi.getText().toString();
				String js=jieshi.getText().toString();
				if(!"".equals(py)&&!"".equals(wz)){
					info.setPinyin(py);
					info.setWenzi(wz);
					info.setJieshi(js);
					db.updateszb(info);
					li.clear();
					li.addAll(db.getAllSzb(null));
					adapter.notifyDataSetChanged();
					Toast.makeText(RehearseActivity.this, "修改成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(RehearseActivity.this, "拼音或文字不能为空", Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

}

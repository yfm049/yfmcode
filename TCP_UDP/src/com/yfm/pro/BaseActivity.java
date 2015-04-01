package com.yfm.pro;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yfm.adapter.DataAdapter;

public class BaseActivity extends Activity {

	
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		showmenuDialog();
		return false;
	}
	private LinearLayout peizhi,about,hes;
	public Dialog menudialog,aboutdialog,listdialog;
	private ListView datalist;
	private DataAdapter dadapter;
	private List<String> ls;
	public SqliteUtils su=new SqliteUtils(this);
	private Button cleardata,closedialog;
	private void showmenuDialog(){
		if(menudialog==null){
			menudialog=new Dialog(this);
			menudialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view=LayoutInflater.from(this).inflate(R.layout.menu, null);
			hes=(LinearLayout)view.findViewById(R.id.hes);
			peizhi=(LinearLayout)view.findViewById(R.id.peizhi);
			about=(LinearLayout)view.findViewById(R.id.about);
			peizhi.setOnClickListener(new OnClickListenerImpl());
			about.setOnClickListener(new OnClickListenerImpl());
			hes.setOnClickListener(new OnClickListenerImpl());
			menudialog.setContentView(view);
		}
		menudialog.show();
		
	}
	public void setconfig(String name,String value){
		Editor editor=sp.edit();
		editor.putString(name, value);
		editor.commit();
	}
	public String getconfig(String name){
		return sp.getString(name, "");
	}
	private void showabout(){
		if(aboutdialog==null){
			aboutdialog=new Dialog(this);
			aboutdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view=LayoutInflater.from(this).inflate(R.layout.about, null);
			aboutdialog.setContentView(view);
			Utils.setDialog(this, aboutdialog);
		}
		aboutdialog.show();
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			
			if(but.getId()==R.id.peizhi){
				menudialog.cancel();
				setpeizhi();
			}
			if(but.getId()==R.id.about){
				menudialog.cancel();
				showabout();
			}
			if(but.getId()==R.id.hes){
				menudialog.cancel();
				show();
			}
			if(but.getId()==R.id.cleardata){
				su.deleteall();
				listdialog.dismiss();
			}
			if(but.getId()==R.id.closedialog){
				listdialog.dismiss();
			}
		}
		
	}
	public void show(){
		if(listdialog==null){
			listdialog=new Dialog(this);
			listdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view=LayoutInflater.from(this).inflate(R.layout.list, null);
			cleardata=(Button)view.findViewById(R.id.cleardata);
			closedialog=(Button)view.findViewById(R.id.closedialog);
			cleardata.setOnClickListener(new OnClickListenerImpl());
			closedialog.setOnClickListener(new OnClickListenerImpl());
			
			datalist=(ListView)view.findViewById(R.id.datalist);
			datalist.setOnItemClickListener(new OnItemClickListenerImpl());
			listdialog.setContentView(view);
			Utils.setDialog(this, listdialog);
		}
		
		ls=su.getall();
		dadapter=new DataAdapter(this, ls);
		datalist.setAdapter(dadapter);
		listdialog.show();
	}
	public void setpeizhi(){
		
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String p=ls.get(arg2);
			listdialog.dismiss();
			setvalue(p);
		}
		
	}
	public void setvalue(String msg){
		
	}
	
}

package com.pro.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pro.adapter.FileAdapter;
import com.pro.pojo.GameFile;
import com.pro.pojo.Simulator;
import com.pro.utils.FileThread;

public class FileActivity extends Activity {
	
	private Simulator slr;
	private List<GameFile> files=new ArrayList<GameFile>();
	private File rootfile=null,currdir=null;
	private ProgressDialog pd;
	private AlertDialog ad;
	private ListView filelist;
	private FileAdapter adapter;
	private OnItemClickListenerImpl listener;
	private TextView filepath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
		listener=new OnItemClickListenerImpl();
		slr=(Simulator)this.getIntent().getSerializableExtra("Simulator");
		
		filepath=(TextView)this.findViewById(R.id.filepath);
		filelist=(ListView)this.findViewById(R.id.filelist);
		adapter=new FileAdapter(this, files);
		filelist.setAdapter(adapter);
		filelist.setOnItemClickListener(listener);
		initRootFile(); 
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==100){
				pd.cancel();
				adapter.notifyDataSetChanged();
			}
		}
		
	};
	private void initRootFile(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			rootfile=new File(Environment.getExternalStorageDirectory(), "game");
			SearChFile(rootfile);
		}else{
			ShowAlertDialog("消息","未检测到SD卡");
		}
	}
	private void ShowProgressDialog(String title,String msg){
		pd=new ProgressDialog(this,ProgressDialog.THEME_HOLO_LIGHT);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.show();
	}
	private void ShowAlertDialog(String title,String msg){
		Builder builder=new Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.file_dialog_text, null);
		ad=builder.create();
		ad.show();
	}
	
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			GameFile file=files.get(pos);
			if(file.getFile().isDirectory()){
				SearChFile(file.getFile());
			}else{
				StartGame(file);
			}
		}
		
	}
	
	private void StartGame(GameFile file){
		String name=file.getSimulator().getName();
		String pname=file.getSimulator().getPackagename();
		String aname=file.getSimulator().getActivityname();
		String type=file.getSimulator().getType();
		Intent intent=new Intent();
		intent.setComponent(new ComponentName(pname, aname));
		if(isIntentAvailable(intent)){
			intent.setData(Uri.parse(file.getFile().getAbsolutePath()));
			if(type!=null&&!"".equals(type)){
				intent.setType(type);
			}
			if("PS".equals(name)){
				SetPs(intent,file);
			}
			if("ARCADE/FBA".equals(name)){
				SetAFBA(intent,file);
			}
			intent.setAction("android.intent.action.VIEW");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}else{
			ShowAlertDialog("消息", file.getSimulator().getName()+"模拟器不存在");
		}
	}
	
	
	private void SearChFile(File file){
		currdir=file;
		filepath.setText(currdir.getAbsolutePath().replace(rootfile.getAbsolutePath(), ""));
		ShowProgressDialog("消息", "正在搜索文件,请稍后...");
		new FileThread(file,slr,handler,files).start();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(currdir.equals(rootfile)){
			super.onBackPressed();
		}else{
			SearChFile(currdir.getParentFile());
		}
	}
	
	public boolean isIntentAvailable(Intent intent) {
		if(getPackageManager().resolveActivity(intent, 0) == null) {  
		   return false;
		}else{
			return true;
		}
	}
	private void SetPs(Intent intent,GameFile file){
		intent.putExtra("com.epsxe.ePSXe.isoName", Uri.parse(file.getFile().getAbsolutePath()).toString());
		intent.putExtra("com.epsxe.ePSXe.isoPath", Uri.parse(file.getFile().getParentFile().getAbsolutePath()).toString());
		intent.putExtra("com.epsxe.ePSXe.isoSlot", "0");
		intent.putExtra("com.epsxe.ePSXe.fcMode", "SELECT_ISO");
	}
	private void SetAFBA(Intent intent,GameFile file){
		String filename=file.getFile().getName();
		intent.putExtra("states", "/sdcard/aFBA/states");
		intent.putExtra("rom", filename.substring(0, filename.indexOf(".")));
		intent.putExtra("data", "/sdcard/aFBA");
		intent.putExtra("roms", Uri.parse(file.getFile().getParentFile().getAbsolutePath()).toString());
		intent.putExtra("buttons", 4);
		intent.putExtra("screenH", 224);
		intent.putExtra("screenW", 304);
		
	}
}

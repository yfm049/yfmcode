package com.pro.yyl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import shidian.tv.sntv.tools.DbUtils;
import shidian.tv.sntv.tools.DialogUtils;
import shidian.tv.sntv.tools.PhoneInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;


public class MainActivity extends Activity {

	private List<PhoneInfo> lpn=new ArrayList<PhoneInfo>();
	private ListView datalist;
	private PhoneAdapter adapter;
	private boolean isExit = false;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datalist=(ListView)this.findViewById(R.id.datalist);
        adapter=new PhoneAdapter(this, lpn);
        datalist.setAdapter(adapter);
        datalist.setOnCreateContextMenuListener(new ListenerImpl());
    }

    public void add(View v){
    	Intent intent=new Intent(this,LoginActivity.class);
    	startActivity(intent);
    }
    public void start(View v){
    	Intent intent=new Intent(this,StartActivity.class);
    	startActivity(intent);
    }
    public void record(View v){
    	Intent intent=new Intent(this,RecordActivity.class);
    	startActivity(intent);
    }
    
    

    public synchronized void export(View v){
    	DialogUtils.showDialog(this, "导出中...");
    	Map<String, String> ms=DbUtils.getInstance(this).exportphone();
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		File file=new File(Environment.getExternalStorageDirectory(), sdf.format(new Date())+" 手机号码.csv");
    		FileOutputStream fos=null;
    		OutputStreamWriter osw=null;
    		BufferedWriter bw=null;
    		try {
				fos=new FileOutputStream(file);
				osw=new OutputStreamWriter(fos, "UTF-8");
				bw=new BufferedWriter(osw);
				Set<String> keys=ms.keySet();
				int i=1;
				for(String key:keys){
					bw.write("\""+i+"\",\""+key+"\",\""+ms.get(key)+"\"\r\n");
					i++;
				}
				DialogUtils.ShowToast(this, "导出成功文件名称是"+file.getAbsolutePath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogUtils.ShowToast(this, "导出失败");
			}finally{
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(osw!=null){
					try {
						osw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
    	}else{
    		DialogUtils.ShowToast(this, "存储卡不存在");
    	}
    	DialogUtils.closeDialog();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	DbUtils.getInstance(this).getAllUser(lpn,null);
    	adapter.notifyDataSetChanged();
    }
    
    class ListenerImpl implements OnCreateContextMenuListener{


		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			AdapterContextMenuInfo menuinfo= (AdapterContextMenuInfo)menuInfo;
			PhoneInfo pi=lpn.get(menuinfo.position);
			menu.setHeaderTitle("操作");
			menu.add(0,1,0,"删除");
			if(pi.getState().equals("可用")){
				menu.add(0,2,0,"禁用");
			}else{
				menu.add(0,2,0,"可用");
			}
			
		}
    	
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	AdapterContextMenuInfo menuinfo= (AdapterContextMenuInfo)item.getMenuInfo();
    	PhoneInfo pi=lpn.get(menuinfo.position);
    	int itemid=item.getItemId();
    	System.out.println(itemid);
    	if(itemid==1){
    		DbUtils.getInstance(this).deleUser(pi);
    		lpn.remove(pi);
    		adapter.notifyDataSetChanged();
    		DialogUtils.ShowToast(this, "删除成功");
    	}else if(itemid==2){
    		pi.setState(item.getTitle().toString());
    		DbUtils.getInstance(this).addUser(pi);
    		adapter.notifyDataSetChanged();
    		DialogUtils.ShowToast(this, "修改成功");
    	}
    	
    	return super.onContextItemSelected(item);
    }
    private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==0){
				isExit = false;
			}
		}
		
	};
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		exit();
	}


	private void exit() {
        if (!isExit) {
            isExit = true;
            DialogUtils.ShowToast(this, "再按一次退出程序");
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }

}

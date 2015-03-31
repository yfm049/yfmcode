package com.pro.appinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.adapter.AppAdapter;
import com.pro.model.Appinfo;
import com.pro.net.HttpUtils;
import com.pro.utils.Utils;
//软件列表显示
public class AppListActivity extends Activity {

	private ListView datalist;
	private List<Appinfo> fl = new ArrayList<Appinfo>();
	private AppAdapter adapter;
	private ProgressDialog dialog;
	private String uri = "app!listapp.action";
	private PackageManager  pm;
	private EditText sname;
	private ImageView sbut,download;
	private List<NameValuePair> lnp=new ArrayList<NameValuePair>();
	private int id;
	private TextView name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app);
		pm=this.getPackageManager();
		datalist = (ListView) super.findViewById(R.id.datalist);
		name=(TextView)super.findViewById(R.id.name);
		sname=(EditText)super.findViewById(R.id.sname);
		sbut=(ImageView)super.findViewById(R.id.sbut);
		download=(ImageView)super.findViewById(R.id.download);
		download.setOnClickListener(new OnClickListenerImpl());
		sbut.setOnClickListener(new OnClickListenerImpl());
		super.findViewById(R.id.sname);
		adapter = new AppAdapter(this, fl);
		datalist.setAdapter(adapter);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			lnp.clear();
			id = bundle.getInt("id", -1);
			lnp.add(new BasicNameValuePair("fid", String.valueOf(id)));
			String nm=bundle.getString("name");
			if(nm!=null&&!"".equals(nm)){
				name.setText(name.getText()+"-"+nm);
			}
		}
	}
	//按钮点击事件
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.sbut){
				lnp.clear();
				String name=sname.getText().toString();
				lnp.add(new BasicNameValuePair("name", name));
				if(id!=-1){
					lnp.add(new BasicNameValuePair("fid", String.valueOf(id)));
				}
				getDate();
			}
			if(but.getId()==R.id.download){
				Intent intent=new Intent(AppListActivity.this,DownLoadActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				AppListActivity.this.startActivity(intent);
			}
			
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDate();
	}
	private void getDate(){
		dialog = new ProgressDialog(this);
		dialog.setMessage("正在获取应用列表");
		dialog.show();
		new GetDate().start();
	}
	//更新数据列表显示
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dialog.cancel();
			adapter.notifyDataSetChanged();
			if (msg.what != 1) {
				Toast.makeText(AppListActivity.this, "获取数据失败",
						Toast.LENGTH_SHORT).show();
			} 
		}

	};
	//用户获取软件列表数据
	class GetDate extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String html = HttpUtils.PostCon(uri,lnp);
			if (html != null) {
				List<Appinfo> lf = Utils.getallApp(html);
				fl.clear();
				fl.addAll(lf);
				isupdate(fl);
				handler.sendEmptyMessage(1);
			} else {
				fl.clear();
				handler.sendEmptyMessage(2);
			}
		}

	}
	//判断软件是否有更新
	public void isupdate(List<Appinfo> fl) {
		if(fl!=null&&fl.size()>0){
			for(int i=0;i<fl.size();i++){
				Appinfo info=fl.get(i);
				try {
					PackageInfo pi=pm.getPackageInfo(info.getPkname(), PackageManager.SIGNATURE_MATCH);
					if(pi.versionCode<info.getVersion()){
						info.setZt(2);
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					Log.i("type", "未找到");
				}
			}
		}
		
	}

}

package com.iptv.pro;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.iptv.adapter.FenleiAdapter;
import com.iptv.dlerbh.R;
import com.iptv.utils.LiveTV;
import com.iptv.utils.TvInfo;
import com.iptv.utils.TvListThread;
import com.iptv.utils.User;

public class FenleiActivity extends Activity {

	private GridView fenlei;
	private FenleiAdapter adapter;
	private IptvApp iptv;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_fenlei);
		iptv=(IptvApp)this.getApplication();
		fenlei=(GridView)super.findViewById(R.id.fenlei);
		adapter=new FenleiAdapter(this, iptv.getUser());
		fenlei.setAdapter(adapter);
		fenlei.setOnItemClickListener(new OnItemClickListenerImpl());
	}
	class OnItemClickListenerImpl implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			iptv.getUser().setCurrlivetv(arg2);
			LiveTV livetv=iptv.getUser().getLivetvlist().get(arg2);
			new TvListThread(handler, livetv.getUrl()).start();
		}
		
	}
	private void show(){
		pd=new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle("获取");
		pd.setMessage("正在进行获取播放列表,请稍后...");
		pd.show();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==9){
				show();
			}else if(msg.what==10){
				pd.cancel();
				List<TvInfo> tvlist=(List<TvInfo>)msg.obj;
				if(tvlist==null){
					Toast.makeText(FenleiActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
				}else if(tvlist.size()<=0){
					Toast.makeText(FenleiActivity.this, "此分类下无节目", Toast.LENGTH_SHORT).show();
				}else{
					iptv.getUser().getTvlist().clear();
					iptv.getUser().getTvlist().addAll(tvlist);
					Intent intent=new Intent(FenleiActivity.this,PlayActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					FenleiActivity.this.startActivity(intent);
				}
			}
			
		}
		
	};
	



}

package com.android.smsserver;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.adapter.ClientAdapter;
import com.android.model.Client;
import com.android.thread.RequestThread;
import com.android.utils.Utils;
import com.android.view.PullToRefreshView;
import com.android.view.PullToRefreshView.OnFooterRefreshListener;
import com.android.view.PullToRefreshView.OnHeaderRefreshListener;

public class MainActivity extends Activity {

	
	private PullToRefreshView refresh_container;
	private ListView clientlist;
	private ListenerImpl listener;
	private ClientAdapter clientadapter;
	private String clienturl="control/server!GetClientList.action";
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Utils.getIntConfig(this, "id", -1)==-1){
			Intent intent=new Intent(this,LoginActivity.class);
			this.startActivity(intent);
		}
		setContentView(R.layout.activity_main);
		listener=new ListenerImpl();
		clientlist=(ListView)this.findViewById(R.id.clientlist);
		refresh_container=(PullToRefreshView)this.findViewById(R.id.refresh_container);
		clientadapter=new ClientAdapter(this, Utils.clients);
		clientlist.setAdapter(clientadapter);
		clientlist.setOnItemClickListener(listener);
		refresh_container.setOnHeaderRefreshListener(listener);
		refresh_container.setOnFooterRefreshListener(listener);
		pd=Utils.createProgressDialog(this);
		pd.setMessage("正在请求数据");
		pd.show();
		GetClientList();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		clientadapter.notifyDataSetChanged();
	}
	class ListenerImpl implements OnHeaderRefreshListener,OnFooterRefreshListener,OnItemClickListener{

		

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			Utils.client=Utils.clients.get(pos);
			Intent intent=new Intent(MainActivity.this,InfoActivity.class);
			MainActivity.this.startActivity(intent);
		}

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			// TODO Auto-generated method stub
			GetClientList();
		}

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			// TODO Auto-generated method stub
			refresh_container.onFooterRefreshComplete();
		}
		
	}
	private void GetClientList(){
		List<NameValuePair> lvp=new ArrayList<NameValuePair>();
		lvp.add(new BasicNameValuePair("mapparam.userid", String.valueOf(Utils.getIntConfig(this, "id", 0))));
		RequestThread rt=new RequestThread(clienturl,lvp,handler);
		rt.start();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				pd.cancel();
				if(msg.what==1){
					refresh_container.onHeaderRefreshComplete();
					if(msg.obj!=null){
						Utils.clients.clear();
						JSONObject jo=(JSONObject)msg.obj;
						JSONArray data=jo.getJSONArray("data");
						for(int i=0;i<data.length();i++){
							JSONObject c=data.getJSONObject(i);
							Client client=new Client();
							client.setId(c.getInt("id"));
							client.setUserid(c.getInt("userid"));
							client.setClientname(c.getString("clientname"));
							client.setClientid(c.getString("clientid"));
							client.setDeviceimei(c.getString("deviceimei"));
							client.setPhone(c.getString("phone"));
							client.setEndtime(c.getString("endtime"));
							client.setEmail(c.getString("email"));
							client.setState(c.getInt("state"));
							Utils.clients.add(client);
						}
						clientadapter.notifyDataSetChanged();
					}else{
						Utils.ShowToast(MainActivity.this, "请求服务器错误");
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(MainActivity.this, "数据错误");
			}
		}
		
	};


}

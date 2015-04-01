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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.android.adapter.PhotoAdapter;
import com.android.model.Call;
import com.android.model.Client;
import com.android.model.Page;
import com.android.model.Photo;
import com.android.thread.RequestThread;
import com.android.utils.Utils;
import com.android.view.PullToRefreshView;
import com.android.view.PullToRefreshView.OnFooterRefreshListener;
import com.android.view.PullToRefreshView.OnHeaderRefreshListener;

public class PhotoActivity extends Activity {

	private PullToRefreshView refresh_container;
	private ListView infodata;
	private ListenerImpl listener;
	private List<Photo> list=new ArrayList<Photo>();
	private PhotoAdapter adapter;
	private Page page=new Page();
	private String clienturl="control/server!DevicePhotoList.action";
	private ProgressDialog pd;
	private Button setbut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Utils.getIntConfig(this, "id", -1)==-1){
			Intent intent=new Intent(this,LoginActivity.class);
			this.startActivity(intent);
		}
		setContentView(R.layout.activity_photo);
		setbut=(Button)findViewById(R.id.setbut);
		listener=new ListenerImpl();
		infodata=(ListView)this.findViewById(R.id.infodata);
		refresh_container=(PullToRefreshView)this.findViewById(R.id.refresh_container);
		adapter=new PhotoAdapter(this, list);
		infodata.setAdapter(adapter);
		refresh_container.setOnHeaderRefreshListener(listener);
		refresh_container.setOnFooterRefreshListener(listener);
		setbut.setOnClickListener(listener);
		pd=Utils.createProgressDialog(this);
		pd.setMessage("正在请求数据");
		pd.show();
		GetDataList(1,1);
	}
	class ListenerImpl implements OnHeaderRefreshListener,OnFooterRefreshListener,OnClickListener{

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			// TODO Auto-generated method stub
			GetDataList(1,1);
		}

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			// TODO Auto-generated method stub
			if(page.hasnextpage()){
				GetDataList(2,page.getNextPage());
			}else{
				refresh_container.onFooterRefreshComplete();
			}
			
			
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(PhotoActivity.this,PhotoSetActivity.class);
			startActivity(intent);
		}
		
	}
	private void GetDataList(int what,int page){
		List<NameValuePair> lvp=new ArrayList<NameValuePair>();
		lvp.add(new BasicNameValuePair("mapparam.deviceid", String.valueOf(Utils.client.getId())));
		lvp.add(new BasicNameValuePair("page.cpage", String.valueOf(page)));
		RequestThread rt=new RequestThread(clienturl,lvp,handler,what);
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
						list.clear();
						JSONObject jo=(JSONObject)msg.obj;
						JSONObject p=jo.getJSONObject("page");
						page.setCpage(p.getInt("cpage"));
						page.setTpage(p.getInt("tpagenum"));
						JSONArray data=jo.getJSONArray("data");
						for(int i=0;i<data.length();i++){
							JSONObject c=data.getJSONObject(i);
							 
							Photo item=new Photo();
							item.setTime(c.getString("phototime"));
							item.setFile(c.getString("file"));
							list.add(item);
						}
						adapter.notifyDataSetChanged();
					}else{
						Utils.ShowToast(PhotoActivity.this, "请求服务器错误");
					}
				}
				if(msg.what==2){
					refresh_container.onFooterRefreshComplete();
					if(msg.obj!=null){
						JSONObject jo=(JSONObject)msg.obj;
						JSONObject p=jo.getJSONObject("page");
						page.setCpage(p.getInt("cpage"));
						page.setTpage(p.getInt("tpagenum"));
						JSONArray data=jo.getJSONArray("data");
						for(int i=0;i<data.length();i++){
							JSONObject c=data.getJSONObject(i);
							Photo item=new Photo();
							item.setTime(c.getString("phototime"));
							item.setFile(c.getString("file"));
							list.add(item);
						}
						adapter.notifyDataSetChanged();
					}else{
						Utils.ShowToast(PhotoActivity.this, "请求服务器错误");
					}
				}
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.ShowToast(PhotoActivity.this, "数据错误");
			}
		}
		
	};


}

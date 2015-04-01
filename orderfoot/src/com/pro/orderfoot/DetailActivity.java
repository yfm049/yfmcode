package com.pro.orderfoot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pro.adapter.DetailListAdapter;
import com.pro.task.CallThread;
import com.pro.task.SendThread;
import com.pro.utils.ComUtils;
import com.pro.utils.SqlUtils;

public class DetailActivity extends Activity {

	

	private ListView detail_list;
	private DetailListAdapter adapter;
	private SqlUtils su;
	private TextView tprice,yhprice,sjprice,xuanzhuo;
	private String totalprice;
	
	private List<Map<String, Object>> order;
	
	private Button queren,fanhui,quxiao,exit;
	private ImageButton hujiao,tomealbut;
	private ListenerImpl listener;
	private SharedPreferences sp;
	private AlertDialog dialog;
	private ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_detail);
		listener=new ListenerImpl();
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		su=new SqlUtils(this);
		
		detail_list=(ListView)this.findViewById(R.id.detail_list);
		order=new ArrayList<Map<String,Object>>();
		adapter=new DetailListAdapter(this,order,datahandler);
		detail_list.setAdapter(adapter);
		xuanzhuo=(TextView)this.findViewById(R.id.xuanzhuo);
		xuanzhuo.setText(MealActivity.desknum);
		
		tprice=(TextView)this.findViewById(R.id.tprice);
		yhprice=(TextView)this.findViewById(R.id.yhprice);
		sjprice=(TextView)this.findViewById(R.id.sjprice);
		queren=(Button)this.findViewById(R.id.queren);
		fanhui=(Button)this.findViewById(R.id.fanhui);
		quxiao=(Button)this.findViewById(R.id.quxiao);
		exit=(Button)this.findViewById(R.id.exit);
		hujiao=(ImageButton)this.findViewById(R.id.hujiao);
		tomealbut=(ImageButton)this.findViewById(R.id.tomealbut);
		
		queren.setOnClickListener(listener);
		fanhui.setOnClickListener(listener);
		quxiao.setOnClickListener(listener);
		exit.setOnClickListener(listener);
		hujiao.setOnClickListener(listener);
		tomealbut.setOnClickListener(listener);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datahandler.sendEmptyMessage(100);
	}
	private void InitData(){
		String sql="select *,(price*DisCount/100) as yh,(price*shuliang) as totalprice from mealorder where OrderId=?";
		List<Map<String, Object>> orders=su.Search(sql, new String[]{MealActivity.ordernum});
		order.clear();
		order.addAll(orders);
		adapter.notifyDataSetChanged();
		sql="select sum(price*shuliang*DisCount/100) as yh,sum(price*shuliang) as totalprice from mealorder where OrderId=? group by OrderId";
		List<Map<String, Object>> total=su.Search(sql, new String[]{MealActivity.ordernum});
		if(total.size()>0){
			Map<String, Object> ts=total.get(0);
			tprice.setText("消费金额 ¥ "+ts.get("totalprice").toString());
			yhprice.setText("优惠金额 ¥ "+ts.get("yh").toString());
			sjprice.setText("实际金额 ¥ "+ts.get("totalprice").toString());
			totalprice=ts.get("totalprice").toString();
		}else{
			tprice.setText("消费金额 ¥ 0");
			yhprice.setText("优惠金额 ¥ 0");
			sjprice.setText("实际金额 ¥ 0");
			totalprice="0";
		}
	}
	private Handler datahandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==100){
				InitData();
			}
		}
		
	};
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.dismiss();
			if(msg.what==100){
				ShowDialog("消息", msg.obj.toString());
			}
		}
		
	};
	public void ShowDialog(String title,String msg){
		Builder builder=ComUtils.CreateAlertDialog(this, title, msg);
		builder.setPositiveButton("知道了", null);
		dialog=builder.create();
		dialog.show();
	}
	class ListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.queren){
				if("0".equals(totalprice)){
					ShowDialog("消息", "你还没有点餐");
				}else{
					String ip=sp.getString("ip", "192.168.0.1");
					int port=sp.getInt("port", 80);
					String pass=sp.getString("pass", "192.168.0.1");
					pd=ComUtils.createProgressDialog(DetailActivity.this, "消息", "正在提交订单");
					pd.show();
					SendThread thread=new SendThread(DetailActivity.this, handler, ip, port, pass, totalprice);
					thread.start();
				}
			}
			if(v.getId()==R.id.fanhui){
				DetailActivity.this.finish();
			}
			if(v.getId()==R.id.quxiao){
				DeleteOrder();
			}
			if(v.getId()==R.id.hujiao){
				if("".equals(MealActivity.desknum)){
					ShowDialog("消息", "你还没有选桌");
				}else{
					pd=ComUtils.createProgressDialog(DetailActivity.this, "消息", "呼叫中...");
					pd.show();
					String ip=sp.getString("ip", "192.168.0.1");
					int port=sp.getInt("port", 80);
					String pass=sp.getString("pass", "192.168.0.1");
					new CallThread(DetailActivity.this, handler, ip, port, pass, MealActivity.desknum);
				}
			}
			if(v.getId()==R.id.tomealbut){
				DetailActivity.this.finish();
			}
			if(v.getId()==R.id.exit){
				Builder builder=ComUtils.CreateAlertDialog(DetailActivity.this, "退出", "退出后您已点的菜将消失。确定要退出吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				});
				builder.create().show();
			}
		}

	}
	public void DeleteOrder(){
		String sql="delete from mealorder where OrderId=?";
		SQLiteDatabase db=su.getWritableDatabase();
		db.execSQL(sql, new Object[]{MealActivity.ordernum});
		db.close();
		MealActivity.ordernum="";
		MealActivity.desknum="";
		this.finish();
	}
	
}

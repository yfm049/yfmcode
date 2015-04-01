package com.pro.orderfoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pro.adapter.DeskListAdapter;
import com.pro.adapter.MealGridAdapter;
import com.pro.adapter.MealListAdapter;
import com.pro.task.CallThread;
import com.pro.utils.ComUtils;
import com.pro.utils.SqlUtils;

public class MealActivity extends Activity {


	
	private List<Map<String, Object>> lmi;
	private List<Map<String, Object>> desks;
	private MealGridAdapter gridadapter;
	private MealListAdapter listadapter;
	private GridView mealgrid;
	private ListView xiadanlist,desklist;
	private Button xiadanbut,xuanzhuo;
	private ListenerImpl listener;
	private List<Map<String, Object>> ddlist;
	private TextView zongjia;
	private LinearLayout buttons;
	private SqlUtils su;
	private ImageButton hujiao,gouwuche;
	private DeskListAdapter adapter;
	
	public static String ordernum="";
	public static String desknum="";
	private AlertDialog dialog;
	private ProgressDialog pd;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_meal);
		sp=this.getSharedPreferences("config", MODE_PRIVATE);
		su=new SqlUtils(this);
		listener = new ListenerImpl();
		
		buttons=(LinearLayout)this.findViewById(R.id.buttons);
		mealgrid = (GridView) this.findViewById(R.id.mealgrid);
		xiadanlist=(ListView) this.findViewById(R.id.xiadanlist);
		xiadanbut = (Button) this.findViewById(R.id.xiadanbut);
		xuanzhuo = (Button) this.findViewById(R.id.xuanzhuo);
		
		
		zongjia=(TextView)this.findViewById(R.id.zongjia);
		hujiao=(ImageButton)this.findViewById(R.id.hujiao);
		gouwuche=(ImageButton)this.findViewById(R.id.gouwuche);
		hujiao.setOnClickListener(listener);
		gouwuche.setOnClickListener(listener);
		xuanzhuo.setOnClickListener(listener);
		
		initAdapter();
		initbuttonsview();
	}
	
	public void initAdapter(){
		ddlist=new ArrayList<Map<String, Object>>();
		listadapter=new MealListAdapter(this, ddlist,zongjia);
		xiadanlist.setAdapter(listadapter);
		lmi = new ArrayList<Map<String, Object>>();
		
		gridadapter = new MealGridAdapter(this, lmi,listadapter);
		mealgrid.setAdapter(gridadapter);
		mealgrid.setOnItemClickListener(listener);
		xiadanbut.setOnClickListener(listener);
		
		desks=new ArrayList<Map<String, Object>>();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		listadapter.notifyDataSetChanged();
	}
	public void initbuttonsview(){
		xuanzhuo.setText(desknum);
		
		buttons.removeAllViews();
		Map<String, Object> all=new HashMap<String, Object>();
		all.put("TypeName", "全部");
		all.put("Id", "-1");
		String sql="select * from ProductType";
		List<Map<String, Object>> ProductTypes=su.Search(sql, new String[]{});
		ProductTypes.add(0, all);
		for(Map<String, Object> mso:ProductTypes){
			LinearLayout buttonlayout=(LinearLayout)LayoutInflater.from(this).inflate(R.layout.activity_fenleibutton, null);
			Button but=(Button)buttonlayout.findViewById(R.id.flbutton);
			but.setText(mso.get("TypeName").toString());
			but.setTag(mso);
			but.setOnClickListener(listener);
			buttons.addView(buttonlayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		sql="select * from Desk";
		List<Map<String, Object>> sdesks=su.Search(sql, new String[]{});
		desks.clear();
		desks.addAll(sdesks);
		sql="select * from Product";
		List<Map<String, Object>> Products=su.Search(sql,new String[]{} );
		lmi.clear();
		lmi.addAll(Products);
		gridadapter.notifyDataSetChanged();
		
	}
	
	
	class ListenerImpl implements OnClickListener,OnItemClickListener {

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.xiadanbut){
				if(ddlist.size()>0){
					ToDetailActivity();
				}else{
					ShowDialog("消息", "你还没有点餐");
				}
				
			}
			if(v.getId()==R.id.flbutton){
				Button but=(Button)v;
				
				Map<String, Object> mso=(Map<String, Object>)but.getTag();
				String id=mso.get("Id").toString();
				List<Map<String, Object>> Products=null;
				if("-1".equals(id)){
					String sql="select * from Product";
					Products=su.Search(sql,new String[]{} );
				}else{
					String sql="select * from Product where ProductTypeId=?";
					Products=su.Search(sql,new String[]{mso.get("Id").toString()} );
				}
				
				lmi.clear();
				lmi.addAll(Products);
				gridadapter.notifyDataSetChanged();
			}
			if(v.getId()==R.id.xuanzhuo){
				showZhuohaoDialog();
			}
			if(v.getId()==R.id.hujiao){
				if("".equals(desknum)){
					ShowDialog("消息", "你还没有选桌");
				}else{
					pd=ComUtils.createProgressDialog(MealActivity.this, "消息", "呼叫中...");
					pd.show();
					String ip=sp.getString("ip", "192.168.0.1");
					int port=sp.getInt("port", 80);
					String pass=sp.getString("pass", "192.168.0.1");
					new CallThread(MealActivity.this, handler, ip, port, pass, desknum);
				}
			}
			
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			if(view.getId()==R.id.deskid){
				Map<String, Object> desk=desks.get(arg2);
				ordernum=String.valueOf(System.currentTimeMillis()+"_"+desk.get("Id"));
				desknum=desk.get("DeskName").toString();
				xuanzhuo.setText(desknum);
				dialog.dismiss();
			}
		}

	}
	public void showZhuohaoDialog(){
		View view=LayoutInflater.from(this).inflate(R.layout.activity_xuanzhuo_dialog, null);
		desklist=(ListView)view.findViewById(R.id.deskid);
		adapter=new DeskListAdapter(this, desks);
		desklist.setAdapter(adapter);
		desklist.setOnItemClickListener(listener);
		Builder builder=ComUtils.CreateAlertDialog(this,"选择餐桌",view);
		dialog=builder.create();
		dialog.show();
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.dismiss();
			if(msg.what==100){
				ShowDialog("呼叫结果", msg.obj.toString());
			}
		}
		
	};
	public void ShowDialog(String title,String msg){
		Builder builder=ComUtils.CreateAlertDialog(this, title, msg);
		builder.setPositiveButton("知道了", null);
		dialog=builder.create();
		dialog.show();
	}
	
	public void ToDetailActivity(){
		Intent intent=new Intent(this,DetailActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}
	
}

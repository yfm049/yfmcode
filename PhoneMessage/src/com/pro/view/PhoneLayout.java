package com.pro.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.model.Tel_query;
import com.pro.model.Tel_text;
import com.pro.phonemessage.R;
import com.pro.utils.ComUtils;
import com.pro.utils.SqlUtils;

public class PhoneLayout extends LinearLayout {

	private Button phonesearch,productsearch,update;
	private EditText phonenum,product;
	private ListenerImpl listener;
	private TextView con;
	private Context context;
	private ProgressBar progress;
	
	public PhoneLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		// TODO Auto-generated constructor stub
		View view=LayoutInflater.from(context).inflate(R.layout.phone, this);
		listener=new ListenerImpl();
		phonesearch=(Button)view.findViewById(R.id.more);
		productsearch=(Button)view.findViewById(R.id.productsearch);
		update=(Button)view.findViewById(R.id.update);
		phonenum=(EditText)view.findViewById(R.id.phonenum);
		product=(EditText)view.findViewById(R.id.product);
		con=(TextView)view.findViewById(R.id.con);
		
		progress=(ProgressBar)view.findViewById(R.id.progress);
		
		update.setOnClickListener(listener);
		phonesearch.setOnClickListener(listener);
		productsearch.setOnClickListener(listener);
	}
	public void update(){
		Intent intent=new Intent("com.pro.phonemessage.download");
		context.sendBroadcast(intent);
	}
	public void setprogressvalue(int i){
		progress.setProgress(i);
	}
	public void setvalue(String pn,String pcon){
		phonenum.setText(pn);
		con.setText(pcon);
	}

	public void phonesearch(){
		String num=phonenum.getText().toString();
		if(!"".equals(num)){
			SqlUtils su=new SqlUtils(context);
			List<Tel_text> lts=su.queryTel_text(num);
			if(lts.size()>0){
				String txt="";
				for(Tel_text tt:lts){
					txt+=tt.getText()+"\r\n";
				}
				con.setText(txt);
			}else{
				con.setText("未查询到任何信息");
				ComUtils.showToast(context,"未查询到任何信息");
			}
		}else{
			ComUtils.showToast(context,"号码不能为空");
		}
	}
	public void productsearch(){
		String pt=product.getText().toString();
		if(!"".equals(pt)){
			SqlUtils su=new SqlUtils(context);
			List<Tel_query> ltq=su.queryTel_query(pt);
			if(ltq.size()>0){
				String txt="";
				for(Tel_query tq:ltq){
					txt+=tq.getText()+"\r\n";
				}
				con.setText(txt);
			}else{
				con.setText("");
				ComUtils.showToast(context,"未查询到任何信息");
			}
		}else{
			ComUtils.showToast(context,"不能为空");
		}
	}
	class ListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.update){
				update();
			}
			if(but.getId()==R.id.more){
				phonesearch();
			}
			if(but.getId()==R.id.productsearch){
				productsearch();
			}
		}
	}
	
}

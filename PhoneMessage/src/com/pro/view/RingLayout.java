package com.pro.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.model.Tel_text;
import com.pro.phonemessage.MainActivity;
import com.pro.phonemessage.R;
import com.pro.utils.ComUtils;
import com.pro.utils.SqlUtils;

public class RingLayout extends LinearLayout {

	private Button more;
	private TextView phone;
	private ListenerImpl listener;
	private TextView con;
	private Context context;
	private String pn;
	private String txt;
	
	public RingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		// TODO Auto-generated constructor stub
		View view=LayoutInflater.from(context).inflate(R.layout.ring, this);
		listener=new ListenerImpl();
		more=(Button)view.findViewById(R.id.more);
		phone=(TextView)view.findViewById(R.id.phone);
		con=(TextView)view.findViewById(R.id.con);
		more.setOnClickListener(listener);
	}
	public void SetPhone(String pn){
		this.pn=pn;
		phone.setText(pn);
		SqlUtils su=new SqlUtils(context);
		List<Tel_text> lts=su.queryTel_text(pn);
		txt="";
		if(lts.size()>0){
			for(Tel_text tt:lts){
				txt+=tt.getText()+"\r\n";
			}
			con.setText(txt);
		}else{
			con.setText("未查询到任何信息");
			ComUtils.showToast(context,"未查询到任何信息");
		}
	}

	class ListenerImpl implements OnClickListener{

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if(but.getId()==R.id.more){
				SendcloseWindow();
				Intent intent=new Intent(context,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("pn", pn);
				intent.putExtra("pcon", txt);
				context.startActivity(intent);
			}
		}
	}
	public void SendcloseWindow(){
		Intent close=new Intent("com.pro.phonemessage.closewindow");
		context.sendBroadcast(close);
	}
	
}

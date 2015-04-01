package com.xhplovewx.tixing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xhplovewx.pojo.ItemInfo;

public class AddActivity extends Activity {

	private int type;
	private EditText title,content,content2,firsttime,endtime;
	private RadioGroup level;
	private DateTimePickDialogUtil dpdialog;
	
	private TxApp txapp;
	
	private int[] levels=new int[]{R.color.bai,R.color.lv,R.color.huang,R.color.cheng,R.color.hong};
	
	private final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		txapp=(TxApp)this.getApplication();
		type=this.getIntent().getIntExtra("type", -1);
		title=(EditText)this.findViewById(R.id.title);
		content=(EditText)this.findViewById(R.id.content);
		content2=(EditText)this.findViewById(R.id.content2);
		firsttime=(EditText)this.findViewById(R.id.firsttime);
		endtime=(EditText)this.findViewById(R.id.endtime);
		level=(RadioGroup)this.findViewById(R.id.level);
	}

	public void butclick(View v){
		int id=v.getId();
		if(id==R.id.quxiao){
			this.finish();
		}else if(id==R.id.wancheng){
			Add();
		}else if(id==R.id.firsttime){
			dpdialog=new DateTimePickDialogUtil(this);
			dpdialog.dateTimePicKDialog(firsttime, 0);
		}else if(id==R.id.endtime){
			dpdialog=new DateTimePickDialogUtil(this);
			dpdialog.dateTimePicKDialog(endtime, 0);
		}else if(id==R.id.back){
			this.finish();
			
		}
	}
	private void Add(){
		try {
			String t=title.getText().toString();
			String c=content.getText().toString();
			String c2=content2.getText().toString();
			String frist=firsttime.getText().toString();
			String end=endtime.getText().toString();
			int le=level.getCheckedRadioButtonId();
			RadioButton rb=(RadioButton)level.findViewById(le);
			int index=level.indexOfChild(rb);
			
			if(t.length()<=0){
				Toast.makeText(this, "标题不能为空", Toast.LENGTH_LONG).show();
				return;
			}
			Date ft=null,ed=null;
			if(frist.length()>0){
				ft=format.parse(frist);
				if(ft.before(new Date())){
					Toast.makeText(this, "设置的时间大于当前时间", Toast.LENGTH_LONG).show();
					return;
				}
			}
			if(end.length()>0){
				ed=format.parse(end);
				if(ed.before(new Date())){
					Toast.makeText(this, "设置的时间大于当前时间", Toast.LENGTH_LONG).show();
					return;
				}
			}
			if(ft!=null||ed!=null){
				ItemInfo info=new ItemInfo();
				info.setTitle(t);
				info.setType(type);
				info.setContent(c);
				info.setContent2(c2);
				info.setFirsttime(ft);
				info.setEndtime(ed);
				info.setLevel(levels[index]);
				txapp.AddTx(info);
				this.finish();
			}else{
				Toast.makeText(this, "最少设置一个时间", Toast.LENGTH_LONG).show();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

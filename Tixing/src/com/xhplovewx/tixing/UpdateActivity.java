package com.xhplovewx.tixing;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xhplovewx.pojo.ItemInfo;

public class UpdateActivity extends Activity {

	private int type;
	private EditText title,content,content2,firsttime,endtime;
	private RadioGroup level;
	private DateTimePickDialogUtil dpdialog;
	
	
	private TxApp txapp;
	private ItemInfo info;
	
	private int[] levels=new int[]{R.color.bai,R.color.lv,R.color.huang,R.color.cheng,R.color.hong};
	
	private final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		txapp=(TxApp)this.getApplication();
		title=(EditText)this.findViewById(R.id.title);
		content=(EditText)this.findViewById(R.id.content);
		content2=(EditText)this.findViewById(R.id.content2);
		firsttime=(EditText)this.findViewById(R.id.firsttime);
		endtime=(EditText)this.findViewById(R.id.endtime);
		level=(RadioGroup)this.findViewById(R.id.level);
		int id=(int)this.getIntent().getIntExtra("iteminfo",-1);
		
		info=getitem(id);
		if(info!=null){
			title.setText(info.getTitle());
			content.setText(info.getContent());
			content2.setText(info.getContent2());
			if(info.getFirsttime()!=null){
				firsttime.setText(format.format(info.getFirsttime()));
			}else{
				firsttime.setText("");
			}
			if(info.getEndtime()!=null){
				endtime.setText(format.format(info.getEndtime()));
			}else{
				endtime.setText("");
			}
			
			int c=getindex(info.getLevel());
			System.out.println(c);
			RadioButton rb=(RadioButton)level.getChildAt(c);
			rb.setChecked(true);
		}
		
	}
	
	public ItemInfo getitem(int id){
		for(ItemInfo info:txapp.getLitem()){
			if(id==info.getId()){
				return info;
			}
		}
		return null;
	}
	
	public int getindex(int lev){
		for(int i=0;i<levels.length;i++){
			if(lev==levels[i]){
				return i;
			}
		}
		return -1;
	}

	public void butclick(View v){
		int id=v.getId();
		if(id==R.id.quxiao){
			this.finish();
		}else if(id==R.id.wancheng){
			update();
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
	private void update(){
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
				if(info!=null){
					info.setTitle(t);
					info.setContent(c);
					info.setContent(c2);
					info.setFirsttime(ft);
					info.setEndtime(ed);
					info.setLevel(levels[index]);
					txapp.update(info);
					this.finish();
				}
				
			}else{
				Toast.makeText(this, "最少设置一个时间", Toast.LENGTH_LONG).show();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

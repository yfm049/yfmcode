package com.chu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;

public class BookingActivity extends Activity implements OnClickListener{
	private Spinner fromSp;
	private Spinner toSp;
	private Spinner levelSp;
	//private DatePicker datePicker;
	private Button mDate;
	private Button findBtn;
	
	private List<String> levels;
	private ImageView iv;
	
	private int mYear;     
	private int mMonth;     
	private int mDay;
	
	private String getSp_01;
	private String getSp_02;
	private String getSp_03;
	
	private DatePickerDialog datePickerDialog;

	
	static final int DATE_DIALOG_ID = 0; 
	
       @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.booking);
    	
    	createLevels();
    	
    	fromSp = (Spinner)findViewById(R.id.booking_sp_01);
    	toSp = (Spinner)findViewById(R.id.booking_sp_02);
    	levelSp = (Spinner)findViewById(R.id.booking_sp_03);
    	//datePicker = (DatePicker)findViewById(R.id.booking_dp);
    	mDate = (Button)findViewById(R.id.booking_date);
    	findBtn = (Button)findViewById(R.id.booking_btn);
    	iv = (ImageView)findViewById(R.id.booking_back);
    	
    	final Calendar calendar = Calendar.getInstance();
    	mYear = calendar.get(Calendar.YEAR);
    	mMonth = calendar.get(Calendar.MONTH);
    	mDay = calendar.get(Calendar.DAY_OF_MONTH);
    	
    	mDate.setOnClickListener(this);
    	findBtn.setOnClickListener(this);   //绑定button的listener
    	iv.setOnClickListener(this);
    	
    	//spinner绑定数据
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
    			R.array.city, 
    			android.R.layout.simple_spinner_item);//定义下拉菜单样式
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//定义每个item样式
    	fromSp.setAdapter(adapter);
    	fromSp.setPrompt("出发城市选择");
    	toSp.setAdapter(adapter);
    	toSp.setPrompt("到达城市选择");
    	
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, levels);
    	levelSp.setAdapter(adapter2);
    	
    	fromSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				fromSp = (Spinner) parent;
				getSp_01 = (String)parent.getItemAtPosition(position);
				System.out.println("---->Sp_01" + getSp_01);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	toSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				toSp = (Spinner) parent;
				getSp_02 = (String)parent.getItemAtPosition(position);
				System.out.println("---->Sp_02" + getSp_02);			
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	levelSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				levelSp = (Spinner)parent;
				getSp_03 = (String)parent.getItemAtPosition(position);
				System.out.println("---->Sp_03" + getSp_03);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
        System.out.println("---->mYear" + mYear);
        System.out.println("---->mMonth" + mMonth);
        System.out.println("---->mDay" + mDay);
    }
       
   private void createLevels(){
	   levels = new ArrayList<String>();
	   levels.add("普通舱");
	   levels.add("经济舱");
	   levels.add("豪华舱");
	   levels.add("商务舱");
   }
    
   

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.booking_date:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.booking_back:
			BookingActivity.this.finish();
			break;
		case R.id.booking_btn:
			Intent intent = new Intent();
			intent.putExtra("getFromSp",getSp_01);
			intent.putExtra("getToSp", getSp_02);
			intent.putExtra("getLevelSp", getSp_03);
			intent.putExtra("mYear", mYear);
			intent.putExtra("mMonth", mMonth);
			intent.putExtra("mDay", mDay);
			intent.setClass(this, GetBookingInfoActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
			
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DATE_DIALOG_ID:
			datePickerDialog = new DatePickerDialog(this, mDateSetListener, 
					mYear, 
					mMonth, 
					mDay);
			datePickerDialog.setTitle(mYear+"年"+(mMonth+1)+"月"+mDay+"日");
			
			return datePickerDialog;
			
			
		default:
			break;
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					System.out.println("--->年" + mYear);
					
				}
				
				
			};
			
	
}

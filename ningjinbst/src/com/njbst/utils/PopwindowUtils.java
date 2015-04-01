package com.njbst.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.njbst.pro.R;
import com.njbst.third.ThirdInterface;

public class PopwindowUtils {

	private Context context;
	private View contentView;
	private PopupWindow pw;
	private OnClickListenerImpl listener;
	
	private ThirdInterface share;
	
	
	
	private LinearLayout QQShare,SinaShare,WxShare,QQKJShare,SmsShare;
	public PopwindowUtils(Context context){
		this.context=context;
		listener=new OnClickListenerImpl();
	}
	public void setShare(ThirdInterface share) {
		this.share = share;
	}
	private void CreatePopwindow(){
		contentView=LayoutInflater.from(context).inflate(R.layout.popwindow_zhuan, null);
		pw=new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		pw.setBackgroundDrawable(new ColorDrawable(-00000000));
		pw.setOutsideTouchable(true);
		pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		QQShare=(LinearLayout)contentView.findViewById(R.id.QQShare);
		SinaShare=(LinearLayout)contentView.findViewById(R.id.SinaShare);
		WxShare=(LinearLayout)contentView.findViewById(R.id.WxShare);
		QQKJShare=(LinearLayout)contentView.findViewById(R.id.QQKJShare);
		SmsShare=(LinearLayout)contentView.findViewById(R.id.SmsShare);
		QQShare.setOnClickListener(listener);
		SinaShare.setOnClickListener(listener);
		WxShare.setOnClickListener(listener);
		QQKJShare.setOnClickListener(listener);
		SmsShare.setOnClickListener(listener);
	}
	
	public void ShowPopwindow(View v){
		if(contentView==null){
			CreatePopwindow();
		}
		pw.showAtLocation(v, Gravity.LEFT|Gravity.BOTTOM, 0, 0);
		
	}
	class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(share!=null){
				if(v.getId()==R.id.QQShare){
					share.QQShare();
				}else if(v.getId()==R.id.SinaShare){
					share.SinaShare();
				}else if(v.getId()==R.id.WxShare){
					share.WxShare();
				}else if(v.getId()==R.id.QQKJShare){
					share.QQKJShare();
				}else if(v.getId()==R.id.SmsShare){
					share.SMSShare();
				}
			}
			pw.dismiss();
			
		}
		
	}
}

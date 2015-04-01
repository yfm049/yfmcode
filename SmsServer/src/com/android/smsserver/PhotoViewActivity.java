package com.android.smsserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.android.thread.ImageThread;
import com.android.utils.Utils;

public class PhotoViewActivity extends Activity {

	private ImageView imgview;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Utils.getIntConfig(this, "id", -1)==-1){
			Intent intent=new Intent(this,LoginActivity.class);
			this.startActivity(intent);
		}
		setContentView(R.layout.activity_photo_view);
		imgview=(ImageView)this.findViewById(R.id.imgview);
		String url=this.getIntent().getStringExtra("imgurl");
		pd=Utils.createProgressDialog(this);
		pd.setMessage("正在下载图片");
		pd.show();
		ImageThread it=new ImageThread(url, handler);
		it.start();
		
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.cancel();
			if(msg.what==1){
				if(msg.obj!=null){
					Bitmap bitmap=(Bitmap)msg.obj;
					imgview.setImageBitmap(bitmap);
				}else{
					Utils.ShowToast(PhotoViewActivity.this, "图片下载失败");
				}
			}else{
				Utils.ShowToast(PhotoViewActivity.this, "请求服务器失败");
			}
		}
		
	};
	


}

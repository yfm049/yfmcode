package com.preview.camerapro;

import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preview.utils.ConfigUtils;
import com.preview.utils.HttpDao;

public class MainActivity extends Activity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MainActivity.data=null;
		String ip=ConfigUtils.GetString(this, "ip", "10.120.148.53");
		String port=ConfigUtils.GetString(this, "port", "8080");
		HttpDao.httpurl="http://"+ip+":"+port+"/CameraServer/";
	}

	private ImageView test;
	private SurfaceView preview;
	private Camera camera;
	private SurfaceHolder holder;
	private boolean isPreview = false;
	private EditText code;
	public static byte[] data;
	public static String json;
	private ProgressDialog pd;
	private TextView setbut;
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				try {
					code.setText("");
					if(json!=null){
						JSONObject jo=new JSONObject(json);
						if(jo.isNull("code")){
							Toast.makeText(MainActivity.this, "无效卡", Toast.LENGTH_SHORT).show();
						}else{
							MainActivity.data=null;
							Picture();
						}
					}else{
						Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		code = (EditText) super.findViewById(R.id.code);
		code.setInputType(InputType.TYPE_NULL);
		setbut=(TextView)super.findViewById(R.id.setbut);
		test=(ImageView)super.findViewById(R.id.test);
		test.setOnClickListener(new testOnClickListener());
		preview = (SurfaceView) super.findViewById(R.id.preview);
		
		holder = preview.getHolder();
		holder.addCallback(new holderCallBack());
		code.addTextChangedListener(new TextWatcherImpl());
		setbut.setOnClickListener(new setbutOnClickListener());
	}
	class testOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			getDate("1234567890");
		}
		
	}
	class setbutOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MainActivity.this,SetActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MainActivity.this.startActivity(intent);
		}
		
	}
	public void getDate(String code){
		pd=new ProgressDialog(MainActivity.this);
		pd.setMessage("正在获取数据请稍后...");
		pd.show();
		new GetJsonData(code).start();
	}
	class TextWatcherImpl implements TextWatcher {


		@Override
		public void afterTextChanged(Editable et) {
			// TODO Auto-generated method stub
			String after = et.toString();
			if(after.length()>=10){
				getDate(after);
			}
			
		}

		@Override
		public void beforeTextChanged(CharSequence before, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub

		}

	}
	
	class GetJsonData extends Thread{
		String text;
		public GetJsonData(String text){
			this.text=text;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			json=HttpDao.GetJsonData("user!GetUserInfo.action?code="+text, pd);
			Message msg=handler.obtainMessage();
			msg.what=1;
			msg.obj=json;
			handler.sendMessage(msg);
		}
		
	}
	public void Picture() {
		camera.takePicture(null, null, jpeg);
	}

	// 返回编码后jpeg照片
	private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this, InfoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MainActivity.data=data;
			MainActivity.this.startActivity(intent);
		}

	};

	class holderCallBack implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			int w=(height/3)*4;
			System.out.println(w);
			preview.setLayoutParams(new LinearLayout.LayoutParams(w, height));
			if (isPreview) {
				camera.stopPreview();
			}
			Parameters ps=camera.getParameters();
			ps.setRotation(0);
			camera.setParameters(ps);
			camera.startPreview();
			isPreview = true;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			try {
				camera = Camera.open(0);
				camera.setPreviewDisplay(holder);
				camera.setDisplayOrientation(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				camera.release();
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			camera.stopPreview();
			camera.release();
		}

	}
	public void displaysize(){
		DisplayMetrics dm=this.getResources().getDisplayMetrics();
		System.out.println(dm.widthPixels+"--"+dm.heightPixels);
	}

}

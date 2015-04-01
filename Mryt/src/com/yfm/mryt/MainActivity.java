package com.yfm.mryt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import com.yfm.http.DownLoadThread;
import com.yfm.http.DownloadThreadListener;
import com.yfm.http.HttpUtils;
import com.yfm.view.GifView;

public class MainActivity extends Activity {

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.finish();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			gifimg = bundle.getString("gifimg");
			if (gifimg != null && !"".equals(gifimg)) {
				file = new File(getpath(), gifimg);
				if (file.exists()) {
					try {
						FileInputStream fis = new FileInputStream(file);
						gifview.setGifDecoderImage(fis);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					file = new File(getpath(), gifimg+".tmp");
					pd = new ProgressDialog(this);
					pd.setMessage("正在下载图片，请稍后...");
					pd.show();
					DownLoadThread dlt = new DownLoadThread(HttpUtils.baseurl
							+ "gif/" + gifimg, file,
							new DownloadThreadListenerImpl());
					dlt.start();
				}
			} else {
				Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
		}
	}

	private GifView gifview;
	private ProgressDialog pd;
	private File file;
	private String gifimg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		gifview = (GifView) super.findViewById(R.id.gifview);

	}

	public File getpath() {
		File path = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			path = new File(Environment.getExternalStorageDirectory().getPath()
					+ "/JPFT");
			if (!path.exists()) {
				path.mkdir();
			}
		}
		return path;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class DownloadThreadListenerImpl implements DownloadThreadListener {

		int bfb=0;
		@Override
		public void afterPerDown(String uri, long totalSize, long updateTotalSize) {
			// TODO Auto-generated method stub
				if((updateTotalSize * 100 / totalSize) - 1 > bfb){
					bfb=bfb+1;
					Message msg=handler.obtainMessage();
					msg.what=100;
					msg.arg1=bfb;
					handler.sendMessage(msg);
					
			}
		}

		@Override
		public void downCompleted(String uri, long count, long rcount,
				boolean isdown, File file) {
			// TODO Auto-generated method stub
			Message msg = handler.obtainMessage();
			msg.what = 2;
			handler.sendMessage(msg);
		}

		@Override
		public void returncode(int statecode) {
			// TODO Auto-generated method stub
			Message msg = handler.obtainMessage();
			msg.what = 3;
			msg.arg1 = statecode;
			handler.sendMessage(msg);
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 2) {
				pd.dismiss();
				try {
					File m=new File(getpath(), gifimg);
					file.renameTo(m);
					FileInputStream fis = new FileInputStream(m);
					gifview.setGifDecoderImage(fis);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (msg.what == 3) {
				pd.dismiss();
				Toast.makeText(MainActivity.this, "下载失败 状态码" + msg.arg1,
						Toast.LENGTH_LONG).show();
			}if(msg.what==100){
				if(pd.isShowing()){
					pd.setMessage("正在下载图片，请稍后... "+msg.arg1+"%");
				}
			}
		}

	};

}

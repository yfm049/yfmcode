package com.pro.game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button ycgx, jb, zdjb, hfjb, set;
	private OnClickListenerImpl listener;
	private Properties pro;
	private EditText bianhao;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listener = new OnClickListenerImpl();
		sp = this.getSharedPreferences("name", MODE_PRIVATE);
		super.setContentView(R.layout.main_layout);
		ycgx = (Button) super.findViewById(R.id.ycgx);
		jb = (Button) super.findViewById(R.id.jb);
		zdjb = (Button) super.findViewById(R.id.zdjb);
		hfjb = (Button) super.findViewById(R.id.hfjb);
		set = (Button) super.findViewById(R.id.set);

		bianhao = (EditText) super.findViewById(R.id.bianhao);
		bianhao.setText(sp.getString("num", "0"));
		ycgx.setOnClickListener(listener);
		jb.setOnClickListener(listener);
		zdjb.setOnClickListener(listener);
		hfjb.setOnClickListener(listener);
		set.setOnClickListener(listener);
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			if (but.getId() == R.id.ycgx) {
				String url = getvalue("jiaobenzip");
				String path = getvalue("jiaobenpath");
				if (url != null && path != null) {
					new AsyncTaskImpl(MainActivity.this).execute(url, path);
				}
			}
			if (but.getId() == R.id.jb) {
				String url = getvalue("1zip");
				String path = getvalue("1path");
				if (url != null && path != null) {
					new AsyncTaskImpl(MainActivity.this).execute(url, path);
				}
			}
			if (but.getId() == R.id.zdjb) {
				String url = getvalue("maxzip");
				String path = getvalue("maxpath");
				if (url != null && path != null) {
					new AsyncTaskImpl(MainActivity.this).execute(url, path);
				}
			}
			if (but.getId() == R.id.hfjb) {
				String url = getvalue("huifuzip");
				String path = getvalue("huifupath");
				if (url != null && path != null) {
					new AsyncTaskImpl(MainActivity.this).execute(url, path,"delete");
				}
			}
			if (but.getId() == R.id.set) {
				String text = bianhao.getText().toString();
				Editor e = sp.edit();
				e.putString("num", text);
				e.commit();
				saveSd(text);
				Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT)
						.show();
				;
			}
		}

	}

	public String getvalue(String key) {
		try {
			if (pro == null) {
				File f=new File(Environment.getExternalStorageDirectory(), "updata.ini");
				if(!f.exists()){
					copyBigDataToSD(f);
				}
				FileInputStream fis=new FileInputStream(f);
				pro = new Properties();
				pro.load(fis);
			}
			return pro.getProperty(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pro=null;
			Toast.makeText(this, "读取文件失败", Toast.LENGTH_LONG).show();
		}
		return null;
	}

	public void saveSd(String text) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File file = new File(Environment.getExternalStorageDirectory(),
						"MachineID.txt");
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				BufferedWriter bw = new BufferedWriter(osw);
				bw.write(text);
				bw.close();
				osw.close();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void copyBigDataToSD(File file) throws IOException {
		if(!file.exists()){
			file.createNewFile();
		}
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(file);
		myInput = this.getAssets().open("updata.ini");
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

}

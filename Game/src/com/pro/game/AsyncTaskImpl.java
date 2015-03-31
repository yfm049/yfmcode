package com.pro.game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class AsyncTaskImpl extends AsyncTask<String, Integer, String> {

	private String wenjian[] = new String[] { "collection_tools.csv",
			"game_config.csv", "jeweler_goods.csv", "newspaper_layout.csv",
			"newspaper_stories.csv" };
	private Context context;
	private ProgressDialog pd;

	public AsyncTaskImpl(Context context){
		this.context=context;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd=new ProgressDialog(context);
		pd.setMessage("开始处理");
		pd.show();
	}

	@Override
	protected String doInBackground(String... val) {
		// TODO Auto-generated method stub
		String name = getfilename(val[0]);
		if (val[1].indexOf("data") > 0) {
			if (!change("chmod -R 777 " +getpath(val[1])+ "\n")) {
				return "修改权限失败";
			}
		}
		if(val.length>=3){
			if("delete".equals(val[2])){
				for(int i=0;i<wenjian.length;i++){
					File file=new File(val[1], wenjian[i]);
					if(file.exists()){
						file.delete();
					}
				}
			}
		}
		File file = downloadfile(val[0], name);
		if (file != null) {
			try {
				upZipFile(file, val[1]);
				if (val[1].indexOf("data") > 0) {
					if (!change("chmod -R 775 " +getpath(val[1])+ "\n")) {
						return "修改权限失败";
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "压解失败";
			}
			return "更新成功";
		} else {
			return "文件下载失败";
		}

	}

	public String getpath(String p){
		String ps[]=p.split("/");
		if(ps.length>4){
			return ps[0]+"/"+ps[1]+"/"+ps[2]+"/"+ps[3];
		}
		return p;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pd.cancel();
		Toast.makeText(context, result, Toast.LENGTH_LONG).show();
		System.out.println(result);
	}

	public File downloadfile(String durl, String name) {
		File file = null;
		try {
			file = createFile(name);
			if (file != null) {
				URL url = new URL(durl);
				URLConnection con = url.openConnection();
				con.connect();
				InputStream in = con.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = in.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				in.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			file = null;
		}
		return file;
	}

	public File createFile(String name) {
		File file = null;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				file = new File(Environment.getExternalStorageDirectory(), name);
				if (!file.exists()) {
					file.createNewFile();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public String getfilename(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

	/**
	 * 解压缩功能. 将ZIP_FILENAME文件解压到ZIP_DIR目录下.
	 * 
	 * @throws Exception
	 */
	public int upZipFile(File zipFile, String folderPath) throws ZipException,
			IOException {
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				String dirstr = folderPath + ze.getName();
				File f = new File(dirstr);
				f.mkdirs();
				continue;
			}
			Log.d("upZipFile", "ze.getName() = " + ze.getName());
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
		return 0;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public File getRealFileName(String baseDir, String absFileName) {
		File ret = new File(baseDir, absFileName);
		if (!ret.exists()) {
			try {
				ret.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}

	public boolean change(String cmd) {
		Runtime runtime = null;
		Process process = null;
		runtime = Runtime.getRuntime();
		try {
			process = runtime.exec("su");
			InputStream inputStream = process.getInputStream();
			OutputStream outputStream = process.getOutputStream();
			System.out.println(cmd);
			outputStream.write(cmd.getBytes());
			outputStream.close();
			System.out.println("Execute:" + cmd);
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			reader.close();
			inputStream.close();
			System.out.println("Execute End!");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

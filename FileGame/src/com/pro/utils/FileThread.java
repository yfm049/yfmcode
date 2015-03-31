package com.pro.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import android.os.Handler;

import com.pro.pojo.GameFile;
import com.pro.pojo.Simulator;

public class FileThread extends Thread{

	private File file;
	private Simulator slr;
	private Handler handler;
	private FileFilter filter;
	public FileThread(File file,Simulator slr,Handler handler,List<GameFile> files){
		this.file=file;
		files.clear();
		this.handler=handler;
		this.slr=slr;
		filter=new FileFilterImpl(slr,files);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		if(file!=null&&slr!=null){
			if("Ŀ¼".equals(slr.getName())){
				file.listFiles(filter);
			}else{
				SearchFile(file,filter);
			}
		}
		handler.sendEmptyMessage(100);
	}
	private void SearchFile(File file, FileFilter filter) {
		if (file != null) {
			if (file.isDirectory()) {
				File[] fs = file.listFiles(filter);
				if (fs != null) {
					for (File f : fs) {
						if (f.isDirectory()) {
							SearchFile(f, filter);
						}
					}
				}
			}
		}

	}
	
}

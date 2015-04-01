package com.pro.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.pro.model.Tel_query;
import com.pro.model.Tel_text;

public class DownLoadUtils {

	public static File down(String furl, String filename) {
		File excel = null;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				excel = new File(makedir(), filename);
				if (!excel.exists()) {
					excel.createNewFile();
				}
				URL url = new URL(furl);
				URLConnection con = url.openConnection();
				InputStream in = con.getInputStream();
				FileOutputStream fos = new FileOutputStream(excel);
				byte[] bs = new byte[1024];
				int len;
				while ((len = in.read(bs)) != -1) {
					fos.write(bs, 0, len);
				}
				fos.close();
				in.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			excel = null;
		}
		return excel;
	}

	private static File makedir() {
		File file = new File(Environment.getExternalStorageDirectory(), "excel");
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}

	public static List<Tel_text> parse_download_text_Excel(File file) {
		List<Tel_text> lst = null;
		FileInputStream fis=null;
		InputStreamReader isr=null;
		BufferedReader br=null;
		try {
			fis=new FileInputStream(file);
			isr=new InputStreamReader(fis, "GBK");
			br = new BufferedReader(isr);
			if (br.readLine() != null) {
				lst = new ArrayList<Tel_text>();
				String line;
				while ((line = br.readLine()) != null) {
					String p[] = line.split("\\t+");
					if (p.length >= 3) {
						Tel_text tt = new Tel_text();
						tt.setSerial(p[0]);
						tt.setTel(p[1]);
						tt.setText(p[2]);
						lst.add(tt);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lst = null;
		}finally{
			try {
				if(br!=null){
					br.close();
				}
				if(isr!=null){
					isr.close();
				}
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return lst;
	}


	public static List<Tel_query> parse_query_xx_Excel(File file) {
		List<Tel_query> lst = null;
		FileInputStream fis=null;
		InputStreamReader isr=null;
		BufferedReader br=null;
		try {
			fis=new FileInputStream(file);
			isr=new InputStreamReader(fis, "GBK");
			br = new BufferedReader(isr);
			if (br.readLine() != null) {
				lst = new ArrayList<Tel_query>();
				String line;
				while ((line = br.readLine()) != null) {
					String p[] = line.split("\\t+");
					if (p.length >= 2) {
						Tel_query tt = new Tel_query();
						tt.setGgxh(p[0]);
						tt.setText(p[1]);
						lst.add(tt);
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lst=null;
		}finally{
			try {
				if(br!=null){
					br.close();
				}
				if(isr!=null){
					isr.close();
				}
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return lst;
	}
//	public static List<Tel_query> parse_query_xx_Excel(File file) {
//		List<Tel_query> lst = null;
//		try {
//			WorkbookSettings workbookSettings = new WorkbookSettings();
//			workbookSettings.setEncoding("GBK");
//			Workbook workbook = Workbook.getWorkbook(file, workbookSettings);
//			int i = workbook.getNumberOfSheets();
//			if (i > 0) {
//				lst = new ArrayList<Tel_query>();
//				Sheet sheet = workbook.getSheet(0);
//				int rs = sheet.getRows();
//				for (int r = 1; r < rs; r++) {
//					Cell[] cells = sheet.getRow(r);
//					if (cells.length >= 2) {
//						Tel_query tt = new Tel_query();
//						tt.setGgxh(cells[0].getContents());
//						tt.setText(cells[1].getContents());
//						lst.add(tt);
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return lst;
//	}
	// public static List<Tel_text> parse_download_text_Excel(File file) {
	// List<Tel_text> lst = null;
	// try {
	// WorkbookSettings workbookSettings = new WorkbookSettings();
	// workbookSettings.setEncoding("GBK");
	// Workbook workbook = Workbook.getWorkbook(file, workbookSettings);
	// int i = workbook.getNumberOfSheets();
	// if (i > 0) {
	// lst = new ArrayList<Tel_text>();
	// Sheet sheet = workbook.getSheet(0);
	// int rs = sheet.getRows();
	// for (int r = 1; r < rs; r++) {
	// Cell[] cells = sheet.getRow(r);
	// if (cells.length >= 3) {
	// Tel_text tt = new Tel_text();
	// tt.setSerial(cells[0].getContents());
	// tt.setTel(cells[1].getContents());
	// tt.setText(cells[2].getContents());
	// lst.add(tt);
	// }
	//
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return lst;
	// }

}

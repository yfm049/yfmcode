package com.pro.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ConfigUtils {

	public static void unzip(InputStream is, String dir) throws IOException {
		File dest = new File(dir);
		ZipInputStream zip = new ZipInputStream(is);
		ZipEntry ze;
		while ((ze = zip.getNextEntry()) != null) {
			final String path = dest.getAbsolutePath() + File.separator
					+ ze.getName();
			String zeName = ze.getName();
			char cTail = zeName.charAt(zeName.length() - 1);
			if (cTail == File.separatorChar) {
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}
				continue;
			}
			FileOutputStream fout = new FileOutputStream(path);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = zip.read(bytes)) != -1) {
				fout.write(bytes, 0, c);
			}
			zip.closeEntry();
			fout.close();
		}
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}

	public static DataInputStream Terminal(String command) throws Exception {
		Process process = Runtime.getRuntime().exec("su");
		OutputStream outstream = process.getOutputStream();
		DataOutputStream DOPS = new DataOutputStream(outstream);
		InputStream instream = process.getInputStream();
		DataInputStream DIPS = new DataInputStream(instream);
		String temp = command + "\n";
		DOPS.writeBytes(temp);
		DOPS.flush();
		DOPS.writeBytes("exit\n");
		DOPS.flush();
		process.waitFor();
		return DIPS;
	}

	public static boolean isRooted() {
		DataInputStream stream;
		boolean flag = false;
		try {
			stream = Terminal("ls /data/");
			if (stream.readLine() != null) {
				flag = true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();

		}

		return flag;
	}

	public static String readResult(DataInputStream stream) {
		StringBuffer sb = new StringBuffer("");
		try {
			String line;
			while ((line = stream.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

}

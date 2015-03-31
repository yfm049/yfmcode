package com.chinaip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.app.db.SqlUtil;

public class replace {

	// 获取数据，使用缓存、解决内存溢出
	private String getData(InputStream inputStream) {
			// Log.i("LOG", "getData");
			// 内存缓冲区
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int len = -1;
			byte[] buf = new byte[1024];// 每次读一K
			try {
				while ((len = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, len);
				}
				byte[] bytes = outputStream.toByteArray();
				inputStream.close();
				outputStream.close();
				return new String(bytes, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
				outputStream=null;
				inputStream=null;
				buf=null;
			}
		}
	public String loadfile(String path){
		File file=new File(path);
		FileInputStream fos = null;
		try {
			fos = new FileInputStream(file);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getData(fos);
		
	}
	public String replace(String html){
		String htmls = html.replaceAll(
				"\"\\);[\\s]+[\\}]{1}[\\s]+[\\}]{1}[\\s]+</script>",
				" ");
		htmls = htmls
				.replaceAll(
						"document.write\\(\"<TD height='25' nowrap class='gray'>&nbsp;?",
						"</SCRIPT><TD height='25' nowrap class='gray'>");
		htmls = htmls.replaceAll(
				"(?is)<script[^>]*?>.*?<\\/script>", "");
		return htmls;
	}
	public void parse(String html){
		Document doc=Jsoup.parse(html);
		Elements trs = doc.getElementsByTag("tr");
		SaveToDatabase(trs);
	}
	private String sql = "insert into data(shenqinghao,gonggao,zhufenlei,mingcheng,fenleihao,shenqingren,famingren,gongkairi,shenqingri,benguozhufenlei,zhuanlidaili,dailiren,dizhi,guoshengdaima) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public void SaveToDatabase(Elements trs) {
		if (trs != null && trs.size() > 0) {
			for (int i = 1; i < trs.size(); i++) {
				Element tr = trs.get(i);
				Elements tds = tr.getElementsByTag("td");
				if (tds != null && tds.size() == 15) {
					SqlUtil.save(sql, new Object[] { tds.get(1).html(),
							tds.get(2).html(), tds.get(3).html(),
							tds.get(4).html(), tds.get(5).html(),
							tds.get(6).html(), tds.get(7).html(),
							tds.get(8).html(), tds.get(9).html(),
							tds.get(10).html(), tds.get(11).html(),
							tds.get(12).html(), tds.get(13).html(),
							tds.get(14).html() });
				} else {
				}
			}
		}

	}
}

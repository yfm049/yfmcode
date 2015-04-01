package com.pro.rfidw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cilico.tools.I2CTools;

public class MainActivity extends Activity {

	private Button uidread, readinfo, wrbut;
	private EditText uid, realname, gongsi, sfcode, wmima;
	private ProgressDialog pd;
	private Info info;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				pd.cancel();
				if (info != null && info.getLci().size() > 0) {
					String rn = "";
					String gs = "";
					String sc = "";
					for (int i = 0; i < info.getLci().size(); i++) {
						CardInfo ci = info.getLci().get(i);
						int cid = ci.getContentTypeID();
						if (cid == 1) {
							rn += ci.getContent();
						}
						if (cid == 3) {
							gs += ci.getContent();
						}
						if (cid == 2) {
							sc += ci.getContent();
						}

					}
					realname.setText(replaceBlank(rn));
					gongsi.setText(replaceBlank(gs));
					sfcode.setText(replaceBlank(sc));

				}
				Toast.makeText(MainActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
			}
			if (msg.what == 2) {
				pd.cancel();
				Toast.makeText(MainActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
			}
		}

	};

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("`|\r\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		uidread = (Button) super.findViewById(R.id.uidread);
		readinfo = (Button) super.findViewById(R.id.readinfo);
		wrbut = (Button) super.findViewById(R.id.wrbut);
		uid = (EditText) super.findViewById(R.id.uid);

		realname = (EditText) super.findViewById(R.id.realname);
		gongsi = (EditText) super.findViewById(R.id.gongsi);
		sfcode = (EditText) super.findViewById(R.id.sfcode);
		wmima = (EditText) super.findViewById(R.id.wmima);
		uidread.setOnClickListener(new uidreadOnClickListener());
		readinfo.setOnClickListener(new readinfoOnClickListener());
		wrbut.setOnClickListener(new wrbutOnClickListener());
	}

	class wrbutOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(MainActivity.this);
			pd.setMessage("正在写卡中,请稍后...");
			pd.setCancelable(false);
			pd.show();
			new WriteIC().start();
		}

	}

	class WriteIC extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String msg = "";
			try {
				msg = changeDate();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg = "写入数据时出现异常";
			} finally {
				Message msgs = handler.obtainMessage();
				msgs.what = 2;
				msgs.obj = msg;
				handler.sendMessage(msgs);
			}

		}
	}

	public String changeDate() throws Throwable {
		String msg = "写入成功";
		if (info != null && info.getLci().size() > 0) {
			for (int i = 0; i < info.getLci().size(); i++) {
				CardInfo ci = info.getLci().get(i);
				String mima = ci.getCurWritePassword();
				byte type = Byte.decode(ci.getCurPassWordTypeID());
				byte addr = (byte) ci.getBlockAddr();
				byte[] passw = I2CTools.stringToBytes(mima);
				byte[] pwrite = new byte[16];
				String con = ci.getContent();
				byte[] data = con.getBytes("GBK");
				if(data.length<16){
					for(int d=0;d<(16-data.length);d++){
						con+="`";
					}
					data=con.getBytes("GBK");
				}
				System.arraycopy(data, 0, pwrite, 0, data.length > 16 ? 16
						: data.length);
				int sec = I2CTools.WriteBlock(pwrite, passw, type, addr);
				if (sec != 0) {
					msg = "写入失败";
					return msg;
				}
			}
			if (info.getLck().size() > 0) {
				for (int i = 0; i < info.getLck().size(); i++) {
					ChangeKeys ceks = info.getLck().get(i);
					byte[] PassWord = I2CTools.stringToBytes(ceks
							.getCurWritePassword());
					byte type = Byte.decode(ceks.getCurPassWordTypeID());
					int add = ceks.getSecAddr();
					byte[] keyA = I2CTools.stringToBytes(ceks.getNewkeyA());
					byte[] keyB = I2CTools.stringToBytes(ceks.getNewkeyB());
					byte[] control = I2CTools.stringToBytes(ceks
							.getNewControlKey());
					int sec = I2CTools.ChangeKeys(PassWord, type, add, keyA,
							keyB, control);
					if (sec != 0) {
						msg = "写入失败";
						return msg;
					}
				}
			}
			String m = WebserviceUtils.NotifyInfo(uid.getText().toString());
			if (!"0".equals(m)) {
				msg = "上传信息失败:";
				return msg;
			}
		} else {
			msg = "请先获取数据";
			return msg;
		}
		return msg;
	}

	class uidreadOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String code = I2CTools.ReadUID();
			if (code.length() != 0) {
				uid.setText(code);
			} else {
				Toast.makeText(MainActivity.this, "读取失败请重新读取",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	class readinfoOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(MainActivity.this);
			pd.setMessage("正在获取数据，请稍后...");
			pd.setCancelable(false);
			pd.show();
			new ReadThread().start();
		}

	}

	class ReadThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String code = uid.getText().toString();
			String xml = WebserviceUtils.RFIDInfo(code, wmima.getText()
					.toString());
			System.out.println(xml);
			Message msg = handler.obtainMessage();
			msg.what = 1;
			if (xml != null && !"".equals(xml)) {
				info = XMlUtils.ParseXml(xml);
				msg.obj = "获取成功";
			} else {
				msg.obj = "获取失败";
			}
			handler.sendMessage(msg);
		}

	}

}

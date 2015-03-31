package com.chu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chu.adapter.LoadingDialog;
import com.chu.adapter.LoadingDialogExecute;
import com.chu.servlet.UserService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GetBookingInfoActivity extends ListActivity implements
		OnClickListener {
	private ImageView iv;
	private List<HashMap<String, String>> list;
	private String getFromCity;
	private String getToCity;
	private String getLevel;
	private String date;
	private JSONObject obj;
	private JSONArray array;
	private String baseUrl = "http://10.0.2.2:8080/AppServer/dataServlet";
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_booking_info);

		iv = (ImageView) findViewById(R.id.booking_back);
		iv.setOnClickListener(this);

		getFromCity = (String) getIntent().getStringExtra("getFromSp");
		getToCity = (String) getIntent().getStringExtra("getToSp");
		getLevel = (String) getIntent().getStringExtra("getLevelSp");

		int getYear = (int) getIntent().getIntExtra("mYear", 0);
		int getMonth = (int) getIntent().getIntExtra("mMonth", 0) + 1;// 从dateDialog取得的月份比实际月份小1
		int getDay = (int) getIntent().getIntExtra("mDay", 0);

		date = getYear + "年" + getMonth + "月" + getDay + "号";
		System.out.println("---->" + getFromCity);
		System.out.println("---->" + date);

		loadingDialog = new LoadingDialog(this, new LoadingDialogExecute() {

			@Override
			public void executeSuccess() {
				// TODO Auto-generated method stub
				setListAdapter();
			}

			@Override
			public void executeFailure() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean execute() {
				// TODO Auto-generated method stub
				try {
					showList(); // 显示列表
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}

			}
		});
		loadingDialog.start();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.booking_back:
			GetBookingInfoActivity.this.finish();
			break;

		default:
			break;
		}
	}

	private void showList() throws IOException, Exception {
		String urlStr = baseUrl + "?getFromCity=" + getFromCity + "&getToCity="
				+ getToCity + "&getLevel=" + getLevel + "&getDate=" + date;
		System.out.println(urlStr);

		list = new ArrayList<HashMap<String, String>>();
		String body = JsonTest.getJsonContent(urlStr);
		System.out.println(body);
		array = new JSONArray(body);
		for (int i = 0; i < array.length(); i++) {
			obj = array.getJSONObject(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("_from_text", obj.getString("_from"));
			map.put("_to_text", obj.getString("_to"));
			map.put("time_text", obj.getString("time"));
			map.put("price", obj.getString("price"));
			map.put("airport", obj.getString("airport"));
			map.put("level", obj.getString("level"));
			list.add(map);
		}

	}

	private void setListAdapter() {
		SimpleAdapter simpleAdapter = new SimpleAdapter(
				GetBookingInfoActivity.this, list,
				R.layout.get_json_plane_item, new String[] { "_from_text",
						"_to_text", "time_text" }, new int[] { R.id._from_text,
						R.id._to_text, R.id.time_text });
		setListAdapter(simpleAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		HashMap<String, String> map = list.get(position);
		final String from = map.get("_from_text");
		final String to = map.get("_to_text");
		final String time = map.get("time_text");
		final String price = map.get("price");
		final String airport = map.get("airport");
		final String level = map.get("level");
		System.out.println("----->出发城市" + from);
		/**
		 * listview中点击按键弹出对话框
		 */
		new AlertDialog.Builder(this)
				.setTitle("是否预定?")
				.setMessage(
						"机票信息:\r\n航线:" + from + "---->" + to + "\r\n登机时间: "
								+ time + "\r\n票价:" + price + "\r\n仓位:" + level)
				.setPositiveButton("预定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if (LoginActivity.loginTag == true) {
							System.out.println("已经登录");
							SharedPreferences sharedPreferences = getSharedPreferences(
									"chu_user", Activity.MODE_PRIVATE);
							String getShpName = sharedPreferences.getString(
									"name", "null");
							String getShpPassword = sharedPreferences
									.getString("password", "null");
							System.out.println("getShpName:" + getShpName);
							System.out.println("getShpPassword:"
									+ getShpPassword);
							String getRespData = UserService.sendChooseTicket(
									getShpName, from, to, time, price, airport,
									level);
							if (getRespData.equals("OK")) {
								Toast.makeText(GetBookingInfoActivity.this,
										"预订成功", Toast.LENGTH_SHORT).show();
							}

							else if (getRespData.equals("Exist")) {
								Toast.makeText(GetBookingInfoActivity.this,
										"您已经预定该机票", Toast.LENGTH_SHORT).show();
							}
							dialog.dismiss();
						}
					}
				}

				)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
	}

	/*
	 * public static void show(Context context,String Msg, Thread thread){ final
	 * Thread th = thread; progressDialog = new ProgressDialog(context);
	 * progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置为圆形
	 * progressDialog.setTitle(null); progressDialog.setIcon(null); //设置提醒信息
	 * progressDialog.setMessage(Msg); //设置是否可以通过返回键取消
	 * progressDialog.setCancelable(true);
	 * progressDialog.setIndeterminate(false); //设置取消监听
	 * progressDialog.setOnCancelListener(new OnCancelListener() {
	 * 
	 * public void onCancel(DialogInterface dialog) { // TODO Auto-generated
	 * method stub th.interrupt(); } }); progressDialog.show(); }
	 */

}

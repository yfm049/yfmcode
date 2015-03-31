package com.chu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.chu.servlet.UserService;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FindUserTicketActivity extends ListActivity {
	private String baseUrl = "http://10.0.2.2:8080/AppServer/getUserTicketInfo";
	// private String baseUrl_2 =
	// "http://10.0.2.2:8080/AppServer/deleteUserTicketServlet";
	private JSONArray array;
	private JSONObject obj;
	private List<HashMap<String, String>> list;
	private ImageView iv;
	private String getUserName = null;
	private String deleteBody;
	private String body;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_user_ticket_info);
		iv = (ImageView) findViewById(R.id.booking_back);
		iv.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				FindUserTicketActivity.this.finish();
			}
		});
		if (LoginActivity.loginTag == true) {

			try {
				showList();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (LoginActivity.loginTag == false) {
			Toast.makeText(FindUserTicketActivity.this, "请先登录",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
		}

		registerForContextMenu(getListView());

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			String from = list.get(info.position).get("_from_user_ticket_info");
			String to = list.get(info.position).get("_to_user_ticket_info");
			System.out.println(from);
			System.out.println(to);

			try {
				deleteBody = UserService
						.deleteUserTicket(getUserName, from, to);
				System.out.println("异常");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(deleteBody);
			if (deleteBody.equals("OK")) {
				Toast.makeText(FindUserTicketActivity.this, "已经取消",
						Toast.LENGTH_LONG).show();

			}
			return true;

		default:
			return super.onContextItemSelected(item);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 0, 0, "取消预订");
		menu.setHeaderTitle("是否取消预订？");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (LoginActivity.loginTag == true) {

			try {
				showList();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void showList() throws IOException, Exception {
		SharedPreferences sharedPreferences = getSharedPreferences("chu_user",
				Activity.MODE_PRIVATE);
		getUserName = sharedPreferences.getString("name", "null");
		System.out.println("得到保存用户名为：" + getUserName);
		String urlStr = baseUrl + "?username=" + getUserName;
		System.out.println(urlStr);

		list = new ArrayList<HashMap<String, String>>();
		body = JsonTest.getJsonContent(urlStr);
		System.out.println(body);
		array = new JSONArray(body);
		for (int i = 0; i < array.length(); i++) {
			obj = array.getJSONObject(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("_from_user_ticket_info", obj.getString("_from"));
			map.put("_to_user_ticket_info", obj.getString("_to"));
			map.put("time_user_ticket_info", obj.getString("time"));
			map.put("price_user_ticket_info", obj.getString("price"));
			map.put("airport_user_ticket_info", obj.getString("airport"));
			map.put("level_user_ticket_info", obj.getString("level"));
			map.put("code_user_ticket_info", obj.getString("code"));
			list.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.get_user_ticket_info_item, new String[] {
						"_from_user_ticket_info", "_to_user_ticket_info",
						"time_user_ticket_info", "price_user_ticket_info",
						"airport_user_ticket_info", "level_user_ticket_info","code_user_ticket_info" },
				new int[] { R.id._from_user_ticket_info,
						R.id._to_user_ticket_info, R.id.time_user_ticket_info,
						R.id.price_user_ticket_info,
						R.id.airport_user_ticket_info,
						R.id.level_user_ticket_info,R.id.code_user_ticket_info });
		setListAdapter(simpleAdapter);
	}

	/*
	 * public boolean onItemLongClick(AdapterView<?> parent, View v, int
	 * positon, long id) { // TODO Auto-generated method stub String from =
	 * (String)list.get(positon).get("_from_user_ticket_info");
	 * Toast.makeText(FindUserTicketActivity.this, data,
	 * Toast.LENGTH_LONG).show(); return true; }
	 */

}

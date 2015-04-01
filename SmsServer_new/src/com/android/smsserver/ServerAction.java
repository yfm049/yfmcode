package com.android.smsserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.android.db.SqlUtil;
import com.android.page.Page;
import com.android.push.PushClient;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ServerAction extends ActionSupport {

	private static Logger log = Logger.getLogger(ServerAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Page page = new Page();
	private List<String> array = new ArrayList<String>();
	private Map<String, Object> map = new HashMap<String, Object>();
	private Map<String, String> mapparam = new HashMap<String, String>();
	private List<Map<String, Object>> lmo = new ArrayList<Map<String, Object>>();
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

	private Date parseData(String time){
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}
	/**
	 * 用户登录认证
	 * 
	 * @return
	 */
	public String ServerLogin() {
		log.info("用户登录认证");
		log.info(mapparam);
		String sql = "select * from user where name=?";
		Map<String, Object> mo = SqlUtil.searchMap(sql,
				new Object[] { mapparam.get("name") });
		if (mo.isEmpty()) {
			map.put("state", 0);
			map.put("msg", "账号不存在");
		} else if (!mapparam.get("pass").equals(mo.get("pass").toString())) {
			map.put("state", 0);
			map.put("msg", "密码错误");
		}else if("0".equals(mo.get("state").toString())){
			map.put("state", 0);
			map.put("msg", "账户已禁用");
		}else if(new Date().after(parseData(mo.get("endtime").toString()))){
			map.put("state", 0);
			map.put("msg", "账户已过期");
		} else {
			map.put("state", 1);
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			session.put("userid", mo.get("id"));
		}
		return "mapjson";
	}

	/**
	 * 用户pushserverid更新
	 * 
	 * @return
	 */
	public String UpdateServerId() {
		log.info("用户pushserverid更新");
		log.info(mapparam);
		String sql = "update user set serverid=? where id=?";
		int i = SqlUtil.update(sql, new Object[] { mapparam.get("serverid"),
				mapparam.get("id") });
		if (i > 0) {
			map.put("state", 1);
		} else {
			map.put("state", 0);
		}
		return "mapjson";
	}
	/**
	 * 用户pushserverid更新
	 * 
	 * @return
	 */
	public String UpdatePassword() {
		log.info("用户pushserverid更新");
		log.info(mapparam);
		Map<String, Object> session = ActionContext.getContext().getSession();
		Object id=session.get("userid");
		if(id!=null){
			String sql = "update user set pass=? where id=?";
			int i = SqlUtil.update(sql, new Object[] { mapparam.get("pass"),
					id });
			if (i > 0) {
				map.put("state", 1);
				map.put("msg", "修改成功");
			} else {
				map.put("state", 0);
				map.put("msg", "修改失败");
			}
		}else {
			map.put("state", 0);
			map.put("msg", "修改失败");
		}
		
		return "mapjson";
	}

	/**
	 * 获取用户Client列表
	 * 
	 * @return
	 */
	public String GetClientList() {
		log.info("获取用户Client列表");
		log.info(mapparam);

		Map<String, Object> session = ActionContext.getContext().getSession();
		Object userid = session.get("userid");
		if (userid != null) {
			String sql = "select * from devices where userid=?";
			List<Map<String, Object>> lmo = SqlUtil.search(sql,
					new Object[] { userid });
			map.put("data", lmo);
			return "userset";
		} else {
			return "userlogin";
		}

	}

	/**
	 * 发送消息到client
	 * 
	 * @return
	 */
	public String sendToClient() {
		log.info("发送消息到client");
		log.info(mapparam);
		String id = mapparam.get("id");
		String sql = "select clientid from devices where id=?";
		Map<String, Object> mso = SqlUtil.searchMap(sql, new Object[] { id });
		if (mso.containsKey("clientid")) {
			String clientid = mso.get("clientid").toString();
			boolean flag = PushClient.SendMessage(clientid,
					mapparam.get("action"));
			if (flag) {
				map.put("msg", "指令发送成功");
			} else {
				map.put("state", 0);
				map.put("msg", "指令发送失败,客户端不在线");
			}
		} else {
			map.put("state", 0);
			map.put("msg", "指令发送失败,客户端不在线");
		}

		return "mapjson";
	}

	/**
	 * 发送消息到client
	 * 
	 * @return
	 */
	public String SetUploadConfig() {
		log.info("发送消息到client");
		log.info(mapparam);
		String id = mapparam.get("id");
		String sql = "select clientid from devices where id=?";
		Map<String, Object> mso = SqlUtil.searchMap(sql, new Object[] { id });
		if (mso.containsKey("clientid")) {
			String clientid = mso.get("clientid").toString();
			String action = mapparam.get("action");

			boolean flag = PushClient.SendMessage(clientid, action);
			if (flag) {
				sql = "update devices set sms=?,`call`=?,loc=?,`count`=? where id=?";
				JSONObject jo = JSONObject.fromObject(action);
				SqlUtil.update(
						sql,
						new Object[] { jo.getString("sms"),
								jo.getString("call"), jo.getString("loc"),
								jo.getInt("count"), id });
				map.put("state", 1);
				map.put("msg", "设置成功");
			} else {
				map.put("state", 0);
				map.put("msg", "设置失败,客户端不在线");
			}
		} else {
			map.put("state", 0);
			map.put("msg", "设置失败,客户端不在线");
		}
		return "mapjson";
	}

	/**
	 * 修改客户端信息
	 * 
	 * @return
	 */
	public String UpdateDeviceBase() {
		log.info("发送消息到client");
		log.info(mapparam);
		String id = mapparam.get("id");
		String name = mapparam.get("name");

		String phone = mapparam.get("phone");
		String email = mapparam.get("email");
		String pass = mapparam.get("pass");

		String sql = "update devices set clientname=?,phone=?,email=?,pass=? where id=?";
		int flag = SqlUtil.update(sql, new Object[] { name, phone, email, pass,
				id });
		if (flag > 0) {
			map.put("msg", "设置成功");
		} else {
			map.put("state", 0);
			map.put("msg", "设置失败,客户端不在线");
		}
		return "mapjson";
	}

	/**
	 * 修改客户端信息
	 * 
	 * @return
	 */
	public String UpdateCallRecord() {
		log.info("发送消息到client");
		log.info(mapparam);
		String id = mapparam.get("id");
		String sql = "select clientid from devices where id=?";
		Map<String, Object> mso = SqlUtil.searchMap(sql, new Object[] { id });
		if (mso.containsKey("clientid")) {
			String clientid = mso.get("clientid").toString();
			String action = mapparam.get("action");

			boolean flag = PushClient.SendMessage(clientid, action);
			if (flag) {
				sql = "update devices set callrecord=? where id=?";
				JSONObject jo = JSONObject.fromObject(action);
				SqlUtil.update(sql, new Object[] { jo.getString("callrecord"),
						id });
				map.put("msg", "设置成功");
			} else {
				map.put("state", 0);
				map.put("msg", "设置失败,客户端不在线");
			}
		}else {
			map.put("state", 0);
			map.put("msg", "设置失败,客户端不在线");
		}
		return "mapjson";
	}

	public Map<String, Object> getMap() {
		log.info(map);
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<Map<String, Object>> getLmo() {
		return lmo;
	}

	public void setLmo(List<Map<String, Object>> lmo) {
		this.lmo = lmo;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Map<String, String> getMapparam() {
		return mapparam;
	}

	public void setMapparam(Map<String, String> mapparam) {
		this.mapparam = mapparam;
	}

}

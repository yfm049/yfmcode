package com.android.smsserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.db.SqlUtil;
import com.android.page.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ControlAction extends ActionSupport {

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
	 * 系统管理员登陆
	 * 
	 * @return
	 */
	public String AdminLogin() {
		String sql = "select * from admin where name=? and pass=?";
		int i = SqlUtil.searchCount(sql, new Object[] { mapparam.get("name"),
				mapparam.get("pass") });
		if (i > 0) {
			ActionContext.getContext().getSession()
					.put("adminuwer", mapparam.get("name"));
			map.put("state", 1);
			map.put("msg", "登陆成功");
		} else {
			map.put("state", 0);
			map.put("msg", "用户名或密码错误");
		}
		return "mapjson";
	}

	/**
	 * 用户列表
	 * 
	 * @return
	 */
	public String UserList() {
		String sql = "select * from user where 1=1";
		if (mapparam.containsKey("name") && !"".equals(mapparam.get("name"))) {
			sql += " and name like ?";
			array.add("%" + mapparam.get("name") + "%");
		}
		page.setTsize(SqlUtil.searchCount(sql,
				array.toArray(new Object[array.size()])));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, array.toArray(new Object[array.size()]),
				page));
		return "userlist";
	}

	/**
	 * 删除用户
	 * 
	 * @return
	 */
	public String DeleteUser() {
		String id = mapparam.get("id");
		String sql = "delete from user where id=?";
		int i = SqlUtil.delete(sql, new Object[] { id });
		if (i <= 0) {
			map.put("state", 0);
			map.put("msg", "删除失败");
		}
		return "mapjson";
	}

	/**
	 * 获取用户
	 * 
	 * @return
	 */
	public String GetUser() {
		String sql = "Select * from user where id=?";
		String id = mapparam.get("id");
		map = SqlUtil.searchMap(sql, new Object[] { id });
		return "updateuser";
	}

	/**
	 * 修改用户状态
	 * 
	 * @return
	 */
	public String UpdateUserState() {
		String id = mapparam.get("id");
		String state = mapparam.get("state");
		String sql = "update user set state=? where id=?";
		int i = SqlUtil.update(sql, new Object[] { state, id });
		if (i <= 0) {
			map.put("state", 0);
			map.put("msg", "操作失败");
		}
		return "mapjson";
	}

	/**
	 * 修改用户
	 * 
	 * @return
	 */
	public String UpdateUser() {
		String id = mapparam.get("id");
		String pass = mapparam.get("pass");
		String phonenum = mapparam.get("phonenum");
		String imei = mapparam.get("imei");
		String endtime = mapparam.get("endtime");
		if ("".equals(pass)) {
			map.put("state", 0);
			map.put("msg", "密码不能为空");
		} else if ("".equals(phonenum)) {
			map.put("state", 0);
			map.put("msg", "号码不能为空");
		} else if ("".equals(endtime)) {
			map.put("state", 0);
			map.put("msg", "截止时间不能为空");
		} else {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			if (session.containsKey("adminuwer")) {

				String sql = "update user set pass=?,phonenum=?,imei=?,endtime=? where id=?";
				int i = SqlUtil.save(sql, new Object[] { pass, phonenum, imei,
						endtime, id });
				if (i > 0) {
					map.put("state", 1);
					map.put("msg", "添加成功");
				} else {
					map.put("state", 0);
					map.put("msg", "添加失敗");
				}
			} else {
				map.put("state", 0);
				map.put("msg", "你还没有登陆");
			}
		}
		return "mapjson";
	}

	/**
	 * 添加用户
	 * 
	 * @return
	 */
	public String Adduser() {
		String name = mapparam.get("name");
		String pass = mapparam.get("pass");
		String phonenum = mapparam.get("phonenum");
		String imei = mapparam.get("imei");
		String endtime = mapparam.get("endtime");
		if ("".equals(name)) {
			map.put("state", 0);
			map.put("msg", "用户名不能为空");
		} else if ("".equals(pass)) {
			map.put("state", 0);
			map.put("msg", "密码不能为空");
		} else if ("".equals(phonenum)) {
			map.put("state", 0);
			map.put("msg", "号码不能为空");
		} else if ("".equals(endtime)) {
			map.put("state", 0);
			map.put("msg", "截止时间不能为空");
		} else {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			if (session.containsKey("adminuwer")) {
				String sql = "select * from user where name=?";
				int count = SqlUtil.searchCount(sql,
						new Object[] { mapparam.get("name") });
				if (count > 0) {
					map.put("state", 0);
					map.put("msg", "用戶已存在");
				} else {
					sql = "insert into user(name,pass,phonenum,imei,endtime,state) values(?,?,?,?,?,?)";
					int i = SqlUtil.save(sql, new Object[] { name, pass,
							phonenum, imei, endtime, "1" });
					if (i > 0) {
						map.put("state", 1);
						map.put("msg", "添加成功");
					} else {
						map.put("state", 0);
						map.put("msg", "添加失敗");
					}
				}
			} else {
				map.put("state", 0);
				map.put("msg", "你还没有登陆");
			}
		}
		return "mapjson";
	}

	/**
	 * 客户端列表
	 * 
	 * @return
	 */
	public String DeviceList() {
		String sql = "SELECT a.*,b.name FROM devices a left join user b on a.userid=b.id where b.id is not null";
		if (mapparam.containsKey("name") && !"".equals(mapparam.get("name"))) {
			sql += " and name like ?";
			array.add("%" + mapparam.get("name") + "%");
		}
		if (mapparam.containsKey("phone") && !"".equals(mapparam.get("phone"))) {
			sql += " and phone like ?";
			array.add("%" + mapparam.get("phone") + "%");
		}
		page.setTsize(SqlUtil.searchCount(sql,
				array.toArray(new Object[array.size()])));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, array.toArray(new Object[array.size()]),
				page));
		return "devicelist";
	}

	/**
	 * 删除客户端
	 * 
	 * @return
	 */
	public String DeleteDevice() {
		String id = mapparam.get("id");
		String sql = "delete from devices where id=?";
		int i = SqlUtil.delete(sql, new Object[] { id });
		if (i <= 0) {
			map.put("state", 0);
			map.put("msg", "删除失败");
		}
		return "mapjson";
	}

	/**
	 * 获取客户端
	 * 
	 * @return
	 */
	public String GetDevice() {
		String sql = "Select * from devices where id=?";
		String id = mapparam.get("id");
		map = SqlUtil.searchMap(sql, new Object[] { id });
		return "updatedevice";
	}

	/**
	 * 修改客户端状态
	 * 
	 * @return
	 */
	public String UpdateDeviceState() {
		String id = mapparam.get("id");
		String state = mapparam.get("state");
		String sql = "update devices set state=? where id=?";
		int i = SqlUtil.update(sql, new Object[] { state, id });
		if (i <= 0) {
			map.put("state", 0);
			map.put("msg", "操作失败");
		}
		return "mapjson";
	}

	/**
	 * 添加客户端
	 * 
	 * @return
	 */
	public String AddDevice() {
		String userid = mapparam.get("userid");
		String clientname = mapparam.get("clientname");
		String deviceimei = mapparam.get("deviceimei");
		String phone = mapparam.get("phone");
		String email = mapparam.get("email");
		String code = mapparam.get("code");
		String endtime = mapparam.get("endtime");
		if ("".equals(code)) {
			map.put("state", 0);
			map.put("msg", "邀请码不能为空");
		} else if ("".equals(endtime)) {
			map.put("state", 0);
			map.put("msg", "截止时间不能为空");
		} else {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			if (session.containsKey("adminuwer")) {
				String sql = "select * from devices where userid=? and code=?";
				int count = SqlUtil.searchCount(sql, new Object[] { userid,
						code });
				if (count > 0) {
					map.put("state", 0);
					map.put("msg", "邀请码已存在");
				} else {
					sql = "insert into devices(userid,clientname,deviceimei,phone,code,endtime,state,email) values(?,?,?,?,?,?,?,?)";
					int i = SqlUtil.save(sql, new Object[] { userid,
							clientname, deviceimei, phone, code, endtime, "1",
							email });
					if (i > 0) {
						map.put("state", 1);
						map.put("msg", "添加成功");
					} else {
						map.put("state", 0);
						map.put("msg", "添加失敗");
					}
				}
			} else {
				map.put("state", 0);
				map.put("msg", "你还没有登陆");
			}
		}
		return "mapjson";
	}

	/**
	 * 修改客户端
	 * 
	 * @return
	 */
	public String UpdateDevice() {
		String id = mapparam.get("id");
		String clientname = mapparam.get("clientname");
		String deviceimei = mapparam.get("deviceimei");
		String phone = mapparam.get("phone");
		String code = mapparam.get("code");
		String email = mapparam.get("email");
		String endtime = mapparam.get("endtime");
		if ("".equals(code)) {
			map.put("state", 0);
			map.put("msg", "邀请码不能为空");
		} else if ("".equals(endtime)) {
			map.put("state", 0);
			map.put("msg", "截止时间不能为空");
		} else {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			if (session.containsKey("adminuwer")) {

				String sql = "update devices set deviceimei=?,clientname=?,phone=?,code=?,endtime=?,email=? where id=?";
				int i = SqlUtil.save(sql, new Object[] { deviceimei,
						clientname, phone, code, endtime, email, id });
				if (i > 0) {
					map.put("state", 1);
					map.put("msg", "修改成功");
				} else {
					map.put("state", 0);
					map.put("msg", "修改失敗");
				}
			} else {
				map.put("state", 0);
				map.put("msg", "你还没有登陆");
			}
		}
		return "mapjson";
	}

	/**
	 * 客户端列表
	 * 
	 * @return
	 */
	public String SmsList() {
		String sql = "SELECT a.*,b.phone,c.name FROM devicesms a left join devices b on a.deviceid=b.id left join user c on b.userid=c.id where b.id is not null";
		if (mapparam.containsKey("name") && !"".equals(mapparam.get("name"))) {
			sql += " and name like ?";
			array.add("%" + mapparam.get("name") + "%");
		}
		if (mapparam.containsKey("phone") && !"".equals(mapparam.get("phone"))) {
			sql += " and phone like ?";
			array.add("%" + mapparam.get("phone") + "%");
		}
		page.setTsize(SqlUtil.searchCount(sql,
				array.toArray(new Object[array.size()])));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, array.toArray(new Object[array.size()]),
				page));
		return "smslist";
	}

	/**
	 * 通话列表
	 * 
	 * @return
	 */
	public String PhoneList() {
		String sql = "SELECT a.*,b.phone,c.name FROM devicephone a left join devices b on a.deviceid=b.id left join user c on b.userid=c.id where b.id is not null";
		if (mapparam.containsKey("name") && !"".equals(mapparam.get("name"))) {
			sql += " and name like ?";
			array.add("%" + mapparam.get("name") + "%");
		}
		if (mapparam.containsKey("phone") && !"".equals(mapparam.get("phone"))) {
			sql += " and phone like ?";
			array.add("%" + mapparam.get("phone") + "%");
		}
		page.setTsize(SqlUtil.searchCount(sql,
				array.toArray(new Object[array.size()])));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, array.toArray(new Object[array.size()]),
				page));
		return "phonelist";
	}

	/**
	 * 位置列表
	 * 
	 * @return
	 */
	public String LocationList() {
		String sql = "SELECT a.*,b.phone,c.name FROM devicelocation a left join devices b on a.deviceid=b.id left join user c on b.userid=c.id where b.id is not null";
		if (mapparam.containsKey("name") && !"".equals(mapparam.get("name"))) {
			sql += " and name like ?";
			array.add("%" + mapparam.get("name") + "%");
		}
		if (mapparam.containsKey("phone") && !"".equals(mapparam.get("phone"))) {
			sql += " and phone like ?";
			array.add("%" + mapparam.get("phone") + "%");
		}
		page.setTsize(SqlUtil.searchCount(sql,
				array.toArray(new Object[array.size()])));
		page.setIsasc(false);
		lmo.addAll(SqlUtil.search(sql, array.toArray(new Object[array.size()]),
				page));
		return "locationlist";
	}

	/**
	 * 环境录音
	 * 
	 * @return
	 */
	public String RecordList() {
		String sql = "SELECT a.*,b.phone,c.name FROM devicesound a left join devices b on a.deviceid=b.id left join user c on b.userid=c.id where b.id is not null";
		if (mapparam.containsKey("name") && !"".equals(mapparam.get("name"))) {
			sql += " and name like ?";
			array.add("%" + mapparam.get("name") + "%");
		}
		if (mapparam.containsKey("phone") && !"".equals(mapparam.get("phone"))) {
			sql += " and phone like ?";
			array.add("%" + mapparam.get("phone") + "%");
		}
		page.setTsize(SqlUtil.searchCount(sql,
				array.toArray(new Object[array.size()])));
		page.setIsasc(true);
		lmo.addAll(SqlUtil.search(sql, array.toArray(new Object[array.size()]),
				page));
		return "recordlist";
	}

	/**
	 * 环境拍照
	 * 
	 * @return
	 */
	public String PhotoList() {
		String sql = "SELECT a.*,b.phone,c.name FROM devicephoto a left join devices b on a.deviceid=b.id left join user c on b.userid=c.id where b.id is not null";
		if (mapparam.containsKey("name") && !"".equals(mapparam.get("name"))) {
			sql += " and name like ?";
			array.add("%" + mapparam.get("name") + "%");
		}
		if (mapparam.containsKey("phone") && !"".equals(mapparam.get("phone"))) {
			sql += " and phone like ?";
			array.add("%" + mapparam.get("phone") + "%");
		}
		page.setTsize(SqlUtil.searchCount(sql,
				array.toArray(new Object[array.size()])));
		page.setIsasc(true);
		lmo.addAll(SqlUtil.search(sql, array.toArray(new Object[array.size()]),
				page));
		return "photolist";
	}

	
	public Map<String, Object> getMap() {
		System.out.println(map);
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

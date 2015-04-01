package com.android.smsserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.android.db.SqlUtil;
import com.android.push.PushClient;
import com.opensymphony.xwork2.ActionSupport;

public class ClientAction extends ActionSupport {

	/**
	 * 
	 */
	private static Logger log=Logger.getLogger(ClientAction.class);
	private static final long serialVersionUID = 1L;
	private Map<String, Object> map = new HashMap<String, Object>();
	private Map<String, String> mapparam = new HashMap<String, String>();
	private List<Map<String, Object>> lmo = new ArrayList<Map<String, Object>>();



	/**
	 * 客户端认证
	 * 
	 * @return
	 */
	public String AuthDevice() {

			String sql = "select a.*,b.serverid from devices a left join user b on a.userid=b.id where code=?";
			Map<String, Object> mo = SqlUtil.searchMap(sql,
					new Object[] { mapparam.get("code") });
			if ("".equals(mo.get("deviceimei").toString())) {
				sql = "update devices set deviceimei=?,phone=?,clientid=? where id=?";
				int i = SqlUtil.update(
						sql,
						new Object[] { mapparam.get("deviceimei"),
								mapparam.get("phone"),
								mapparam.get("clientid"), mo.get("id") });
				if (i > 0) {
					map.put("state", 1);
					map.put("sms", mo.get("sms"));
					map.put("call", mo.get("call"));
					map.put("loc", mo.get("loc"));
					map.put("count", mo.get("count"));
					map.put("id", mo.get("id"));
					map.put("serverid", mo.get("serverid"));
				} else {
					map.put("state", 0);
					map.put("msg", "认证失败,请联系管理员");
				}
			} else {
				if (mapparam.get("deviceimei").equals(
						mo.get("deviceimei").toString())) {
					sql = "update devices set phone=? ,clientid=? where id=?";
					SqlUtil.update(sql, new Object[] { mapparam.get("phone"),
							mapparam.get("clientid"), mo.get("id") });
					map.put("state", 1);
					map.put("sms", mo.get("sms"));
					map.put("call", mo.get("call"));
					map.put("loc", mo.get("loc"));
					map.put("count", mo.get("count"));
					map.put("id", mo.get("id"));
					map.put("serverid", mo.get("serverid"));
				} else {
					map.put("state", 0);
					map.put("msg", "该邀请码已经绑定用户");
				}
			
		} 
		return "mapjson";
	}
	
	/**
	 * 客户端获取客户端配置信息
	 * 
	 * @return
	 */
	public String getDeviceConfig(){
		String sql="SELECT a.email,a.pass FROM devices a where deviceimei=? and endtime>SYSDATE() and state=1";
		map=SqlUtil.searchMap(sql, new String[]{mapparam.get("deviceimei")});
		return "mapjson";
	}
	

	/**
	 * 客户端pushclientid更新
	 * 
	 * @return
	 */
	public String UpdateClientId() {
		String sql = "update devices set clientid=? where deviceimei=?";
		int i = SqlUtil.update(sql, new Object[] { mapparam.get("clientid"),
				mapparam.get("imei") });
		if (i > 0) {
			map.put("state", 1);
		} else {
			map.put("state", 0);
		}
		return "mapjson";
	}

	/**
	 * 发送消息到Server
	 * 
	 * @return
	 */
	public String sendToServer() {
		
		boolean isflag = PushClient.SendMessage(mapparam.get("clientid"),
				mapparam.get("action"));
		if (isflag) {
			map.put("state", 1);
		} else {
			map.put("state", 0);
		}
		return "mapjson";
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


	public Map<String, String> getMapparam() {
		return mapparam;
	}

	public void setMapparam(Map<String, String> mapparam) {
		this.mapparam = mapparam;
	}

	
}

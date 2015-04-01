package com.zjzs.struts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.zjzs.db.SqlUtil;
import com.zjzs.utils.Page;

public class VipAction extends ActionSupport {

	private Page page = new Page();
	private List<Object> param = new ArrayList<Object>();
	private Map<String, Object> map=new HashMap<String, Object>();
	private List<Map<String, Object>> lmo;
	private int id;
	private int zt=-1;
	private String imei;
	private String end;
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	

	private String currid;
	

	

	public String getCurrid() {
		return currid;
	}

	public void setCurrid(String currid) {
		this.currid = currid;
	}

	public String viplist() {
		String sql = "select a.*,case when a.state=0 then '禁用' when a.state=1 then '可用' else '加黑' end as zt from user a where 1=1";
		if(imei!=null&&!"".equals(imei)){
			sql=sql+" and a.imei like ?";
			param.add("%"+imei+"%");
		}
		if(zt!=-1){
			sql=sql+" and a.state=?";
			param.add(zt);
		}
		page.setTsize(SqlUtil.searchCount(sql, param.toArray()));
		lmo = SqlUtil.search(sql, param.toArray(), page);
		return "list";
	}

	public String upzt() {
		String sql = "update user set state=? where id=?";
		param.add(zt);
		param.add(id);
		int i=SqlUtil.update(sql, param.toArray());
		if(i>0){
			map.put("msg", "修改成功");
		}else{
			map.put("msg", "修改失败");
		}
		return "json";
	}
	public String update() {
		String sql = "update user set state=?,end=?,currid=? where id=?";
		param.add(zt);
		param.add(end);
		param.add(currid);
		param.add(id);
		int i=SqlUtil.update(sql, param.toArray());
		if(i>0){
			map.put("msg", "修改成功");
		}else{
			map.put("msg", "修改失败");
		}
		return "json";
	}
	
	public String toupdatePage(){
		String sql="select * from user where id=?";
		param.add(id);
		map=SqlUtil.searchMap(sql, param.toArray());
		sql="select * from image where state=1";
		param.clear();
		lmo=SqlUtil.search(sql, null);
		return "update";
	}
	public String dele(){
		String sql="delete from user where id=?";
		param.add(id);
		int i=SqlUtil.update(sql, param.toArray());
		if(i>0){
			map.put("msg", "删除成功");
		}else{
			map.put("msg", "删除失败");
		}
		return "json";
	}
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Map<String, Object> getMap() {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getZt() {
		return zt;
	}

	public void setZt(int zt) {
		this.zt = zt;
	}

}

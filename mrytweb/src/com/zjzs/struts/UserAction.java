package com.zjzs.struts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.zjzs.db.SqlUtil;
import com.zjzs.pojo.User;

public class UserAction extends ActionSupport {

	private User user;
	private List<Object> param=new ArrayList<Object>();
	private Map<String, Object> map=new HashMap<String, Object>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String login(){
		String sql="select * from admin where name=? and pass=? and endtime>=CURDATE()";
		param.add(user.getName());
		param.add(user.getPass());
		int i=SqlUtil.searchCount(sql, param.toArray());
		if(i>0){
			ActionContext.getContext().getSession().put("name", user.getName());
			return "main";
		}
		map.put("msg", "用户名或密码错误");
		return "login";
	}
	public String update(){
		String sql="update admin set pass=? where name=?";
		param.add(user.getPass());
		Object name=ActionContext.getContext().getSession().get("name");
		param.add(name);
		int i=SqlUtil.update(sql, param.toArray());
		if(i>0){
			map.put("msg", "修改密码成功");
		}else{
			map.put("msg", "修改密码失败");
		}
		return "json";
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}

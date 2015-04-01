package com.zjzs.struts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.zjzs.db.SqlUtil;

public class AutoAction extends ActionSupport {

	private String term;
	private List<Object> param=new ArrayList<Object>();
	private List<Map<String, Object>> lmo;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String name(){
		System.out.println(term);
		String sql="select name value from out_data where name like ? group by name";
		param.add("%"+term+"%");
		System.out.println(param.toString());
		lmo=SqlUtil.search(sql, param.toArray());
		System.out.println(lmo.size());
		return SUCCESS;
	}
	public String pinpai(){
		System.out.println(term);
		String sql="select pinpai value from out_data where pinpai like ? group by pinpai";
		param.add(term+"%");
		lmo=SqlUtil.search(sql, param.toArray());
		System.out.println(lmo.size());
		return SUCCESS;
	}

	public String guige(){
		System.out.println(term);
		String sql="select guige value from out_data where guige like ? group by guige";
		param.add("%"+term+"%");
		lmo=SqlUtil.search(sql, param.toArray());
		System.out.println(lmo.size());
		return SUCCESS;
	}

	public List<Map<String, Object>> getLmo() {
		return lmo;
	}

	public void setLmo(List<Map<String, Object>> lmo) {
		this.lmo = lmo;
	}


	public String getTerm() {
		return term;
	}


	public void setTerm(String term) {
		this.term = term;
	}

}

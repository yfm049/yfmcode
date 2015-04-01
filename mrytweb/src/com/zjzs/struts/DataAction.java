package com.zjzs.struts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.opensymphony.xwork2.ActionSupport;
import com.zjzs.db.SqlUtil;

public class DataAction extends ActionSupport {

	private String imei;
	private String test;
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private Map<String, Object> map=new HashMap<String, Object>();
	private List<Object> param = new ArrayList<Object>();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	public String regUser(){
		String sql="select * from user where imei=?";
		param.add(imei);
		Map<String, Object> mo=SqlUtil.searchMap(sql, param.toArray());
		if(mo.containsKey("imei")){
			if("1".equals(mo.get("state").toString())){
				if(isbefore(mo.get("end").toString())){
					if(mo.get("code").toString().equals(code)||"true".equals(test)){
						map.put("flag", true);
						getgifurl();
					}else{
						map.put("msg", "验证码错误");
						map.put("flag", false);
					}
					
				}else{
					map.put("msg", "服务已到期,如需要继续使用请联系管理员!");
					map.put("flag", false);
				}
			}else{
				map.put("msg", "该用户已经禁用");
				map.put("flag", false);
			}
		}else if("true".equals(test)){
			sql="insert into user(imei,code,start,end,last,currid,state) values(?,?,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 10 DAY),CURDATE(),?,1)";
			param.add(getRandomNumber(4));
			String imgname=sdf.format(new Date())+".gif";
			param.add(imgname);
			int i=SqlUtil.save(sql, param.toArray());
			if(i>0){
				map.put("flag", true);
				getgifurl();
			}else{
				map.put("msg", "服务已到期,如需要继续使用请联系管理员!");
				map.put("flag", false);
			}
		}else{
			map.put("msg", "用户不存在");
			map.put("flag", false);
		}
		return "json";
	}
	public String getregCode(){
		String sql="select * from user where imei=?";
		param.add(imei);
		Map<String, Object> mo=SqlUtil.searchMap(sql, param.toArray());
		if(mo.containsKey("imei")){
			if("1".equals(mo.get("state").toString())){
				if(isbefore(mo.get("end").toString())){
					map.put("flag", true);
				}else{
					map.put("msg", "服务已到期,,如需要继续使用请联系管理员!");
					map.put("flag", false);
				}
			}else{
				map.put("msg", "该用户被禁用");
				map.put("flag", false);
			}
		}else{
			String imgname=sdf.format(new Date())+".gif";
			sql="insert into user(imei,code,start,end,last,currid,state) values(?,?,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 10 DAY),CURDATE(),?,1)";
			param.add(getRandomNumber(4));
			param.add(imgname);
			int i=SqlUtil.save(sql, param.toArray());
			if(i>0){
				map.put("flag", true);
			}else{
				map.put("msg", "该用户被禁用");
				map.put("flag", false);
			}
		}
		return "json";
	}
	public String getgifurl(){
		String imgname=sdf.format(new Date())+".gif";
		String sql="update user set currid=?,last=CURDATE() where imei=?";
		param.clear();
		param.add(imgname);
		param.add(imei);
		SqlUtil.update(sql, param.toArray());
		sql="select * from image where img=?";
		param.clear();
		param.add(imgname);
		Map<String, Object> mo=SqlUtil.searchMap(sql, param.toArray());
		if(mo.containsKey("state")){
			if("1".equals(mo.get("state").toString())){
				map.put("imgflag", true);
				map.put("gifimg", imgname);
			}else{
				map.put("msg", "图片已停用");
				map.put("flag", false);
			}
			
		}else{
			map.put("msg", "用户不存在");
			map.put("flag", false);
		}
		System.out.println(map.toString());
		return "json";
	}
	public boolean isbefore(String end){
		try {
			Date endtime=sdf.parse(end);
			if(endtime.after(new Date())){
				return true;
			}else{
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private String getRandomNumber(int n) {   
        int [] seed = {0,1,2,3,4,5,6,7,8,9};   
        StringBuffer sb=new StringBuffer();
        Random ran = new Random();   
        for(int i = 0 ; i<n ; i++){   
            int j = ran.nextInt(seed.length-i);   
            sb.append(seed [j]);   
        }   
        return sb.toString();
    }
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}

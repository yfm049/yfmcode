package com.zjzs.struts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;


import com.opensymphony.xwork2.ActionSupport;
import com.zjzs.db.SqlUtil;
import com.zjzs.utils.Page;

public class ImgAction extends ActionSupport {

	private Page page = new Page();
	private List<Object> param = new ArrayList<Object>();
	private Map<String, Object> map=new HashMap<String, Object>();
	private List<Map<String, Object>> lmo;
	private int id=-1;
	private int zt=-1;
	private File filedata;
	private String filedataFileName;
	private String filename;
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiledataFileName() {
		return filedataFileName;
	}

	public void setFiledataFileName(String filedataFileName) {
		this.filedataFileName = filedataFileName;
	}

	public File getFiledata() {
		return filedata;
	}

	public void setFiledata(File filedata) {
		this.filedata = filedata;
	}

	public String imglist() {
		String sql = "select *,case when state=1 then '可用' else '禁用' end as zt from image where 1=1";
		if(zt!=-1){
			sql=sql+" and state=?";
			param.add(zt);
		}
		page.setTsize(SqlUtil.searchCount(sql, param.toArray()));
		page.setPaixu("img");
		lmo = SqlUtil.search(sql, param.toArray(), page);
		return "list";
	}

	public String upzt() {
		String sql = "update image set state=? where id=?";
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
	public String dele(){
		
		String sql="delete from image where id=?";
		param.add(id);
		int i=SqlUtil.update(sql, param.toArray());
		if(i>0){
			String path=ServletActionContext.getServletContext().getRealPath("/gif");
			File file=new File(path,filename);
			if(file.exists()){
				file.delete();
			}
			map.put("msg", "删除成功");
		}else{
			map.put("msg", "删除失败");
		}
		return "json";
	}
	public String upload(){
		String path=ServletActionContext.getServletContext().getRealPath("/gif");
		File file=new File(path+"/"+filedataFileName);
		filedata.renameTo(file);
		if(id!=-1){
			String sql="update image set img=?,imgtime=CURDATE() where id=?";
			param.add(file.getName());
			param.add(id);
			SqlUtil.update(sql, param.toArray());
		}else{
			String sql="insert into image(img,imgtime,state) values (?,CURDATE(),?)";
			param.add(file.getName());
			param.add(1);
			SqlUtil.save(sql, param.toArray());
		}
		return "json";
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

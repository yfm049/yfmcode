package com.android.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.android.page.Page;
import com.android.smsserver.ServerAction;

public class SqlUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
	        "yyyy-MM-dd HH:mm:ss");
	private static Logger log=Logger.getLogger(SqlUtil.class);
	/**
	 * 调用存储过程
	 * 
	 * @param sql
	 * @param lo
	 * @return
	 */
	public static List<Map<String, Object>> callproc(String sql, Object[] lo) {
		log.info("----------------------------------------");
		printArrays(lo);
		log.info(sql);
		Connection con = DbCon.getcon();
		List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = con.prepareStatement("{call "+sql+" }");
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData rm = rs.getMetaData();
			int cont = rm.getColumnCount();
			while (rs.next()) {
				Map<String, Object> mp = new LinkedHashMap<String, Object>();
				for (int c = 1; c <= cont; c++) {
					Object o = rs.getObject(c);
					if (o instanceof Date) {
						String date = o == null ? "" : sdf.format(o);
						mp.put(rm.getColumnName(c), date);
						continue;
					}
					mp.put(rm.getColumnName(c), o == null ? "" : o);
				}
				lm.add(mp);
			}
		} catch (SQLException e) {
			log.info("执行" + sql + "抛出异常");
			e.printStackTrace();
			lm=null;
		} finally {
			DbCon.closecon(con,ps,rs);
		}
		return lm;
	}
	
	/**
	 * 调用存储过程取得的记录总数
	 * 
	 * @param sql
	 * @param lo
	 * @return
	 */
	public static int callprocCount(String sql,Object[] lo) {
		log.info("----------------------------------------");
		String sqlcount = "select count(*) ct from (" + sql + ") p";
		int count = 0;
		printArrays(lo);
		log.info(sqlcount);
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection con = DbCon.getcon();
		try {
			ps = con.prepareStatement(sqlcount);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			rs = ps.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt("ct");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("执行" + sql + "抛出异常");
			e.printStackTrace();
			count=-1;
		} finally {
			DbCon.closecon(con,ps,rs);
		}
		return count;
	}
	
	/**
	 * 
	 * @param sql
	 *            执行的sql
	 * @param lo
	 *            输入参数
	 * @return 返回由list 和map组成的数据集合
	 */
	public static List<Map<String, Object>> search(String sql, Object[] lo) {
		log.info("----------------------------------------");
		printArrays(lo);
		log.info(sql);
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection con = DbCon.getcon();
		List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
		try {
			ps = con.prepareStatement(sql);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData rm = rs.getMetaData();
			int cont = rm.getColumnCount();
			while (rs.next()) {
				Map<String, Object> mp = new LinkedHashMap<String, Object>();
				for (int c = 1; c <= cont; c++) {
					Object o = rs.getObject(c);
					if (o instanceof Date) {
						String date = o == null ? "" : sdf.format(o);
						mp.put(rm.getColumnName(c), date);
						continue;
					}
					mp.put(rm.getColumnName(c), o == null ? "" : o);
				}
				lm.add(mp);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("执行" + sql + "抛出异常");
			e.printStackTrace();
			lm=null;
		} finally {
			DbCon.closecon(con,ps,rs);
		}
		return lm;
	}
	
	/**
	 * 
	 * @param sql
	 *            执行的sql
	 * @param lo
	 *            执行参数
	 * @param page
	 *            分页查询
	 * @return 返回由list 和map组成的数据集合
	 */
	public static List<Map<String, Object>> search(String sql, Object[] lo,Page page) {
		log.info("----------------------------------------");
		printArrays(lo);
		int start = (page.getCpage() - 1) * page.getPsize();
		int end = page.getCpage() * page.getPsize();
		String px = "desc";
		if (page.isIsasc()) {
			px = "asc";
		}
//		String pagesql = "select * from (select *, ROW_NUMBER() over(order by "
//		        + page.getPaixu() + " " + px + ") rownum from (" + sql
//		        + ") m) p where rownum between " + start + " and " + end + "";
		String pagesql = "select * from (select * from (" + sql + ") m ORDER BY "+page.getPaixu()+" "+px+") p LIMIT " + start + "," + page.getPsize() + "";
		
//		String pagesql="select * from (select a.*, rownum rn from ("+sql+") a where rownum <= "+end+")where rn >= "+start+"";
		log.info(pagesql);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection con = DbCon.getcon();
		List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
		try {
			ps = con.prepareStatement(pagesql);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData rm = rs.getMetaData();
			int cont = rm.getColumnCount();
			while (rs.next()) {
				Map<String, Object> mp = new HashMap<String, Object>();
				for (int c = 1; c <= cont; c++) {
					Object o = rs.getObject(c);
					if (o instanceof Date) {
						String date = o == null ? "" : sdf.format(o);
						mp.put(rm.getColumnName(c), date);
						continue;
					}
					mp.put(rm.getColumnName(c), o == null ? "" : o);
				}
				lm.add(mp);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("执行" + sql + "抛出异常");
			e.printStackTrace();
			lm=null;
		} finally {
			DbCon.closecon(con,ps,rs);
		}
		return lm;
	}
	
	/**
	 * 
	 * @param sql
	 *            执行的sql
	 * @param lo
	 *            参数
	 * @return 返回执行后保存数据的主键 例如id
	 */
	public static int save(String sql, Object[] lo) {
		log.info("----------------------------------------");
		printArrays(lo);
		log.info(sql);
		int row = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection con = DbCon.getcon();
		try {
			ps = con.prepareStatement(sql,
			        PreparedStatement.RETURN_GENERATED_KEYS);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			row = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				row = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			row=-1;
		} finally {
			DbCon.closecon(con,ps,rs);
		}
		return row;
	}
	/**
	 * 
	 * @param sql
	 *            执行的sql
	 * @param lo
	 *            参数
	 * @return 返回执行后保存数据的主键 例如id
	 */
	public static int save(Connection con,String sql, Object[] lo,boolean isreturnkey) {
		log.info("----------------------------------------");
		printArrays(lo);
		log.info(sql);
		PreparedStatement ps=null;
		ResultSet rs=null;
		int row = 0;
		try {
			if(isreturnkey){
				ps = con.prepareStatement(sql,
				        PreparedStatement.RETURN_GENERATED_KEYS);
			}else{
				ps = con.prepareStatement(sql);
			}
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			ps.executeUpdate();
			if(isreturnkey){
				rs = ps.getGeneratedKeys();
				if (rs != null && rs.next()) {
					row = rs.getInt(1);
				}
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			row=-1;
		} finally{
			DbCon.closecon(con,ps,rs);
		}
		return row;
	}
	
	/**
	 * 
	 * @param sql
	 *            执行sql
	 * @param lo
	 *            输入参数
	 * @return 返回更新的条数
	 */
	public static int update(String sql, Object[] lo) {
		log.info("----------------------------------------");
		printArrays(lo);
		log.info(sql);
		int row = 0;
		PreparedStatement ps=null;
		Connection con = DbCon.getcon();
		try {
			
			ps = con.prepareStatement(sql);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			row = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			row=-1;
		} finally {
			DbCon.closecon(con,ps,null);
		}
		return row;
	}
	
	/**
	 * 
	 * @param sql
	 *            执行的sql
	 * @param lo
	 *            参数
	 * @return 返回一条数据组成的map集合
	 */
	public static Map<String, Object> searchMap(String sql, Object[] lo) {
		log.info("----------------------------------------");
		printArrays(lo);
		log.info(sql);
		Connection con = DbCon.getcon();
		Map<String, Object> mp = new HashMap<String, Object>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = con.prepareStatement(sql);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData rm = rs.getMetaData();
			int cont = rm.getColumnCount();
			while (rs.next()) {
				
				for (int c = 1; c <= cont; c++) {
					Object o = rs.getObject(c);
					if (o instanceof Date) {
						String date = o == null ? "" : sdf.format(o);
						mp.put(rm.getColumnName(c), date);
						continue;
					}
					mp.put(rm.getColumnName(c), o == null ? "" : o);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			log.info("执行" + sql + "抛出异常");
			e.printStackTrace();
			mp=null;
		} finally {
			DbCon.closecon(con,ps,rs);
		}
		return mp;
	}
	
	/**
	 * 
	 * @param sql
	 *            执行sql
	 * @param lo
	 *            输入参数
	 * @return 返回总条数
	 */
	public static int searchCount(String sql, Object[] lo) {
		log.info("----------------------------------------");
		printArrays(lo);
		int count = 0;
		
		String sqlcount = "select count(*) ct from (" + sql + ") p";
		log.info(sqlcount);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection con = DbCon.getcon();
		try {
			ps = con.prepareStatement(sqlcount);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt("ct");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("执行" + sql + "抛出异常");
			e.printStackTrace();
			count=-1;
		} finally {
			DbCon.closecon(con,ps,rs);
		}
		return count;
	}
	
	/**
	 * 
	 * @param sql
	 *            执行sql
	 * @param lo
	 *            执行参数
	 * @return 返回删除数据条数
	 */
	public static int delete(String sql, Object[] lo) {
		log.info("----------------------------------------");
		printArrays(lo);
		log.info(sql);
		int row = 0;
		Connection con = DbCon.getcon();
		PreparedStatement ps=null;
		try {
			
			ps = con.prepareStatement(sql);
			if (lo != null) {
				
				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			row = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("执行" + sql + "抛出异常");
			row=-1;
		} finally {
			
			DbCon.closecon(con,ps,null);
		}
		return row;
	}
	
	
	
	public static void printArrays(Object[] lo){
		StringBuffer sb=new StringBuffer();
		if(lo!=null){
			sb.append("[");
			for(Object o:lo){
				sb.append(o+";");
			}
			sb.append("]");
		}
		log.info(sb.toString());
	}
	
}

package com.app.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SqlUtil {
	
	public static int save(String sql, Object[] lo){
		int row=0;
		Connection con = DbCon.getcon();
		try {
			
			PreparedStatement ps = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			if (lo != null) {

				for (int i = 1; i <= lo.length; i++) {
					ps.setObject(i, lo[i - 1]);
				}
			}
			ps.executeUpdate();
			ResultSet rs=ps.getGeneratedKeys();
			if(rs!=null&&rs.next()){
				row=rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DbCon.closecon(con);
		}
		return row;
	}
	
}

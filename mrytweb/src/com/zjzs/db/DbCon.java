package com.zjzs.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DbCon {
	public static BoneCP connectionPool = null;
	static{
		try {
			InputStream inputStream = DbCon.class.getResourceAsStream("/default.properties");
			Properties p = new Properties();
			p.load(inputStream);
			Class.forName("com.mysql.jdbc.Driver");
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(p.getProperty("db.jdbc"));
			config.setUsername(p.getProperty("db.username"));
			config.setPassword(p.getProperty("db.pwd"));
			config.setMinConnectionsPerPartition(5);
			config.setMaxConnectionsPerPartition(10);
			config.setPartitionCount(1);
			connectionPool = new BoneCP(config); // setup the connection pool
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getcon() {
		Connection connection=null;
		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public static boolean closecon(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return true;
	}

}

package cn.sj1.dict.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class H2DB {
	DataSource dataSource;

	public H2DB(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Connection conn;

//	public void connectDb() throws ClassNotFoundException, SQLException { // 加载H2数据库驱动
////		Class.forName(DRIVER_CLASS);
////		// 根据连接URL，用户名，密码获取数据库连接
////		this.conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
//	}
//
//	public static H2DB connect(String dir) throws ClassNotFoundException, SQLException {
//		H2DB h2db = new H2DB(dir);
//		h2db.connectDb();
//		return h2db;
//	}

	public Connection getConn() throws SQLException {
		return dataSource.getConnection();
	}

	public void createTable(String sql) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute("CREATE TABLE USERWORDS(ID INTEGER,USERID INTEGER,WORD VARCHAR,UPDATED TIMESTAMP");
		stmt.close();
		conn.close();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		Connection conn = dataSource.getConnection();
		return conn.prepareStatement(sql);
	}

	public void close() throws SQLException {
	}

}

package com.engreader.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DB {
	static String DEFAULT_dbDir = "./data/db.h2";
	// 数据库连接URL，通过使用TCP/IP的服务器模式（远程连接）
	private String JDBC_URL;;
	// 连接数据库时使用的用户名
	private static final String USER = "sa";
	// 连接数据库时使用的密码
	private static final String PASSWORD = "123";
	// 连接H2数据库时使用的驱动类，org.h2.Driver这个类是由H2数据库自己提供的，在H2数据库的jar包中可以找到
	private static final String DRIVER_CLASS = "org.h2.Driver";
	Connection conn;

	final String dir;

	private H2DB(String dir) {
		this.dir = dir;
		JDBC_URL = "jdbc:h2:" + dir;
	}

	public void connectDb() throws ClassNotFoundException, SQLException { // 加载H2数据库驱动
		Class.forName(DRIVER_CLASS);
		// 根据连接URL，用户名，密码获取数据库连接
		this.conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	}

	public static H2DB connect(String dir) throws ClassNotFoundException, SQLException {
		H2DB h2db = new H2DB(dir);
		h2db.connectDb();
		return h2db;
	}

	public void createTable(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.execute("CREATE TABLE USERWORDS(ID INTEGER,USERID INTEGER,WORD VARCHAR,UPDATED TIMESTAMP");
		stmt.close();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}

	public void close() throws SQLException {
		this.conn.close();
	}

}

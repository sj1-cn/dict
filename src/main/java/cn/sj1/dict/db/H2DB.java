package cn.sj1.dict.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class H2DB {
	DataSource dataSource;

	public H2DB(DataSource dataSource) {
		this.dataSource = dataSource;
	}

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

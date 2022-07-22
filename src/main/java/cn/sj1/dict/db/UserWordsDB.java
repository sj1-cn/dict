package cn.sj1.dict.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cn.sj1.user.UserWord;

public class UserWordsDB {
	H2DB h2db;

	public UserWordsDB(H2DB h2db) {
		super();
		this.h2db = h2db;
	}

	public void createTable() {
		try {
			Statement stmt = this.h2db.conn.createStatement();
			stmt.execute("CREATE TABLE USERWORDS(ID INTEGER,USERID INTEGER,WORD VARCHAR,UPDATED TIMESTAMP)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//CREATE INDEX index_name	ON table_name (column_name)
	public void createIndex() {
		try {
			Statement stmt = this.h2db.conn.createStatement();
			stmt.execute("CREATE INDEX USERID ON USERWORDS (USERID)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<UserWord> getAll(int userid) {

		List<UserWord> list;
		try {
			PreparedStatement stmt = h2db.conn
				.prepareStatement("SELECT ID,USERID,WORD,UPDATED FROM USERWORDS WHERE USERID=?");

			stmt.setInt(1, userid);
			ResultSet res = stmt.executeQuery();

			list = new ArrayList<>();

			while (res.next()) {
				int i = 1;
				int id = res.getInt(i++);
				int userId = res.getInt(i++);
				String word = res.getString(i++);
				Timestamp updated = res.getTimestamp(i++);
				UserWord userword = new UserWord(id, userId, word, updated);
				list.add(userword);
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return list;
	}

	public boolean insert(UserWord data) {
		PreparedStatement stmt;
		boolean result;
		try {
			stmt = h2db.conn.prepareStatement("INSERT INTO USERWORDS(ID,USERID,WORD,UPDATED) VALUES(?,?,?,?)");
			int i = 1;
			stmt.setInt(i++, data.getId());
			stmt.setInt(i++, data.getUserId());
			stmt.setString(i++, data.getWord());
			stmt.setTimestamp(i++, data.getUpdated());
			result = stmt.execute();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public boolean insert(List<UserWord> list) {
//		boolean result;
		try {
			PreparedStatement stmt = h2db.conn
				.prepareStatement("INSERT INTO USERWORDS(ID,USERID,WORD,UPDATED) VALUES(?,?,?,?)");

			for (UserWord data : list) {
				int i = 1;
				stmt.setInt(i++, data.getId());
				stmt.setInt(i++, data.getUserId());
				stmt.setString(i++, data.getWord());
				stmt.setTimestamp(i++, data.getUpdated());
				stmt.addBatch();
			}
			stmt.executeBatch();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return true;
	}

	public boolean remove(int id) {
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement("DELETE FROM USERWORDS WHERE ID=?");
			stmt.setInt(1, id);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return true;
	}

	public boolean remove(List<UserWord> userwords) {
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement("DELETE FROM USERWORDS WHERE ID=?");
			for (UserWord w : userwords) {
				stmt.setInt(1, w.getId());
				stmt.addBatch();
			}
			boolean result = stmt.execute();
			stmt.close();
			return result;
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
//
//	void remove(List<Integer> ids) {
//		try {
//			PreparedStatement stmt = h2db.conn.prepareStatement("DELETE FROM USERWORDS WHERE ID=?");
//			for (int id : ids) {
//				stmt.setInt(1, id);
//				stmt.addBatch();
//			}
//			stmt.execute();
//			stmt.close();
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}

}

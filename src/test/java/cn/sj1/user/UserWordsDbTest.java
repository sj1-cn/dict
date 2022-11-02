package cn.sj1.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.sj1.dict.db.H2DB;
import cn.sj1.dict.db.UserWordsDB;

class UserWordsDbTest {
	H2DB h2db;
	UserWordsDB store;
	static String dbFileName = "./dbtest/UserWordsDbTest001.h2";

	@BeforeEach
	void setup() throws ClassNotFoundException, SQLException {

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl("jdbc:h2:" + dbFileName);
		hikariConfig.setUsername("sa");
		hikariConfig.setPassword("123");

		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		h2db = new H2DB(dataSource);

		store = new UserWordsDB(h2db);
	}

	@AfterEach
	void tearDown() throws SQLException {
		h2db.close();
	}
//
//	@Test
//	void testUserWordsStore() {
//		fail("Not yet implemented");
//	}

	@Test
	void testCreateTable() {
		try {
			store.createTable();
			store.createIndex();
		} catch (Exception e) {
		}
	}

	@Test
	void testGetAll() {
		List<UserWord> list = store.getAll(0);
		assertTrue(list.size() > 0);
	}

	@Test
	void testInsertUserWord() {
		store.insert(new UserWord(0, 0, "hello", new Timestamp(new Date().getTime())));
	}

	@Test
	void testInsertListOfUserWord() {

		List<UserWord> list = new ArrayList<UserWord>();
		for (int i = 0; i < 100; i++) {
			list.add(new UserWord(i, 0, "hello", new Timestamp(new Date().getTime())));
		}

		store.insert(list);
	}

}

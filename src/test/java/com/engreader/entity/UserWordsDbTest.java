package com.engreader.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.engreader.db.H2DB;
import com.engreader.db.UserWordsDB;
import com.engreader.entity.UserWord;

class UserWordsDbTest {
	H2DB h2db;
	UserWordsDB store;

	@BeforeEach
	void setup() throws ClassNotFoundException, SQLException {
		h2db = H2DB.connect();
		store = new UserWordsDB(h2db);
	}

	@AfterEach
	void tearDown() throws SQLException {
		h2db.close();
	}

	@Test
	void testUserWordsStore() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateTable() {
		store.createTable();
		store.createIndex();
	}

	@Test
	void testGetAll() {
		List<UserWord> list = store.getAll(0);
		assertTrue(list.size() > 1000);
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

	@Test
	void testRemoveInt() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveListOfInteger() {
		fail("Not yet implemented");
	}

}

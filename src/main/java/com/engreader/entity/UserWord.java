package com.engreader.entity;

import java.sql.Timestamp;

public class UserWord {
	int id;
	int userId;
	String word;
	Timestamp updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public UserWord(int id, int userId, String word, Timestamp updated) {
		super();
		this.id = id;
		this.userId = userId;
		this.word = word;
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "UserWord [id=" + id + ", userId=" + userId + ", word=" + word + ", updated=" + updated + "]";
	}

	public UserWord() {
		super();
	}

}

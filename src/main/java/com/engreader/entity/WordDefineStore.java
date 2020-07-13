package com.engreader.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.engreader.db.WordDefineDB;

public class WordDefineStore {
	WordDefineDB db;

	public WordDefineStore(WordDefineDB db) {
		super();
		this.db = db;
	}

	Map<String, WordDefine> cache = new HashMap<>();

	Collection<WordDefine> getAll() {
		if (cache.size() > 0) {
			return cache.values();
		}

		List<WordDefine> list = db.getAll();
		for (WordDefine wordDefine : list) {
			cache.put(wordDefine.word, wordDefine);
		}
		return cache.values();
	}

	WordDefine get(String word) {
		return cache.get(word);
	}

	void insert(WordDefine data) {
		boolean result = db.insert(data);
		if (result) {
			cache.put(data.word, data);
		}

	}
}

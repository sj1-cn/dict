package cn.sj1.dict;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sj1.dict.db.WordDefineDB;

public class WordDefineStore {
	WordDefineDB db;

	public WordDefineStore(WordDefineDB db) {
		super();
		this.db = db;
	}

	Map<String, WordDefine> cache = new HashMap<>();

	public Collection<WordDefine> getAll() {
		if (cache.size() > 0) {
			return cache.values();
		}

		List<WordDefine> list = db.getAll();
		for (WordDefine wordDefine : list) {
			cache.put(wordDefine.word, wordDefine);
		}
		return cache.values();
	}
	
	public Map<String, WordDefine> getWords() {
		if (cache.size() > 0) {
			return cache;
		}
		List<WordDefine> list = db.getAll();
		for (WordDefine wordDefine : list) {
			cache.put(wordDefine.word, wordDefine);
		}
		return cache;
	}

	public WordDefine get(String word) {
		return cache.get(word);
	}

	public void insert(WordDefine data) {
		boolean result = db.insert(data);
		if (result) {
			cache.put(data.word, data);
		}

	}
}

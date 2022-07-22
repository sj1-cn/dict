package cn.sj1.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sj1.dict.db.UserWordsDB;

public class UserWordsStore {
	UserWordsDB db;

	public UserWordsStore(UserWordsDB db) {
		super();
		this.db = db;
	}

	Map<Integer, List<UserWord>> cache = new HashMap<>();

	public List<UserWord> getAll(int userId) {
		if (cache.containsKey(userId)) {
			return cache.get(userId);
		}

		List<UserWord> list = db.getAll(userId);
		cache.put(userId, list);
		return list;
	}

	void insert(UserWord data) {
		boolean result = db.insert(data);
		if (result) {
			cache.get(data.getUserId()).add(data);
		}

	}

	public List<UserWord> remember(int userId, String[] words) {
		List<UserWord> cachedList = null;
		if (!cache.containsKey(userId)) {
			cachedList = this.getAll(userId);
		} else {
			cachedList = cache.get(userId);
		}

		List<UserWord> list = new ArrayList<UserWord>();
		for (String word : words) {
			boolean exist = false;
			for (UserWord userWord : cachedList) {
				if (userWord.getWord().equals(word)) {
					exist = true;
					break;
				}
			}
			if (!exist)
				list.add(new UserWord(userId, userId, word, new Timestamp(new Date().getTime())));
		}

		boolean result = db.insert(list);
		if (result) {
			cache.get(userId).addAll(list);
		}
		return list;
	}

	void insert(List<UserWord> list) {
		boolean result = db.insert(list);
		if (result) {
			cache.get(list.get(0).getUserId()).addAll(list);
		}

	}

	public List<UserWord> forget(int userId, String[] words) {
		List<UserWord> cachedList = null;
		if (!cache.containsKey(userId)) {
			cachedList = this.getAll(userId);
		} else {
			cachedList = cache.get(userId);
		}

		List<UserWord> list = new ArrayList<>();
		for (String word : words) {
			int i = 0;
			for (; i < cachedList.size(); i++) {
				if (cachedList.get(i).getWord().equals(word)) {
					list.add(cachedList.get(i));
					break;
				}
			}
		}
		if (list.size() > 0) {

			boolean result = db.remove(list);
			if (result) {
				for (UserWord userWord : list) {
					cachedList.remove(userWord);
				}
			}
		}
		return list;
	}

	void remove(int userId, String word) {
		List<UserWord> cachedList = cache.get(userId);
		UserWord data = null;
		int i = 0;
		for (; i < cachedList.size(); i++) {
			if (cachedList.get(i).getWord().equals(word)) {
				data = cachedList.get(i);
				break;
			}
		}
		if (data != null) {
			boolean result = db.remove(data.getId());
			if (result) {
				cachedList.remove(data);
			}
		}
	}
}

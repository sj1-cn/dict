package cn.sj1.aireader;

import java.util.HashMap;
import java.util.Map;

import cn.sj1.dict.WordDefine;

public class WordFrequency {
	String lema;
	int frequency;
	Map<String, Integer> sp = new HashMap<>();
	WordDefine wordDefine;
	
	public WordFrequency(String lema, String word) {
		this.lema = lema;
		sp.put(word, 1);
		frequency = 1;
	}

	void add(String word) {
		frequency++;
		if (sp.containsKey(word)) {
			sp.put(word, sp.get(word) + 1);
		} else {
			sp.put(word, 1);
		}
	}

	public String getLema() {
		return lema;
	}

	public int getFrequency() {
		return frequency;
	}

	public Map<String, Integer> getSp() {
		return sp;
	}

	@Override
	public String toString() {
		return lema + "," + frequency + ",\"" + sp.toString() + "\"";
	}

	public WordDefine getWordDefine() {
		return wordDefine;
	}

	public void setWordDefine(WordDefine wordDefine) {
		this.wordDefine = wordDefine;
	}

}
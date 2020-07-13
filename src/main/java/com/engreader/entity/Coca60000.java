package com.engreader.entity;

public class Coca60000 {
	int coca60kRank;	
	String cocaPos;	  
	String word;
	int cocaTotal;
	int cocaSpoken;
	int cocaFiction;
	int cocaMagazine;
	int cocaNewspaper;
	int cocaAcademic;
	
	public Coca60000(int coca60kRank, String cocaPos, String word, int cocaTotal, int cocaSpoken, int cocaFiction,
			int cocaMagazine, int cocaNewspaper, int cocaAcademic) {
		super();
		this.coca60kRank = coca60kRank;
		this.cocaPos = cocaPos;
		this.word = word;
		this.cocaTotal = cocaTotal;
		this.cocaSpoken = cocaSpoken;
		this.cocaFiction = cocaFiction;
		this.cocaMagazine = cocaMagazine;
		this.cocaNewspaper = cocaNewspaper;
		this.cocaAcademic = cocaAcademic;
	}

	public int getCoca60kRank() {
		return coca60kRank;
	}

	public void setCoca60kRank(int coca60kRank) {
		this.coca60kRank = coca60kRank;
	}

	public String getCocaPos() {
		return cocaPos;
	}

	public void setCocaPos(String cocaPos) {
		this.cocaPos = cocaPos;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCocaTotal() {
		return cocaTotal;
	}

	public void setCocaTotal(int cocaTotal) {
		this.cocaTotal = cocaTotal;
	}

	public int getCocaSpoken() {
		return cocaSpoken;
	}

	public void setCocaSpoken(int cocaSpoken) {
		this.cocaSpoken = cocaSpoken;
	}

	public int getCocaFiction() {
		return cocaFiction;
	}

	public void setCocaFiction(int cocaFiction) {
		this.cocaFiction = cocaFiction;
	}

	public int getCocaMagazine() {
		return cocaMagazine;
	}

	public void setCocaMagazine(int cocaMagazine) {
		this.cocaMagazine = cocaMagazine;
	}

	public int getCocaNewspaper() {
		return cocaNewspaper;
	}

	public void setCocaNewspaper(int cocaNewspaper) {
		this.cocaNewspaper = cocaNewspaper;
	}

	public int getCocaAcademic() {
		return cocaAcademic;
	}

	public void setCocaAcademic(int cocaAcademic) {
		this.cocaAcademic = cocaAcademic;
	}
	

}
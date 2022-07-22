package cn.sj1.dict.entity;

public class CocaWord {
	@Override
	public String toString() {
		return "CocaWord [level=" + level + ", rankFrequency=" + rankFrequency + ", lemma=" + lemma + ", pos=" + pos
				+ ", rawFrequency=" + rawFrequency + ", dispersion=" + dispersion + ", meaningTip=" + meaningTip
				+ ", meaning=" + meaning + "]";
	}

	int level;
	int rankFrequency;
	String lemma;
	String pos;
	int rawFrequency;
	int dispersion; // dispersion * 100
	String meaningTip;
	String meaning;

	public CocaWord(int rankFrequency, String lemma, String pos, int rawFrequency, int dispersion, String meaningTip,
			String meaning) {
		this.level = (int) ((rankFrequency - 1) / 1000) + 1;
		this.rankFrequency = rankFrequency;
		if(lemma.equals("in")) {
			System.out.println(this.level + " " + lemma);
		}
		this.lemma = lemma;
		this.pos = pos;
		this.rawFrequency = rawFrequency;
		this.dispersion = dispersion;
		this.meaningTip = meaningTip;
		this.meaning = meaning;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRankFrequency() {
		return rankFrequency;
	}

	public void setRankFrequency(int rankFrequency) {
		this.rankFrequency = rankFrequency;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public int getRawFrequency() {
		return rawFrequency;
	}

	public void setRawFrequency(int rawFrequency) {
		this.rawFrequency = rawFrequency;
	}

	public int getDispersion() {
		return dispersion;
	}

	public void setDispersion(int dispersion) {
		this.dispersion = dispersion;
	}

	public String getMeaningTip() {
		return meaningTip;
	}

	public void setMeaningTip(String meaningTip) {
		this.meaningTip = meaningTip;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

}
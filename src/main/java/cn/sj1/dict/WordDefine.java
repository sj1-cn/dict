package cn.sj1.dict;

import java.sql.Timestamp;

/**
 * 原形（Original Form） 第三人du称单数形式（Singular From in Third Personal）、 过去式（Past
 * Form）、 过去分词（Past Participle）、 现在分词（Present Participle）。
 * 
 * @author admin
 *
 */
public class WordDefine {
	int id;
	String word;
	String tense;
	String accentEn;
	String accentUs;
	String meanZh;
	String meanBriefZh;
	String meanEn;
	String pos;// Part of speech
	int freq;

	int cocaLevel;
	int cocaRankFrequency;
	int cocaRawFrequency;
	int cocaDispersion;

	int coca60kRank;
	int cocaTotal;
	int cocaSpoken;
	int cocaFiction;
	int cocaMagazine;
	public int getCoca60kRank() {
		return coca60kRank;
	}

	public void setCoca60kRank(int coca60kRank) {
		this.coca60kRank = coca60kRank;
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

	int cocaNewspaper;
	int cocaAcademic;

	Timestamp updated;

	public WordDefine() {
		// TODO Auto-generated constructor stub
	}

	public int getCocaLevel() {
		return cocaLevel;
	}

	public void setCocaLevel(int cocaLevel) {
		this.cocaLevel = cocaLevel;
	}

	public int getCocaRankFrequency() {
		return cocaRankFrequency;
	}

	public void setCocaRankFrequency(int cocaRankFrequency) {
		this.cocaRankFrequency = cocaRankFrequency;
	}

	public int getCocaRawFrequency() {
		return cocaRawFrequency;
	}

	public void setCocaRawFrequency(int cocaRawFrequency) {
		this.cocaRawFrequency = cocaRawFrequency;
	}

	public int getCocaDispersion() {
		return cocaDispersion;
	}

	public void setCocaDispersion(int cocaDispersion) {
		this.cocaDispersion = cocaDispersion;
	}

	public String getMeanBriefZh() {
		return meanBriefZh;
	}

	public void setMeanBriefZh(String meanBriefZh) {
		this.meanBriefZh = meanBriefZh;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getAccentEn() {
		return accentEn;
	}

	public void setAccentEn(String accentEn) {
		this.accentEn = accentEn;
	}

	public String getAccentUs() {
		return accentUs;
	}

	public void setAccentUs(String accentUs) {
		this.accentUs = accentUs;
	}

	public String getMeanZh() {
		return meanZh;
	}

	public void setMeanZh(String meanZh) {
		this.meanZh = meanZh;
	}

	public String getMeanEn() {
		return meanEn;
	}

	public void setMeanEn(String meanEn) {
		this.meanEn = meanEn;
	}

	public String getTense() {
		return tense;
	}

	public void setTense(String tense) {
		this.tense = tense;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public WordDefine(int id, String word, String tense, String accentEn, String accentUs, String meanZh,
			String meanBriefZh, String meanEn, String pos, int freq, Timestamp updated) {
		super();
		this.id = id;
		this.word = word;
		this.tense = tense;
		this.accentEn = accentEn;
		this.accentUs = accentUs;
		this.meanZh = meanZh;
		this.meanBriefZh = meanBriefZh;
		this.meanEn = meanEn;
		this.pos = pos;
		this.freq = freq;
		this.updated = updated;
	}

	public WordDefine(int id, String word, String tense, String accentEn, String accentUs, String meanZh,
			String meanBriefZh, String meanEn, String pos, int freq, int cocaLevel, int cocaRankFrequency,
			int cocaRawFrequency, int cocaDispersion, Timestamp updated) {
		super();
		this.id = id;
		this.word = word;
		this.tense = tense;
		this.accentEn = accentEn;
		this.accentUs = accentUs;
		this.meanZh = meanZh;
		this.meanBriefZh = meanBriefZh;
		this.meanEn = meanEn;
		this.pos = pos;
		this.freq = freq;
		this.cocaLevel = cocaLevel;
		this.cocaRankFrequency = cocaRankFrequency;
		this.cocaRawFrequency = cocaRawFrequency;
		this.cocaDispersion = cocaDispersion;
		this.updated = updated;
	}

	public WordDefine(int id, String word, String tense, String accentEn, String accentUs, String meanZh,
			String meanBriefZh, String meanEn, String pos, int freq, int cocaLevel, int cocaRankFrequency,
			int cocaRawFrequency, int cocaDispersion, int coca60kRank, int cocaTotal, int cocaSpoken, int cocaFiction,
			int cocaMagazine, int cocaNewspaper, int cocaAcademic, Timestamp updated) {
		super();
		this.id = id;
		this.word = word;
		this.tense = tense;
		this.accentEn = accentEn;
		this.accentUs = accentUs;
		this.meanZh = meanZh;
		this.meanBriefZh = meanBriefZh;
		this.meanEn = meanEn;
		this.pos = pos;
		this.freq = freq;
		this.cocaLevel = cocaLevel;
		this.cocaRankFrequency = cocaRankFrequency;
		this.cocaRawFrequency = cocaRawFrequency;
		this.cocaDispersion = cocaDispersion;
		this.coca60kRank = coca60kRank;
		this.cocaTotal = cocaTotal;
		this.cocaSpoken = cocaSpoken;
		this.cocaFiction = cocaFiction;
		this.cocaMagazine = cocaMagazine;
		this.cocaNewspaper = cocaNewspaper;
		this.cocaAcademic = cocaAcademic;
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "WordDefine [id=" + id + ", word=" + word + ", tense=" + tense + ", accentEn=" + accentEn + ", accentUs="
				+ accentUs + ", meanZh=" + meanZh + ", meanBriefZh=" + meanBriefZh + ", meanEn=" + meanEn + ", pos="
				+ pos + ", freq=" + freq + ", updated=" + updated + "]";
	}

	public static enum EE {
		OriginalForm, SingularFrominThirdPersonal, PastForm, PastParticiple, PresentParticiple, Singular, Plural
	}
}

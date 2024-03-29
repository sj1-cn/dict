package cn.sj1.aireader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.sj1.dict.db.H2DB;
import cn.sj1.dict.db.WordDefineDB;
import cn.sj1.dict.entity.CocaWord;
import cn.sj1.dict.store.WordDefineStore;

class StanfordNLPStemmerTest {
	static String dbFileName = "./db/dict.h2";
	StanfordNLPStemmer stemmer;
	H2DB h2db;
	HikariDataSource dataSource;

	@BeforeEach
	void setup() throws IOException, ClassNotFoundException, SQLException {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl("jdbc:h2:" + dbFileName);
		hikariConfig.setUsername("sa");
		hikariConfig.setPassword("123");

		dataSource = new HikariDataSource(hikariConfig);
		h2db = new H2DB(dataSource);
		WordDefineDB db = new WordDefineDB(h2db);
		try {
			db.removeAll();
			db.dropTable();
		} catch (Exception e) {
		}
		db.createTable();
		db.createIndex();
		WordDefineStore store = new WordDefineStore(db);
		stemmer = new StanfordNLPStemmer(store.getWords());
	}

	@AfterEach
	void teardown() {
		dataSource.close();
	}

	@Test
	void test() throws IOException, ClassNotFoundException, SQLException {
		stemmer.parseWords("I like you!");
	}

	@Test
	void tesssst() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("words20000.csv");
		CSVParser csvParser = CSVParser.parse(is, Charset.forName("utf-8"), CSVFormat.EXCEL);

		Map<String, CocaWord> cocaWords = new HashMap<>();

		List<CSVRecord> res = csvParser.getRecords();

		for (int i = 10 - 1; i >= 0; i--) {
			CSVRecord r = res.get(i);
			cocaWords.put(/* r.get(2) + "_" + */ r.get(1), new CocaWord(Integer.parseInt(r.get(0)), r.get(1), r.get(2),
					Integer.parseInt(r.get(4)), (int) (Float.parseFloat(r.get(5)) * 100), r.get(9), r.get(10)));
		}

		System.out.println(cocaWords);

	}

	@Test
	void test2() {
		String sample = "		The Story of Ms. Marvel\n"
				+ "		Until recently, Jersey City's Kamala Khan didn't think she was special.\n"
				+ "		But one night not too long ago, everything changed. Kamala was caught in a\n"
				+ "		mysterious mist, and when she recovered, she realized that her Inhuman\n"
				+ "		powers were suddenly activated. Kamala went from being an ordinary high\n"
				+ "		school student to being Ms. Marvel—a Super Hero with the power to stretch,\n"
				+ "		morph, and heal.\n";
		stemmer.parseWords(sample);
	}

	@Test
	void test3() {
		String sample = " Super Hero .\n";
		stemmer.parseWords(sample);
	}

	@Test
	void test4() {
		stemmer.parseWords("“Aren’t you going to write about the magic person?” said Annie.");
	}
}

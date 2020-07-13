package com.engreader.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.engreader.entity.WordDefine;

public class WordDefineDB {
	H2DB h2db;

	public WordDefineDB(H2DB h2db) {
		super();
		this.h2db = h2db;
	}

	public void dropTable() {
		try {
			Statement stmt = this.h2db.conn.createStatement();
			stmt.execute("DROP TABLE WORDDEFINE");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void createTable() {
		try {
			Statement stmt = this.h2db.conn.createStatement();
			stmt.execute(
					"CREATE TABLE WORDDEFINE(ID INTEGER,WORD VARCHAR,TENSE VARCHAR,ACCENT_EN VARCHAR,ACCENT_US VARCHAR,MEAN_ZH VARCHAR,MEAN_BRIEF_ZH VARCHAR,MEAN_EN VARCHAR,POS VARCHAR,FREQ INTEGER,"
					+ "COCALEVEL INTEGER,COCARANKFREQUENCY INTEGER,COCARAWFREQUENCY INTEGER,COCADISPERSION INTEGER,"
					+ "COCA60KRANK INTEGER,COCATOTAL INTEGER,COCASPOKEN INTEGER,COCAFICTION INTEGER,COCAMAGAZINE INTEGER,COCANEWSPAPER INTEGER,COCAACADEMIC INTEGER,"
					+ "UPDATED TIMESTAMP,CONSTRAINT pk_WORDDEFINE_ID PRIMARY KEY (ID))");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// CREATE INDEX index_name ON table_name (column_name)
	public void createIndex() {
		try {
			Statement stmt = this.h2db.conn.createStatement();
			stmt.execute("CREATE INDEX WORDDEFINE_WORD ON WORDDEFINE (WORD)");
			stmt.execute("CREATE INDEX WORDDEFINE_ID ON WORDDEFINE (ID)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<WordDefine> getAll() {

		List<WordDefine> list;
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement(
					"SELECT ID,WORD,TENSE,ACCENT_EN,ACCENT_US,MEAN_ZH,MEAN_BRIEF_ZH,MEAN_EN,POS,FREQ,"
					+ "COCALEVEL,COCARANKFREQUENCY,COCARAWFREQUENCY,COCADISPERSION,"
					+ "COCA60KRANK,COCATOTAL,COCASPOKEN,COCAFICTION,COCAMAGAZINE,COCANEWSPAPER,COCAACADEMIC,"
					+ "UPDATED FROM WORDDEFINE");

//			stmt.setInt(1, userid);
			ResultSet res = stmt.executeQuery();

			list = new ArrayList<>();

			while (res.next()) {
				int i = 1;
				int id = res.getInt(i++);
				String word = res.getString(i++);
				String tense = res.getString(i++);
				String accentEn = res.getString(i++);
				String accentUs = res.getString(i++);
				String meanZh = res.getString(i++);
				String meanBriefZh = res.getString(i++);
				String meanEn = res.getString(i++);
				String pos = res.getString(i++);
				int freq = res.getInt(i++);
				
				int cocoLevel = res.getInt(i++);
				int cocaRankFrequency = res.getInt(i++);
				int cocaRawFrequency = res.getInt(i++);
				int cocaDispersion = res.getInt(i++);

				int	coca60kRank = res.getInt(i++);
				int	cocaTotal = res.getInt(i++);
				int	cocaSpoken = res.getInt(i++);
				int	cocaFiction = res.getInt(i++);
				int	cocaMagazine = res.getInt(i++);
				int	cocaNewspaper = res.getInt(i++);
				int	cocaAcademic = res.getInt(i++);

				Timestamp updated = res.getTimestamp(i++);
				WordDefine userword = new WordDefine(id, word, tense, accentEn, accentUs, meanZh, meanBriefZh, meanEn,pos, freq, 
						cocoLevel, cocaRankFrequency, cocaRawFrequency, cocaDispersion, 
						coca60kRank,cocaTotal,cocaSpoken,cocaFiction,cocaMagazine,cocaNewspaper,cocaAcademic,
						updated);
				list.add(userword);
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return list;
	}
	

	public WordDefine get(int id) {

		WordDefine data = null;
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement(
					"SELECT ID,WORD,TENSE,ACCENT_EN,ACCENT_US,MEAN_ZH,MEAN_BRIEF_ZH,MEAN_EN,POS,FREQ,"
					+ "COCALEVEL,COCARANKFREQUENCY,COCARAWFREQUENCY,COCADISPERSION,"
					+ "COCA60KRANK,COCATOTAL,COCASPOKEN,COCAFICTION,COCAMAGAZINE,COCANEWSPAPER,COCAACADEMIC,"
					+ "UPDATED FROM WORDDEFINE WHERE ID=?");

			stmt.setInt(1, id);
			ResultSet res = stmt.executeQuery();

			while (res.next()) {
				int i = 1;
				res.getInt(i++);
				String word = res.getString(i++);
				String tense = res.getString(i++);
				String accentEn = res.getString(i++);
				String accentUs = res.getString(i++);
				String meanZh = res.getString(i++);
				String meanBriefZh = res.getString(i++);
				String meanEn = res.getString(i++);
				String pos = res.getString(i++);
				int freq = res.getInt(i++);
				
				int cocoLevel = res.getInt(i++);
				int cocaRankFrequency = res.getInt(i++);
				int cocaRawFrequency = res.getInt(i++);
				int cocaDispersion = res.getInt(i++);
				
				int	coca60kRank = res.getInt(i++);
				int	cocaTotal = res.getInt(i++);
				int	cocaSpoken = res.getInt(i++);
				int	cocaFiction = res.getInt(i++);
				int	cocaMagazine = res.getInt(i++);
				int	cocaNewspaper = res.getInt(i++);
				int	cocaAcademic = res.getInt(i++);


				Timestamp updated = res.getTimestamp(i++);
				data = new WordDefine(id, word, tense, accentEn, accentUs, meanZh, meanBriefZh, meanEn,	pos, freq, 
						cocoLevel, cocaRankFrequency, cocaRawFrequency, cocaDispersion,
						coca60kRank,cocaTotal,cocaSpoken,cocaFiction,cocaMagazine,cocaNewspaper,cocaAcademic,
						updated);
//				list.add(userword);
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return data;
	}

	public boolean insert(WordDefine data) {
		PreparedStatement stmt;
		boolean result;
		try {
			stmt = h2db.conn.prepareStatement(
					"INSERT INTO WORDDEFINE(ID,WORD,TENSE,ACCENT_EN,ACCENT_US,MEAN_ZH,MEAN_BRIEF_ZH,MEAN_EN,POS,FREQ,"
					+ "COCALEVEL,COCARANKFREQUENCY,COCARAWFREQUENCY,COCADISPERSION,"
					+ "COCA60KRANK,COCATOTAL,COCASPOKEN,COCAFICTION,COCAMAGAZINE,COCANEWSPAPER,COCAACADEMIC,"
					+ "UPDATED) VALUES(?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,"
					+ "?,?,?,?,?,?,?,"
					+ "?)");
			int i = 1;
			stmt.setInt(i++, data.getId());
			stmt.setString(i++, data.getWord());
			stmt.setString(i++, data.getTense());
			stmt.setString(i++, data.getAccentEn());
			stmt.setString(i++, data.getAccentUs());
			stmt.setString(i++, data.getMeanZh());
			stmt.setString(i++, data.getMeanBriefZh());
			stmt.setString(i++, data.getMeanEn());
			stmt.setString(i++, data.getPos());
			stmt.setInt(i++, data.getFreq());
			
			stmt.setInt(i++, data.getCocaLevel());
			stmt.setInt(i++, data.getCocaRankFrequency());
			stmt.setInt(i++, data.getCocaRawFrequency());
			stmt.setInt(i++, data.getCocaDispersion());

			stmt.setInt(i++, data.getCoca60kRank());
			stmt.setInt(i++, data.getCocaTotal());
			stmt.setInt(i++, data.getCocaSpoken());
			stmt.setInt(i++, data.getCocaFiction());
			stmt.setInt(i++, data.getCocaMagazine());
			stmt.setInt(i++, data.getCocaNewspaper());
			stmt.setInt(i++, data.getCocaAcademic());


			stmt.setTimestamp(i++, new Timestamp(new Date().getTime()));
			result = stmt.execute();
			this.h2db.conn.commit();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public boolean insert(List<WordDefine> list) {
//		boolean result;
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement(
					"INSERT INTO WORDDEFINE(ID,WORD,TENSE,ACCENT_EN,ACCENT_US,MEAN_ZH,MEAN_BRIEF_ZH,MEAN_EN,POS,FREQ,"
					+ "COCALEVEL,COCARANKFREQUENCY,COCARAWFREQUENCY,COCADISPERSION,"
					+ "COCA60KRANK,COCATOTAL,COCASPOKEN,COCAFICTION,COCAMAGAZINE,COCANEWSPAPER,COCAACADEMIC,"
					+ "UPDATED) VALUES(?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,"
					+ "?,?,?,?,?,?,?,"
					+ "?)");

			for (WordDefine data : list) {
				int i = 1;
				stmt.setInt(i++, data.getId());
				stmt.setString(i++, data.getWord());
				stmt.setString(i++, data.getTense());
				stmt.setString(i++, data.getAccentEn());
				stmt.setString(i++, data.getAccentUs());
				stmt.setString(i++, data.getMeanZh());
				stmt.setString(i++, data.getMeanBriefZh());
				stmt.setString(i++, data.getMeanEn());
				stmt.setString(i++, data.getPos());
				stmt.setInt(i++, data.getFreq());
				
				stmt.setInt(i++, data.getCocaLevel());
				stmt.setInt(i++, data.getCocaRankFrequency());
				stmt.setInt(i++, data.getCocaRawFrequency());
				stmt.setInt(i++, data.getCocaDispersion());
				

				stmt.setInt(i++, data.getCoca60kRank());
				stmt.setInt(i++, data.getCocaTotal());
				stmt.setInt(i++, data.getCocaSpoken());
				stmt.setInt(i++, data.getCocaFiction());
				stmt.setInt(i++, data.getCocaMagazine());
				stmt.setInt(i++, data.getCocaNewspaper());
				stmt.setInt(i++, data.getCocaAcademic());


				stmt.setTimestamp(i++, new Timestamp(new Date().getTime()));
				stmt.addBatch();
			}
			stmt.executeBatch();
			this.h2db.conn.commit();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return true;
	}

	public boolean remove(int id) {
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement("DELETE FROM WORDDEFINE WHERE ID=?");
			stmt.setInt(1, id);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return true;
	}

	public boolean remove(List<WordDefine> userwords) {
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement("DELETE FROM WORDDEFINE WHERE ID=?");
			for (WordDefine w : userwords) {
				stmt.setInt(1, w.getId());
				stmt.addBatch();
			}
			stmt.executeBatch();
			stmt.close();
			return true;
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public boolean removeAll() {
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement("DELETE FROM WORDDEFINE ");
			boolean result = stmt.execute();
			stmt.close();
			return result;
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public boolean update(List<WordDefine> list) {
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement(
					"UPDATE WORDDEFINE SET TENSE=?,ACCENT_EN=?,ACCENT_US=?,MEAN_ZH=?,MEAN_BRIEF_ZH=?,MEAN_EN=?,POS=?,FREQ=?,"
					+ "COCALEVEL=?,COCARANKFREQUENCY=?,COCARAWFREQUENCY=?,COCADISPERSION=?,"
					+ "COCA60KRANK=?,COCATOTAL=?,COCASPOKEN=?,COCAFICTION=?,COCAMAGAZINE=?,COCANEWSPAPER=?,COCAACADEMIC=?,"
					+ "UPDATED=? WHERE ID=?");

			for (WordDefine data : list) {
				int i = 1;
				stmt.setString(i++, data.getTense());
				stmt.setString(i++, data.getAccentEn());
				stmt.setString(i++, data.getAccentUs());
				stmt.setString(i++, data.getMeanZh());
				stmt.setString(i++, data.getMeanBriefZh());
				stmt.setString(i++, data.getMeanEn());
				stmt.setString(i++, data.getPos());
				stmt.setInt(i++, data.getFreq());
				
				stmt.setInt(i++, data.getCocaLevel());
				stmt.setInt(i++, data.getCocaRankFrequency());
				stmt.setInt(i++, data.getCocaRawFrequency());
				stmt.setInt(i++, data.getCocaDispersion());

				stmt.setInt(i++, data.getCoca60kRank());
				stmt.setInt(i++, data.getCocaTotal());
				stmt.setInt(i++, data.getCocaSpoken());
				stmt.setInt(i++, data.getCocaFiction());
				stmt.setInt(i++, data.getCocaMagazine());
				stmt.setInt(i++, data.getCocaNewspaper());
				stmt.setInt(i++, data.getCocaAcademic());
				
				stmt.setTimestamp(i++, new Timestamp(new Date().getTime()));
				stmt.setInt(i++, data.getId());
				stmt.addBatch();
			}
			stmt.executeBatch();
			this.h2db.conn.commit();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return true;
		
	}

	public boolean update(WordDefine data) {
		try {
			PreparedStatement stmt = h2db.conn.prepareStatement(
					"UPDATE WORDDEFINE SET TENSE=?,ACCENT_EN=?,ACCENT_US=?,MEAN_ZH=?,MEAN_BRIEF_ZH=?,MEAN_EN=?,POS=?,FREQ=?,"
					+ "COCALEVEL=?,COCARANKFREQUENCY=?,COCARAWFREQUENCY=?,COCADISPERSION=?,"
					+ "COCA60KRANK=?,COCATOTAL=?,COCASPOKEN=?,COCAFICTION=?,COCAMAGAZINE=?,COCANEWSPAPER=?,COCAACADEMIC=?,"
					+ "UPDATED=? WHERE ID=?");
			
				int i = 1;
				stmt.setString(i++, data.getTense());
				stmt.setString(i++, data.getAccentEn());
				stmt.setString(i++, data.getAccentUs());
				stmt.setString(i++, data.getMeanZh());
				stmt.setString(i++, data.getMeanBriefZh());
				stmt.setString(i++, data.getMeanEn());
				stmt.setString(i++, data.getPos());
				stmt.setInt(i++, data.getFreq());
				
				stmt.setInt(i++, data.getCocaLevel());
				stmt.setInt(i++, data.getCocaRankFrequency());
				stmt.setInt(i++, data.getCocaRawFrequency());
				stmt.setInt(i++, data.getCocaDispersion());
				
				stmt.setInt(i++, data.getCoca60kRank());
				stmt.setInt(i++, data.getCocaTotal());
				stmt.setInt(i++, data.getCocaSpoken());
				stmt.setInt(i++, data.getCocaFiction());
				stmt.setInt(i++, data.getCocaMagazine());
				stmt.setInt(i++, data.getCocaNewspaper());
				stmt.setInt(i++, data.getCocaAcademic());
				

				stmt.setTimestamp(i++, new Timestamp(new Date().getTime()));
				stmt.setInt(i++, data.getId());
			stmt.execute();
			this.h2db.conn.commit();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return true;
		
	}

}

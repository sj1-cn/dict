package cn.sj1.aireader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sj1.dict.WordDefine;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordNLPStemmer {
	Logger log = LoggerFactory.getLogger(getClass());

	public StanfordNLPStemmer(Map<String, WordDefine> cocaWords) {
		super();
		this.cocaWords = cocaWords;
	}

	Map<String, WordDefine> cocaWords;
//
//	public ArrayList<WordFrequency> parseWordFrequency(String srcText) {
//		Properties props = new Properties(); // set up pipeline properties
//		props.put("annotators", "tokenize, ssplit, pos, lemma");// , ner, truecase, parse, dcoref"); // 分词、分句、词性标注和次元信息。
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		Map<String, WordFrequency> wordFrequencies = new HashMap<>();
//
//		Annotation document = new Annotation(srcText);
//		pipeline.annotate(document);
//		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//		for (CoreMap sentence : sentences) {
//			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//				String word = token.get(CoreAnnotations.TextAnnotation.class); // 获取单词信息
//				String lema = token.get(CoreAnnotations.LemmaAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
//				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
//
//				String lemakey = lema.toLowerCase();
//				if ('a' <= lemakey.charAt(0) && lemakey.charAt(0) <= 'z') {
//					if (wordFrequencies.containsKey(lemakey)) {
//						wordFrequencies.get(lemakey).add(word);
//					} else {
//						wordFrequencies.put(lemakey, new WordFrequency(lemakey, word));
//					}
//					// System.out.print("[" + word + "/" + lema + " [" + pos + "] " +
//					// token.toString() + "]");
//
//				} else {
//					// System.out.println(pos + " " + word);
//				}
//			}
//			// System.out.println(".");
//		}
//
//		ArrayList<WordFrequency> awe1 = new ArrayList<>();
//		awe1.addAll(wordFrequencies.values());
//		awe1.sort((l, r) -> -(l.frequency - r.frequency));
//
//		ArrayList<WordFrequency> awe = awe1;
//		return awe;
//	}

	public StringBuffer parseWords(String srcText) {
		Properties props = new Properties(); // set up pipeline properties
		props.put("annotators", "tokenize, ssplit, pos, lemma");// , ner, truecase, parse, dcoref"); // 分词、分句、词性标注和次元信息。
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Map<String, WordFrequency> wordFrequencies = new HashMap<>();

		StringBuffer sb = new StringBuffer();

		Map<String, List<String>> posList = new HashMap<>();

		Annotation document = new Annotation(srcText);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		int offset = 0;
		for (CoreMap sentence : sentences) {
			int sentenceOffsetBegin = sentence.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
			int sentenceOffsetEnd = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词

			if (sentenceOffsetBegin > offset) {
				sb.append(srcText.substring(offset, sentenceOffsetBegin)/* .replaceAll("\n", "<br/>") */);
			}
			offset = sentenceOffsetBegin;

			sb.append("<span class=\"sentence\">");
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(CoreAnnotations.TextAnnotation.class); // 获取单词信息
				String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
				int wordOffsetBegin = token.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
				int wordOffsetEnd = token.get(CoreAnnotations.CharacterOffsetEndAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词

				if (wordOffsetBegin > offset) {
					sb.append(srcText.substring(offset, wordOffsetBegin).replaceAll("\n", "</span><span class=\"sentence\">"));
				}

				if (!posList.containsKey(pos)) {
					posList.put(pos, new ArrayList<>());
				}

				posList.get(pos).add(word);

				boolean comma = false;
				WordDefine wordDefine = null;
				WordDefine lemmaDefine = null;

				if (cocaWords.containsKey(word)) {
					wordDefine = cocaWords.get(word);
				}

				if (cocaWords.containsKey(lemma)) {
					lemmaDefine = cocaWords.get(lemma);
				} else {
					String lemakey = lemma.toLowerCase();
					if (cocaWords.containsKey(lemakey)) {
						lemmaDefine = cocaWords.get(lemakey);
					} else if (!('a' <= lemakey.charAt(0) && lemakey.charAt(0) <= 'z')) {// 非字母开始
//						sb.append(word);
						comma = true;
					} else if (!('a' <= lemakey.charAt(lemakey.length() - 1)
							&& lemakey.charAt(lemakey.length() - 1) <= 'z')) {// 非字母结束
								String lemaAbbr = lemma.substring(0, lemma.length() - 1);
								if (cocaWords.containsKey(lemaAbbr)) {
									lemmaDefine = cocaWords.get(lemaAbbr);
								}
							}
				}

				// 后期再考虑下述情况
				if (wordDefine != null && lemmaDefine == null) {
					log.debug("ERROR :{}  {}", word, lemma);
				}

				if (lemmaDefine != null) {
					log.debug("{} {} {} {} {} {}", word, lemma, pos, lemmaDefine.getCocaLevel(),
							lemmaDefine.getCocaRankFrequency(), lemmaDefine.getCocaRawFrequency());
					if (lemmaDefine.getCocaLevel() > 5) {
//						sb.append("<span class=\"w\">");
						sb.append("<ruby class='w lo'>");
						sb.append(word);
						sb.append("<rt class=\"lv").append(lemmaDefine.getCocaLevel()).append(">");
						sb.append(lemmaDefine.getMeanBriefZh());
						sb.append("</rt>");
						sb.append("<span class=\"tooltiptext\">");
						sb.append(lemmaDefine.getMeanZh());
						sb.append("</span>");
						sb.append("</ruby>");
//						sb.append("</span>");
					} else if (lemmaDefine.getCocaLevel() > 3) {
//						sb.append("<span class=\"w\">");
						sb.append("<ruby class='w lo l" + lemmaDefine.getCocaLevel() + "'>");
						sb.append(word);
						sb.append("<rt class=\"").append(lemmaDefine.getCocaLevel()).append(">");
						sb.append(lemmaDefine.getMeanBriefZh());
						sb.append("</rt>");
						sb.append("<span class=\"tooltiptext\">");
						sb.append(lemmaDefine.getMeanZh());
						sb.append("</span>");
						sb.append("</ruby>");
//						sb.append("</span>");
					} else if (lemmaDefine.getCocaLevel() > 1) {
//						sb.append("<span class=\"w\">");
						sb.append("<ruby class='w ll l" + lemmaDefine.getCocaLevel() + "'>");
						sb.append(word);
						sb.append("<rt>");
						sb.append(lemmaDefine.getCocaLevel() + " " + lemmaDefine.getMeanBriefZh());
						sb.append("</rt>");
						sb.append("<span class=\"tooltiptext\">");
						sb.append(lemmaDefine.getMeanZh());
						sb.append("</span>");
						sb.append("</ruby>");
//						sb.append("</span>");
					} else if (lemmaDefine.getCocaLevel() > 1) {
						sb.append("<ruby class='w l" + lemmaDefine.getCocaLevel() + "'>");
						sb.append(word);
//						sb.append("<rt>");
						sb.append("<span class=\"tooltiptext\">");
						sb.append(lemmaDefine.getMeanZh());
						sb.append("</span>");
//						sb.append(cocaWord.level);
//						sb.append("</rt>");
						sb.append("</ruby>");
					} else {
						sb.append(word);
					}
				} else if (comma) {
					sb.append(word);
				} else if ("n't".equals(lemma) || "n’t".equals(lemma)) {
					sb.append(word);
				} else {
					sb.append("<ruby class='levelUnknown'>");
					sb.append(word);
					sb.append("</ruby>");
				}

				offset = wordOffsetEnd;
			}
			// System.out.println(".");
			sb.append("</span>");
		}
		log.debug("{}", sb);

//		for (String	pos : posList.keySet()) {
//			log.debug("{} > {}",pos,posList.get(pos));
//			
//		}

		return sb;
	}

	static class WordFrequency {
		String lema;
		int frequency;
		Map<String, Integer> sp = new HashMap<>();

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

		@Override
		public String toString() {
			return lema + "," + frequency + ",\"" + sp.toString() + "\"";
		}
	}
}
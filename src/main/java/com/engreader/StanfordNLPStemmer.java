package com.engreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.engreader.entity.WordDefine;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * 代码功能：词性还原、词干提取 jar包下载地址：https://stanfordnlp.github.io/CoreNLP/
 * 工具包API地址：https://stanfordnlp.github.io/CoreNLP/api.html
 * 
 * ROOT：要处理文本的语句 IP：简单从句 NP：名词短语 VP：动词短语 PU：断句符，通常是句号、问号、感叹号等标点符号 LCP：方位词短语
 * PP：介词短语 CP：由‘的’构成的表示修饰性关系的短语 DNP：由‘的’构成的表示所属关系的短语 ADVP：副词短语 ADJP：形容词短语
 * DP：限定词短语 QP：量词短语 NN：常用名词 NR：固有名词 NT：时间名词 PN：代词 VV：动词 VC：是 CC：表示连词 VE：有
 * VA：表语形容词 AS：内容标记（如：了） VRD：动补复合词 CD: 表示基数词 DT: determiner 表示限定词 EX:
 * existential there 存在句 FW: foreign word 外来词 IN: preposition or conjunction,
 * subordinating 介词或从属连词 JJ: adjective or numeral, ordinal 形容词或序数词 JJR:
 * adjective, comparative 形容词比较级 JJS: adjective, superlative 形容词最高级 LS: list
 * item marker 列表标识 MD: modal auxiliary 情态助动词 PDT: pre-determiner 前位限定词 POS:
 * genitive marker 所有格标记 PRP: pronoun, personal 人称代词 RB: adverb 副词 RBR: adverb,
 * comparative 副词比较级 RBS: adverb, superlative 副词最高级 RP: particle 小品词 SYM: symbol
 * 符号 TO:”to” as preposition or infinitive marker 作为介词或不定式标记 WDT: WH-determiner
 * WH限定词 WP: WH-pronoun WH代词 WP$: WH-pronoun, possessive WH所有格代词 WRB:Wh-adverb
 * WH副词
 * 
 * 关系表示 abbrev: abbreviation modifier，缩写 acomp: adjectival complement，形容词的补充；
 * advcl : adverbial clause modifier，状语从句修饰词 advmod: adverbial modifier状语 agent:
 * agent，代理，一般有by的时候会出现这个 amod: adjectival modifier形容词 appos: appositional
 * modifier,同位词 attr: attributive，属性 aux: auxiliary，非主要动词和助词，如BE,HAVE
 * SHOULD/COULD等到 auxpass: passive auxiliary 被动词 cc: coordination，并列关系，一般取第一个词
 * ccomp: clausal complement从句补充 complm: complementizer，引导从句的词好重聚中的主要动词 conj :
 * conjunct，连接两个并列的词。 cop: copula。系动词（如be,seem,appear等），（命题主词与谓词间的）连系 csubj :
 * clausal subject，从主关系 csubjpass: clausal passive subject 主从被动关系 dep:
 * dependent依赖关系 det: determiner决定词，如冠词等 dobj : direct object直接宾语 expl:
 * expletive，主要是抓取there infmod: infinitival modifier，动词不定式 iobj : indirect
 * object，非直接宾语，也就是所以的间接宾语； mark: marker，主要出现在有“that” or “whether”“because”,
 * “when”, mwe: multi-word expression，多个词的表示 neg: negation modifier否定词 nn: noun
 * compound modifier名词组合形式 npadvmod: noun phrase as adverbial modifier名词作状语
 * nsubj : nominal subject，名词主语 nsubjpass: passive nominal subject，被动的名词主语 num:
 * numeric modifier，数值修饰 number: element of compound number，组合数字 parataxis:
 * parataxis: parataxis，并列关系 partmod: participial modifier动词形式的修饰 pcomp:
 * prepositional complement，介词补充 pobj : object of a preposition，介词的宾语 poss:
 * possession modifier，所有形式，所有格，所属 possessive: possessive
 * modifier，这个表示所有者和那个’S的关系 preconj : preconjunct，常常是出现在 “either”, “both”,
 * “neither”的情况下 predet: predeterminer，前缀决定，常常是表示所有 prep: prepositional modifier
 * prepc: prepositional clausal modifier prt: phrasal verb particle，动词短语 punct:
 * punctuation，这个很少见，但是保留下来了，结果当中不会出现这个 purpcl : purpose clause modifier，目的从句
 * quantmod: quantifier phrase modifier，数量短语 rcmod: relative clause modifier相关关系
 * ref : referent，指示物，指代 rel : relative root: root，最重要的词，从它开始，根节点 tmod: temporal
 * modifier xcomp: open clausal complement xsubj : controlling subject 掌控者
 * 中心语为谓词 subj — 主语 nsubj — 名词性主语（nominal subject） （同步，建设） top — 主题（topic）
 * （是，建筑） npsubj — 被动型主语（nominal passive subject），专指由“被”引导的被动句中的主语，一般是谓词语义上的受事
 * （称作，镍） csubj — 从句主语（clausal subject），中文不存在 xsubj — x主语，一般是一个主语下面含多个从句 （完善，有些）
 * 中心语为谓词或介词 obj — 宾语 dobj — 直接宾语 （颁布，文件） iobj — 间接宾语（indirect object），基本不存在
 * range — 间接宾语为数量词，又称为与格 （成交，元） pobj — 介词宾语 （根据，要求） lobj — 时间介词 （来，近年） 中心语为谓词
 * comp — 补语 ccomp — 从句补语，一般由两个动词构成，中心语引导后一个动词所在的从句(IP) （出现，纳入） xcomp —
 * x从句补语（xclausal complement），不存在 acomp — 形容词补语（adjectival complement） tcomp —
 * 时间补语（temporal complement） （遇到，以前） lccomp — 位置补语（localizer complement） （占，以上）
 * — 结果补语（resultative complement） 中心语为名词 mod — 修饰语（modifier） pass —
 * 被动修饰（passive） tmod — 时间修饰（temporal modifier） rcmod — 关系从句修饰（relative clause
 * modifier） （问题，遇到） numod — 数量修饰（numeric modifier） （规定，若干） ornmod —
 * 序数修饰（numeric modifier） clf — 类别修饰（classifier modifier） （文件，件） nmod —
 * 复合名词修饰（noun compound modifier） （浦东，上海） amod — 形容词修饰（adjetive modifier） （情况，新）
 * advmod — 副词修饰（adverbial modifier） （做到，基本） vmod — 动词修饰（verb
 * modifier，participle modifier） prnmod — 插入词修饰（parenthetical modifier） neg —
 * 不定修饰（negative modifier） (遇到，不) det — 限定词修饰（determiner modifier） （活动，这些） possm
 * — 所属标记（possessive marker），NP poss — 所属修饰（possessive modifier），NP dvpm —
 * DVP标记（dvp marker），DVP （简单，的） dvpmod — DVP修饰（dvp modifier），DVP （采取，简单） assm —
 * 关联标记（associative marker），DNP （开发，的） assmod — 关联修饰（associative modifier），NP|QP
 * （教训，特区） prep — 介词修饰（prepositional modifier） NP|VP|IP（采取，对） clmod —
 * 从句修饰（clause modifier） （因为，开始） plmod — 介词性地点修饰（prepositional localizer
 * modifier） （在，上） asp — 时态标词（aspect marker） （做到，了） partmod– 分词修饰（participial
 * modifier） 不存在 etc — 等关系（etc） （办法，等） 中心语为实词 conj — 联合(conjunct) cop —
 * 系动(copula) 双指助动词？？？？ cc — 连接(coordination)，指中心词与连词 （开发，与） 其它 attr — 属性关系
 * （是，工程） cordmod– 并列联合动词（coordinated verb compound） （颁布，实行） mmod — 情态动词（modal
 * verb） （得到，能） ba — 把字关系 tclaus — 时间从句 （以后，积累） — semantic dependent cpm —
 * 补语化成分（complementizer），一般指“的”引导的CP （振兴，的） ————————————————
 * 版权声明：本文为CSDN博主「Vincenho_」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/l919898756/article/details/81670228
 */
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

	/**
	 * CC Coordinating conjunction 连接词
	 * 
	 * CD Cardinal number 基数词
	 * 
	 * DT Determiner 限定词（如this,that,these,those,such，不定限定词：no,s
	 * ome,any,each,every,enough,either,neither,all,both,half,several,many,much,(a)
	 * few,(a) little,other,another. 4. EX Existential there 存在句
	 * 
	 * FW Foreign word 外来词
	 * 
	 * IN Preposition or subordinating conjunction 介词或从属连词
	 * 
	 * JJ Adjective 形容词或序数词
	 * 
	 * JJR Adjective, comparative 形容词比较级
	 * 
	 * JJS Adjective, superlative 形容词最高级
	 * 
	 * LS List item marker 列表标示
	 * 
	 * MD Modal 情态助动词
	 * 
	 * NN Noun, singular or mass 常用名词 单数形式
	 * 
	 * NNS Noun, plural 常用名词 复数形式
	 * 
	 * NNP Proper noun, singular 专有名词，单数形式
	 * 
	 * NNPS Proper noun, plural 专有名词，复数形式
	 * 
	 * PDT Predeterminer 前位限定词
	 * 
	 * POS Possessive ending 所有格结束词
	 * 
	 * PRP Personal pronoun 人称代词
	 * 
	 * PRP$ Possessive pronoun 所有格代名词
	 * 
	 * RB Adverb 副词
	 * 
	 * RBR Adverb, comparative 副词比较级
	 * 
	 * RBS Adverb, superlative 副词最高级
	 * 
	 * RP Particle 小品词
	 * 
	 * SYM Symbol 符号
	 * 
	 * TO to 作为介词或不定式格式
	 * 
	 * UH Interjection 感叹词
	 * 
	 * VB Verb, base form 动词基本形式
	 * 
	 * VBD Verb, past tense 动词过去式
	 * 
	 * VBG Verb, gerund or present participle 动名词和现在分词
	 * 
	 * VBN Verb, past participle 过去分词
	 * 
	 * VBP Verb, non-3rd person singular present 动词非第三人称单数
	 * 
	 * VBZ Verb, 3rd person singular present 动词第三人称单数
	 * 
	 * WDT Wh-determiner 限定词（如关系限定词：whose,which.疑问限定词： what,which,whose.）
	 * 
	 * WPWh-pronoun 代词（who whose which）
	 * 
	 * WP$ Possessive wh-pronoun 所有格代词
	 * 
	 * WRB Wh-adverb疑问代词（how where when） ————————————————
	 * 版权声明：本文为CSDN博主「ROOOOOOM」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
	 * 原文链接：https://wangzeling.blog.csdn.net/article/details/106314579
	 * 
	 * @param srcText
	 * @return
	 */
	public StringBuffer parseWords(String srcText) {
		Properties props = new Properties(); // set up pipeline properties
		props.put("annotators", "tokenize, ssplit, pos, lemma");// , ner, truecase, parse, dcoref"); // 分词、分句、词性标注和次元信息。
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Map<String, WordFrequency> wordFrequencies = new HashMap<>();

		StringBuffer sb = new StringBuffer();
		
		Map<String,List<String>> posList = new HashMap<>();

		Annotation document = new Annotation(srcText);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		int offset = 0;
		for (CoreMap sentence : sentences) {
			int sentenceOffsetBegin = sentence.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
			int sentenceOffsetEnd = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class); // 获取对应上面word的词元信息，即我所需要的词形还原后的单词

			if (sentenceOffsetBegin > offset) {
				sb.append(srcText.substring(offset, sentenceOffsetBegin)/*.replaceAll("\n", "<br/>")*/);
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
					sb.append(srcText.substring(offset, wordOffsetBegin).replaceAll("\n", "<br/>"));
				}

				if(!posList.containsKey(pos)) {
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
						sb.append("<rt>");
						sb.append(lemmaDefine.getCocaLevel() + " " + lemmaDefine.getMeanBriefZh());
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
						sb.append("<rt>");
						sb.append(lemmaDefine.getCocaLevel() + " " + lemmaDefine.getMeanBriefZh());
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
package cn.sj1.aireader;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

/**
 * 代码功能：词性还原、词干提取
 * jar包下载地址：https://stanfordnlp.github.io/CoreNLP/
 * 工具包API地址：https://stanfordnlp.github.io/CoreNLP/api.html
 */
public class StemmerTest {

    public static void main(String[] args){
        Properties props = new Properties();  // set up pipeline properties
        props.put("annotators", "tokenize, ssplit, pos, lemma");   //分词、分句、词性标注和次元信息。
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String txtWords = "Franklin said, If a man empties his purse into his head,no man can take it away from him,an investment in knowledge always pays the best interest.";  // 待处理文本
        Annotation document = new Annotation(txtWords);
        pipeline.annotate(document);
        List<CoreMap> words = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap word_temp: words) {
            for (CoreLabel token: word_temp.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);   // 获取单词信息
                String lema = token.get(CoreAnnotations.LemmaAnnotation.class);  // 获取对应上面word的词元信息，即我所需要的词形还原后的单词
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);  // 获取对应上面word的词元信息，即我所需要的词形还原后的单词

                
                System.out.println(word + " " + lema + "  " + pos);
            }
        }
        
        pipeline.prettyPrint(document, System.out);
    }
}
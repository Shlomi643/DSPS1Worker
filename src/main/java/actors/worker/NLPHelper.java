package actors.worker;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

//import edu.stanford.nlp.rnn.RNNCoreAnnotations;

public class NLPHelper {

    private StanfordCoreNLP sentNLP;
    private StanfordCoreNLP entNLP;

    public NLPHelper() {
        Properties sentProps = new Properties();
        Properties entProps = new Properties();
        sentProps.put("annotators", "tokenize, ssplit, parse, sentiment");
        entProps.put("annotators", "tokenize , ssplit, pos, lemma, ner");
        this.sentNLP = new StanfordCoreNLP(sentProps);
        this.entNLP = new StanfordCoreNLP(entProps);
    }

    public String findSentiment(String review) {
        int mainSentiment = 0;
        if (review != null && review.length() > 0) {
            int longest = 0;
            Annotation annotation = sentNLP.process(review);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        return "" + mainSentiment;
    }

    public String extractEntities(String review) {
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(review);
        // run all Annotators on this text
        this.entNLP.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        Set<String> ret = new HashSet<>();
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);
                if (!ne.equals("O"))
                    ret.add(ne);
            }
        }
        return ret.toString();

    }

}

package com.basicTweetsClassification;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordLemmatizer {

	// protected StanfordCoreNLP pipeline;
	public static StanfordCoreNLP pipeline;

	public StanfordLemmatizer() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		// dcoref,net,ner,parse
		pipeline = new StanfordCoreNLP(props);
	}

	public static List<CoreMap> stanfordLammetizer(String text) {

		// read some text in the text variable
		/*
		 * String text = "the #fox jumped over the lazy dog.Good girl." + "\n" +
		 * "jpojojo";
		 */

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		// System.out.println("Annonating:" + document);
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types

		// this is the parse tree of the current sentence
		// Tree tree = sentence.get(TreeAnnotation.class);

		// this is the Stanford dependency graph of the current sentence
		// SemanticGraph dependencies = sentence
		// .get(CollapsedCCProcessedDependenciesAnnotation.class);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		return sentences;
		/*
		 * try { pipeline.xmlPrint(document, System.out); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}

	public static void main(String[] args) {
		StanfordLemmatizer sL = new StanfordLemmatizer();
		long startingtTime = System.currentTimeMillis();
		String tweetText = "RT @DjBlack_Pearl: wat muhfuckaz running for party?????";
		List<CoreMap> sentences = sL.stanfordLammetizer(tweetText);
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				// String word = token.get(TextAnnotation.class);
				// System.out.println("word:" + word + ":lemma:" +
				// token.lemma());
				String lemma = token.lemma();
				String posTag = token.get(PartOfSpeechAnnotation.class);
				System.out.println("Lemma:" + lemma + ":posTag:" + posTag);
			}
		}
		long endingTime = System.currentTimeMillis();
		System.out.println("Total Time:" + (endingTime - startingtTime) / 1000);
	}
}
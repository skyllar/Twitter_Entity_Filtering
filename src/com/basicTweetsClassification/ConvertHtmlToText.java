package com.basicTweetsClassification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.basicTweetsClassification.TweetsParsing.EntityContents;
import com.basicTweetsClassification.TweetsParsing.TweetContents;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

public class ConvertHtmlToText {
	HashMap<String, HashMap<Long, TweetContents>> databaseMap = GlobalVariables.databaseMap;

	HashMap<String, EntityContents> entityInfo = GlobalVariables.entityInfo;

	StanfordLemmatizer sL = new StanfordLemmatizer();

	public HashSet<String> stopWords = new HashSet<String>(Arrays.asList("a",
			"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "able",
			"about", "across", "after", "all", "almost", "also", "am", "among",
			"an", "and", "any", "are", "as", "at", "be", "because", "been",
			"but", "by", "can", "cannot", "could", "dear", "did", "do", "does",
			"either", "else", "ever", "every", "for", "from", "get", "got",
			"had", "has", "have", "he", "her", "hers", "him", "his", "how",
			"however", "if", "in", "into", "is", "it", "its", "just", "least",
			"let", "like", "likely", "may", "me", "might", "most", "must",
			"my", "neither", "no", "nor", "not", "of", "off", "often", "on",
			"only", "or", "other", "our", "own", "rather", "said", "say",
			"says", "she", "should", "since", "so", "some", "than", "that",
			"the", "their", "them", "then", "there", "these", "they", "this",
			"tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
			"what", "when", "where", "which", "while", "who", "whom", "why",
			"will", "with", "would", "yet", "you", "your", "null", "aa",
			"blank", "ca", "caption"));

	public HashSet<String> requiredPosTags = GlobalVariables.requiredPosTags;

	public HashSet<String> requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage = GlobalVariables.requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage;

	private String punctuationFormattingForHtmlPage(String text) {

		text = text.replaceAll("[^a-z. ]", "");
		// text = text.replaceAll("\n", ".");
		text = text.replaceAll("\\s+", " ");
		// text = text.replaceAll("\\.+", ".");
		text = text.trim();

		// text = text.replaceAll("[^a-z. \n]", "");
		// System.out.println("--" + text + "---");
		// text = text.replaceAll("\n", ".");
		// System.out.println("--" + text + "---");
		//
		// text = text.replaceAll("\\s+", " ");
		// System.out.println("--" + text + "---");
		// text = text.replaceAll("\\.+", ".");
		// System.out.println("--" + text + "---");
		return text;
	}

	private String preProcessHtmlText(String htmlText) {

		// tweetText = "#abcd $234adsad%34        dssda ads";
		// convert to lowerCase
		// System.out.println("text:" + tweetText);
		if (!htmlText.equals("")) {
			htmlText = htmlText.toLowerCase();

			// punctuation removal except hashtag #
			htmlText = punctuationFormattingForHtmlPage(htmlText);
			if (!htmlText.equals("")) {
				htmlText = htmlText.trim();
			}
		}
		// System.out.println("after punctuationFormatting:" + tweetText);
		return htmlText;
	}

	private void convertHtmlToText(File readFilePointer, File writeFilePointer,
			File lemmatisedWriteFilePointer) {
		try {

			BufferedWriter bW = new BufferedWriter(new FileWriter(
					writeFilePointer));
			String bodyText = "";
			String title = "";
			Document doc = Jsoup.parse(readFilePointer, "UTF-8", "");
			if (doc.body() != null) {
				bodyText = doc.body().text();
			}
			if (doc.body() != null) {
				title = doc.title();
			}
			title = preProcessHtmlText(title);
			bodyText = preProcessHtmlText(bodyText);
			bW.write(title + "\n----------\n" + bodyText);
			bW.close();

			if (lemmatisedWriteFilePointer != null) {
				System.out.println("-----CallingHtmlText-------");
				// System.out.println(title + ":" + bodyText);
				dumpLemmatisedHtmlText(title, bodyText,
						lemmatisedWriteFilePointer);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void dumpLemmatisedHtmlText(String title1, String bodyText1,
			File writeFilePointer) {
		try {
			BufferedWriter bW = new BufferedWriter(new FileWriter(
					writeFilePointer));
			// System.out
			// .println("-------IN dumpLemmatisedHtmlText--------------");
			// System.out.println(title1 + ":" + bodyText1);
			String lemma;
			String posTag;
			String[] breakSentence = title1.split("\\.");
			for (String title : breakSentence) {
				// System.out.print("Token:");
				// System.out.println(":" + title);
				if (!title.equals("")) {
					List<CoreMap> sentences = sL.stanfordLammetizer(title);
					for (CoreMap sentence : sentences) {
						// traversing the words in the current sentence
						// a CoreLabel is a CoreMap with additional
						// token-specific
						// methods
						for (CoreLabel token : sentence
								.get(TokensAnnotation.class)) {
							// this is the text of the token
							// String word = token.get(TextAnnotation.class);
							// System.out.println("word:" + word + ":lemma:" +
							// token.lemma());
							lemma = token.lemma();
							posTag = token.get(PartOfSpeechAnnotation.class);
							// System.out.println("Writing ---------------->");
							// System.out.println("token:" + lemma + ":posTag:"
							// + posTag);
							// System.out.println("Writng:--->" + lemma + ":"
							// + posTag + "\n");
							bW.write(lemma + ":" + posTag + "\n");

						}
					}
				}
			}
			bW.write("--------------\n");
			breakSentence = bodyText1.split("\\.");
			for (String bodyText : breakSentence) {
				// System.out.print("Token:");
				// System.out.println(":" + bodyText);
				if (!bodyText.equals("")) {
					List<CoreMap> sentences = sL.stanfordLammetizer(bodyText);
					for (CoreMap sentence : sentences) {
						// traversing the words in the current sentence
						// a CoreLabel is a CoreMap with additional
						// token-specific
						// methods
						for (CoreLabel token : sentence
								.get(TokensAnnotation.class)) {
							// this is the text of the token
							// String word = token.get(TextAnnotation.class);
							// System.out.println("word:" + word + ":lemma:" +
							// token.lemma());
							lemma = token.lemma();
							posTag = token.get(PartOfSpeechAnnotation.class);
							// System.out.println("Writing ---------------->");
							// System.out.println("token:" + lemma + ":posTag:"
							// + posTag);
							// System.out.println("Writng:--->" + lemma + ":"
							// + posTag + "\n");
							bW.write(lemma + ":" + posTag + "\n");

						}
					}
				}
			}

			bW.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void convertEntitiesHtmlPageToText() {
		try {
			File file1, file2;
			Iterator<Entry<String, EntityContents>> entries = entityInfo
					.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String entityId = (String) entry.getKey();
				EntityContents entityContent = (EntityContents) entry
						.getValue();

				String pathForEntitiesWikiPage = GlobalVariables.pathForEntitiesHtmlPage
						+ "/" + entityContent.md5_wikipedia_en;
				String pathForEntitiesHomePage = GlobalVariables.pathForEntitiesHtmlPage
						+ "/" + entityContent.md5_homepage;

				String pathForModifiedEntityId = GlobalVariables.pathForModifiedEntitiesHtmlPage
						+ "/" + entityId;

				new File(pathForModifiedEntityId).mkdir();
				// System.out.println("Created Folder:" +
				// pathForModifiedEntityId);
				String pathForEntitiesWikiText = pathForModifiedEntityId + "/"
						+ entityContent.md5_wikipedia_en;
				String pathForEntitiesHomeText = pathForModifiedEntityId + "/"
						+ entityContent.md5_homepage;

				if (entityContent.md5_homepage != "") {
					new File(pathForEntitiesHomeText).mkdir();
					// System.out.println("Created Folder:"
					// + pathForEntitiesHomeText);
					file1 = new File(pathForEntitiesHomePage);
					for (File file : file1.listFiles()) {
						file2 = new File(pathForEntitiesHomeText + "/"
								+ file.getName());
						file2.createNewFile();
						// System.out.println("Creating File:"
						// + pathForEntitiesHomeText + "/"
						// + file.getName());
						String lemmatisedHtmlFileName = pathForEntitiesHomeText
								+ "/" + file.getName() + "-lemmatised";
						File file3 = new File(lemmatisedHtmlFileName);
						file3.createNewFile();
						convertHtmlToText(file, file2, file3);

					}
				}
				if (entityContent.md5_wikipedia_en != "") {
					new File(pathForEntitiesWikiText).mkdir();
					// System.out.println("Created Folder:"
					// + pathForEntitiesWikiText);
					file1 = new File(pathForEntitiesWikiPage);
					for (File file : file1.listFiles()) {
						file2 = new File(pathForEntitiesWikiText + "/"
								+ file.getName());
						file2.createNewFile();
						// System.out.println("Creating File:"
						// + pathForEntitiesWikiText + "/"
						// + file.getName());
						String lemmatisedHtmlFileName = pathForEntitiesWikiText
								+ "/" + file.getName() + "-lemmatised";
						File file3 = new File(lemmatisedHtmlFileName);
						file3.createNewFile();
						convertHtmlToText(file, file2, file3);
					}
				}
				// break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convertTweetsExternalHtmlPageToText() {
		try {
			java.util.Iterator<Entry<String, HashMap<Long, TweetContents>>> entries = databaseMap
					.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String entityId = (String) entry.getKey();
				HashMap<Long, TweetContents> innerMap = (HashMap<Long, TweetContents>) entry
						.getValue();
				// System.out.println("--------->Entity Id = " + entityId);

				String pathForModifiedEntityId = GlobalVariables.pathForModifiedDownloadedExternalLinks
						+ "/" + entityId;
				new File(pathForModifiedEntityId).mkdir();
				// System.out.println("Creating Folder:" +
				// pathForModifiedEntityId);
				java.util.Iterator<Entry<Long, TweetContents>> innerEntries = innerMap
						.entrySet().iterator();
				while (innerEntries.hasNext()) {
					// System.out.println("--------------");
					Map.Entry entryInner = (Map.Entry) innerEntries.next();
					Long tweetId = (Long) entryInner.getKey();
					TweetContents tC = (TweetContents) entryInner.getValue();

					for (int i = 0; i < tC.md5_extended_urls.length; i++) {

						String md5_extended_urls = tC.md5_extended_urls[i];

						if (!md5_extended_urls.equals("")) {

							String pathForExtendedUrl = GlobalVariables.pathForDownloadedExternalLinks
									+ "/" + entityId + "/" + md5_extended_urls;

							File file1 = new File(pathForExtendedUrl);

							if (file1.exists()) {

								// System.out.println("md5_extended_urls--->"
								// + md5_extended_urls);
								String pathForModifiedExtendedUrl = pathForModifiedEntityId
										+ "/" + md5_extended_urls;

								new File(pathForModifiedExtendedUrl).mkdir();
								// System.out.println("Creating Folder---:"
								// + pathForModifiedExtendedUrl);

								for (File file : file1.listFiles()) {
									File file2 = new File(
											pathForModifiedExtendedUrl + "/"
													+ file.getName());
									try {
										file2.createNewFile();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// System.out.println("Creating File--:"
									// + pathForModifiedExtendedUrl + "/"
									// + file.getName());
									String lemmatisedHtmlFileName = pathForModifiedExtendedUrl
											+ "/"
											+ file.getName()
											+ "-lemmatised";
									File file3 = new File(
											lemmatisedHtmlFileName);
									file3.createNewFile();
									convertHtmlToText(file, file2, file3);

								}
							}
						}
					}

					// break;
				}

				// break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

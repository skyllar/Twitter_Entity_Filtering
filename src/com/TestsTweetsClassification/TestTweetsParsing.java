package com.TestsTweetsClassification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.basicTweetsClassification.StanfordLemmatizer;
import com.basicTweetsClassification.TweetsParsing.EntityContents;
import com.basicTweetsClassification.TweetsParsing.LemmaInfo;
import com.basicTweetsClassification.TweetsParsing.TweetContents;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

public class TestTweetsParsing {

	public static HashMap<String, EntityContents> entityInfo = GlobalVariablesForTestTweets.entityInfo;

	public static HashMap<String, HashMap<Long, TweetContents>> databaseMap = GlobalVariablesForTestTweets.databaseMap;

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

	public HashSet<String> requiredPosTags = GlobalVariablesForTestTweets.requiredPosTags;

	public HashSet<String> requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage = GlobalVariablesForTestTweets.requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage;

	static int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}

	public void printDataBaseMap() {
		java.util.Iterator<Entry<String, HashMap<Long, TweetContents>>> entries = databaseMap
				.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			HashMap<Long, TweetContents> innerMap = (HashMap<Long, TweetContents>) entry
					.getValue();
			System.out.println("--------->Entity Id = " + key);

			java.util.Iterator<Entry<Long, TweetContents>> innerEntries = innerMap
					.entrySet().iterator();
			while (innerEntries.hasNext()) {
				// System.out.println("--------------");
				Map.Entry entryInner = (Map.Entry) innerEntries.next();
				Long keyInner = (Long) entryInner.getKey();
				TweetContents tC = (TweetContents) entryInner.getValue();
				System.out.println("TweetId:" + keyInner + ":" + tC.extendedURL
						+ ":author:" + tC.author + ":text:" + tC.tweetText
						+ ":related:" + tC.related
						+ ":numberOfTokensExcluding:"
						+ tC.numberOfTokensExcludingStopWords + ":Feature1:"
						+ tC.feature1 + ":Feature2:" + tC.feature2);
				System.out.println("lammetised tweet:");
				for (int i = 0; i < tC.lammetisedText.size(); i++) {
					System.out.println(tC.lammetisedText.get(i).lemma
							+ ":PosTag:" + tC.lammetisedText.get(i).posTag);
				}
				// break;
			}
			// break;
		}
	}

	private void printUnRelatedHashTagIndex(EntityContents entityContent) {
		System.out
				.println("-------------------------->UnRelated hash Tag Index:");
		Iterator<Entry<String, Integer>> hashTagEntries = entityContent.unRelatedHashTagIndex
				.entrySet().iterator();

		while (hashTagEntries.hasNext()) {
			System.out.println("--------------");
			Map.Entry hashTagEntry = (Map.Entry) hashTagEntries.next();
			String hashTag = (String) hashTagEntry.getKey();

			Integer count = (Integer) hashTagEntry.getValue();
			System.out.println("hashtag:" + hashTag + ":count:" + count);
		}

	}

	private void printRelatedHashTagIndex(EntityContents entityContent) {
		System.out
				.println("-------------------------->Related hash Tag Index:");
		Iterator<Entry<String, Integer>> hashTagEntries = entityContent.relatedHashTagIndex
				.entrySet().iterator();

		while (hashTagEntries.hasNext()) {
			System.out.println("--------------");
			Map.Entry hashTagEntry = (Map.Entry) hashTagEntries.next();
			String hashTag = (String) hashTagEntry.getKey();

			Integer count = (Integer) hashTagEntry.getValue();
			System.out.println("hashtag:" + hashTag + ":count:" + count);
		}

	}

	private void printUnRelatedInvertedIndex(EntityContents entityContent) {
		System.out
				.println("------------------------>UnRelated Inverted index :");
		Iterator<Entry<String, HashMap<Long, Byte>>> invertedIndexEntries = entityContent.unRelatedInvertedIndex
				.entrySet().iterator();

		while (invertedIndexEntries.hasNext()) {
			// System.out.println("--------------");
			Map.Entry invertedIndexEntry = (Map.Entry) invertedIndexEntries
					.next();
			String lemma = (String) invertedIndexEntry.getKey();
			System.out.println("lemma:" + lemma);
			HashMap<Long, Byte> innerHash = (HashMap<Long, Byte>) invertedIndexEntry
					.getValue();
			Iterator<Entry<Long, Byte>> innerEntries = innerHash.entrySet()
					.iterator();
			while (innerEntries.hasNext()) {
				Map.Entry innerEntry = (Map.Entry) innerEntries.next();
				Long tweetId = (Long) innerEntry.getKey();
				Byte b = (Byte) innerEntry.getValue();
				System.out
						.println("Tweet Id = " + tweetId + ":Byte Count:" + b);
			}
		}

	}

	private void printRelatedInvertedIndex(EntityContents entityContent) {
		System.out.println("------------------------>Related Inverted index :");
		Iterator<Entry<String, HashMap<Long, Byte>>> invertedIndexEntries = entityContent.relatedInvertedIndex
				.entrySet().iterator();

		while (invertedIndexEntries.hasNext()) {
			// System.out.println("--------------");
			Map.Entry invertedIndexEntry = (Map.Entry) invertedIndexEntries
					.next();
			String lemma = (String) invertedIndexEntry.getKey();
			System.out.println("lemma:" + lemma);
			HashMap<Long, Byte> innerHash = (HashMap<Long, Byte>) invertedIndexEntry
					.getValue();
			Iterator<Entry<Long, Byte>> innerEntries = innerHash.entrySet()
					.iterator();
			while (innerEntries.hasNext()) {
				Map.Entry innerEntry = (Map.Entry) innerEntries.next();
				Long tweetId = (Long) innerEntry.getKey();
				Byte b = (Byte) innerEntry.getValue();
				System.out
						.println("Tweet Id = " + tweetId + ":Byte Count:" + b);
			}
		}

	}

	public void printEntityMap() {
		Iterator<Entry<String, EntityContents>> entries = entityInfo.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String entityId = (String) entry.getKey();
			EntityContents entityContent = (EntityContents) entry.getValue();
			System.out.println("Entity Id = " + entityId + "\n related:"
					+ entityContent.relatedTweets + ":unrelated:"
					+ entityContent.unRelatedTweets);

			printRelatedInvertedIndex(entityContent);
			printUnRelatedInvertedIndex(entityContent);
			printRelatedHashTagIndex(entityContent);
			printUnRelatedHashTagIndex(entityContent);

			// break;
		}
	}

	private void printTop10ListOfList(List<LemmaInfo> top10List) {

		for (LemmaInfo lI : top10List) {
			System.out.println("Lemma:" + lI.lemma + ":Count:" + lI.count);
		}
	}

	private String punctuationFormatting(String text) {
		text = text.replaceAll("[^a-z# ]", "");
		return text;
	}

	private void lammetiseTweetText(String tweetText, TweetContents tC,
			BufferedWriter dumpInto) {
		// System.out.println("---------------------");
		String lemma;
		String posTag;
		LemmaInfo lemmaInfo;

		List<CoreMap> sentences = sL.stanfordLammetizer(tweetText);
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				// String word = token.get(TextAnnotation.class);
				// System.out.println("word:" + word + ":lemma:" +
				// token.lemma());

				lemma = token.lemma();
				posTag = token.get(PartOfSpeechAnnotation.class);

				if (dumpInto != null) {
					try {
						dumpInto.write(lemma + ":" + posTag + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (lemma.charAt(0) == '#') {

					lemmaInfo = new LemmaInfo();
					lemmaInfo.posTag = posTag;
					lemmaInfo.lemma = lemma;
					tC.lammetisedText.add(lemmaInfo);
					// hash tag mapping
					lemma = lemma.substring(1);

				} else {
					lemmaInfo = new LemmaInfo();
					lemmaInfo.posTag = posTag;
					lemmaInfo.lemma = lemma;
					tC.lammetisedText.add(lemmaInfo);
					if (requiredPosTags.contains(posTag))
						tC.numberOfTokensExcludingStopWords++;

				}
			}
			// System.out.println("---------------------");
		}
	}

	public void fetchTweetsModifiedLemmatisedExternalHtmlPage() {
		java.util.Iterator<Entry<String, HashMap<Long, TweetContents>>> entries = databaseMap
				.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String entityId = (String) entry.getKey();
			HashMap<Long, TweetContents> innerMap = (HashMap<Long, TweetContents>) entry
					.getValue();
			// System.out.println("--------->Entity Id = " + entityId);

			String pathForModifiedEntityId = GlobalVariablesForTestTweets.pathForModifiedDownloadedExternalLinks
					+ "/" + entityId;
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

						String pathForModifiedExtendedUrl = pathForModifiedEntityId
								+ "/" + md5_extended_urls;

						File file1 = new File(pathForModifiedExtendedUrl);

						if (file1.exists()) {
							if (file1.getName().contains("-lemmatised")) {
								// do something

								populateLammetisedVectorOfTweet(file1, tC);
							}
						}
					}
				}

				// break;
			}

			// break;
		}
	}

	private void populateLammetisedVectorOfTweet(File file1, TweetContents tC) {
		try {
			BufferedReader bR = new BufferedReader(new FileReader(file1));

			String line;
			// System.out.println("Title--->");
			while ((line = bR.readLine()) != null) {
				if (!line.contains("---")) {

					// System.out.println(line);
					String token[] = line.split(":");
					LemmaInfo lI = new LemmaInfo();
					lI.lemma = token[0];
					lI.posTag = token[1];
					tC.lammetisedText.add(lI);

				} else
					break;
			}
			// System.out.println("Body--->");
			while ((line = bR.readLine()) != null) {

				// System.out.println(line);
				LemmaInfo lI = new LemmaInfo();
				String token[] = line.split(":");
				lI.lemma = token[0];
				lI.posTag = token[1];
				tC.lammetisedText.add(lI);
			}
			bR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addToCommonInvertedIndex(String lemma, String posTag,
			HashMap<String, Integer> commonInvertedIndex) {

		if (requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage
				.contains(posTag)) {
			if (commonInvertedIndex.containsKey(lemma)) {
				Integer count = commonInvertedIndex.get(lemma) + 1;
				commonInvertedIndex.put(lemma, count);
			} else {
				commonInvertedIndex.put(lemma, 1);
			}
		}
	}

	private void processTweetText(TweetContents tC, BufferedWriter dumpInto) {

		String tweetText = tC.tweetText;
		// tweetText = "#abcd $234adsad%34        dssda ads";
		// convert to lowerCase
		// System.out.println("text:" + tweetText);
		if (!tweetText.equals("")) {
			tweetText = tweetText.toLowerCase();

			// punctuation removal except hashtag #
			tweetText = punctuationFormatting(tweetText);
			// System.out.println("after punctuationFormatting:" +
			// tweetText);

			if (!tweetText.equals("")) {
				// tokenisation+lammetisation+stop word removal
				lammetiseTweetText(tweetText, tC, dumpInto);
			}
		}

	}

	private void getTweetTextFromFile(TweetContents tC, BufferedReader bR) {
		String line, lemma, posTag;
		LemmaInfo lemmaInfo;
		try {
			while ((line = bR.readLine()) != null) {
				String token[] = line.split(":");
				if (token.length == 2) {
					lemma = token[0];
					posTag = token[1];
					if (lemma.charAt(0) == '#') {

						lemmaInfo = new LemmaInfo();
						lemmaInfo.posTag = posTag;
						lemmaInfo.lemma = lemma;
						tC.lammetisedText.add(lemmaInfo);
						// hash tag mapping
						lemma = lemma.substring(1);

					} else {
						lemmaInfo = new LemmaInfo();
						lemmaInfo.posTag = posTag;
						lemmaInfo.lemma = lemma;
						tC.lammetisedText.add(lemmaInfo);
						if (requiredPosTags.contains(posTag))
							tC.numberOfTokensExcludingStopWords++;

					}
				}
				// System.out.println("-------");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void fillProcessedTweetText(TweetContents tC, Long tweetId) {
		try {
			String tweetFilePath = GlobalVariablesForTestTweets.processedTestTweetsTextFolderPath
					+ "/" + tweetId;
			File openTweetFile = null;
			if (GlobalVariablesForTestTweets.tweetFilePointers
					.containsKey(tweetId)) {
				openTweetFile = GlobalVariablesForTestTweets.tweetFilePointers
						.get(tweetId);
			}
			if (openTweetFile != null) {

				BufferedReader bR = new BufferedReader(new FileReader(
						openTweetFile));
				getTweetTextFromFile(tC, bR);
				bR.close();
			} else {
				// System.out.println("###########????????????????????????????");
				openTweetFile = new File(tweetFilePath);
				openTweetFile.createNewFile();
				BufferedWriter bW = new BufferedWriter(new FileWriter(
						openTweetFile));
				processTweetText(tC, bW);
				bW.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void parseTweets() {
		System.out
				.println("***************In Function parseTweets()***********");

		int i = 0;
		File dir;
		String line;
		String str[];
		Long tweetId;
		int row = 0;

		BufferedReader[] readTweetInfoPath = new BufferedReader[GlobalVariablesForTestTweets.totalFiles];
		BufferedReader[] readTweetModified = new BufferedReader[GlobalVariablesForTestTweets.totalFiles];
		BufferedReader[] readLabelled = new BufferedReader[GlobalVariablesForTestTweets.totalFiles];

		try {

			dir = new File(GlobalVariablesForTestTweets.pathForTweetInfoPath);
			// System.out.println(GlobalVariables.pathForTweetInfoPath + ":");
			for (File child : dir.listFiles()) {
				readTweetInfoPath[i++] = new BufferedReader(new FileReader(
						child));
				// System.out.println(child.getName());
			}

			i = 0;
			dir = new File(GlobalVariablesForTestTweets.pathForTweetModified);
			// System.out.println(GlobalVariables.pathForTweetModified + ":");
			for (File child : dir.listFiles()) {
				readTweetModified[i++] = new BufferedReader(new FileReader(
						child));
				// System.out.println(child.getName());
			}

			// i = 0;
			// dir = new File(GlobalVariablesForTestTweets.pathForLabelled);
			// System.out.println(GlobalVariables.pathForLabelled + ":");
			// for (File child : dir.listFiles()) {
			// readLabelled[i++] = new BufferedReader(new FileReader(child));
			// // System.out.println(child.getName());
			// }

			GlobalVariablesForTestTweets.totalFiles = i;
			for (i = 0; i < GlobalVariablesForTestTweets.totalFiles; i++) {

				readTweetInfoPath[i].readLine();
				while ((line = readTweetInfoPath[i].readLine()) != null) {
					str = line.split("\"\\s+\"");
					// System.out.println(line + "$$");
					// 0:tweetId,2:entityId,4:Langugae,7:ExtendedURL
					/*
					 * if( str.length != 10){ row++; }
					 */
					if (!str.equals("") && str.length == 10
							&& str[4].trim().compareToIgnoreCase("EN") == 0) {
						if (str[0] != "")
							str[0] = str[0].replaceFirst("\"", "").trim();
						if (str[2] != "")
							str[2] = str[2].trim();
						if (str[7] != "")
							str[7] = str[7].trim();
						if (str[8] != "")
							str[8] = str[8].trim();

						// System.out
						// .println("******md5_extended_urls :" + str[8]);

						if (!databaseMap.containsKey(str[2])) {

							// EntityContents eC = new EntityContents();
							// entityInfo.put(str[2], eC);

							TweetContents tC = new TweetContents();
							tC.extendedURL = str[7];
							tC.md5_extended_urls = str[8].split(",");
							HashMap<Long, TweetContents> map = new HashMap<Long, TweetContents>();
							tweetId = Long.parseLong(str[0]);
							// System.out.println("^^^" + tweetId);
							map.put(tweetId, tC);
							databaseMap.put(str[2], map);
						} else {
							TweetContents tC = new TweetContents();
							tC.extendedURL = str[7];
							tC.md5_extended_urls = str[8].split(",");
							tweetId = Long.parseLong(str[0]);
							// System.out.println("^^^" + tweetId);
							databaseMap.get(str[2]).put(tweetId, tC);
						}
					}
				}
				// System.out.println("rows:"+row);
				// row=0;
			}
			// printDataBaseMap();
			for (i = 0; i < GlobalVariablesForTestTweets.totalFiles; i++) {

				readTweetModified[i].readLine();

				while ((line = readTweetModified[i].readLine()) != null) {

					str = line.split("\"\\s+\"");
					// System.out.println(line + "$$");
					// 0:tweetId,1:author,2:entityId,3:text
					if (!str.equals("") && str.length == 4) {
						if (str[0] != "")
							str[0] = str[0].replaceFirst("\"", "").trim();
						if (str[1] != "")
							str[1] = str[1].trim();
						if (str[2] != "")
							str[2] = str[2].trim();
						if (str[3] != "") {
							str[3] = str[3].trim();
							str[3] = str[3].substring(0, str[3].length() - 1);
						}

						if (databaseMap.containsKey(str[2])) {
							// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
							tweetId = Long.parseLong(str[0]);
							// System.out.println("tweetId:"+tweetId);
							if (databaseMap.get(str[2]).containsKey(tweetId)) {
								TweetContents tC = databaseMap.get(str[2]).get(
										tweetId);
								tC.author = str[1];
								// System.out.println("----------------------------:"
								// + str[1]);
								tC.tweetText = str[3];
								// processTweetText(tC,null);
								fillProcessedTweetText(tC, tweetId);
							}
						}

					}
				}
			}

			// System.out.println("------Above Reading Labelled------------");

			// printDataBaseMap();

			// for (i = 0; i < GlobalVariablesForTestTweets.totalFiles; i++) {
			String labelledFile = GlobalVariablesForTestTweets.pathForLabelled;
			File readLabel = new File(labelledFile);
			BufferedReader readerLabelled = new BufferedReader(new FileReader(
					readLabel));
			readerLabelled.readLine();
			// System.out.println("------Reading Labelled------------");
			while ((line = readerLabelled.readLine()) != null) {
				// System.out.println("line:" + line);

				str = line.split("\"\\s+\"");
				// System.out.println(line + "$$");
				// 0:tweetId,1:entityId,2:related/unrelated
				if (!str.equals("") && str.length >= 3) {
					// System.out.println("$$$$$$$$$$$$$");
					if (str[0] != "")
						str[0] = str[0].replaceFirst("\"", "").trim();
					if (str[1] != "")
						str[1] = str[1].trim();
					if (str[2] != "") {
						str[2] = str[2].trim();
						str[2] = str[2].substring(0, str[2].length() - 1);
						str[2] = str[2].trim();
					}
					// System.out.println("Entity:" + str[0]);
					if (databaseMap.containsKey(str[0])) {
						// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
						tweetId = Long.parseLong(str[1]);
						// System.out.println("tweetId:"+tweetId);
						if (databaseMap.get(str[0]).containsKey(tweetId)) {
							TweetContents tC = databaseMap.get(str[0]).get(
									tweetId);
							if (str[2].compareToIgnoreCase("related") == 0) {
								tC.related = true;
								// System.out.println("true");
							} else {
								// System.out.println("false--->");
								tC.related = false;
							}
							// /To Do............
						}
					}
				}
			}

			// printDataBaseMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

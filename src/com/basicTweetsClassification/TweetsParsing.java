package com.basicTweetsClassification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

public class TweetsParsing {

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

	public static class EntityContents {

		public String entityName = "";
		public String homePageURL = "";
		public String homePageTitle = "";

		public String md5_homepage = "";
		public String md5_wikipedia_en = "";

		public String wikiPageUrl = "";
		public String wikiTitle = "";

		public String category = "";
		public String queryWord = "";

		public List<LemmaInfo> topNLemmas = new LinkedList<LemmaInfo>();

		public Vector<LemmaInfo> lammetisedWikiTitleText = new Vector<LemmaInfo>();
		public Vector<LemmaInfo> lammetisedHomeTitleText = new Vector<LemmaInfo>();

		public Vector<LemmaInfo> lammetisedWikiBodyText = new Vector<LemmaInfo>();
		public Vector<LemmaInfo> lammetisedHomeBodyText = new Vector<LemmaInfo>();

		public HashMap<String, Integer> commonInvertedIndex = new HashMap<String, Integer>();

		public HashMap<String, Integer> invertedWikiIndex = new HashMap<String, Integer>();
		public HashMap<String, Integer> invertedHomePageIndex = new HashMap<String, Integer>();

		// HashMap<Token,HashMap<TweetId,Tf>>
		public HashMap<String, HashMap<Long, Byte>> relatedInvertedIndex = new HashMap<String, HashMap<Long, Byte>>();
		public HashMap<String, HashMap<Long, Byte>> unRelatedInvertedIndex = new HashMap<String, HashMap<Long, Byte>>();
		public HashMap<String, Integer> relatedHashTagIndex = new HashMap<String, Integer>();
		public HashMap<String, Integer> unRelatedHashTagIndex = new HashMap<String, Integer>();
		public Integer relatedTweets = 0;
		public Integer unRelatedTweets = 0;

	}

	public static class LemmaInfo {
		public String lemma = "";
		public String posTag = "";
		public Integer count = 0;
	}

	public static class TweetContents {
		public boolean related = false;
		public String tweetText;
		public long tweetId;
		public String extendedURL;
		public String author;
		public double feature1 = 0.0;
		public double feature2 = 0.0;
		public double feature3 = 0.0;
		public double feature4 = 0.0;
		public String[] md5_extended_urls;
		public Byte numberOfTokensExcludingStopWords = 0;
		// contain every token of tweet after lammetisation & punctuation
		// formatting
		public Vector<LemmaInfo> lammetisedText = new Vector<LemmaInfo>();
	}

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

	public void calculateWeightForEntities() {
		Iterator<Entry<String, EntityContents>> entries = entityInfo.entrySet()
				.iterator();
		int totalTweets = 0;
		try {
			File file = new File(GlobalVariables.pathForWeightOfEntities);
			file.createNewFile();
			BufferedWriter bW = new BufferedWriter(new FileWriter(file));
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String entityId = (String) entry.getKey();
				EntityContents entityContent = (EntityContents) entry
						.getValue();
				// System.out.println("Entity Id = " + entityId + "\n related:"
				// + entityContent.relatedTweets + ":unrelated:"
				// + entityContent.unRelatedTweets);
				bW.write(entityId + ":" + entityContent.relatedTweets + ":"
						+ entityContent.unRelatedTweets + "\n");
				totalTweets += entityContent.relatedTweets
						+ entityContent.unRelatedTweets;
			}
			System.out.println("Total Tweeets:" + totalTweets);
			bW.close();
		} catch (IOException e) {
			e.printStackTrace();
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

	private void fillProcessedTweetText(TweetContents tC, Long tweetId) {
		try {
			String tweetFilePath = GlobalVariables.processedTweetTextFolderPath
					+ "/" + tweetId;
			File openTweetFile = null;
			if (GlobalVariables.tweetFilePointers.containsKey(tweetId)) {
				openTweetFile = GlobalVariables.tweetFilePointers.get(tweetId);
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

	public void fillInvertedIndexForTweet(TweetContents tweetContent,
			Long tweetId, HashMap<String, HashMap<Long, Byte>> invertedIndex,
			HashMap<String, Integer> hashTagIndex) {
		// System.out.println("---------------------");
		String lemma;
		String posTag;
		Byte i;
		for (i = 0; i < tweetContent.lammetisedText.size(); i++) {
			lemma = tweetContent.lammetisedText.get(i).lemma;
			posTag = tweetContent.lammetisedText.get(i).posTag;

			if (lemma.charAt(0) == '#') {
				// tweetContent.numberOfTokensExcludingStopWords++;
				// hash tag mapping
				lemma = lemma.substring(1);
				if (hashTagIndex.containsKey(lemma)) {
					Integer count = hashTagIndex.get(lemma);
					hashTagIndex.put(lemma, count + 1);
				} else {
					hashTagIndex.put(lemma, 1);
				}
			} else {

				if (requiredPosTags.contains(posTag)) {
					// stop word removal
					// System.out.println("---------------------:" + lemma);

					if (invertedIndex.containsKey(lemma)) {
						// System.out.println("*******************:" +
						// lemma);
						if (invertedIndex.get(lemma).containsKey(tweetId)) {
							Byte count = invertedIndex.get(lemma).get(tweetId);
							count++;
							invertedIndex.get(lemma).put(tweetId, count);
						} else {
							invertedIndex.get(lemma).put(tweetId, (byte) 1);
						}
					} else {
						// System.out.println("#############:" + lemma);

						HashMap<Long, Byte> hM = new HashMap<Long, Byte>();
						hM.put(tweetId, (byte) 1);
						invertedIndex.put(lemma, hM);
					}
				}
			}
		}
		// System.out.println("---------------------");
	}

	public void divideIntoRelatedUnrelatedInvertedIndex() {

		java.util.Iterator<Entry<String, HashMap<Long, TweetContents>>> entries = databaseMap
				.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String entityId = (String) entry.getKey();
			HashMap<Long, TweetContents> innerMap = (HashMap<Long, TweetContents>) entry
					.getValue();
			// System.out.println("Entity Id = " + entityId);
			java.util.Iterator<Entry<Long, TweetContents>> innerEntries = innerMap
					.entrySet().iterator();
			// entityInfo.get(entityId).numberOfTweets =innerMap.size();

			while (innerEntries.hasNext()) {
				// System.out.println("--------------");
				Map.Entry entryInner = (Map.Entry) innerEntries.next();
				Long tweetId = (Long) entryInner.getKey();
				TweetContents tC = (TweetContents) entryInner.getValue();

				// System.out.println("TweetId:" + tweetId + ":" +
				// tC.extendedURL
				// + ":author:" + tC.author + ":text:" + tC.tweetText
				// + "related:" + tC.related);

				if (tC.tweetText != null) {

					if (tC.related) {
						entityInfo.get(entityId).relatedTweets++;
						fillInvertedIndexForTweet(tC, tweetId,

						entityInfo.get(entityId).relatedInvertedIndex,
								entityInfo.get(entityId).relatedHashTagIndex);
					} else {
						entityInfo.get(entityId).unRelatedTweets++;
						fillInvertedIndexForTweet(tC, tweetId,

						entityInfo.get(entityId).unRelatedInvertedIndex,
								entityInfo.get(entityId).unRelatedHashTagIndex);
					}
					// System.out.println("tweetText:" +tC.tweetText);

				}

				// break;
			}
			// break;
		}

	}

	public void parseEntities() {

		try {
			BufferedReader readEntitiesInformation = new BufferedReader(
					new FileReader(new File(
							GlobalVariables.pathForEntityInformation)));
			String str[], line;
			readEntitiesInformation.readLine();
			while ((line = readEntitiesInformation.readLine()) != null) {
				str = line.split("\"\\s+\"");
				// System.out.println(line + "$$");
				// 0:entity_id,1:query,2:entity_name,3:category,4:homepage,5:wikipedia_en,7:md5_homepage,8:md5_wikipedia_en
				/*
				 * if( str.length != 10 ){ row++; }
				 */
				if (!str.equals("") && str.length == 10) {
					if (str[0] != "")
						str[0] = str[0].replaceFirst("\"", "").trim();
					if (str[1] != "")
						str[1] = str[1].trim();
					if (str[2] != "")
						str[2] = str[2].trim();
					if (str[3] != "")
						str[3] = str[3].trim();
					if (str[4] != "")
						str[4] = str[4].trim();
					if (str[5] != "")
						str[5] = str[5].trim();
					if (str[7] != "")
						str[7] = str[7].trim();
					if (str[8] != "")
						str[8] = str[8].trim();

					if (entityInfo.containsKey(str[0])) {

						EntityContents eC = entityInfo.get(str[0]);
						eC.queryWord = str[1];
						eC.entityName = str[2];
						eC.category = str[3];
						eC.homePageURL = str[4];
						eC.wikiPageUrl = str[5];
						eC.md5_homepage = str[7];
						eC.md5_wikipedia_en = str[8];
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

			String pathForModifiedEntityId = GlobalVariables.pathForModifiedDownloadedExternalLinks
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

	public void fetchEntitiesModifiedLemmatisedHtmlPageIntoEntityInfo() {
		try {
			File file1, file2;
			Iterator<Entry<String, EntityContents>> entries = entityInfo
					.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String entityId = (String) entry.getKey();
				EntityContents entityContent = (EntityContents) entry
						.getValue();

				String pathForModifiedEntityId = GlobalVariables.pathForModifiedEntitiesHtmlPage
						+ "/" + entityId;

				// System.out.println("Created Folder:" +
				// pathForModifiedEntityId);
				String pathForEntitiesWikiText = pathForModifiedEntityId + "/"
						+ entityContent.md5_wikipedia_en;
				String pathForEntitiesHomeText = pathForModifiedEntityId + "/"
						+ entityContent.md5_homepage;
				// System.out.println("EntityId:------------>" + entityId);

				if (entityContent.md5_homepage != "") {
					file1 = new File(pathForEntitiesHomeText);
					for (File file : file1.listFiles()) {
						if (file.getName().contains("-lemmatised")) {
							BufferedReader bR = new BufferedReader(
									new FileReader(new File(
											pathForEntitiesHomeText + "/"
													+ file.getName())));
							String line;
							// System.out.println("Acccesing File Path:"
							// + pathForEntitiesHomeText + "/"
							// + file.getName());
							// System.out.println("Title--->");
							while ((line = bR.readLine()) != null) {
								if (!line.contains("---")) {

									// System.out.println(line);
									LemmaInfo lI = new LemmaInfo();
									String token[] = line.split(":");
									lI.lemma = token[0];
									lI.posTag = token[1];
									entityContent.lammetisedHomeTitleText
											.add(lI);
									addToCommonInvertedIndex(lI.lemma,
											lI.posTag,
											entityContent.commonInvertedIndex);

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
								entityContent.lammetisedHomeBodyText.add(lI);
								addToCommonInvertedIndex(lI.lemma, lI.posTag,
										entityContent.commonInvertedIndex);
							}

						}
					}
				}

				if (entityContent.md5_wikipedia_en != "") {
					file1 = new File(pathForEntitiesWikiText);
					for (File file : file1.listFiles()) {
						if (file.getName().contains("-lemmatised")) {
							BufferedReader bR = new BufferedReader(
									new FileReader(new File(
											pathForEntitiesWikiText + "/"
													+ file.getName())));
							String line;
							// System.out.println("Acccesing File Path:"
							// + pathForEntitiesWikiText + "/"
							// + file.getName());
							// System.out.println("Title--->");
							while ((line = bR.readLine()) != null) {
								if (!line.contains("---")) {
									// System.out.println(line);
									LemmaInfo lI = new LemmaInfo();
									String token[] = line.split(":");
									lI.lemma = token[0];
									lI.posTag = token[1];
									entityContent.lammetisedWikiTitleText
											.add(lI);
									addToCommonInvertedIndex(lI.lemma,
											lI.posTag,
											entityContent.commonInvertedIndex);

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
								entityContent.lammetisedWikiBodyText.add(lI);
								addToCommonInvertedIndex(lI.lemma, lI.posTag,
										entityContent.commonInvertedIndex);
							}
						}
					}
				}
				// break;
			}
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

	public void parseTweets() {
		int i = 0;
		File dir;
		String line;
		String str[];
		Long tweetId;
		int row = 0;

		BufferedReader[] readTweetInfoPath = new BufferedReader[GlobalVariables.totalFiles];
		BufferedReader[] readTweetModified = new BufferedReader[GlobalVariables.totalFiles];
		BufferedReader[] readLabelled = new BufferedReader[GlobalVariables.totalFiles];

		try {

			dir = new File(GlobalVariables.pathForTweetInfoPath);
			// System.out.println(GlobalVariables.pathForTweetInfoPath + ":");
			for (File child : dir.listFiles()) {
				readTweetInfoPath[i++] = new BufferedReader(new FileReader(
						child));
				// System.out.println(child.getName());
			}

			i = 0;
			dir = new File(GlobalVariables.pathForTweetModified);
			// System.out.println(GlobalVariables.pathForTweetModified + ":");
			for (File child : dir.listFiles()) {
				readTweetModified[i++] = new BufferedReader(new FileReader(
						child));
				// System.out.println(child.getName());
			}

			i = 0;
			dir = new File(GlobalVariables.pathForLabelled);
			// System.out.println(GlobalVariables.pathForLabelled + ":");
			for (File child : dir.listFiles()) {
				readLabelled[i++] = new BufferedReader(new FileReader(child));
				// System.out.println(child.getName());
			}

			GlobalVariables.totalFiles = i;
			for (i = 0; i < GlobalVariables.totalFiles; i++) {

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

							EntityContents eC = new EntityContents();
							entityInfo.put(str[2], eC);

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
			for (i = 0; i < GlobalVariables.totalFiles; i++) {

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
								// processTweetText(tC, null);
								fillProcessedTweetText(tC, tweetId);
							}
						}

					}
				}
			}

			// printDataBaseMap();

			for (i = 0; i < GlobalVariables.totalFiles; i++) {

				readLabelled[i].readLine();

				while ((line = readLabelled[i].readLine()) != null) {

					str = line.split("\"\\s+\"");
					// System.out.println(line + "$$");
					// 0:tweetId,1:author,2:entityId,3:related/unrelated
					if (!str.equals("") && str.length >= 4) {
						// System.out.println("$$$$$$$$$$$$$");
						if (str[0] != "")
							str[0] = str[0].replaceFirst("\"", "").trim();
						if (str[1] != "")
							str[1] = str[1].trim();
						if (str[2] != "")
							str[2] = str[2].trim();
						if (str[3] != "")
							str[3] = str[3].trim();
						if (databaseMap.containsKey(str[2])) {
							// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
							tweetId = Long.parseLong(str[0]);
							// System.out.println("tweetId:"+tweetId);
							if (databaseMap.get(str[2]).containsKey(tweetId)) {
								TweetContents tC = databaseMap.get(str[2]).get(
										tweetId);
								if (str[3].compareToIgnoreCase("related") == 0) {
									tC.related = true;
									// System.out.println("true");
								} else
									tC.related = false;
								// System.out.println("----------------------------:"

							}
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

package com.basicTweetsClassification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.basicTweetsClassification.TweetsParsing.EntityContents;
import com.basicTweetsClassification.TweetsParsing.LemmaInfo;
import com.basicTweetsClassification.TweetsParsing.TweetContents;

public class FeatureCalculation {

	HashMap<String, HashMap<Long, TweetContents>> databaseMap = GlobalVariables.databaseMap;

	HashMap<String, EntityContents> entityInfo = GlobalVariables.entityInfo;

	public HashSet<String> requiredPosTags = GlobalVariables.requiredPosTags;

	public HashSet<String> requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage = GlobalVariables.requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage;

	public int getTopNFrequentLemma(Integer N,
			HashMap<String, Integer> targetHashMap, List<LemmaInfo> toFill) {
		LemmaInfo lI;
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		for (int i = 0; i < N; i++) {
			java.util.Iterator<Entry<String, Integer>> entries = targetHashMap
					.entrySet().iterator();
			Integer max = 0;
			String maxCountLemma = "";
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String lemma = (String) entry.getKey();
				Integer count = (Integer) entry.getValue();
				if (max < count && !visited.containsKey(lemma)) {
					maxCountLemma = lemma;
					max = count;
				}
			}

			if (max == 0)
				break;
			else {
				lI = new LemmaInfo();
				lI.count = max;
				lI.lemma = maxCountLemma;
				toFill.add(lI);
				visited.put(maxCountLemma, true);
			}
		}
		return toFill.size();
	}

	public void fillTop10LemmasFromEntitiesCommonMap() {
		Iterator<Entry<String, EntityContents>> entries = entityInfo.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String entityId = (String) entry.getKey();
			EntityContents entityContent = (EntityContents) entry.getValue();
			// System.out.println("Entity Id = " + entityId + "\n related:"
			// + entityContent.relatedTweets + ":unrelated:"
			// + entityContent.unRelatedTweets);
			getTopNFrequentLemma(10, entityContent.commonInvertedIndex,
					entityContent.topNLemmas);
			// System.out.println("Entity Id:" + entityId);
			// printTop10ListOfList(entityContent.topNLemmas);
			// break;
		}
	}

	private double getFeature1(TweetContents tC, String lemma, String entityId) {
		double df;
		double Nf = 0.0;
		double N = 0.0;

		N = entityInfo.get(entityId).relatedTweets;
		// System.out.println("Feature1:");
		if (N == 0) {
			// System.out.println("lemma:" + lemma + ":N:" + N + ":Nf:" + Nf);
			return 0.0;
		}
		if (entityInfo.get(entityId).relatedInvertedIndex.containsKey(lemma))
			Nf = (double) (entityInfo.get(entityId).relatedInvertedIndex
					.get(lemma).size());
		else
			Nf = 0.0;
		df = Nf / N;
		// System.out.println("lemma:" + lemma + ":N:" + N + ":Nf:" + Nf);
		return df;
	}

	private double getFeature2(TweetContents tC, String lemma, String entityId) {
		double df;
		double Nf = 0.0;
		double N = 0.0;
		N = entityInfo.get(entityId).unRelatedTweets;
		// System.out.println("Feature2:");
		if (N == 0) {
			// System.out.println("lemma:"+lemma+":N:"+N+":Nf:"+Nf);
			return 0.0;
		}

		if (entityInfo.get(entityId).unRelatedInvertedIndex.containsKey(lemma))
			Nf = (double) (entityInfo.get(entityId).unRelatedInvertedIndex
					.get(lemma).size());
		else
			Nf = 0.0;
		df = Nf / N;
		// System.out.println("lemma:"+lemma+":N:"+N+":Nf:"+Nf);
		return df;
	}

	private double getFeature3(String tweetLemma, List<LemmaInfo> topNList) {
		double temp = 0.0;
		for (LemmaInfo lI : topNList) {
			String invertedIndexLemma = lI.lemma;
			Integer count = lI.count;
			// temp += WordNetSimilarityCalculation.run(tweetLemma,
			// invertedIndexLemma);
			temp += LoadOrDumpObjectInFile.getAndFillSimilarityValue(
					tweetLemma, invertedIndexLemma) / GlobalVariables.infinity;
		}
		// System.out.println("Final Temp:"+temp);
		temp = temp / topNList.size();

		return temp;
	}

	private double getFeature4(TweetContents tC, String lemma, String entityId) {
		double tf = 0.0;
		if (entityInfo.get(entityId).commonInvertedIndex.containsKey(lemma)) {
			tf = entityInfo.get(entityId).commonInvertedIndex.get(lemma);
		}
		return tf;
	}

	private void fillFeatures(String entityId, Long tweetId, TweetContents tC) {
		String lemma;
		String posTag;
		// feature 1 calculation
		for (int i = 0; i < tC.lammetisedText.size(); i++) {
			// System.out.println(tC.lammetisedText.get(i).lemma + ":PosTag:"
			// + tC.lammetisedText.get(i).posTag);
			lemma = tC.lammetisedText.get(i).lemma;
			posTag = tC.lammetisedText.get(i).posTag;

			if (requiredPosTags.contains(posTag)) {
				tC.feature1 += getFeature1(tC, lemma, entityId);
				tC.feature2 += getFeature2(tC, lemma, entityId);

				if (tC.feature3 >= ((GlobalVariables.infinity / 1000.00))) {
					tC.feature3 = ((GlobalVariables.infinity / 1000.00));
				} else {
					tC.feature3 += getFeature3(lemma,
							entityInfo.get(entityId).topNLemmas);
				}
				tC.feature4 += getFeature4(tC, lemma, entityId);
			}
		}

		if (tC.feature3 >= ((GlobalVariables.infinity / 1000.00))) {
			tC.feature3 = ((GlobalVariables.infinity / 1000.00));
		}

		if (tC.numberOfTokensExcludingStopWords != 0) {
			tC.feature1 = (tC.feature1 / tC.numberOfTokensExcludingStopWords) * 1000;
			tC.feature2 = (tC.feature2 / tC.numberOfTokensExcludingStopWords) * 1000;
			tC.feature3 = (tC.feature3 / tC.numberOfTokensExcludingStopWords) * 1000;
			// System.out.println("This is Feature3:" + tC.feature3);
			tC.feature4 = (tC.feature4 / tC.numberOfTokensExcludingStopWords) * 1000;
		}
	}

	public void calculateFeature() {

		// System.out.println("In Calculate feature:----->");
		long startingTime = System.currentTimeMillis();
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
			while (innerEntries.hasNext()) {
				// System.out.println("--------------");
				Map.Entry entryInner = (Map.Entry) innerEntries.next();
				Long tweetId = (Long) entryInner.getKey();
				TweetContents tC = (TweetContents) entryInner.getValue();
				// System.out.println("Fill Feature:" + ":tweetId:" + tweetId);
				fillFeatures(entityId, tweetId, tC);
			}
			// break;
		}
		long endingTime = System.currentTimeMillis();
		System.out.println("Total Time Taken By :Calculating Feature:"
				+ (endingTime - startingTime) / 1000 + "s");
	}
}

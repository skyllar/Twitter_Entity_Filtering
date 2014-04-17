package com.basicTweetsClassification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.TestsTweetsClassification.TestTweetsMain;

public class Main {

	public static void main(String[] args) {
		BufferedWriter bW = null;
		BufferedReader bR = null;

		try {

			loadFilePointersOfTweets(
					GlobalVariables.processedTweetTextFolderPath,
					GlobalVariables.tweetFilePointers);
			bW = new BufferedWriter(new FileWriter(
					GlobalVariables.similarityFilePath, true));
			bR = new BufferedReader(new FileReader(new File(
					GlobalVariables.similarityFilePath)));

			// call for basic parsing
			long startingTime = System.currentTimeMillis();

			LoadOrDumpObjectInFile objectInFile = new LoadOrDumpObjectInFile(
					bR, bW);

			TweetsParsing tweetsParsing = new TweetsParsing();
			// ConvertHtmlToText conevrtHtml = new ConvertHtmlToText();
			FeatureCalculation featureCal = new FeatureCalculation();
			DumpTrainingData dump = new DumpTrainingData();

			System.out
					.println("Parsing Training Tweets...............................");

			System.out
					.println("****************Calling loadSimilaritiesFromFile()***********");
			objectInFile.loadSimilaritiesFromFile();

			System.out
					.println("****************Calling Function parseTweets()***********");
			// this will also populate lammetisedText vector
			tweetsParsing.parseTweets();

			// System.out.println("-----------Database Map---------------");
			// tweetsParsing.printDataBaseMap();

			// System.out
			// .println("********fetchTweetsModifiedExternalHtmlPage()*************");
			// tweetsParsing.fetchTweetsModifiedLemmatisedExternalHtmlPage();

			System.out
					.println("***********Calling function divideIntoRelatedUnrelatedInvertedIndex()**********");
			tweetsParsing.divideIntoRelatedUnrelatedInvertedIndex();

			// System.out.println("-----------Database Map---------------");
			// tweetsParsing.printDataBaseMap();

			System.out
					.println("***********Calling function parseEntities()**********");
			tweetsParsing.parseEntities();

			// System.out.println("-----------Entity Map---------------");
			// tweetsParsing.printEntityMap();

			// System.out
			// .println("********Calling function convertEntitiesHtmlPageToText()*************");
			// conevrtHtml.convertEntitiesHtmlPageToText();
			// System.out
			// .println("********Calling function convertTweetsExternalHtmlPageToText()*************");
			// conevrtHtml.convertTweetsExternalHtmlPageToText();

			tweetsParsing.calculateWeightForEntities();

			System.out
					.println("********fetchEntitiesModifiedHtmlPagentoEntityInfo()*************");
			tweetsParsing
					.fetchEntitiesModifiedLemmatisedHtmlPageIntoEntityInfo();
			// For calculateFeature3() ,above is func is necessary.

			// necessary for feature 3
			// featureCal.fillTop10LemmasFromEntitiesCommonMap();

			// System.out
			// .println("***********Calling function calculateFeature()**************");
			// featureCal.calculateFeature();
			// //
			// System.out
			// .println("***********************Dumping Training Set*****************************");
			// dump.trainingDataDump();

			long endingTime = System.currentTimeMillis();
			System.out.println("Total Time Taken:"
					+ (endingTime - startingTime) / 1000 + "s");

			// System.out.println("-----------Entity Map---------------");
			// tweetsParsing.printEntityMap();

			try {
				if (bW != null)
					bW.close();
				if (bR != null)
					bR.close();
			} catch (IOException e) {
			}

			startingTime = System.currentTimeMillis();

			// System.out
			// .println("Parsing Test Tweets...............................................");
			// TestTweetsMain testTweetsMain = new TestTweetsMain();
			// testTweetsMain.testTweetsMain();
			endingTime = System.currentTimeMillis();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {

		}
	}

	private static void loadFilePointersOfTweets(String dirPath,
			HashMap<Long, File> tweetFilePointers) {
		File dir = new File(dirPath);
		for (File file : dir.listFiles()) {
			Long tweetId = Long.parseLong(file.getName());
			tweetFilePointers.put(tweetId, file);
		}
	}
}

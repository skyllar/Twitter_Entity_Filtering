package com.TestsTweetsClassification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class TestTweetsMain {

	// public TestTweetsMain(HashMap<String, EntityContents> entityInfo) {
	// GlobalVariablesForTestTweets.entityInfo = entityInfo;
	// }

	public void testTweetsMain() {

		loadFilePointersOfTweets(
				GlobalVariablesForTestTweets.processedTestTweetsTextFolderPath,
				GlobalVariablesForTestTweets.tweetFilePointers);

		BufferedWriter bW = null;
		BufferedReader bR = null;
		try {
			bW = new BufferedWriter(new FileWriter(
					GlobalVariablesForTestTweets.similarityFilePath, true));
			bR = new BufferedReader(new FileReader(new File(
					GlobalVariablesForTestTweets.similarityFilePath)));

			// call for basic parsing
			long startingTime = System.currentTimeMillis();

			LoadOrDumpObjectInFile objectInFile = new LoadOrDumpObjectInFile(
					bR, bW);
			TestTweetsParsing tweetsParsing = new TestTweetsParsing();
			// ConvertHtmlToText conevrtHtml = new ConvertHtmlToText();
			FeatureCalculation featureCal = new FeatureCalculation();
			DumpTestTrainingData dump = new DumpTestTrainingData();

			// System.out
			// .println("-----------Entity Map In Test Tweets---------------");
			// tweetsParsing.printEntityMap();

			System.out
					.println("****************Calling Function parseTweets()***********");
			// this will also populate lammetisedText vector
			tweetsParsing.parseTweets();

			// System.out
			// .println("*****************Entity Map In Test Tweets*******************");
			// tweetsParsing.printEntityMap();

			// System.out.println("-----------Database Map---------------");
			// tweetsParsing.printDataBaseMap();

			// System.out
			// .println("********fetchTweetsModifiedExternalHtmlPage()*************");
			// tweetsParsing.fetchTweetsModifiedLemmatisedExternalHtmlPage();

			// System.out
			// .println("********Calling function convertTweetsExternalHtmlPageToText()*************");
			// conevrtHtml.convertTweetsExternalHtmlPageToText();

			System.out
					.println("***********Calling function calculateFeature()**************");
			featureCal.calculateFeature();

			System.out
					.println("***********************Dumping Training Set*****************************");
			dump.trainingDataDump();

			long endingTime = System.currentTimeMillis();
			System.out.println("Total Time Taken:"
					+ (endingTime - startingTime) / 1000 + "s");

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {
				if (bW != null)
					bW.close();
				if (bR != null)
					bR.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

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

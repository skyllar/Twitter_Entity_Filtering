package com.TestsTweetsClassification;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.basicTweetsClassification.GlobalVariables;
import com.basicTweetsClassification.TweetsParsing.EntityContents;
import com.basicTweetsClassification.TweetsParsing.TweetContents;

public class GlobalVariablesForTestTweets {

	public static String pathForTweetInfoPath = "../Data/test/tweet_info";

	public static String pathForTweetModified = "../Data/test/tweet_text_modified";
	// //////
	public static String pathForLabelled = "../Data/test/labelled/labeled/goldstandard_filtering.dat";

	public static String pathForDownloadedExternalLinks = "../Data/test/downloaded_external_links";

	public static String pathForModifiedDownloadedExternalLinks = "../Data/test/modified_downloaded_external_links";

	public static String processedTestTweetsTextFolderPath = "../ProcessedTestTweets";
	// public static String pathForEntitiesHtmlPage =
	// "../Data/entities/downloaded_entities_urls";
	//
	// public static String pathForEntityInformation =
	// "../Data/entities/replab2013_entities.tsv";
	//
	// public static String pathForModifiedEntitiesHtmlPage =
	// "../Data/entities/modified_downloaded_entities_urls";
	//
	// public static String pathForLemmmatisedTweetText =
	// "../Data/training/lemmatised_tweet_text";

	// public static String pathForTweetInfoPath =
	// "../smallData/training/tweet_info";
	// public static String pathForTweetModified =
	// "../smallData/training/tweet_text_modified";
	// public static String pathForLabelled =
	// "../smallData/training/labelled/labeled";

	// public static HashMap<String, EntityContents> entityInfo = new
	// HashMap<String, EntityContents>();

	public static HashMap<String, HashMap<Long, TweetContents>> databaseMap = new HashMap<String, HashMap<Long, TweetContents>>();
	public static HashMap<String, EntityContents> entityInfo = GlobalVariables.entityInfo;

	// to be initialised
	public static int totalFiles = 60;
	public static int totalEntities;
	public static String trainingFilePath = "../TestTrainingData";

	public static double infinity = 1.7976931348623157E308;

	public static HashSet<String> requiredPosTags = new HashSet<String>(
			Arrays.asList("FW", "JJ", "JJR", "JJS", "NN", "NNP", "NNPS", "NNS",
					"RB", "RBR", "RBS", "UH", "VB", "VBN", "VBP", "VBZ", "VBD",
					"VBG"));

	public static HashSet<String> requiredPosTagsForEntitiesCommonHashOfWikiAndHomePage = new HashSet<String>(
			Arrays.asList("FW", "JJ", "JJR", "JJS", "NN", "NNP", "NNPS", "NNS",
					"RB", "RBR", "RBS", "UH", "VB", "VBN", "VBP", "VBZ", "VBD",
					"VBG"));

	public static String similarityFilePath = "../Data/WordNetSimilarity.txt";

	public static HashMap<String, HashMap<String, Double>> wordNetSimilarity = GlobalVariables.wordNetSimilarity;
	public static HashMap<Long, File> tweetFilePointers = new HashMap<Long, File>();

}
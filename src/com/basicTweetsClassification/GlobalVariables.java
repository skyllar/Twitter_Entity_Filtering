package com.basicTweetsClassification;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.basicTweetsClassification.TweetsParsing.EntityContents;
import com.basicTweetsClassification.TweetsParsing.TweetContents;

public class GlobalVariables {

	public static String pathForTweetInfoPath = "../Data/training/tweet_info";

	public static String pathForTweetModified = "../Data/training/tweet_text_modified";

	public static String pathForLabelled = "../Data/training/labelled/labeled";

	public static String pathForDownloadedExternalLinks = "../Data/training/downloaded_external_links";

	public static String pathForModifiedDownloadedExternalLinks = "../Data/training/modified_downloaded_external_links";

	public static String pathForEntitiesHtmlPage = "../Data/entities/downloaded_entities_urls";

	public static String pathForEntityInformation = "../Data/entities/replab2013_entities.tsv";

	public static String pathForModifiedEntitiesHtmlPage = "../Data/entities/modified_downloaded_entities_urls";

	public static String pathForLemmmatisedTweetText = "../Data/training/lemmatised_tweet_text";

	// public static String pathForTweetInfoPath =
	// "../smallData/training/tweet_info";
	// public static String pathForTweetModified =
	// "../smallData/training/tweet_text_modified";
	// public static String pathForLabelled =
	// "../smallData/training/labelled/labeled";

	public static HashMap<String, EntityContents> entityInfo = new HashMap<String, EntityContents>();

	public static HashMap<String, HashMap<Long, TweetContents>> databaseMap = new HashMap<String, HashMap<Long, TweetContents>>();

	public static String pathForEntities = "";
	public static String pathForRelatedFile = "related";
	public static String pathForUnReatedFile = "unrelated";

	public static String trainingFilePath = "../TrainingData";

	// to be initialised
	public static int totalFiles = 60;
	public static int totalEntities;

	public static String processedTweetTextFolderPath = "../ProcessedTrainingTweets";

	public static String modelPath1 = "../Libraries/ark-tweet-nlp-0.3.2/50mpaths2";
	public static String modelPath2 = "../Libraries/ark-tweet-nlp-0.3.2/model.20120919";
	public static String inputTweet = "../Libraries/ark-tweet-nlp-0.3.2/examples/inputTweet.txt";

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

	public static HashMap<String, HashMap<String, Double>> wordNetSimilarity = new HashMap<String, HashMap<String, Double>>();

	public static HashMap<Long, File> tweetFilePointers = new HashMap<Long, File>();

	public static String pathForWeightOfEntities = "../Data/weight.txt";
}
The Project has following components:
1. Identifies if a given tweet and a given entity are relevant.
2. TweetFilter involves extraction of feature vector.
3. Building a relevance classifier using supervised learning approach.

Video Overview: https://www.youtube.com/watch?v=JgqexBy0K7s

Approach:

• Supervised Machine learning is used to decide if the entity belongs to an entity or
not.

• Dataset from RepLab, home page and wikipedia page of the entity is being used.

• It involves pre-processing of the above data, extracting features from the data to
train using SVM.

• Test data also goes through same procedure, the output is predicted using the
weight vector obtained from the trained model.

Usage:
There are three packages in the "src" file:

	a)com.basicTweetsClassification
	b)com.svmTraining
	c)com.TestsTweetsClassification

Description of pacakges:

	a)com.basicTweetsClassification:It contains following java files :
		1.ConvertHtmlToText.java 
		2.DumpTrainingData.java
		3.FeatureCalculation.java
		4.GoabalVariables.java
		5.LoadOrDumpObjectInFile.java
		6.Main.java
		7.RunTagger.java
		8.StanfordLemmatizer.java
		9.Tagger.java
		10.TweetsParsing.java
		11.WordNetSimilarityCalculation.java

	b)com.svmTraining:It contains following java files :
		1.GlobalVariables.java
		2.mainSVM.java
		3.svm_train.java
		4.WeightContents.java
	
	c)com.TestsTweetsClassification:It contains following java files :
		1.DumpTestTrainingData.java
		2.FeatureCalculation.java
		3.GlobalVariablesForTestTweets.java
		4.LoadOrDumpObjectInFile.java
		5.TestTweetsMain.java
		6.TestTweetsParsing.java

How to Run:

	a)Preprocessing,Feature Extraction and building model for Training tweets:
		1.Set the appropriate paths for the training tweets in  GoabalVariables.java (package:com.basicTweetsClassification).
		2.Run the Main.java (package:com.basicTweetsClassification).It will build feature-vector for training tweets ,output will be dumped in file.
		3.Run mainSVM.java (package:com.svmTraining) for SVM-training from available feature vector.

	b)Preprocessing,Feature Extraction and predicting entity for Test tweets:
		1.Uncomment testTweetsMain.testTweetsMain() & Comment "featureCal.calculateFeature()" & "dump.trainingDataDump()" in Main.java(package:com.basicTweetsClassification).		
		2.Run the Main.java (package:com.basicTweetsClassification).It will build feature-vector for test tweets,output will be dumped in file.

		3.Run mainSVM.java (package:com.svmTraining) for SVM-prediction of test tweets.		


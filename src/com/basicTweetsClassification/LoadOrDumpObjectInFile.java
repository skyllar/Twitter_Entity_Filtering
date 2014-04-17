package com.basicTweetsClassification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class LoadOrDumpObjectInFile {

	public static HashMap<String, HashMap<String, Double>> wordNetSimilarity = GlobalVariables.wordNetSimilarity;
	static BufferedWriter bW = null;
	static BufferedReader bR = null;

	public LoadOrDumpObjectInFile(BufferedReader bR, BufferedWriter bW) {
		this.bR = bR;
		this.bW = bW;
	}

	public LoadOrDumpObjectInFile() {

	}

	public static double getAndFillSimilarityValue(String word1, String word2) {
		if (wordNetSimilarity.containsKey(word1)
				&& wordNetSimilarity.get(word1).containsKey(word2)) {
			return (wordNetSimilarity.get(word1).get(word2));
		}
		if (wordNetSimilarity.containsKey(word2)
				&& wordNetSimilarity.get(word2).containsKey(word1)) {
			return (wordNetSimilarity.get(word2).get(word1));
		}
		if (!wordNetSimilarity.containsKey(word2)
				&& !wordNetSimilarity.containsKey(word1)) {
			HashMap<String, Double> innerHash = new HashMap<String, Double>();
			double temp = WordNetSimilarityCalculation.run(word1, word2);
			innerHash.put(word2, temp);
			wordNetSimilarity.put(word1, innerHash);
			writeWordsIntoFile(word1, word2, temp);
			return temp;
		}
		if (wordNetSimilarity.containsKey(word1)) {
			HashMap<String, Double> innerHash = wordNetSimilarity.get(word1);
			double temp = WordNetSimilarityCalculation.run(word1, word2);
			innerHash.put(word2, temp);
			wordNetSimilarity.put(word1, innerHash);
			writeWordsIntoFile(word1, word2, temp);
			return temp;
		} else {
			HashMap<String, Double> innerHash = wordNetSimilarity.get(word2);
			double temp = WordNetSimilarityCalculation.run(word1, word2);
			innerHash.put(word1, temp);
			wordNetSimilarity.put(word2, innerHash);
			writeWordsIntoFile(word1, word2, temp);
			return temp;
		}
	}

	public static double onlyGetSimilarityValue(String word1, String word2) {
		if (wordNetSimilarity.containsKey(word1)
				&& wordNetSimilarity.get(word1).containsKey(word2)) {
			return (wordNetSimilarity.get(word1).get(word2));
		}
		if (wordNetSimilarity.containsKey(word2)
				&& wordNetSimilarity.get(word2).containsKey(word1)) {
			return (wordNetSimilarity.get(word2).get(word1));
		}
		if (!wordNetSimilarity.containsKey(word2)
				&& !wordNetSimilarity.containsKey(word1)) {
			HashMap<String, Double> innerHash = new HashMap<String, Double>();
			double temp = WordNetSimilarityCalculation.run(word1, word2);
			innerHash.put(word2, temp);
			wordNetSimilarity.put(word1, innerHash);
			return temp;
		}
		if (wordNetSimilarity.containsKey(word1)) {
			HashMap<String, Double> innerHash = wordNetSimilarity.get(word1);
			double temp = WordNetSimilarityCalculation.run(word1, word2);
			innerHash.put(word2, temp);
			wordNetSimilarity.put(word1, innerHash);
			return temp;
		} else {
			HashMap<String, Double> innerHash = wordNetSimilarity.get(word2);
			double temp = WordNetSimilarityCalculation.run(word1, word2);
			innerHash.put(word1, temp);
			wordNetSimilarity.put(word2, innerHash);
			return temp;
		}
	}

	private static void writeWordsIntoFile(String word1, String word2,
			double temp) {
		try {
			bW.write(word1 + ":" + word2 + ":" + temp + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeSimilarityMap() {
		try {
			bR.close();
			bW.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static void putSimilarityValueInMap(String word1, String word2,
			double similarity) {
		if (wordNetSimilarity.containsKey(word1)
				&& wordNetSimilarity.get(word1).containsKey(word2)) {
			return;
		}
		if (wordNetSimilarity.containsKey(word2)
				&& wordNetSimilarity.get(word2).containsKey(word1)) {
			return;
		}
		if (!wordNetSimilarity.containsKey(word2)
				&& !wordNetSimilarity.containsKey(word1)) {
			HashMap<String, Double> innerHash = new HashMap<String, Double>();
			double temp = similarity;
			innerHash.put(word2, temp);
			wordNetSimilarity.put(word1, innerHash);
			return;
		}
		if (wordNetSimilarity.containsKey(word1)) {
			HashMap<String, Double> innerHash = wordNetSimilarity.get(word1);
			double temp = similarity;
			innerHash.put(word2, temp);
			wordNetSimilarity.put(word1, innerHash);
			return;
		} else {
			HashMap<String, Double> innerHash = wordNetSimilarity.get(word2);
			double temp = similarity;
			innerHash.put(word1, temp);
			wordNetSimilarity.put(word2, innerHash);
			return;
		}
	}

	public void loadSimilaritiesFromFile() {
		String line;
		try {
			while ((line = bR.readLine()) != null) {
				if (!line.equals("")) {
					String token[] = line.split(":");
					if (token.length == 3) {
						putSimilarityValueInMap(token[0], token[1],
								Double.parseDouble(token[2]));
					}
				}

			}
			bR.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}

}

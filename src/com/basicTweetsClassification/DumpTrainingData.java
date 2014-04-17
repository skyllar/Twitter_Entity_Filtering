package com.basicTweetsClassification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.basicTweetsClassification.TweetsParsing.EntityContents;
import com.basicTweetsClassification.TweetsParsing.TweetContents;

public class DumpTrainingData {
	HashMap<String, HashMap<Long, TweetContents>> databaseMap = GlobalVariables.databaseMap;

	HashMap<String, EntityContents> entityInfo = GlobalVariables.entityInfo;

	public void trainingDataDump() {
		try {

			java.util.Iterator<Entry<String, HashMap<Long, TweetContents>>> entries = databaseMap
					.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String key = (String) entry.getKey();
				HashMap<Long, TweetContents> innerMap = (HashMap<Long, TweetContents>) entry
						.getValue();
				// System.out.println("Creating File = "
				// + GlobalVariables.trainingFilePath + "/" + key);
				// create file for that entity
				File file = new File(GlobalVariables.trainingFilePath + "/"
						+ key);
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				java.util.Iterator<Entry<Long, TweetContents>> innerEntries = innerMap
						.entrySet().iterator();
				while (innerEntries.hasNext()) {
					// System.out.println("--------------");
					Map.Entry entryInner = (Map.Entry) innerEntries.next();
					Long keyInner = (Long) entryInner.getKey();
					TweetContents tC = (TweetContents) entryInner.getValue();

					// System.out.println("TweetId:" + keyInner + ":" +
					// tC.extendedURL + ":author:" + tC.author + ":text:" +
					// tC.tweetText + "related:" + tC.related +
					// ":numberOfTokens:" + tC.numberOfTokensExcludingStopWords
					// + ":tfIdf:" +
					// tC.feature1+":f2:"+tC.feature2);

					// System.out.println("TweetId:" + keyInner);
					// String toWrite = "TweetId:" + keyInner + ":";
					// if (tC.feature1 != 0.0 && tC.feature2 != 0.0 ) {
					String toWrite = "";
					if (tC.related)
						toWrite += "+1 ";
					else
						toWrite += "-1 ";
					// toWrite += "1:" + tC.feature1 + " 2:" + tC.feature2 +
					// "\n";

					toWrite += "1:" + tC.feature1 + " 2:" + tC.feature2 + " 3:"
							+ tC.feature3 + "\n";

					// toWrite += "1:" + tC.feature1 + " 2:" + tC.feature2 +
					// " 3:"
					// + tC.feature3 + " 4:" + tC.feature4 + "\n";

					bw.write(toWrite);
					// }
					// break;
				}

				bw.close();
				fw.close();
				// break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

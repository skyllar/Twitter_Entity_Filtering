package com.basicTweetsClassification;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class WordNetSimilarityCalculation {

	private static ILexicalDatabase db = new NictWordNet();
	// private static RelatednessCalculator[] rcs = { new HirstStOnge(db),
	// new LeacockChodorow(db), new Lesk(db), new WuPalmer(db),
	// new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db) };

	private static RelatednessCalculator[] rcs = { new Resnik(db) };

	public static double run(String word1, String word2) {
		WS4JConfiguration.getInstance().setMFS(true);
		double s = 0.0;
		s = rcs[0].calcRelatednessOfWords(word1, word2);
		// System.out.println("Relatedness:" + rcs[0].getClass().getName() + ":"
		// + s);
		return s;
	}

	public static void runAll(String word1, String word2) {
		RelatednessCalculator[] rcs = { new HirstStOnge(db),
				new LeacockChodorow(db), new Lesk(db), new WuPalmer(db),
				new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db) };
		WS4JConfiguration.getInstance().setMFS(true);

		for (RelatednessCalculator rc : rcs) {
			double s = rc.calcRelatednessOfWords(word1, word2);
			// s = (s / GlobalVariables.infinity) * 1000;
			System.out.println(rc.getClass().getName() + "\t" + s);
		}

	}

	public static void main(String[] args) {
		// System.out.println("----------Start----------");
		long t0 = System.currentTimeMillis();
		runAll("metal", "cake");
		long t1 = System.currentTimeMillis();
		System.out.println("Done in " + (t1 - t0) + " msec.");
		// System.out.println("---------End-----------");
	}
}
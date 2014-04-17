package com.svmTraining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class mainSVM {
	public static Double averageAccuracy = 0.0;

	public static HashMap<String, WeightContents> LCM = new HashMap<String, WeightContents>();

	private static Integer gcd(Integer num1, Integer num2) {

		if (num2 == 0) {
			return num1;
		} else {
			Integer temp = num1;
			num1 = num2;
			num2 = temp % num2;
		}
		return gcd(num1, num2);
	}

	private static Long getLCM(Integer num1, Integer num2) {

		if (num1 == 0)
			return (long) num2;
		if (num2 == 0)
			return (long) num1;

		Integer gcd = gcd(num1, num2);
		Long lcm = (long) ((num1 * num2) / gcd);
		return lcm;
	}

	public static double getSVMResult(String testFilePath,
			String modelFilePath, String outputFilePath) {
		double currentAcccuracy = 0.0;
		try {
			// System.out.println("Calling svm-train");
			// System.out.println("Training File Path:" + trainingFilePath
			// + ":modelFilePath:" + modelFilePath);
			String cmdarr[] = { GlobalVariables.svmPredict, testFilePath,
					modelFilePath, outputFilePath };

			Process process = Runtime.getRuntime().exec(cmdarr);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			// System.out.println("Processing Test File:" + testFilePath);
			String line;
			System.out.println("Entity:" + testFilePath);
			while ((line = input.readLine()) != null) {
				// System.out.println("-----" + line);
				String token[] = line.split("= ");
				String subToken[] = token[1].split("%");
				System.out.println("Current Accuracy:" + subToken[0] + "--");
				currentAcccuracy = Double.parseDouble(subToken[0]);
				// System.out.println("--" + line);
			}
			// System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return currentAcccuracy;
	}

	private static void getResultBasedOnModel() {
		String testTweetFolderPath = GlobalVariables.testTweetFolderPath;
		File dir = new File(testTweetFolderPath);
		String modelFolderPath = GlobalVariables.modelFolderPath;
		double counter = 0.0;
		double totalAcccuracy = 0.0;
		for (File file : dir.listFiles()) {
			counter++;
			String testFileName = file.getName();
			String testFilePath = testTweetFolderPath + "/" + testFileName;
			String modelFilePath = modelFolderPath + "/" + testFileName
					+ "-model";

			String outputFilePath = GlobalVariables.output + "/"
					+ file.getName() + "-output";
			File output = new File(outputFilePath);
			try {
				output.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File modelFile = new File(modelFilePath);
			if (modelFile.exists()) {
				totalAcccuracy += getSVMResult(testFilePath, modelFilePath,
						outputFilePath);
			}
		}
		averageAccuracy = totalAcccuracy / counter;
	}

	public static void buildModelForFile(String arg1, String arg2,
			String trainingFilePath, String modelFilePath) {
		try {
			System.out.println("Calling svm-train");
			System.out.println("Training File Path:" + trainingFilePath
					+ ":modelFilePath:" + modelFilePath);
			System.out.println("Arg1:" + arg1 + "\narg2:" + arg2);
			// String cmdarr[] = { GlobalVariables.svmPath, arg1, arg2,
			// trainingFilePath, modelFilePath };

			String cmdarr[] = { GlobalVariables.svmPath, arg2, "-c C",
					trainingFilePath, modelFilePath };

			Process process = Runtime.getRuntime().exec(cmdarr);
			// String args[] = new String[4];
			// args[0] = arg1;
			// args[1] = arg2;
			// args[2] = trainingFilePath;
			// args[3] = modelFilePath;
			// svm_train.train(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void buildModel() {
		// System.out.println("In Build Model");

		try {
			String trainingFolderPath = GlobalVariables.trainingFolderPath;
			String modelFolderPath = GlobalVariables.modelFolderPath;
			File dir = new File(trainingFolderPath);
			for (File file : dir.listFiles()) {
				String fileName = file.getName();
				String trainingFileFullPath = trainingFolderPath + "/"
						+ fileName;
				String modelFileFullPath = modelFolderPath + "/" + fileName
						+ "-model";
				File newModelFile = new File(modelFileFullPath);
				newModelFile.createNewFile();
				WeightContents wC = LCM.get(fileName);
				// System.out.println("Entity Name:" + fileName);
				// System.out.println("wC.weightRelated" + wC.weightRelated);
				// System.out.println("wC.weightUnRelated" +
				// wC.weightUnrelated);
				String arg1 = "-w+1 " + wC.weightRelated;
				String arg2 = "-w-1 " + wC.weightUnrelated;
				buildModelForFile(arg1, arg2, trainingFileFullPath,
						modelFileFullPath);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printFile(String currentFileToModel) {
		try {
			BufferedReader bR = new BufferedReader(new FileReader(new File(
					currentFileToModel)));
			String line;
			while ((line = bR.readLine()) != null) {
				System.out.println("Line:" + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void fetchCounts() {

		try {
			File file = new File(GlobalVariables.pathForWeightOfEntities);
			BufferedReader bR = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bR.readLine()) != null) {
				String token[] = line.split(":");
				if (token.length == 3) {
					Integer countRelated = Integer.parseInt(token[1]);
					Integer countUnRelated = Integer.parseInt(token[2]);

					WeightContents wC = new WeightContents();
					Long lcm = getLCM(countRelated, countUnRelated);
					wC.countRelated = countRelated;
					wC.countUnRelated = countUnRelated;

					wC.weightRelated = lcm / countRelated;
					if (countUnRelated == 0) {
						countUnRelated = 1;
					}
					wC.weightUnrelated = lcm / countUnRelated;
					LCM.put(token[0], wC);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// System.out.println("In Main----");
		// System.out.println(":GCD:" + gcd(590, 0));
		// System.out.println(":LCM:" + getLCM(590, 0));

		fetchCounts();
		buildModel();
		getResultBasedOnModel();
		System.out.println("Average Accuracy---:" + averageAccuracy + " %");
	}

}

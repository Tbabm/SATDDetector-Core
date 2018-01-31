package train;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import satd_detector.core.models.Document;
import satd_detector.core.models.SATDStopwordsHandler;
import satd_detector.core.utils.DataReader;
import satd_detector.core.utils.FileUtil;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.SnowballStemmer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Train {
	private static double ratio = 0.1;

	public static void main(String[] args) {
		train(args);
	}

	public static void train(String[] args) {
		Options opts = createOptions();
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cl = null;
		
		String commentFile = null;
		String labelFile = null;
		String projectFile = null;
		String outDir = null;
		
		try {
			cl = parser.parse(opts, args);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		if (cl.getOptions().length < 3) {
			formatter.printHelp("train", opts);
			return;
		}
		if (cl.hasOption("h")) {
			formatter.printHelp("train", opts);
			return;
		}
		commentFile = cl.getOptionValue("comments");
		labelFile = cl.getOptionValue("labels");
		projectFile = cl.getOptionValue("projects");
		if (commentFile == null || labelFile == null || projectFile == null) {
			formatter.printHelp("train", opts);
			return;
		}
		outDir = cl.getOptionValue("out_dir");
		if (outDir == null)
			outDir = "./models/";

		// create output dir
		File outDirFile = new File(outDir);
		if (!outDirFile.isDirectory()) {
			outDirFile.mkdir();
		}

		try {
			buildModels(commentFile, labelFile, projectFile, outDir);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Finish Training!");
		// System.out.println(System.getProperty("user.dir"));
	}

	private static Options createOptions() {
		Options opts = new Options();
		opts.addOption("h", false, "Show help messages.");
		opts.addOption("comments", true, "The file which stores all comments.");
		opts.addOption("labels", true, "The file which stores labels of all comments.");
		opts.addOption("projects", true, "The file which stores projects of all comments.");
		opts.addOption("out_dir", true, "Dir to store trained models.");
		return opts;
	}

	private static void buildModels(String commentFile, String labelFile, String projectFile, String outDir)
			throws Exception {
		List<String> projects = parseProjects(projectFile);
		List<Document> comments = DataReader.readComments(commentFile, labelFile, projectFile);
		for (String pro:projects) {
			Set<String> proForTraining = new HashSet<String>();
			proForTraining.add(pro);
			List<Document> trainDoc = DataReader.selectProject(comments, proForTraining);

			String trainingDataPath = Paths.get(outDir, "trainingDataPath.arff").toString();
			DataReader.outputArffData(trainDoc, trainingDataPath);

			// string to word vector (both for training and testing data)
			StringToWordVector stw = new StringToWordVector(100000);
			stw.setOutputWordCounts(true);
			stw.setIDFTransform(true);
			stw.setTFTransform(true);
			SnowballStemmer stemmer = new SnowballStemmer();
			stw.setStemmer(stemmer);
			stw.setStopwordsHandler(new SATDStopwordsHandler());
			Instances trainSet = DataSource.read(trainingDataPath);
			stw.setInputFormat(trainSet);
			trainSet = Filter.useFilter(trainSet, stw);
			trainSet.setClassIndex(0);

			// attribute selection for training data
			AttributeSelection attSelection = new AttributeSelection();
			Ranker ranker = new Ranker();
			ranker.setNumToSelect((int) (trainSet.numAttributes() * ratio));
			InfoGainAttributeEval ifg = new InfoGainAttributeEval();
			attSelection.setEvaluator(ifg);
			attSelection.setSearch(ranker);
			attSelection.setInputFormat(trainSet);
			trainSet = Filter.useFilter(trainSet, attSelection);

			Classifier classifier = new NaiveBayesMultinomial();
			classifier.buildClassifier(trainSet);
			
			String stwPath = Paths.get(outDir, pro+".stw").toString();
			String asPath = Paths.get(outDir, pro+".as").toString();
			String modelPath = Paths.get(outDir, pro + ".model").toString();
			SerializationHelper.write(stwPath, stw);
			SerializationHelper.write(asPath, attSelection);
			SerializationHelper.write(modelPath, classifier);

			// delete training data
			File trainingDataFile = new File(trainingDataPath);
			if (trainingDataFile.exists() && trainingDataFile.isFile())
				trainingDataFile.delete();
			System.out.println("Finish Saving classifier of " + pro);
		}
	}

	private static List<String> parseProjects(String proFile) {
		List<String> lines = FileUtil.readLinesFromFile(proFile);
		List<String> projects = new ArrayList<String>(new HashSet<String>(lines));

		return projects;
	}
}
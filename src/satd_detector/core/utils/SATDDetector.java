package satd_detector.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import satd_detector.core.models.Dataset;
import satd_detector.core.models.Document;
import satd_detector.core.models.Models;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SATDDetector {
	private Models models;
	private Dataset ds;

	public SATDDetector() {
		models = new Models();
		ds = new Dataset();
	}

	public SATDDetector(String modelDir) {
		models = new Models(modelDir);
		ds = new Dataset();
	}

	public boolean isSATD(String source) {
		// identify specifial tags
		if (containsNSATDTag(source))
			return false;
		if (containsSATDTag(source))
			return true;
		if (!isPossible(source))
			return false;
		if (containsSATDPatterns(source))
			return true;

		return compositeClassifier(source);
	}

	private boolean compositeClassifier(String source) {
		Document doc = new Document(source);
		if (doc.getWords().isEmpty())
			return false;

		Instance ins = createInstance(doc);
		int score = 0;

		try {
			score = getScore(ins);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (score >= 0) {
			return true;
		} else {
			return false;
		}
	}

	private Instance createInstance(Document doc) {
		Instance ins = new DenseInstance(2);
		ins.setDataset(ds.getDataset());
		ins.setValue(0, String.join(" ", doc.getWords()));
		
		return ins;
	}

	private int getScore(Instance ins) throws Exception {
		int score = 0;
		List<String> projects = new ArrayList<String>(models.getProjects());
		for (int i = 0; i < projects.size(); i++) {
			// Models models = Activator.getModels();
			Instance tmp = ins;
			StringToWordVector stw = models.getStw(i);
			AttributeSelection as = models.getAttSel(i);
			stw.input(tmp);
			tmp = stw.output();
			as.input(tmp);
			tmp = as.output();
			
			double cls_score = models.getClass(i).classifyInstance(tmp);
			// System.out.println(projects.get(i));
			// System.out.println("Score " + cls_score);
			if (cls_score == 1.0) {
				score += 1;
			} else {
				score += -1;
			}
		}

		return score;
	}

	public static boolean isPossible(String source) {
		// ignore useless javadoc
		if (isJavadoc(source))
			return false;
		// ignore auto generated comments
		if (isAutoComment(source))
			return false;
		// ignore License comments
		if (isLicenseComment(source))
			return false;

		return true;
	}

	public static boolean isJavadoc(String source) {
		String[] tags = { "todo", "fixme", "xxx" };
		source = source.toLowerCase();
		for (String tag : tags) {
			if (source.contains(tag))
				return false;
		}

		if (isDocComment(source))
			return true;

		return false;
	}

	public static boolean isDocComment(String source) {
		source = source.trim();
		if (source.startsWith("/**") && source.endsWith("*/"))
			return true;
		return false;
	}

	public static boolean isAutoComment(String source) {
		String pattern = ".*auto.*generated.*";
		source = source.toLowerCase();

		return Pattern.matches(pattern, source);
	}

	public static boolean isLicenseComment(String source) {
		String[] tags = { "license", "copyright", "all rights reserved" };
		source = source.toLowerCase();
		for (String tag : tags) {
			if (source.contains(tag))
				return true;
		}

		return false;
	}

	public static boolean containsSATDTag(String source) {
		// improvement: identify those comments with specific tags
		// do not use TODO, because auto-generated comments always start with TODO
		String pattern = ".*(([^N]|^)SATD).*";
		return Pattern.matches(pattern, source);
	}

	public static boolean containsNSATDTag(String source) {
		String[] tags = { "NSATD" };
		for (String tag : tags) {
			if (source.contains(tag)) {
				return true;
			}
		}

		return false;
	}

	public boolean containsSATDPatterns(String source) {
		String pattern = ".*(TODO|FIXME|XXX).*";
		if (Pattern.matches(pattern, source))
			return true;
		source = source.toLowerCase();
		List<String> patterns = models.getSATDPatterns();
		for (String pat : patterns) {
			if (Pattern.matches(pat, source))
				return true;
		}
		
		return false;
	}

	public Dataset getDs() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds = ds;
	}

	public Models getModels() {
		return models;
	}

	public void setModels(Models models) {
		this.models = models;
	}
}

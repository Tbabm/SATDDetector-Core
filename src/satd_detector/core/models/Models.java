package satd_detector.core.models;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import satd_detector.core.utils.FileUtil;
import weka.classifiers.Classifier;
import weka.core.SerializationHelper;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Models {
	private static String[] defaultProjects = { "argouml", "columba-1.4-src", "hibernate-distribution-3.3.2.GA",
			"jEdit-4.2", "jfreechart-1.0.19", "apache-jmeter-2.10", "jruby-1.4.0", "sql12" };
	private Set<String> projects;
	ArrayList<Classifier> classes = new ArrayList<Classifier>();
	ArrayList<StringToWordVector> stws = new ArrayList<StringToWordVector>();
	ArrayList<AttributeSelection> attSels = new ArrayList<AttributeSelection>();
	List<String> SATDPatterns = null;

	public Models() {
		InputStream pattern_io = getClass().getResourceAsStream("/patterns/SATDPatterns.re");
		SATDPatterns = FileUtil.readLinesFromFile(pattern_io);
		projects = new HashSet<String>(Arrays.asList(defaultProjects));
		loadDefaultModels();
	}

	public Models(String modelDir) {
		InputStream pattern_io = getClass().getResourceAsStream("/patterns/SATDPatterns.re");
		SATDPatterns = FileUtil.readLinesFromFile(pattern_io);
		if (modelDir == null) {
			projects = new HashSet<String>(Arrays.asList(defaultProjects));
			loadDefaultModels();
		}
		else {
			projects = parseProjects(modelDir);
			loadModels(modelDir);
		}
	}

	private void loadDefaultModels() {
		for (String pro : projects) {
			try {
				InputStream cls_io = getClass().getResourceAsStream("/models/" + pro + ".model");
				InputStream stw_io = getClass().getResourceAsStream("/models/" + pro + ".stw");
				InputStream as_io = getClass().getResourceAsStream("/models/" + pro + ".as");
				Classifier cls = (Classifier) SerializationHelper.read(cls_io);
				StringToWordVector stw = (StringToWordVector) SerializationHelper.read(stw_io);
				AttributeSelection as = (AttributeSelection) SerializationHelper.read(as_io);
				classes.add(cls);
				stws.add(stw);
				attSels.add(as);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadModels(String modelDir) {
		for (String pro : projects) {
			try {
				String basePath = Paths.get(modelDir, pro).toString();
				Classifier cls = (Classifier) SerializationHelper.read(basePath + ".model");
				StringToWordVector stw = (StringToWordVector) SerializationHelper.read(basePath + ".stw");
				AttributeSelection as = (AttributeSelection) SerializationHelper.read(basePath + ".as");
				classes.add(cls);
				stws.add(stw);
				attSels.add(as);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Set<String> parseProjects(String modelDir) {
		List<String> projects = new ArrayList<String>();
		File modelFile = new File(modelDir);
		File[] fileList = modelFile.listFiles();
		for (File file : fileList) {
			String name = file.getName();
			if (name.endsWith(".model")) {
				String pName = name.substring(0, name.length() - 6);
				projects.add(pName);
			}
		}
		Set<String> projectSet = new HashSet<String>(projects);
		return projectSet;
	}

	public static String[] getDefaultProjects() {
		return defaultProjects;
	}

	public Set<String> getProjects() {
		return projects;
	}

	public ArrayList<Classifier> getClasses() {
		return classes;
	}

	public ArrayList<StringToWordVector> getStws() {
		return stws;
	}

	public ArrayList<AttributeSelection> getAttSels() {
		return attSels;
	}

	public List<String> getSATDPatterns() {
		return SATDPatterns;
	}

	public Classifier getClass(int index) {
		return classes.get(index);
	}

	public StringToWordVector getStw(int index) {
		return stws.get(index);
	}

	public AttributeSelection getAttSel(int index) {
		return attSels.get(index);
	}
}

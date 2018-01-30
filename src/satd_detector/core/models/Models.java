package satd_detector.core.models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import satd_detector.core.utils.FileUtil;

//import org.eclipse.core.runtime.FileLocator;
//import org.eclipse.core.runtime.Path;
//import org.osgi.framework.Bundle;

import weka.classifiers.Classifier;
//import weka.core.PropertyPath.Path;
import weka.core.SerializationHelper;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Models {
	private static String[] projects = { "argouml", "columba-1.4-src", "hibernate-distribution-3.3.2.GA", "jEdit-4.2",
			"jfreechart-1.0.19", "apache-jmeter-2.10", "jruby-1.4.0", "sql12" };
	ArrayList<Classifier> classes = new ArrayList<Classifier>();
	ArrayList<StringToWordVector> stws = new ArrayList<StringToWordVector>();
	ArrayList<AttributeSelection> attSels = new ArrayList<AttributeSelection>();
	List<String> SATDPatterns = null;

	public Models() {
		InputStream pattern_io = getClass().getResourceAsStream("/models/SATDPatterns.re");
		SATDPatterns = FileUtil.readLinesFromFile(pattern_io);

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

//	public Models(Bundle bundle) {
//		// load Emad's patterns
//		SATDPatterns = FileUtil.readLinesFromFile(bundle, "models/SATDPatterns.re");
//
//		for (String pro : projects) {
//			try {
//				InputStream cls_io = FileLocator.openStream(bundle, new Path("models/" + pro + ".model"), false);
//				InputStream stw_io = FileLocator.openStream(bundle, new Path("models/" + pro + ".stw"), false);
//				InputStream as_io = FileLocator.openStream(bundle, new Path("models/" + pro + ".as"), false);
//				Classifier cls = (Classifier) SerializationHelper.read(cls_io);
//				StringToWordVector stw = (StringToWordVector) SerializationHelper.read(stw_io);
//				AttributeSelection as = (AttributeSelection) SerializationHelper.read(as_io);
//				classes.add(cls);
//				stws.add(stw);
//				attSels.add(as);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public static String[] getProjects() {
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

package satd_detector.core.models;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instances;

public class Dataset {
	private Instances tdDataset;

	public Dataset() {
		Attribute text = new Attribute("Text", true);
		ArrayList<String> classVal = new ArrayList<String>();
		classVal.add("negative");
		classVal.add("positive");
		Attribute class_att = new Attribute("class-att", classVal);
		
		ArrayList<Attribute> attrs = new ArrayList<Attribute>();
		attrs.add(text);
		attrs.add(class_att);

		tdDataset = new Instances("TechDebt", attrs, 1);
		tdDataset.setClassIndex(1);
	}

	public Instances getDataset() {
		return tdDataset;
	}
}

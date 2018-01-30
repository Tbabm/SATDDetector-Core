package test;

import satd_detector.core.models.Dataset;
import satd_detector.core.models.Models;
import satd_detector.core.utils.SATDFilter;

public class Test {
	public static void main(String[] args) {
		SATDFilter.setModels(new Models());
		SATDFilter.setDs(new Dataset());

		String comment = "TODO need a global config";
		// String comment = args[1];
		if (SATDFilter.isSATD(comment))
			System.out.println("SATD");
		else
			System.out.println("Not SATD");
	}
}

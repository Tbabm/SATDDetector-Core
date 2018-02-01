package satd_detector.core.test;

import satd_detector.core.models.SATDStopwordsHandler;
import weka.core.stopwords.StopwordsHandler;

public class TestSW {
	public static void main(String[] args) {
		StopwordsHandler satdHandler = new SATDStopwordsHandler();
		String words = "123 456 abc def ghe the haha if for niubi";
		String[] wordList = words.split(" ");
		for (String word : wordList) {
			if (satdHandler.isStopword(word)) {
				System.out.println(word);
			}
		}
	}

}

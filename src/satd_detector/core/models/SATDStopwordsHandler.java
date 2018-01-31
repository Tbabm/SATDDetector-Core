package satd_detector.core.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import weka.core.stopwords.StopwordsHandler;

public class SATDStopwordsHandler implements StopwordsHandler, Serializable {
	private static final long serialVersionUID = 524970244560017965L;
	private HashSet<String> satdStopWords;

	public SATDStopwordsHandler() {
		// load stopwords from file
		// InputStream is = this.getClass().getResourceAsStream("/dic/stopwords.txt");
		// List<String> lines = FileUtil.readLinesFromFile(is);
		List<String> lines = Arrays.asList("the", "for");
		satdStopWords = new HashSet<String>(lines);
	}

	public boolean isStopword(String word) {
		return satdStopWords.contains(word);
	}
}

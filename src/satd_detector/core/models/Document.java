package satd_detector.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import satd_detector.core.utils.WordSplit;

public class Document implements Comparable<Document>{
	
	private String content;
	private List<String> words;
	
	private String project,label;
	
	private Map<String,Double> tf;
	private Map<String,Double> tf_idf;
	
	double scoreForRank;
	
	public int compareTo(Document doc){
		if(this.scoreForRank<doc.scoreForRank)
			return -1;
		else if(this.scoreForRank>doc.scoreForRank)
			return 1;
		else return 0;
	}

	// When we create a doc, we preprocess the comment
	public Document(String content){
		this.content = content;		
		this.words = WordSplit.split(content);
		
		//build term frequency
		this.tf = new HashMap<String,Double>();
		for(String word:this.words){
			if(tf.containsKey(word))
				tf.put(word, tf.get(word)+1);
			else tf.put(word, 1.0);
		}
		//normalize tf
		for(Map.Entry<String, Double> entry:this.tf.entrySet()){
			entry.setValue(entry.getValue()*1.0/this.words.size());			
		}
	}

	public List<String> getWords() {
		return words;
	}
	public void setWords(List<String> words) {
		this.words = words;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, Double> getTf() {
		return tf;
	}

	public void setTf(Map<String, Double> tf) {
		this.tf = tf;
	}

	public Map<String, Double> getTf_idf() {
		return tf_idf;
	}

	public void setTf_idf(Map<String, Double> tf_idf) {
		this.tf_idf = tf_idf;
	}

	public double getScoreForRank() {
		return scoreForRank;
	}

	public void setScoreForRank(double scoreForRank) {
		this.scoreForRank = scoreForRank;
	}

}

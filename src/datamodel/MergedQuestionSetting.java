package datamodel;

import java.util.ArrayList;
import java.util.List;

public class MergedQuestionSetting {
	private int mergedQuestionNumber;
	private List<String> mergedQuestionCorrectAnswersList;
	private List<Integer> originalQuestionNumbersList;
	private  int mergedScore;
	private boolean arbitrary;
	
	
	public MergedQuestionSetting() {
		this.mergedQuestionCorrectAnswersList = new ArrayList<String>();
		this.originalQuestionNumbersList = new ArrayList<Integer>();
	}


	public int getMergedQuestionNumber() {
		return mergedQuestionNumber;
	}


	public void setMergedQuestionNumber(int mergedQuestionNumber) {
		this.mergedQuestionNumber = mergedQuestionNumber;
	}


	public int getMergedScore() {
		return mergedScore;
	}


	public void setMergedScore(int mergedScore) {
		this.mergedScore = mergedScore;
	}


	public List<String> getMergedQuestionCorrectAnswersList() {
		return mergedQuestionCorrectAnswersList;
	}


	public List<Integer> getOriginalQuestionNumbersList() {
		return originalQuestionNumbersList;
	}


	public boolean isArbitrary() {
		return arbitrary;
	}


	public void setArbitrary(boolean arbitrary) {
		this.arbitrary = arbitrary;
	}

	
	
	

}

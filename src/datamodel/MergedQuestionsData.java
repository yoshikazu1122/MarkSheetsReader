package datamodel;

import java.util.ArrayList;
import java.util.List;

public class MergedQuestionsData {
	private final int mergedQuestionNumber;
	private List<String> mergedChosenAnswerAboveSpinnerThresholdList;
	private int mergedGottenScore;
	
	public MergedQuestionsData(int mergedQuestionNumber) {
		this.mergedQuestionNumber = mergedQuestionNumber;
		mergedChosenAnswerAboveSpinnerThresholdList = new ArrayList<String>();
	}

	public int getMergedGottenScore() {
		return mergedGottenScore;
	}

	public void setMergedGottenScore(int mergedGottenScore) {
		this.mergedGottenScore = mergedGottenScore;
	}

	public int getMergedQuestionNumber() {
		return mergedQuestionNumber;
	}

	public List<String> getMergedChosenAnswerAboveSpinnerThresholdList() {
		return mergedChosenAnswerAboveSpinnerThresholdList;
	}
	
	
	
	
	

}

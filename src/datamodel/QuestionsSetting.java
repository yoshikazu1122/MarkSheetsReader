package datamodel;

import java.util.ArrayList;
import java.util.List;

public class QuestionsSetting {
	private final int questionNumber;
	private List<String> correctAnswersList;
	private int score;
	private  int mergedQuestionNuber;
	private  boolean merging;
	private  boolean arbitrary;
	
	public QuestionsSetting(int questionNumber, int mergedQuestionNuber, boolean merging, boolean arbitrary) {
		this.questionNumber = questionNumber;
		this.correctAnswersList = new ArrayList<String>();
		this.mergedQuestionNuber = mergedQuestionNuber;
		this.merging = merging;
		this.arbitrary = arbitrary;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public List<String> getCorrectAnswersList() {
		return correctAnswersList;
	}

	public int getScore() {
		return score;
	}

	
	public void setScore(int score) {
		this.score = score;
	}

	public int getMergedQuestionNuber() {
		return mergedQuestionNuber;
	}

	public boolean isMerging() {
		return merging;
	}

	public boolean isArbitrary() {
		return arbitrary;
	}

	public void setCorrectAnswersList(List<String> correctAnswersList) {
		this.correctAnswersList = correctAnswersList;
	}

	public void setMergedQuestionNuber(int mergedQuestionNuber) {
		this.mergedQuestionNuber = mergedQuestionNuber;
	}

	public void setMerging(boolean merging) {
		this.merging = merging;
	}

	public void setArbitrary(boolean arbitrary) {
		this.arbitrary = arbitrary;
	}
	
	



	

	

	
}

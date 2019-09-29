package datamodel;

import java.util.ArrayList;
import java.util.List;

public class QuestionsData {
	private final int questionNumber;
	private List<String> answerChoicesList = new ArrayList<String>();
	private List<Integer> ratiosList = new ArrayList<Integer>();
	private int gottenScore = 0;
	private List<String> choosenAnswerAboveSpinnerThresholdList = new ArrayList<String>();
	
	public QuestionsData(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public List<String> getAnswerChoicesList() {
		return answerChoicesList;
	}

	public List<Integer> getRatiosList() {
		return ratiosList;
	}


	public int getGottenScore() {
		return gottenScore;
	}

	public void setGottenScore(int gottenScore) {
		this.gottenScore = gottenScore;
	}

	public List<String> getChoosenAnswerAboveSpinnerThresholdList() {
		return choosenAnswerAboveSpinnerThresholdList;
	}



	
	
	

	
	
	





}

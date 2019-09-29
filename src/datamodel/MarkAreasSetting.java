package datamodel;


public class MarkAreasSetting {	
	private final int markAreaID;
	private final int questionNumber;
	private  String correctAnswer = "-1";
	private  String answerChoice = "-1";
	
	
	public MarkAreasSetting(int markAreaID, int questionNumber) {
		this.markAreaID = markAreaID;
		this.questionNumber = questionNumber;
	}


	public String getCorrectAnswer() {
		return correctAnswer;
	}


	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}


	public String getAnswerChoice() {
		return answerChoice;
	}


	public void setAnswerChoice(String answerChoice) {
		this.answerChoice = answerChoice;
	}


	public int getMarkAreaID() {
		return markAreaID;
	}


	public int getQuestionNumber() {
		return questionNumber;
	}
	
	
	
	
	
	
	
	
	
	
}

package datamodel;

public class MarkAreasData {
    private static int nextMarkAreaId = 1;
	
    private final int markAreaID;
    private final int questionNumber;
    private final int sumOfMarkArea;
    private final int ratioOfMarkArea;
    private final String answerChoice;
    private final String choosenAnswer;
    
    
    
	public MarkAreasData(int questionNumber, int sumOfMarkArea, int ratioOfMarkArea, String answerChoice, String choosenAnswer) {
		this.markAreaID = nextMarkAreaId++;
		this.questionNumber = questionNumber;
		this.sumOfMarkArea = sumOfMarkArea;
		this.ratioOfMarkArea = ratioOfMarkArea;
		this.answerChoice = answerChoice;
		this.choosenAnswer = choosenAnswer;
	}

	public int getMarkAreaID() {
		return markAreaID;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public int getSumOfMarkArea() {
		return sumOfMarkArea;
	}

	public int getRatioOfMarkArea() {
		return ratioOfMarkArea;
	}

	public String getAnswerChoice() {
		return answerChoice;
	}

	public String getChoosenAnswer() {
		return choosenAnswer;
	}


	
    
    
    
    
}

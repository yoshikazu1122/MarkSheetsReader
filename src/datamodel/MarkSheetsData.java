package datamodel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MarkSheetsData {
	private final File filePath;
	private List<MarkAreasData> markAreaDataArrayList;
	private List<QuestionsData> questionsDatasArrayList;
	private int sumOfScores;
	private List<MergedQuestionsData> mergedQuestionsDatasList;
	
	private double circleX0;
	private double circleY0;
	private double circleX1;
	private double circleY1;
	private double circleX2;
	private double circleY2;
	private double circleX3;
	private double circleY3;
	
	public MarkSheetsData(File filePath, double circleX0, double circleY0, double circleX1, double circleY1,
			double circleX2, double circleY2, double circleX3, double circleY3) {
		this.filePath = filePath;
		this.markAreaDataArrayList = new ArrayList<MarkAreasData>();
		this.questionsDatasArrayList = new ArrayList<QuestionsData>();
		this.mergedQuestionsDatasList = new ArrayList<MergedQuestionsData>();
		this.circleX0 = circleX0;
		this.circleY0 = circleY0;
		this.circleX1 = circleX1;
		this.circleY1 = circleY1;
		this.circleX2 = circleX2;
		this.circleY2 = circleY2;
		this.circleX3 = circleX3;
		this.circleY3 = circleY3;
	}
	public List<MarkAreasData> getMarkAreaDataArrayList() {
		return markAreaDataArrayList;
	}
	public void setMarkAreaDataArrayList(List<MarkAreasData> markAreaDataArrayList) {
		this.markAreaDataArrayList = markAreaDataArrayList;
	}
	
	public List<QuestionsData> getQuestionsDatasArrayList() {
		return questionsDatasArrayList;
	}
	public File getFilePath() {
		return filePath;
	}
	public double getCircleX0() {
		return circleX0;
	}
	public double getCircleY0() {
		return circleY0;
	}
	public double getCircleX1() {
		return circleX1;
	}
	public double getCircleY1() {
		return circleY1;
	}
	public double getCircleX2() {
		return circleX2;
	}
	public double getCircleY2() {
		return circleY2;
	}
	public double getCircleX3() {
		return circleX3;
	}
	public double getCircleY3() {
		return circleY3;
	}
	public int getSumOfScores() {
		return sumOfScores;
	}
	public void setSumOfScores(int sumOfScores) {
		this.sumOfScores = sumOfScores;
	}
	public List<MergedQuestionsData> getMergedQuestionsDatasList() {
		return mergedQuestionsDatasList;
	}
	
	
	
	

}

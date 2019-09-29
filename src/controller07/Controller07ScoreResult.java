package controller07;


import java.util.ArrayList;
import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkSheetsData;
import datamodel.MergedQuestionSetting;
import datamodel.MergedQuestionsData;
import datamodel.QuestionsData;
import datamodel.QuestionsSetting;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;


public class Controller07ScoreResult {
	@FXML private VBox vbox;
	@FXML private AnchorPane anchorPane;
	protected int questionNumberWidth = 55;
	protected int totalScoreWidth = 70;
	protected int filenameWidth = 250;
	protected String CSS = "tableCellClass";
	private List<QuestionsSetting> questionsSettingList =Main.getInstance().getQuestionsSettingList();
	private List<MarkSheetsData> markSheetDataArrayList = Main.getInstance().getMarkSheetDataArrayList();
	private List<MergedQuestionSetting> mergedQuestionSettingsList = Main.getInstance().getMergedQuestionSettingsList();
	private String styleCell = "-fx-border-color:black;-fx-background-color:white;-fx-font-size:20;";
	private String styleHeader = "-fx-border-color:black;-fx-background-color:lightgray;-fx-font-size:20;-fx-font-weight:bold;";
	private String styleAnswer ="-fx-border-color:black;-fx-background-color:#fdd0db;-fx-font-size:20;";
	private String styleBorderLeft ="-fx-border-color:black transparent black black;";
	private String styleBorderCenter ="-fx-border-color:black transparent;";
	private String styleBorderRight ="-fx-border-color:black black black transparent;";


	private int spinnerThreshold = (int)Main.getInstance().getSpinnerThreshold();
	List<HBox> hBoxsList = new ArrayList<HBox>();

	private List<String> correctAnswersList;
	private List<String> scoresList;
	private List<String> questionNumbersHeader;


	@FXML private void initialize() {
		
		//　QuestionDataのデータを作る
		new ScoreResultCreatingQuestionData();
		// メソッドcreateRowLabelsで必要になる答えのリストを作る
		createCorrectAnswersList();
		// メソッドcreateRowLabelsで必要になる配点の点数のヘッダー部分の問題番号のリストを作る
		createListForScoresListAndQuestionNumbersList();

		createRowLabels("答え", "", correctAnswersList, styleAnswer);
		//　結合問題であれば、MergedQuestionSetting。でなければQuestionSettingを使って合計点を出す。
		boolean isHandleMergeQuestionNumerButtonPusshed = Main.getInstance().isHandleMergeQuestionNumerButtonPusshed();
		createRowLabels("配点", scoreSum(isHandleMergeQuestionNumerButtonPusshed), scoresList,styleCell);
		createRowLabels("ファイル名", "得点", questionNumbersHeader,styleHeader);
		//　読みとったマークシートのデータの行を挿入する
		for (int i = 0; i < markSheetDataArrayList.size(); i++) {
			createRowLabels(markSheetDataArrayList.get(i).getFilePath().getName(), caculateTotalScore(i, isHandleMergeQuestionNumerButtonPusshed),createChosenAnswerList(i), styleCell);
		}
		if (!Main.getInstance().isHandleMergeQuestionNumerButtonPusshed()) {
			changeStyleForCorrectAnswerCellForSingleQuestion();
		}else {
			changeStyleForCorrectAnswerCellForMultipleQuestion();
		}

		//　結合問題であれば以下を実行
		if (Main.getInstance().isHandleMergeQuestionNumerButtonPusshed()) {
			//セルのボーダーを変更して結合する
			changeBorderColorForMergedQuestions();
			//連続する問題の最初の数字文字だけにする
			deleteLabelTextAfterFirstConsecutiveNumber();
			// 順不同にチェックの入った問題には、★をつける
			Platform.runLater(() -> createMarkForArbitraryQuestion());

		}
	}

	private void createMarkForArbitraryQuestion() {
		List<Integer> duplicateNumbersList = Main.getInstance().getDuplicateNumberList();
		List<QuestionsSetting> questionsSettingsList = Main.getInstance().getQuestionsSettingList();
		
		for (int r = 0; r < hBoxsList.size(); r++) {
			r = (r==1)? r+1:r;
			HBox hBox = hBoxsList.get(r);
			Label labelAnswer = (Label) hBox.getChildren().get(0);
			Label labelScore = (Label) hBox.getChildren().get(1);
			int count = 0;
			for (int i = 2; i < hBox.getChildren().size(); i++) {
				int duplicateNumber =duplicateNumbersList.get(count++);
				
				boolean isArbitrary = questionsSettingsList.get(i-2).isArbitrary();
				if (isArbitrary) {
					Label label = (Label) hBox.getChildren().get(i);
					Label labelStar = new Label("★");
					labelStar.setTextFill(Color.BLACK);
					double left = labelAnswer.getWidth()+labelScore.getWidth()+label.getWidth()*(i-2)+10;
					double top = labelAnswer.getHeight()*r;
					AnchorPane.setLeftAnchor(labelStar, left-10);
					AnchorPane.setTopAnchor(labelStar, top);
					anchorPane.getChildren().add(labelStar);
				}
				i = duplicateNumber>1? (i+duplicateNumber-1):i;
			}
		}
	}

	private void changeStyleForCorrectAnswerCellForMultipleQuestion() {
		List<MergedQuestionSetting> mergedQuestionSettingsList = Main.getInstance().getMergedQuestionSettingsList();
		boolean judgementForCorrectOrIncorrectAnswer;
		List<Label> labelForChoosenAnswersList = new ArrayList<Label>();
		if (hBoxsList.size()>=3) {
			for (int k = 3; k < hBoxsList.size(); k++) {
				int count = 0;	
				for (int i = 2; i < hBoxsList.get(k).getChildren().size(); i++) {
					labelForChoosenAnswersList.clear();
					judgementForCorrectOrIncorrectAnswer = true;
					int numberOfSame = Main.getInstance().getDuplicateNumberList().get(count);

					int m = i;
					for (int j = 0; j < numberOfSame; j++) {
						List<String> answersList = mergedQuestionSettingsList.get(count).getMergedQuestionCorrectAnswersList();
						Label labelCorrectAnswer =(Label) hBoxsList.get(0).getChildren().get(m+j);
						String correctAnswer = labelCorrectAnswer.getText();

						Label labelChoosenAnswer = (Label) hBoxsList.get(k).getChildren().get(m+j);
						labelForChoosenAnswersList.add(labelChoosenAnswer);
						String  choosenAnswer = labelChoosenAnswer.getText();
						
						System.out.println(answersList+" : choosenAnswer="+choosenAnswer+"  "+answersList.contains(choosenAnswer));
						if (!answersList.contains(choosenAnswer)) {
							judgementForCorrectOrIncorrectAnswer = false;
						}
						if (numberOfSame!=1 && j!=(numberOfSame-1)) {
							i++;
						}
					}
					count++;
					
					if (judgementForCorrectOrIncorrectAnswer) {
						for (int j = 0; j < labelForChoosenAnswersList.size(); j++) {
							Label label = labelForChoosenAnswersList.get(j);
							label.setStyle(label.getStyle()+"-fx-background-color:#fdd0db;");
						}
					}

				}

			}

		}


	}

	private void changeStyleForCorrectAnswerCellForSingleQuestion() {
		if (hBoxsList.size()>=3) {

			for (int k = 3; k < hBoxsList.size(); k++) {

				for (int i = 0; i < hBoxsList.get(k).getChildren().size(); i++) {
					String answer=null;
					if (hBoxsList.size()>2 && i>2) {
						Label labelAnswer =(Label) hBoxsList.get(0).getChildren().get(i);
						answer = labelAnswer.getText();
					}
					Label label = (Label) hBoxsList.get(k).getChildren().get(i);
					if (answer!=null && answer.equals(label.getText()) && !answer.equals("/")) {
						label.setStyle(styleAnswer);
					}

				}

			}

		}

	}

	private void deleteLabelTextAfterFirstConsecutiveNumber() {
		List<Node> list = hBoxsList.get(1).getChildren();
		int count = 0;
		for (int i = 2; i < list.size(); i++) {
			if(i>=2){
				// numberOfSameで何個数字が連続しているかの確認（１問連続も含む）
				int numberOfSame = Main.getInstance().getDuplicateNumberList().get(count++);

				for (int j = 0; j < numberOfSame; j++) {
					Label label = (Label) list.get(i);
					//２連続以上の結合問題に対して、セルのボーダーを透明に変更する。連続の始まりは右ボーダーを透明にする
					if (numberOfSame!=1 && j==0) {
						i++;							
					}else if(numberOfSame!=1 && j==(numberOfSame-1)) {
						label.setText(null);
					}else if (numberOfSame!=1 && j!=0) {
						label.setText(null);
						i++;		
					}
				}

			}
		}





	}

	private void createCorrectAnswersList() {
		correctAnswersList = new ArrayList<String>();
		for (int i = 0; i < questionsSettingList.size(); i++) {
			List<String> getCorrectAnswersList =questionsSettingList.get(i).getCorrectAnswersList();
			String correctAnswer = getCorrectAnswersList.size()>0? getCorrectAnswersList.get(0):"/"; 
			correctAnswersList.add(correctAnswer);
		}
	}

	private void createListForScoresListAndQuestionNumbersList() {
		scoresList = new ArrayList<String>();
		questionNumbersHeader = new ArrayList<String>();
		for (int i = 0; i < questionsSettingList.size(); i++) {
			int score = questionsSettingList.get(i).getScore();
			String scoreString = questionsSettingList.get(i).getScore()>0? String.valueOf(score):"/";
			scoresList.add(scoreString);
			questionNumbersHeader.add(String.valueOf(i+1));
		}

	}

	private void changeBorderColorForMergedQuestions() {
		for (int k = 0; k < hBoxsList.size(); k++) {
			List<Node> list = hBoxsList.get(k).getChildren();
			int count = 0;
			for (int i = 2; i < list.size(); i++) {//問題はi=2から始まる				
				if(i>=2){
					// numberOfSameで何個数字が連続しているかの確認（１問連続も含む）
					int numberOfSame = Main.getInstance().getDuplicateNumberList().get(count++);

					for (int j = 0; j < numberOfSame; j++) {
						Label label = (Label) list.get(i);
						//２連続以上の結合問題に対して、セルのボーダーを透明に変更する。連続の始まりは右ボーダーを透明にする
						if (numberOfSame!=1 && j==0) {
							label.setStyle(label.getStyle()+styleBorderLeft);
							i++;
							//連続の終わりは左ボーダーを透明にする
						}else if (numberOfSame!=1 && j==(numberOfSame-1)) {
							label.setStyle(label.getStyle()+styleBorderRight);
							//連続の中間のセルは左右の枠を透明にする
						}else if (numberOfSame!=1) {
							label.setStyle(label.getStyle()+styleBorderCenter);
							i++;
						}

					}
				}

			}
		}


	}

	private List<String> createChosenAnswerList(int markSheetNumber) {
		List<QuestionsData> questionsDatasList = markSheetDataArrayList.get(markSheetNumber).getQuestionsDatasArrayList();
		List<String> choosenAnswersList = new ArrayList<String>();
		for (int i = 0; i < questionsDatasList.size(); i++) {
			int ratio = questionsDatasList.get(i).getRatiosList().get(0);
			String choosen = ratio>spinnerThreshold? questionsDatasList.get(i).getAnswerChoicesList().get(0):"/";
			choosenAnswersList.add(choosen);
		}		
		return choosenAnswersList;
	}

	private String caculateTotalScore(int markSheetNumber, boolean isHandleMergeQuestionNumerButtonPusshed) {
		int sum = 0;
		if (isHandleMergeQuestionNumerButtonPusshed) {
			List<MergedQuestionsData> mergedQuestionsDatas = markSheetDataArrayList.get(markSheetNumber).getMergedQuestionsDatasList();
			for (int i = 0; i < mergedQuestionsDatas.size(); i++) {
				int score =mergedQuestionsDatas.get(i).getMergedGottenScore()>0? mergedQuestionsDatas.get(i).getMergedGottenScore():0;
				sum = sum + score;
			}

		}else {
			List<QuestionsData> questionsDataList = markSheetDataArrayList.get(markSheetNumber).getQuestionsDatasArrayList();
			for (int i = 0; i < questionsDataList.size(); i++) {
				int score =questionsDataList.get(i).getGottenScore()>0? questionsDataList.get(i).getGottenScore():0;
				sum = sum+score;
			}
		}
		return String.valueOf(sum);
	}

	private void createRowLabels(String fileName, String totalScore, List<String> questionNumbersContents, String style) {
		Label labelFileName = new Label(fileName);
		labelFileName.setPrefWidth(filenameWidth);
		Label labelTotalScore = new Label(totalScore);
		labelTotalScore.setPrefWidth(totalScoreWidth);
		HBox hBox = new HBox(labelFileName,labelTotalScore);
		for (int i = 0; i < questionNumbersContents.size(); i++) {
			Label label = new Label(questionNumbersContents.get(i));
			label.setPrefWidth(questionNumberWidth);
			hBox.getChildren().add(label);
		}
		for (int i = 0; i < hBox.getChildren().size(); i++) {;
		Label label = (Label) hBox.getChildren().get(i);
		label.setAlignment(Pos.CENTER);
		label.setStyle(style);
		}
		vbox.getChildren().add(hBox);
		hBoxsList.add(hBox);
	}

	private String scoreSum(boolean isHandleMergeQuestionNumerButtonPusshed) {
		//　結合問題であるかどうかで処理が変わる。
		int sum = 0;
		if (isHandleMergeQuestionNumerButtonPusshed) {
			for (int i = 0; i <mergedQuestionSettingsList.size() ; i++) {
				int score = mergedQuestionSettingsList.get(i).getMergedScore();
				sum += score;
			}
		}else {
			for (int i = 0; i < questionsSettingList.size(); i++) {
				int score = questionsSettingList.get(i).getScore()>0? questionsSettingList.get(i).getScore():0;
				sum += score;
			}
		}
		return String.valueOf(sum);
	}




	@FXML private void handlePrev() {
		Main.getInstance().prevPage(0);
	}

	@FXML private void handleNext() {
		Main.getInstance().nextPage(0);
	}
}

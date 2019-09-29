package controller04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.security.auth.kerberos.KerberosKey;

import com.springfire.jp.Main;

import datamodel.QuestionsSetting;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ScoreAndMergedQuestionInputMultiple extends ScoreAndMergedQuestionInputMain{
	private AnchorPane anchorPane2;
	private RadioButton radioButtonForMultiple;
	private List<Label> labelToEntryMergeNumberList = new ArrayList<Label>();
	private List<TextField> textFieldsListMergedNumberList = new LinkedList<TextField>();
	private List<TextField> textFieldsForMergedScoreList = new ArrayList<TextField>();
	private String styleForTextField;
	private List<Integer> duplicateNumberList = Main.getInstance().getDuplicateNumberList();
	private List<CheckBox> checkboxsForArbitrarOrderList =new ArrayList<CheckBox>();
	private Integer[] intArray;
	private Label labelForSum;
	private Label labelForWarningBottom;
	private List<Label> labelsListForMultipleAnswer = new ArrayList<Label>();


	protected ScoreAndMergedQuestionInputMultiple(AnchorPane anchorPane2, RadioButton radioButtonForMultiple,
			List<Integer> questionNumbersList, String styleForLabel, String styleForTextField, Button mergeButton, Integer[] intArray, HBox hBoxForSumLabel,
			Label labelForSum, List<Integer> questionNumberHavingCorrectAnswer, Label labelForWarningBottom) {
		super(questionNumbersList, styleForLabel, styleForTextField, hBoxForSumLabel, labelForSum, questionNumberHavingCorrectAnswer);
		this.anchorPane2 = anchorPane2;
		this.radioButtonForMultiple = radioButtonForMultiple;
		this.styleForTextField = styleForTextField;
		this.intArray = intArray;
		this.labelForSum = labelForSum;
		this.labelForWarningBottom =labelForWarningBottom;

		initializeInputForm();
	}

	private void initializeInputForm() {
		
		//　択一問題か結合問題・順不同問題かラジオボタンで選択する
		createRadioButtonToChoseOneOrMergeAnswer();
		//　択一問題か結合問題・順不同問題かラジオボタンで選択する
		createRadioButtonToChoseOneOrMergeAnswer();
		//　「問題番号」「配点」「合計」のラベルを配置する
		super.createLabelTitleForQuestionNumberScoreSum(anchorPane2, 0, super.heightRect);
		//　問題番号のラベルを横列に並べる
		super.createLabelRectangleInColumns(anchorPane2, labelToEntryMergeNumberList);
		//　配点を入力するTextFieldを横列に並べる
		super.createTextFieldToInputScoresOrMergeNumber(anchorPane2, textFieldsListMergedNumberList);
		// 正解のLabelを並べる
		super.createAnswerLabel(anchorPane2, labelsListForMultipleAnswer, 0, super.heightRect);

		//　「結合」のラベルを配置する
		createLabelTitleForMerge();
		//　初期値はDisableとなる
		anchorPane2.setDisable(true);
		// ClipBoardからのコピーを可能にする。EnterキーをTABキーと同じ動きにする。キープレスの監視。
		super.createKeyListenersForSeveralFunctions(textFieldsListMergedNumberList);
		//　答えをもっていないTextFieldはDisableにする
		//	 super.setDisableForNoAnswerNumber(textFieldsListMergedNumberList,false);

		
		//　AnchorPaneの幅の調整
		Platform.runLater(() -> anchorPane2.setPrefWidth(anchorPane2.getWidth()+70));
	}


	public List<TextField> getTextFieldsListMergedNumber() {
		return textFieldsListMergedNumberList;
	}

	public List<TextField> getTextFieldsForMergedScore() {
		return textFieldsForMergedScoreList;
	}

	public List<Integer> getDuplicateNumberList() {
		return duplicateNumberList;
	}
	
	public List<CheckBox> getCheckboxsForArbitrarOrderList() {
		return checkboxsForArbitrarOrderList;
	}


	private void createRadioButtonToChoseOneOrMergeAnswer() {
		radioButtonForMultiple.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					anchorPane2.setDisable(false);
				}else {
					anchorPane2.setDisable(true);
				}
			}
		});
	}

	private void createLabelTitleForMerge() {
		Label labelMergeAndAirbitrayOrder = new Label("結合後の問題番号");
		labelMergeAndAirbitrayOrder.setAlignment(Pos.CENTER_LEFT);
		AnchorPane.setTopAnchor(labelMergeAndAirbitrayOrder, super.firstRectY+super.heightRect*2+5);
		AnchorPane.setLeftAnchor(labelMergeAndAirbitrayOrder, super.firstRectX-super.startXForLabelX);
		anchorPane2.getChildren().addAll(labelMergeAndAirbitrayOrder);
	}

	protected boolean handleMergeQuestionNumerButton() {
		//結合後問題番号にすべての数字が入力されているか確認する
		boolean allTextFieldsAreFilled = confirmAllMergedNumberTextFieldsAreFilledWith();
		if (allTextFieldsAreFilled) {
			//　結合の入力場所で、連続して同じ数字が入力された数のリスト
			createDuplicateNumberList();
			//　結合された問題の配点を入力するTextFieldを配置する
			createMergedTextFiedlsInColumns();
			//　解答の順番をとわない場合は、チェックを入れる
			createCheckBoxForArbitraryOrder();
			super.createTextFieldProperty(anchorPane2,textFieldsForMergedScoreList, labelForSum);
			// ClipBoardからのコピーを可能にする。EnterキーをTABキーと同じ動きにする。キープレスの監視。
			super.createKeyListenersForSeveralFunctions(textFieldsForMergedScoreList);
		}else {
			labelForWarningBottom.setText("結合後問題番号のすべてに問題番号を入力してください。");
		}
		return allTextFieldsAreFilled;
	}

	private boolean confirmAllMergedNumberTextFieldsAreFilledWith() {
		boolean allTextFieldsAreFilled = true;
		
		for (int i = 0; i < textFieldsListMergedNumberList.size(); i++) {
			if (textFieldsListMergedNumberList.get(i).getText().length()==0) {
				allTextFieldsAreFilled=false;
			}
		}
		
		return allTextFieldsAreFilled;
	}

	private void createDuplicateNumberList() {
		duplicateNumberList.clear();
		int count=1;
		for (int i = 1; i < textFieldsListMergedNumberList.size(); i++) {
			if (textFieldsListMergedNumberList.get(i).getText().length()!=0 && textFieldsListMergedNumberList.get(i-1).getText().equals(textFieldsListMergedNumberList.get(i).getText())) {
				count++;
			}else {
				duplicateNumberList.add(count);
				count = 1;
			}			
		}
		duplicateNumberList.add(count);
	}

	private void createMergedTextFiedlsInColumns() {
		for (int i = 0; i < textFieldsForMergedScoreList.size(); i++) {
			TextField textField = textFieldsForMergedScoreList.get(i);
			anchorPane2.getChildren().remove(textField);
		}
		textFieldsForMergedScoreList.clear();
		
		List<QuestionsSetting> questionsSettingList = Main.getInstance().getQuestionsSettingList();
		int sum=0;
		int mergedTextFieldNumberSum =0;
		int count =0;
		
		for (int i = 0; i < duplicateNumberList.size(); i++) {
			//同じ番号が何個ならんでいるか
			int sameNumber = duplicateNumberList.get(i);

			int mergedAnswersListSize =0;
			for (int j = 0; j < sameNumber; j++) {
				int answersListSize = questionsSettingList.get(count++).getCorrectAnswersList().size();
				answersListSize = answersListSize==0? 1:answersListSize;
				mergedAnswersListSize +=  answersListSize-1;
			}
			
			TextField textField = new TextField();
			textFieldsForMergedScoreList.add(textField);
			textField.setStyle(styleForTextField);
			textField.setPrefHeight(super.heightRect);
			textField.setPrefWidth(super.widthRect*sameNumber+widthRect*mergedAnswersListSize*0.5);
			textField.setAlignment(Pos.CENTER_LEFT);
			AnchorPane.setLeftAnchor(textField, super.firstRectX + mergedTextFieldNumberSum * super.nextRightRect+widthRect*sum*0.5);
			AnchorPane.setTopAnchor(textField, super.firstRectY+super.heightRect*3);
			anchorPane2.getChildren().add(textField);
			mergedTextFieldNumberSum += duplicateNumberList.get(i);
			sum += mergedAnswersListSize;
		}
	}
	private void createCheckBoxForArbitraryOrder() {
		for (int i = 0; i < checkboxsForArbitrarOrderList.size(); i++) {
			anchorPane2.getChildren().remove(checkboxsForArbitrarOrderList.get(i));
		}
		anchorPane2.getChildren().removeAll(checkboxsForArbitrarOrderList);
		checkboxsForArbitrarOrderList.clear();
		
		List<QuestionsSetting> questionsSettingList = Main.getInstance().getQuestionsSettingList();
		int sum =0;
		int count =0;
		int mergedTextFieldNumberSum =0;
		int countID = 0;
		
		for (int i = 0; i < duplicateNumberList.size(); i++) {
			int sameNumber = duplicateNumberList.get(i);
			boolean isAnswersSizeMoreThan2 = false;
			for (int j = 0; j < sameNumber; j++) {
				int answersListSize = questionsSettingList.get(count++).getCorrectAnswersList().size();
				isAnswersSizeMoreThan2 = answersListSize>1? true:isAnswersSizeMoreThan2;
				answersListSize = answersListSize==0? 1:answersListSize;
				sum +=  answersListSize-1;
			}
			
			mergedTextFieldNumberSum += duplicateNumberList.get(i);
			if (duplicateNumberList.get(i)>1) {
				CheckBox checkbox = new CheckBox();
				checkbox.setId(String.valueOf(countID));
				checkbox.setDisable(isAnswersSizeMoreThan2);
				checkboxsForArbitrarOrderList.add(checkbox);
				AnchorPane.setLeftAnchor(checkbox,  super.firstRectX + mergedTextFieldNumberSum * super.nextRightRect 
						- super.widthRect+11 +super.widthRect*sum*0.5);
				AnchorPane.setTopAnchor(checkbox, super.firstRectY+super.heightRect*3+7);
				anchorPane2.getChildren().add(checkbox);
				countID++;
			}
			
		}
	}
}

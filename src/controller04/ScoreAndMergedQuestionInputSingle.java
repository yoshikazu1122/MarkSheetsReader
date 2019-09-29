package controller04;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ScoreAndMergedQuestionInputSingle  extends ScoreAndMergedQuestionInputMain{
	private AnchorPane anchorPane1;
	private RadioButton radioButtonForSingle;
	private List<Label> labelsListForSingleQuestionNumber = new ArrayList<Label>();
	private List<TextField> textFieldsList = new LinkedList<TextField>();
	private Label labelForSum;
	private List<Label> labelsListForSingleAnswer = new ArrayList<Label>();

	protected ScoreAndMergedQuestionInputSingle(AnchorPane anchorPane1, RadioButton radioButtonForOne,
			List<Integer> questionNumbersList, String styleForLabel, String styleForTextField, HBox hBoxForSumLabel, List<Integer> questionNumberHavingCorrectAnswer, Label labelForSum) {
		super(questionNumbersList, styleForLabel, styleForTextField, hBoxForSumLabel, labelForSum, questionNumberHavingCorrectAnswer);
		this.anchorPane1 = anchorPane1;
		this.radioButtonForSingle = radioButtonForOne;
		this.labelForSum = labelForSum;
		initializeInputForm();
	}

	public List<TextField> getTextFieldsList() {
		return textFieldsList;
	}

	private void initializeInputForm() {
		//　択一問題か結合問題・順不同問題かラジオボタンで選択する
		createRadioButtonForChoseOneOrMergeAnswer();
		//　「問題番号」「配点」のラベルを配置する
		super.createLabelTitleForQuestionNumberScoreSum(anchorPane1,-super.heightRect,0);
		//　問題番号のラベルを横列に並べる
		super.createLabelRectangleInColumns(anchorPane1, labelsListForSingleQuestionNumber);
		//　配点を入力するTextFieldを横列に並べる
		super.createTextFieldToInputScoresOrMergeNumber(anchorPane1, textFieldsList);
		//　問題番号、配点のLabelとTextFieldの高さの位置を調整する
		adjustLocationOfheightForLabelAndTextField();
		// 正解のLabelを並べる
		super.createAnswerLabel(anchorPane1, labelsListForSingleAnswer, 0, 0);
		//　答えをもっていないTextFieldはDisableにする
		super.setDisableForNoAnswerNumber(textFieldsList,false);
		//　文字入力の制限と、合計点数の表示
		super.createTextFieldProperty(anchorPane1,textFieldsList,labelForSum);
		// ClipBoardからのコピーを可能にする。EnterキーをTABキーと同じ動きにする。キープレスの監視。
		super.createKeyListenersForSeveralFunctions(textFieldsList);
		Platform.runLater(() -> anchorPane1.setPrefWidth(anchorPane1.getWidth()+70));
		
	}

	private void adjustLocationOfheightForLabelAndTextField() {
		for (int i = 0; i < labelsListForSingleQuestionNumber.size(); i++) {
			Label label = labelsListForSingleQuestionNumber.get(i);
			AnchorPane.setTopAnchor(label, AnchorPane.getTopAnchor(label)-super.heightRect);
		}
		for (int i = 0; i < textFieldsList.size(); i++) {
			TextField textField = textFieldsList.get(i);
			AnchorPane.setTopAnchor(textField, AnchorPane.getTopAnchor(textField)-super.heightRect);			
		}
		
	}

	private void createRadioButtonForChoseOneOrMergeAnswer() {
		radioButtonForSingle.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					anchorPane1.setDisable(false);
				}else {
					anchorPane1.setDisable(true);
				}
			}
		});
	}



}




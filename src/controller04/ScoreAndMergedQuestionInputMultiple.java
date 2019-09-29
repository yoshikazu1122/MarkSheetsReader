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
		
		//�@�����肩�������E���s����肩���W�I�{�^���őI������
		createRadioButtonToChoseOneOrMergeAnswer();
		//�@�����肩�������E���s����肩���W�I�{�^���őI������
		createRadioButtonToChoseOneOrMergeAnswer();
		//�@�u���ԍ��v�u�z�_�v�u���v�v�̃��x����z�u����
		super.createLabelTitleForQuestionNumberScoreSum(anchorPane2, 0, super.heightRect);
		//�@���ԍ��̃��x��������ɕ��ׂ�
		super.createLabelRectangleInColumns(anchorPane2, labelToEntryMergeNumberList);
		//�@�z�_����͂���TextField������ɕ��ׂ�
		super.createTextFieldToInputScoresOrMergeNumber(anchorPane2, textFieldsListMergedNumberList);
		// ������Label����ׂ�
		super.createAnswerLabel(anchorPane2, labelsListForMultipleAnswer, 0, super.heightRect);

		//�@�u�����v�̃��x����z�u����
		createLabelTitleForMerge();
		//�@�����l��Disable�ƂȂ�
		anchorPane2.setDisable(true);
		// ClipBoard����̃R�s�[���\�ɂ���BEnter�L�[��TAB�L�[�Ɠ��������ɂ���B�L�[�v���X�̊Ď��B
		super.createKeyListenersForSeveralFunctions(textFieldsListMergedNumberList);
		//�@�����������Ă��Ȃ�TextField��Disable�ɂ���
		//	 super.setDisableForNoAnswerNumber(textFieldsListMergedNumberList,false);

		
		//�@AnchorPane�̕��̒���
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
		Label labelMergeAndAirbitrayOrder = new Label("������̖��ԍ�");
		labelMergeAndAirbitrayOrder.setAlignment(Pos.CENTER_LEFT);
		AnchorPane.setTopAnchor(labelMergeAndAirbitrayOrder, super.firstRectY+super.heightRect*2+5);
		AnchorPane.setLeftAnchor(labelMergeAndAirbitrayOrder, super.firstRectX-super.startXForLabelX);
		anchorPane2.getChildren().addAll(labelMergeAndAirbitrayOrder);
	}

	protected boolean handleMergeQuestionNumerButton() {
		//��������ԍ��ɂ��ׂĂ̐��������͂���Ă��邩�m�F����
		boolean allTextFieldsAreFilled = confirmAllMergedNumberTextFieldsAreFilledWith();
		if (allTextFieldsAreFilled) {
			//�@�����̓��͏ꏊ�ŁA�A�����ē������������͂��ꂽ���̃��X�g
			createDuplicateNumberList();
			//�@�������ꂽ���̔z�_����͂���TextField��z�u����
			createMergedTextFiedlsInColumns();
			//�@�𓚂̏��Ԃ��Ƃ�Ȃ��ꍇ�́A�`�F�b�N������
			createCheckBoxForArbitraryOrder();
			super.createTextFieldProperty(anchorPane2,textFieldsForMergedScoreList, labelForSum);
			// ClipBoard����̃R�s�[���\�ɂ���BEnter�L�[��TAB�L�[�Ɠ��������ɂ���B�L�[�v���X�̊Ď��B
			super.createKeyListenersForSeveralFunctions(textFieldsForMergedScoreList);
		}else {
			labelForWarningBottom.setText("��������ԍ��̂��ׂĂɖ��ԍ�����͂��Ă��������B");
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
			//�����ԍ������Ȃ��ł��邩
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

package controller03;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.springfire.jp.Main;

import datamodel.MarkAreasSetting;
import datamodel.StoreMarkSheetSetting;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class Controller03AnswerSetting {
	@FXML private ListView<File> listViewForSaveFiles;
	@FXML private AnchorPane anchorPane;
	@FXML private TextField textFieldForSaveName;
	@FXML private CheckBox checkBoxForSavingSettingData;
	@FXML private Label labelForDoubleMark;
	private List<Integer> questionNumbersList = Main.getInstance().getQuestionNumbersList();
	private List<Label> labelMarkAreaRectanglesList = new LinkedList<Label>();
	private String styleForLabelMarkArea = "-fx-border-color:black;-fx-border-width:1;";
	private String styleForSelectedLabelMarkArea = "-fx-border-color:blue;-fx-border-width:2;";
	private String styleForDoubleLabelMarkArea = "-fx-border-color:red;-fx-border-width:2;";
	private AnswerSettingOpenFile openFile;
	List<MarkAreasSetting> markAreasSettingsList = Main.getInstance().getMarkAreasSettingList();

	@FXML private void initialize() {
		//�@�}�[�N�G���A�̐ݒ�
		new AnswerSettingInputArea(anchorPane, labelMarkAreaRectanglesList, styleForLabelMarkArea);
		//�@���j���[�̐ݒ�
		new AnswerSettingMenu(labelMarkAreaRectanglesList, styleForLabelMarkArea, styleForSelectedLabelMarkArea ,textFieldForSaveName, styleForDoubleLabelMarkArea);
		// �ۑ��t�@�C��
		openFile = new AnswerSettingOpenFile(listViewForSaveFiles ,labelMarkAreaRectanglesList, styleForLabelMarkArea, styleForSelectedLabelMarkArea);
	}

	@FXML private void handlePrev() {
		Main.getInstance().prevPage(0);
	}

	@FXML private void handleNext() throws IOException {

		for (int i = 0; i < markAreasSettingsList.size(); i++) {
			markAreasSettingsList.get(i).setCorrectAnswer("-1");
		}

		if (textFieldForSaveName.getText()==null || textFieldForSaveName.getText().equals("")) {
			textFieldForSaveName.setStyle("-fx-border-color:red;-fx-border-width:2;-fx-border-radius:2;");
		}else {
			for (int i = 0; i < labelMarkAreaRectanglesList.size(); i++) {
				if (labelMarkAreaRectanglesList.get(i).getStyle().equals(styleForSelectedLabelMarkArea)) {
					String correctAnswer = labelMarkAreaRectanglesList.get(i).getText();
					MarkAreasSetting markAreasSetting = markAreasSettingsList.get(i);
					markAreasSetting.setCorrectAnswer(correctAnswer);
				}
			}

			boolean isThereDoubleMark = findQuestionsNumberMoreThanOneAnswer();
			
			if (!isThereDoubleMark) {
				if (checkBoxForSavingSettingData.isSelected()) {
					StoreMarkSheetSetting.getInstance().storeSettingData(textFieldForSaveName.getText());
				}
				//		Controller04��initialize��MarkAreasSetting��������x�ǂݍ��܂��悤�ɂ��邽�߂ɁA�V����Scene�𐶐�����
				Main.getInstance().setFirstTimeScene3(true);
				Main.getInstance().nextPage(0);
				
			}

		}

	}

	private boolean findQuestionsNumberMoreThanOneAnswer() {

		// ���ԍ��̃��X�g�����iControoler04�ł����X�g������Ă��邪�A�����Ŏg�������B���Ƃ�Controller04�͍폜����j
		List<MarkAreasSetting> markAreasSettingList = Main.getInstance().getMarkAreasSettingList();
		for (int i = 0; i < markAreasSettingList.size(); i++) {
			if ( ! questionNumbersList.contains(markAreasSettingList.get(i).getQuestionNumber()) && markAreasSettingList.get(i).getQuestionNumber()>0) {
				questionNumbersList.add(markAreasSettingList.get(i).getQuestionNumber());
			}
		}

		// questionNumberList�̒������������ɕ��בւ���ibubble sort�j
		int temp=0;
		for (int i = 0; i < questionNumbersList.size()-1; i++) {
			for (int j = i+1; j < questionNumbersList.size(); j++) {
				if (questionNumbersList.get(i)>questionNumbersList.get(j)) {
					temp = questionNumbersList.get(i);
					questionNumbersList.set(i, questionNumbersList.get(j));
					questionNumbersList.set(j, temp);
				}
			}
		}


		//�@Integer�͖��ԍ��A���X�g�̒��g�̓}�[�N�i���o�[
		Map<Integer, List<Integer>> sameNumberMap = new TreeMap<Integer, List<Integer>>();		
		for (int i = 0; i < questionNumbersList.size(); i++) {
			sameNumberMap.put(questionNumbersList.get(i), new ArrayList<Integer>());

			int markAreasSettingListSize = markAreasSettingList.size();//
			for (int j = 0; j < markAreasSettingListSize; j++) {
				MarkAreasSetting markAreasSetting = markAreasSettingList.get(j);
				int markAreaSettingQuestionNumber = markAreasSetting.getQuestionNumber();
				int questionNumber = questionNumbersList.get(i);
				if (questionNumber==markAreaSettingQuestionNumber) {
					sameNumberMap.get(questionNumbersList.get(i)).add(j);
				}
			}

		}

		boolean isThereDoubleMark =false;

		// sameNumberMap������ԍ����Ƃ肾���A���̖��ԍ��̃}�[�N�G���A���X�g���g���ă_�u���}�[�N���݂���
		List<Integer> answersList = new ArrayList<Integer>();
		for(Map.Entry<Integer, List<Integer>> entry : sameNumberMap.entrySet()) {
			answersList.clear();

			int sameNumberListSize= entry.getValue().size();
			if (sameNumberListSize>1) {
				//�@�}�[�N�G���A���X�g�̒��g�Ń}�[�N����Ă�����̂́AanswersList�ɒǉ�����
				for (int i = 0; i < sameNumberListSize; i++) {
					int markAreaNumber = entry.getValue().get(i);
					if (labelMarkAreaRectanglesList.get(markAreaNumber).getStyle().equals(styleForSelectedLabelMarkArea)) {
						answersList.add(markAreaNumber);
					}
				}

				//�@�P�̖��ԍ��ɂQ�ȏ�}�[�N����Ă���}�[�N�G���A�̃X�^�C�����u���[�ɂ���
				int answersListSize= answersList.size();
				if (answersListSize>1) {
					for (int i = 0; i < answersListSize; i++) {
						labelMarkAreaRectanglesList.get(answersList.get(i)).setStyle(styleForDoubleLabelMarkArea);
						//�@�e���ԍ��̑I�����Ƀ_�u���}�[�N�������true�ɂ��A���ɐi�߂Ȃ��悤�ɂ���
						isThereDoubleMark = true;
					}
				}

			}
		}
		
		// �_�u���}�[�N������itrue�j�ꍇ��Label�Ɍx����������
		if (isThereDoubleMark) {
			labelForDoubleMark.setText("�e���ԍ��̑I�����̓����͂Q�ȏ�ݒ肷�邱�Ƃ��ł��܂���B");
		}
		return isThereDoubleMark;
	}

	@FXML private void handleOpenFileButton() throws IOException {
		openFile.handleOpenFileButton();
	}
}

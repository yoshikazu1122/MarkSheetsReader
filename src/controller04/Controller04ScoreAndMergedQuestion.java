package controller04;

import java.util.ArrayList;
import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkAreasSetting;
import datamodel.QuestionsSetting;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class Controller04ScoreAndMergedQuestion {
	@FXML private RadioButton radioButtonForSingle;
	@FXML private AnchorPane anchorPane1;
	@FXML private RadioButton radioButtonForMultiple;
	@FXML private AnchorPane anchorPane2;
	@FXML private Button mergeButton;
	@FXML private HBox hBoxForSumLabel;	
	@FXML private Label labelForSum;
	@FXML private Label labelForWarningBottom;

	private Integer[] intArray;
	private List<Integer> questionNumbersList = Main.getInstance().getQuestionNumbersList();
	private ScoreAndMergedQuestionInputMultiple multiple;
	private ScoreAndMergedQuestionInputSingle single;
	private List<Integer> questionNumberHavingCorrectAnswer;
	private String styleForTextField;
	private String styleForLabel;



	@FXML private void initialize() {
		// TextField��Label��Style
		createStyleSettingForTextFieldAndForLabel();
		// markAreaSetting������ԍ���questionNumbersList�Ɏ擾
		createQuestionNumberList();
		// questionNumberList�̒������������ɕ��בւ���ibubble sort�j
		changeIntoAscendingOrder();
		//�@�����������Ă���QuesitionNumber���擾
		createQuestionNumberHavingCorrectAnswerList();
		//�@QuestionSetting�̃N���X�𐶐�����
		createQuestionSetting();
		
		new ScoreAndMergedQuestionInputMain(questionNumbersList, styleForLabel, styleForTextField, hBoxForSumLabel, labelForSum,questionNumberHavingCorrectAnswer);
		//�@��L�̎q�̃N���X�i����j
		single = new ScoreAndMergedQuestionInputSingle(anchorPane1, radioButtonForSingle,questionNumbersList,
				styleForLabel, styleForTextField,hBoxForSumLabel,questionNumberHavingCorrectAnswer,labelForSum);
		//�@��L�̎q�̃N���X�i�����E���s���j
		multiple = new ScoreAndMergedQuestionInputMultiple(anchorPane2, radioButtonForMultiple,questionNumbersList,
				styleForLabel, styleForTextField, mergeButton, intArray,
				hBoxForSumLabel,labelForSum,questionNumberHavingCorrectAnswer, labelForWarningBottom);
	}

	private void createStyleSettingForTextFieldAndForLabel() {
		styleForTextField =
				"   -fx-font-size: 15;" + 
						"   -fx-padding: 1,1,1,1;" + 
						"   -fx-border-color: black;" + 
						"   -fx-border-width: 1;" + 
						"   -fx-border-radius: 0;" + 
						"   -fx-border: gone;" + 
						"   -fx-background-color: white;" + 
						"   -fx-text-fill: red;"+
						"-fx-prompt-text-fill: gray;";
		styleForLabel =
				"   -fx-font-size: 15;" + 
						"   -fx-padding: 1,1,1,1;" + 
						"   -fx-border-color: black;" + 
						"   -fx-border-width: 1;" + 
						"   -fx-background-color: lightgray;";
	}

	private void createQuestionNumberList() {
		List<MarkAreasSetting> markAreasSettingList = Main.getInstance().getMarkAreasSettingList();
		for (int i = 0; i < markAreasSettingList.size(); i++) {
			if ( ! questionNumbersList.contains(markAreasSettingList.get(i).getQuestionNumber()) && markAreasSettingList.get(i).getQuestionNumber()>0) {
				questionNumbersList.add(markAreasSettingList.get(i).getQuestionNumber());
			}
		}
	}

	private void changeIntoAscendingOrder() {
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
	}

	private void createQuestionNumberHavingCorrectAnswerList() {
		questionNumberHavingCorrectAnswer = new ArrayList<Integer>();
		List<MarkAreasSetting> markAreasSettingsList = Main.getInstance().getMarkAreasSettingList();
		for (int i = 0; i < questionNumbersList.size(); i++) {
			for (int j = 0; j < markAreasSettingsList.size(); j++) {
				if (questionNumbersList.get(i)==markAreasSettingsList.get(j).getQuestionNumber()) {
					if (!markAreasSettingsList.get(j).getCorrectAnswer().equals("-1") && !questionNumberHavingCorrectAnswer.contains(i+1)) {
						questionNumberHavingCorrectAnswer.add((i+1));
					}					
				}
			}
		}		
	}

	@FXML private void handlePrev() {
		Main.getInstance().setHandleMergeQuestionNumerButtonPusshed(false);
		Main.getInstance().prevPage(0);
	}

	@FXML private void handleNext() {
		//�@�߂�{�^���Ŗ߂��Ă����悤�ɁA���g�����Z�b�g���� �������Ń��Z�b�g����Ƃ������������QuestionSetting��������
//		Main.getInstance().getQuestionsSettingList().clear();

		//�@����̃��W�I�{�^�����I������Ă���Ƃ�
		if (radioButtonForSingle.isSelected()) {
			addScoreIntoQuestionsSettingListForSingle();			
			Main.getInstance().setFirstTimeScene4(true);
			Main.getInstance().nextPage(0);
			//�@�����E���s���̃��W�I�{�^�����I������Ă���Ƃ�
		}else if (radioButtonForMultiple.isSelected() && Main.getInstance().isHandleMergeQuestionNumerButtonPusshed()) {
			createQuestionsSettingListForMultiple();
			Main.getInstance().setFirstTimeScene4(true);
			Main.getInstance().nextPage(0);
		}
	}
	
	private void createQuestionSetting() {
		List<QuestionsSetting> questionsSettingsList = Main.getInstance().getQuestionsSettingList();
		questionsSettingsList.clear();
		
		//�@QuestionsSetting�̃C���X�^���X�𐶐�����BcorrectAnswersList�ɂ��Ă͉��Ŏ擾����
		for (int i = 0; i < questionNumbersList.size(); i++) {
			int questionNumber = questionNumbersList.get(i);
			QuestionsSetting questionsSetting = new QuestionsSetting(questionNumber, -1, false, false);
			Main.getInstance().getQuestionsSettingList().add(questionsSetting);
		}
		//�@QuestionsSetting��correctAnswersList���擾����
		for (int r = 0; r < questionsSettingsList.size(); r++) {
			QuestionsSetting questionsSetting = questionsSettingsList.get(r);
			for (int m = 0; m < Main.getInstance().getMarkAreasSettingList().size(); m++) {
				MarkAreasSetting markAreasSetting = Main.getInstance().getMarkAreasSettingList().get(m);
				if(questionsSetting.getQuestionNumber()==markAreasSetting.getQuestionNumber() &&
						!markAreasSetting.getCorrectAnswer().equals("-1")) {
					questionsSetting.getCorrectAnswersList().add(markAreasSetting.getCorrectAnswer());
				}
			}
		}
		
	}

	private void addScoreIntoQuestionsSettingListForSingle() {
		//�@QuestionsSetting�̃C���X�^���X�𐶐�����BcorrectAnswersList�ɂ��Ă͉��Ŏ擾����
		List<QuestionsSetting> questionsSettingsList  = Main.getInstance().getQuestionsSettingList();
		for (int i = 0; i < questionsSettingsList.size(); i++) {
			int score = single.getTextFieldsList().get(i).getText().length()==0? -1:Integer.parseInt(single.getTextFieldsList().get(i).getText());
			questionsSettingsList.get(i).setScore(score);
		}

	}

	private void createQuestionsSettingListForMultiple() {
		// QuestionsSetting�𐶐�����̂ɕK�v�ȂR�̃��X�g�����
		List<Integer> scoresList = new ArrayList<Integer>();
		List<Boolean> mergingsList = new ArrayList<Boolean>(); 
		List<Boolean> arbitrarysList = new ArrayList<Boolean>();
		createDuplicateScoresMergingsArbitrarysList(scoresList,mergingsList,arbitrarysList);

		// QuestionsSetting�̃C���X�^���X�𐶐�����
		for (int i = 0; i < questionNumbersList.size(); i++) {
			int margedQuestionNumber = Integer.valueOf(multiple.getTextFieldsListMergedNumber().get(i).getText());
			int score =scoresList.get(i); 
			boolean merging = mergingsList.get(i);
			boolean arbitrary = arbitrarysList.get(i);
			QuestionsSetting questionsSetting = Main.getInstance().getQuestionsSettingList().get(i);
			questionsSetting.setMergedQuestionNuber(margedQuestionNumber);
			questionsSetting.setMerging(merging);
			questionsSetting.setArbitrary(arbitrary);
			questionsSetting.setScore(score);
		}
		//�@QuestionsSetting��correctAnswersList���擾����
		for (int r = 0; r < Main.getInstance().getQuestionsSettingList().size(); r++) {

			QuestionsSetting questionsSetting = Main.getInstance().getQuestionsSettingList().get(r);
			questionsSetting.getCorrectAnswersList().clear();
			
			for (int m = 0; m < Main.getInstance().getMarkAreasSettingList().size(); m++) {
				MarkAreasSetting markAreasSetting = Main.getInstance().getMarkAreasSettingList().get(m);

				if(questionsSetting.getQuestionNumber()==markAreasSetting.getQuestionNumber() &&
						!markAreasSetting.getCorrectAnswer().equals("-1")) {

					questionsSetting.getCorrectAnswersList().add(markAreasSetting.getCorrectAnswer());

				}
			}
		}
		
		List<QuestionsSetting> settingsList = Main.getInstance().getQuestionsSettingList();

	}

	private void createDuplicateScoresMergingsArbitrarysList(List<Integer> scoresList, List<Boolean> mergingsList,
			List<Boolean> arbitrarysList) {
		//�@scoreList��mergingsList���擾����
		int countDuplicate = 0;
		for (int i = 0; i < multiple.getTextFieldsForMergedScore().size(); i++) {

			int numberOfMergedQuestion = multiple.getDuplicateNumberList().get(countDuplicate++);

			for (int j = 0; j < numberOfMergedQuestion; j++) {
				boolean textIsEmpty =multiple.getTextFieldsForMergedScore().get(i).getText().isEmpty();
				int score = textIsEmpty? 0:Integer.valueOf(multiple.getTextFieldsForMergedScore().get(i).getText());
				scoresList.add(score);

				if (numberOfMergedQuestion>1) {
					mergingsList.add(true);

				}else if(numberOfMergedQuestion==1) {
					mergingsList.add(false);

				}

			}

		}

		//�@arbitrarysList���擾����
		countDuplicate = 0;
		int countForCheckBox = 0;
		for (int i = 0; i < multiple.getDuplicateNumberList().size(); i++) {
			if (multiple.getDuplicateNumberList().get(i)>1) {
				for (int k = 0; k < multiple.getDuplicateNumberList().get(i); k++) {
					boolean arbitrary = multiple.getCheckboxsForArbitrarOrderList().get(countForCheckBox).isSelected();
					arbitrarysList.add(arbitrary);
				}
				countForCheckBox++;
			}else if (multiple.getDuplicateNumberList().get(i)==1) {
				arbitrarysList.add(false);
			}
			
		}

	}
	

	@FXML private void handleMergeQuestionNumerButton() {
		boolean noErrorIsDetected =multiple.handleMergeQuestionNumerButton();
		if (noErrorIsDetected) {
			Main.getInstance().setHandleMergeQuestionNumerButtonPusshed(true);
		}
	}


}



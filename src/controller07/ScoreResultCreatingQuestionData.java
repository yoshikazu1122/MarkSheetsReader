package controller07;

import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkAreasData;
import datamodel.MarkSheetsData;
import datamodel.MergedQuestionSetting;
import datamodel.MergedQuestionsData;
import datamodel.QuestionsData;
import datamodel.QuestionsSetting;

public class ScoreResultCreatingQuestionData {
	private List<MarkSheetsData> markSheetDataArrayList = Main.getInstance().getMarkSheetDataArrayList();
	private List<QuestionsSetting> questionsSettingList = Main.getInstance().getQuestionsSettingList();
	private List<String> answerChoicesList;
	private List<Integer> ratiosList;
	private List<MergedQuestionSetting> mergedQuestionSettingsList;


	public ScoreResultCreatingQuestionData() {
		//�@MegedQuestionSetting�̃C���X�^���X�𐶐����AMegedQuestionSettingsList�ɕۊǂ���B
		if (Main.getInstance().isHandleMergeQuestionNumerButtonPusshed()) {
			createMergedQuestionSetting();
		}

//		System.out.println("----------------------------MergedQuestionSettingList------------------------------");
//		int mergedQuestionSettingsListSize = mergedQuestionSettingsList.size();
//		for (int i = 0; i < mergedQuestionSettingsListSize; i++) {
//			MergedQuestionSetting setting = mergedQuestionSettingsList.get(i);
//			System.out.println(setting.getMergedQuestionNumber()+". ");
//			System.out.println(" getMergedQuestionCorrectAnswersList="+setting.getMergedQuestionCorrectAnswersList());
//			System.out.println(" getOriginalQuestionNumbersList="+setting.getOriginalQuestionNumbersList());
//			System.out.println(" getMergedScore="+setting.getMergedScore());
//			System.out.println(" isArbitrary="+setting.isArbitrary());
//		}
//		System.out.println("------------------------------------------------------------------------------------------");

		// QuestionData�̃C���X�^���X�𐶐�����
		for (int k = 0; k < markSheetDataArrayList.size(); k++) {
			MarkSheetsData markSheetsData = markSheetDataArrayList.get(k);
			createQuestionData(markSheetsData);
		}

		if (Main.getInstance().isHandleMergeQuestionNumerButtonPusshed()) {
			// MegedQuestionsData�̃C���X�^���X�𐶐�����
			createMergedQuestionsData();
		}

	}



	private void createMergedQuestionSetting() {
		mergedQuestionSettingsList = Main.getInstance().getMergedQuestionSettingsList();
		mergedQuestionSettingsList.clear();
		//MergedQuestionSetting�̃C���X�^���X�𐶐����AmergedQuestionSettingsList�ɕۊǂ���
		int count =0;
		for (int i = 0; i < questionsSettingList.size(); i++) {
			int numberOfSame = Main.getInstance().getDuplicateNumberList().get(count++);
			MergedQuestionSetting mergedQuestionSetting = new MergedQuestionSetting();
			mergedQuestionSettingsList.add(mergedQuestionSetting);
			// MergedQuestionSetting�̒���mergedQuestionNumber�AmergedScore�Aarbitrary�A
			int m =i;
			for (int j = 0; j < numberOfSame; j++) {
				if (j==0) {
					//�܂��̓��X�g�ȊO�̈ȉ��O�̏����擾����
					int mergedQuestionNumber = questionsSettingList.get(m).getMergedQuestionNuber();
					System.out.println("B:mergedQuestionNumber="+mergedQuestionNumber);
					int mergedScore = questionsSettingList.get(m).getScore();
					boolean arbitrary = questionsSettingList.get(m).isArbitrary();
					mergedQuestionSetting.setMergedQuestionNumber(mergedQuestionNumber);
					mergedQuestionSetting.setMergedScore(mergedScore);
					mergedQuestionSetting.setArbitrary(arbitrary);
				}
				//���Ƀ��X�g�̏����擾���Ă����B
				int correctAnswersListSize = questionsSettingList.get(m+j).getCorrectAnswersList().size();
				int originalQuestionNumber = questionsSettingList.get(m+j).getQuestionNumber();
				if (correctAnswersListSize>0) {
					String correctAnswer = questionsSettingList.get(m+j).getCorrectAnswersList().get(0);
					mergedQuestionSetting.getMergedQuestionCorrectAnswersList().add(correctAnswer);
					mergedQuestionSetting.getOriginalQuestionNumbersList().add(originalQuestionNumber);
				}else {
					mergedQuestionSetting.getMergedQuestionCorrectAnswersList().add("NoAnswer");
					mergedQuestionSetting.getOriginalQuestionNumbersList().add(originalQuestionNumber);
				}
				//���X�g�̏����擾�����������AquestionsSettingList��i��i�߂�
				if (numberOfSame!=1 && j!=(numberOfSame-1)) {
					i++;
				}
			}
		}
	}

	private void createQuestionData(MarkSheetsData markSheetsData) {
		//�@���o�����}�[�N�V�[�g���烊�X�g�����Ƃ��Ă���@markAreasDatasList.size =�@�R�S�~�R�S���P�P�T�U
		List<MarkAreasData> markAreasDatasList = markSheetsData.getMarkAreaDataArrayList();
		//�@���o�����}�[�N�V�[�g���烊�X�g�����Ƃ��Ă���@questionsDatasList.size = QuestionSettingForSIngle.size
		List<QuestionsData> questionsDatasList = markSheetsData.getQuestionsDatasArrayList();

		//�@QuestionsData�̃C���X�^���X�𐶐�����B�K�v�Ȃ��̂�questionNumber�AanswerChoicesList�AratiosList�AgottenScore�AchosenAnswerAboveSpinnerThresholdList
		for (int i = 0; i < questionsSettingList.size(); i++) {
			int questionNumber = questionsSettingList.get(i).getQuestionNumber();
			QuestionsData questionsData = new QuestionsData(questionNumber);
			// QuestionsData�̒���answerChoicesList�AratiosList�̏����擾���Ă���
			createAnswerChoicesListAndRationsList(questionsData, markAreasDatasList,questionNumber);
			// ratiosList�͐��l���������ɕ��וԂ�BanswerChoicesList�������ɕ��בւ���B�o�u���\�[�g
			sortRatiosListAndAnswerChoicesList(ratiosList, answerChoicesList);
			questionsDatasList.add(questionsData);
		}

		// ���answerChoiceList�͕��בւ������Ă��āAratio���������ɂȂ��ł���̂�
		// answerChoicesList.get(0)�����ƂȂ�A����Ɠ�������v�����gottenScore�ɓ��_���擾����
		for (int r = 0; r < questionsDatasList.size(); r++) {
			int correctAnswersListSize = questionsSettingList.get(r).getCorrectAnswersList().size();

			if (correctAnswersListSize!=0) {
				String highestAnswer = questionsDatasList.get(r).getAnswerChoicesList().get(0);
				String correctAnswer = questionsSettingList.get(r).getCorrectAnswersList().get(0);
				//Spinner��threshold�̒l�ȏ�̂��̂𓾓_�Ƃ���
				int spinnerThreshold = (int) Main.getInstance().getSpinnerThreshold();
				int highestRatio = questionsDatasList.get(r).getRatiosList().get(0);					
				if (highestAnswer.equals(correctAnswer) && highestRatio>spinnerThreshold) {
					int getScore = questionsSettingList.get(r).getScore();
					questionsDatasList.get(r).setGottenScore(getScore);
					questionsDatasList.get(r).getChoosenAnswerAboveSpinnerThresholdList().add(highestAnswer);
				}
			}
		}

		System.out.println("\n--------------- markSheetsData: "+markSheetsData.getFilePath().getName()+"---------------");
		for (int i = 0; i < questionsDatasList.size(); i++) {
			QuestionsData data = questionsDatasList.get(i);
			System.out.println(data.getQuestionNumber()+". ");
			System.out.println("  getAnswerChoicesList="+data.getAnswerChoicesList());
			System.out.println("  getRatiosList="+data.getRatiosList());
			System.out.println("  getGottenScore="+data.getGottenScore());
			System.out.println("  getChoosenAnswerAboveSpinnerThresholdList="+data.getChoosenAnswerAboveSpinnerThresholdList());
		}


	}

	private void createAnswerChoicesListAndRationsList(QuestionsData questionsData, List<MarkAreasData> markAreasDatasList, int questionNumber) {
		answerChoicesList = questionsData.getAnswerChoicesList();
		ratiosList = questionsData.getRatiosList();
		//�@�ǂ݂Ƃ����f�[�^�ƃ}�[�N�V�[�g�̐ݒ�Ƃ��g���Ȃ���I�����ƃ}�[�N�Z�x��QuestionData��answerChocesList��ratiosList�Ɏ擾����
		for (int j = 0; j < markAreasDatasList.size(); j++) {
			MarkAreasData markAreasData = markAreasDatasList.get(j);
			if (questionNumber==markAreasData.getQuestionNumber()) {
				answerChoicesList.add(markAreasData.getAnswerChoice());
				ratiosList.add(markAreasData.getRatioOfMarkArea());
			}
		}
	}

	private void sortRatiosListAndAnswerChoicesList(List<Integer> ratiosList, List<String> answerChoicesList) {
		for (int j = 0; j < ratiosList.size()-1; j++) {
			for (int m = j+1; m < ratiosList.size(); m++) {
				if (ratiosList.get(j)<ratiosList.get(m)) {
					int temp = ratiosList.get(j);
					String temp2 = answerChoicesList.get(j);
					ratiosList.set(j, ratiosList.get(m));
					ratiosList.set(m, temp);
					answerChoicesList.set(j, answerChoicesList.get(m));
					answerChoicesList.set(m,temp2);
				}
			}
		}
	}

	private void createMergedQuestionsData() {
		for (int i = 0; i < markSheetDataArrayList.size(); i++) {
			MarkSheetsData markSheet = markSheetDataArrayList.get(i);

			//�@MarkSheetData����QuestionsDatasList���擾
			List<MergedQuestionsData> mergedQuestionsDatasList = markSheet.getMergedQuestionsDatasList();

			//�@�������ꂽ�������AMergedQuestionsData�̃C���X�^���X�𐶐����AmergedQuestionsDatasList�ɕۊǂ���
			for (int j = 0; j < mergedQuestionSettingsList.size(); j++) {
				int mergedQuestionNumber = mergedQuestionSettingsList.get(j).getMergedQuestionNumber();
				MergedQuestionsData mergedQuestionsData = new MergedQuestionsData(mergedQuestionNumber);
				mergedQuestionsDatasList.add(mergedQuestionsData);
			}

			// questionsDatasArrayList.size�͌��������O�̐��i�U�T�j��������A
			// mergedQuestionCorrectAnswersList,mergedChosenAnswerAboveSpinnerThresholdList�͌������ꂽ�������Ȃ��̂�count���g��
			// numberOfSame�̃��[�v�őO�҂ƌ�҂̃��X�g�ԍ��͍ŏI�I�Ɉ�v����
			int threshold = (int) Main.getInstance().getSpinnerThreshold();
			int count =0;
			List<QuestionsData> questionsDatasArrayList = markSheet.getQuestionsDatasArrayList();
			for (int j = 0; j < questionsDatasArrayList.size(); j++) {

				List<String> mergedQuestionCorrectAnswersList = mergedQuestionSettingsList.get(count).getMergedQuestionCorrectAnswersList();
				List<String> mergedChosenAnswerAboveSpinnerThresholdList =
						mergedQuestionsDatasList.get(count).getMergedChosenAnswerAboveSpinnerThresholdList();
				boolean isArbitrary = mergedQuestionSettingsList.get(count).isArbitrary();

				int m = j;
				//�@numberOfSame�Ō������ꂽ�������A�����Ă��邩�擾����
				int numberOfSame = Main.getInstance().getDuplicateNumberList().get(count);
				for (int k = 0; k < numberOfSame; k++) {
					String chosenAnswerAboveSpinnerThreshold = questionsDatasArrayList.get(m+k).getAnswerChoicesList().get(0);
					int highestRatio = questionsDatasArrayList.get(m+k).getRatiosList().get(0);
					if (highestRatio>threshold) {
						mergedChosenAnswerAboveSpinnerThresholdList.add(chosenAnswerAboveSpinnerThreshold);
					}
					if (numberOfSame!=1 && k!=(numberOfSame-1)) {
						j++;
					}
				}

				//�@�������X�g�Ɖ𓚂��ꂽ���X�g����v����Γ_����������B��ł��Ԉ���Ă���΂O�_�ƂȂ�B
				boolean getScore = true;
				// ���s���iisArbitrary=true�j���I������Ă���Ώ��Ԃ�����Ă��_���ƂȂ�Bfalse�ł���Ώ��Ԃ������Ă��Ȃ���Γ_���ƂȂ�Ȃ��B


				if (isArbitrary) {
					for (int k = 0; k < mergedQuestionCorrectAnswersList.size(); k++) {
						String chosenAnswer="NoChosenAnswer";
						if (mergedChosenAnswerAboveSpinnerThresholdList.size()>1 && k<mergedChosenAnswerAboveSpinnerThresholdList.size()) {
							chosenAnswer = mergedChosenAnswerAboveSpinnerThresholdList.get(k);
						}
						if (!mergedQuestionCorrectAnswersList.contains(chosenAnswer)) {
							getScore = false;
						}
					}
				}else {
					for (int k = 0; k < mergedQuestionCorrectAnswersList.size(); k++) {
						String correctAnswer = mergedQuestionCorrectAnswersList.size()>0?mergedQuestionCorrectAnswersList.get(k):"noCorrectAnswer";
						String chosenAnswer="NoChosenAnswer";
						if (mergedChosenAnswerAboveSpinnerThresholdList.size()>0 && k<(mergedChosenAnswerAboveSpinnerThresholdList.size())) {
							chosenAnswer = mergedChosenAnswerAboveSpinnerThresholdList.get(k);
						}
						if (!correctAnswer.equals(chosenAnswer)) {
							getScore = false;
						}
					}
				}
				if (getScore) {
					int mergedGottenScore = mergedQuestionSettingsList.get(count).getMergedScore();
					mergedQuestionsDatasList.get(count).setMergedGottenScore(mergedGottenScore);
				}
				count++;
			}
			
			System.out.println("\n-----------------------MergedQuestionsData------------------------");
			System.out.println(markSheet.getFilePath().getName());

			int mergedQuestionsDatasListSize = markSheet.getMergedQuestionsDatasList().size();
			for (int j = 0; j < mergedQuestionsDatasListSize; j++) {
				List<MergedQuestionsData> data = markSheet.getMergedQuestionsDatasList();
				System.out.println(data.get(j).getMergedQuestionNumber()+".");
				System.out.println("  MergedChosenAnswerAboveSpinnerThresholdList() = "+data.get(j).getMergedChosenAnswerAboveSpinnerThresholdList());
				System.out.println("  MergedGottenScore = "+data.get(j).getMergedGottenScore());
			}



		}




	}



}








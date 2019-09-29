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
		//　MegedQuestionSettingのインスタンスを生成し、MegedQuestionSettingsListに保管する。
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

		// QuestionDataのインスタンスを生成する
		for (int k = 0; k < markSheetDataArrayList.size(); k++) {
			MarkSheetsData markSheetsData = markSheetDataArrayList.get(k);
			createQuestionData(markSheetsData);
		}

		if (Main.getInstance().isHandleMergeQuestionNumerButtonPusshed()) {
			// MegedQuestionsDataのインスタンスを生成する
			createMergedQuestionsData();
		}

	}



	private void createMergedQuestionSetting() {
		mergedQuestionSettingsList = Main.getInstance().getMergedQuestionSettingsList();
		mergedQuestionSettingsList.clear();
		//MergedQuestionSettingのインスタンスを生成し、mergedQuestionSettingsListに保管する
		int count =0;
		for (int i = 0; i < questionsSettingList.size(); i++) {
			int numberOfSame = Main.getInstance().getDuplicateNumberList().get(count++);
			MergedQuestionSetting mergedQuestionSetting = new MergedQuestionSetting();
			mergedQuestionSettingsList.add(mergedQuestionSetting);
			// MergedQuestionSettingの中のmergedQuestionNumber、mergedScore、arbitrary、
			int m =i;
			for (int j = 0; j < numberOfSame; j++) {
				if (j==0) {
					//まずはリスト以外の以下三つの情報を取得する
					int mergedQuestionNumber = questionsSettingList.get(m).getMergedQuestionNuber();
					System.out.println("B:mergedQuestionNumber="+mergedQuestionNumber);
					int mergedScore = questionsSettingList.get(m).getScore();
					boolean arbitrary = questionsSettingList.get(m).isArbitrary();
					mergedQuestionSetting.setMergedQuestionNumber(mergedQuestionNumber);
					mergedQuestionSetting.setMergedScore(mergedScore);
					mergedQuestionSetting.setArbitrary(arbitrary);
				}
				//次にリストの情報を取得していく。
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
				//リストの情報を取得した分だけ、questionsSettingListのiを進める
				if (numberOfSame!=1 && j!=(numberOfSame-1)) {
					i++;
				}
			}
		}
	}

	private void createQuestionData(MarkSheetsData markSheetsData) {
		//　取り出したマークシートからリストをっとってくる　markAreasDatasList.size =　３４×３４＝１１５６
		List<MarkAreasData> markAreasDatasList = markSheetsData.getMarkAreaDataArrayList();
		//　取り出したマークシートからリストをっとってくる　questionsDatasList.size = QuestionSettingForSIngle.size
		List<QuestionsData> questionsDatasList = markSheetsData.getQuestionsDatasArrayList();

		//　QuestionsDataのインスタンスを生成する。必要なものはquestionNumber、answerChoicesList、ratiosList、gottenScore、chosenAnswerAboveSpinnerThresholdList
		for (int i = 0; i < questionsSettingList.size(); i++) {
			int questionNumber = questionsSettingList.get(i).getQuestionNumber();
			QuestionsData questionsData = new QuestionsData(questionNumber);
			// QuestionsDataの中のanswerChoicesList、ratiosListの情報を取得していく
			createAnswerChoicesListAndRationsList(questionsData, markAreasDatasList,questionNumber);
			// ratiosListは数値が高い順に並べ返る。answerChoicesListも同時に並べ替える。バブルソート
			sortRatiosListAndAnswerChoicesList(ratiosList, answerChoicesList);
			questionsDatasList.add(questionsData);
		}

		// 上でanswerChoiceListは並べ替えをしていて、ratioが高い順にならんでいるので
		// answerChoicesList.get(0)が答となり、これと答えが一致すればgottenScoreに得点を取得する
		for (int r = 0; r < questionsDatasList.size(); r++) {
			int correctAnswersListSize = questionsSettingList.get(r).getCorrectAnswersList().size();

			if (correctAnswersListSize!=0) {
				String highestAnswer = questionsDatasList.get(r).getAnswerChoicesList().get(0);
				String correctAnswer = questionsSettingList.get(r).getCorrectAnswersList().get(0);
				//Spinnerのthresholdの値以上のものを得点とする
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
		//　読みとったデータとマークシートの設定とを使いながら選択肢とマーク濃度をQuestionDataのanswerChocesListとratiosListに取得する
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

			//　MarkSheetDataからQuestionsDatasListを取得
			List<MergedQuestionsData> mergedQuestionsDatasList = markSheet.getMergedQuestionsDatasList();

			//　結合された数だけ、MergedQuestionsDataのインスタンスを生成し、mergedQuestionsDatasListに保管する
			for (int j = 0; j < mergedQuestionSettingsList.size(); j++) {
				int mergedQuestionNumber = mergedQuestionSettingsList.get(j).getMergedQuestionNumber();
				MergedQuestionsData mergedQuestionsData = new MergedQuestionsData(mergedQuestionNumber);
				mergedQuestionsDatasList.add(mergedQuestionsData);
			}

			// questionsDatasArrayList.sizeは結合される前の数（６５）だけあり、
			// mergedQuestionCorrectAnswersList,mergedChosenAnswerAboveSpinnerThresholdListは結合された数しかないのでcountを使う
			// numberOfSameのループで前者と後者のリスト番号は最終的に一致する
			int threshold = (int) Main.getInstance().getSpinnerThreshold();
			int count =0;
			List<QuestionsData> questionsDatasArrayList = markSheet.getQuestionsDatasArrayList();
			for (int j = 0; j < questionsDatasArrayList.size(); j++) {

				List<String> mergedQuestionCorrectAnswersList = mergedQuestionSettingsList.get(count).getMergedQuestionCorrectAnswersList();
				List<String> mergedChosenAnswerAboveSpinnerThresholdList =
						mergedQuestionsDatasList.get(count).getMergedChosenAnswerAboveSpinnerThresholdList();
				boolean isArbitrary = mergedQuestionSettingsList.get(count).isArbitrary();

				int m = j;
				//　numberOfSameで結合された数が何個連続してあるか取得する
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

				//　正解リストと解答されたリストが一致すれば点数を加える。一つでも間違っていれば０点となる。
				boolean getScore = true;
				// 順不同（isArbitrary=true）が選択されていれば順番が違っても点数となる。falseであれば順番もあっていなければ点数とならない。


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








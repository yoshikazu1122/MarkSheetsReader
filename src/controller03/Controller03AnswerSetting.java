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
		//　マークエリアの設定
		new AnswerSettingInputArea(anchorPane, labelMarkAreaRectanglesList, styleForLabelMarkArea);
		//　メニューの設定
		new AnswerSettingMenu(labelMarkAreaRectanglesList, styleForLabelMarkArea, styleForSelectedLabelMarkArea ,textFieldForSaveName, styleForDoubleLabelMarkArea);
		// 保存ファイル
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
				//		Controller04のinitializeでMarkAreasSettingがもう一度読み込まれるようにするために、新しいSceneを生成する
				Main.getInstance().setFirstTimeScene3(true);
				Main.getInstance().nextPage(0);
				
			}

		}

	}

	private boolean findQuestionsNumberMoreThanOneAnswer() {

		// 問題番号のリストを作る（Controoler04でもリストを作ってあるが、ここで使いたい。あとでController04は削除する）
		List<MarkAreasSetting> markAreasSettingList = Main.getInstance().getMarkAreasSettingList();
		for (int i = 0; i < markAreasSettingList.size(); i++) {
			if ( ! questionNumbersList.contains(markAreasSettingList.get(i).getQuestionNumber()) && markAreasSettingList.get(i).getQuestionNumber()>0) {
				questionNumbersList.add(markAreasSettingList.get(i).getQuestionNumber());
			}
		}

		// questionNumberListの中を小さい順に並べ替える（bubble sort）
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


		//　Integerは問題番号、リストの中身はマークナンバー
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

		// sameNumberMapから問題番号をとりだし、その問題番号のマークエリアリストを使ってダブルマークをみつける
		List<Integer> answersList = new ArrayList<Integer>();
		for(Map.Entry<Integer, List<Integer>> entry : sameNumberMap.entrySet()) {
			answersList.clear();

			int sameNumberListSize= entry.getValue().size();
			if (sameNumberListSize>1) {
				//　マークエリアリストの中身でマークされているものは、answersListに追加する
				for (int i = 0; i < sameNumberListSize; i++) {
					int markAreaNumber = entry.getValue().get(i);
					if (labelMarkAreaRectanglesList.get(markAreaNumber).getStyle().equals(styleForSelectedLabelMarkArea)) {
						answersList.add(markAreaNumber);
					}
				}

				//　１つの問題番号に２つ以上マークされているマークエリアのスタイルをブルーにする
				int answersListSize= answersList.size();
				if (answersListSize>1) {
					for (int i = 0; i < answersListSize; i++) {
						labelMarkAreaRectanglesList.get(answersList.get(i)).setStyle(styleForDoubleLabelMarkArea);
						//　各問題番号の選択肢にダブルマークがあればtrueにし、次に進めないようにする
						isThereDoubleMark = true;
					}
				}

			}
		}
		
		// ダブルマークがある（true）場合にLabelに警告文をかく
		if (isThereDoubleMark) {
			labelForDoubleMark.setText("各問題番号の選択肢の答えは２つ以上設定することができません。");
		}
		return isThereDoubleMark;
	}

	@FXML private void handleOpenFileButton() throws IOException {
		openFile.handleOpenFileButton();
	}
}

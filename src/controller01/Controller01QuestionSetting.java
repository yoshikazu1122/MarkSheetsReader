package controller01;


import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkAreasSetting;
import javafx.scene.control.Spinner;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class Controller01QuestionSetting {
	@FXML private Spinner<Integer> spinnerForQuestionNumber;
	@FXML private Button nextButtonForChooseOption;
	@FXML private ListView<File> listViewForSavedFiles;
	@FXML private AnchorPane anchorPane;
	@FXML private ImageView imageViewSettingBackgroundImage;
	
	private QuestionSettingMenu menu;
	private QuestionSettingOpenFile openFile;
	private List<Label> labelMarkAreaRectanglesList = new LinkedList<Label>();
	
	@FXML private void initialize() {
		
		//　マークエリアの設定
		new QuestionNumberInputArea(anchorPane,labelMarkAreaRectanglesList,spinnerForQuestionNumber);
		//　メニューの設定
		menu = new QuestionSettingMenu(anchorPane,imageViewSettingBackgroundImage, labelMarkAreaRectanglesList,spinnerForQuestionNumber);
		// 保存ファイル
		openFile = new QuestionSettingOpenFile(listViewForSavedFiles, labelMarkAreaRectanglesList,imageViewSettingBackgroundImage);
		
	}
	

	@FXML private void handleNext() {
		// 次の画面で戻る画面がおされた場合のために、MarkAreasSettingListの中身を空にしておく
		Main.getInstance().getMarkAreasSettingList().clear();
		
		// inputAreaで入力された問題番号を使ってMarkAreasSettingクラスを生成し、Mainで保管されているMarkAreasSettingListに加える。
		int questionNumberInt; 
		String questionNumber;
		for (int i = 0; i < labelMarkAreaRectanglesList.size(); i++) {
			questionNumber = labelMarkAreaRectanglesList.get(i).getText();
			if (questionNumber==null || questionNumber.equals("")) {
				questionNumberInt = -1;
			}else {
				questionNumberInt = Integer.parseInt(questionNumber);
			}
			MarkAreasSetting markAreasSetting = new MarkAreasSetting((i+1),questionNumberInt);
			Main.getInstance().getMarkAreasSettingList().add(markAreasSetting);
		}
	
		//Controller02のinitializeでMarkAreasSettingがもう一度読み込まれるようにするために、新しいSceneを生成する
		Main.getInstance().setFirstTimeScene1(true);
		Main.getInstance().nextPage(0);
	}
	@FXML private void handleSettingBackgroundImageButton() throws Exception {
		menu.handleSettingBackgroundImageButton();
	}

	@FXML private void handleSettingResetButton() {
		menu.handleSettingResetButton();
	}

	@FXML private void handleFileOpenButton() throws IOException {
		openFile.handleFileOpenButton();
	}


	@FXML private void handleSavedFilesDeleteButton() {
		openFile.handleSavedFileDeleteButton();
	}





}

package controller02;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkAreasSetting;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ListView;

public class Controller02ChoiceSetting {

	@FXML private AnchorPane anchorPane;
	@FXML private TextField textFieldForSaveName;
	@FXML private ListView<File> listViewForSaveFiles;
	
	private List<TextField> textFieldMarkAreaRectanglesList = new LinkedList<TextField>();
	private ChoiceSettingMenu menu;
	private ChoiceSettingOpenFile openFIle;
	
	@FXML private void initialize() {

		
		//　マークエリアの設定
		new ChoiceSettingInputArea(textFieldMarkAreaRectanglesList, anchorPane);
		//　メニューの設定
		menu = new ChoiceSettingMenu(textFieldMarkAreaRectanglesList, anchorPane);
		// 保存ファイル
		openFIle = new ChoiceSettingOpenFile(listViewForSaveFiles, textFieldMarkAreaRectanglesList);

	}
	

	@FXML private void handlePrev() {
		Main.getInstance().prevPage(0);
	}

	@FXML private void handleNext() {
		List<MarkAreasSetting> markAreasSettingsList = Main.getInstance().getMarkAreasSettingList();
		String answerChoice;
		for (int i = 0; i < textFieldMarkAreaRectanglesList.size(); i++) {
			// もし選択肢が未入力のときはnullとはならず、空白になる。getText().equals("")とするとエラーが起きるが、getText().length==0ならキャッチできる。
			//空白のときは、StringPiece[2]でIndexOutOfBoundsExceptionがでる
			if (textFieldMarkAreaRectanglesList.get(i).getText()==null) {
				answerChoice="-1";
			}else if(textFieldMarkAreaRectanglesList.get(i).getText().length()==0) {
				answerChoice="/";
			}else {
				answerChoice = textFieldMarkAreaRectanglesList.get(i).getText();
			}			
			markAreasSettingsList.get(i).setAnswerChoice(answerChoice);
		}
		//Controller03のinitializeでMarkAreasSettingがもう一度読み込まれるようにするために、新しいSceneを生成する
		Main.getInstance().setFirstTimeScene2(true);
		Main.getInstance().nextPage(0);
	}

	@FXML private void handleCopy() {
		menu.handleCopy();
	}

	@FXML private void handleOpenFileButton() throws IOException {
		openFIle.handleOpenFileButton();
	}

}

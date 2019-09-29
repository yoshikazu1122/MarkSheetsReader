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

		
		//�@�}�[�N�G���A�̐ݒ�
		new ChoiceSettingInputArea(textFieldMarkAreaRectanglesList, anchorPane);
		//�@���j���[�̐ݒ�
		menu = new ChoiceSettingMenu(textFieldMarkAreaRectanglesList, anchorPane);
		// �ۑ��t�@�C��
		openFIle = new ChoiceSettingOpenFile(listViewForSaveFiles, textFieldMarkAreaRectanglesList);

	}
	

	@FXML private void handlePrev() {
		Main.getInstance().prevPage(0);
	}

	@FXML private void handleNext() {
		List<MarkAreasSetting> markAreasSettingsList = Main.getInstance().getMarkAreasSettingList();
		String answerChoice;
		for (int i = 0; i < textFieldMarkAreaRectanglesList.size(); i++) {
			// �����I�����������͂̂Ƃ���null�Ƃ͂Ȃ炸�A�󔒂ɂȂ�BgetText().equals("")�Ƃ���ƃG���[���N���邪�AgetText().length==0�Ȃ�L���b�`�ł���B
			//�󔒂̂Ƃ��́AStringPiece[2]��IndexOutOfBoundsException���ł�
			if (textFieldMarkAreaRectanglesList.get(i).getText()==null) {
				answerChoice="-1";
			}else if(textFieldMarkAreaRectanglesList.get(i).getText().length()==0) {
				answerChoice="/";
			}else {
				answerChoice = textFieldMarkAreaRectanglesList.get(i).getText();
			}			
			markAreasSettingsList.get(i).setAnswerChoice(answerChoice);
		}
		//Controller03��initialize��MarkAreasSetting��������x�ǂݍ��܂��悤�ɂ��邽�߂ɁA�V����Scene�𐶐�����
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

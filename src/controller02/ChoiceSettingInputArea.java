package controller02;

import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkAreasSetting;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import setting.MarkSheetSetting;

public class ChoiceSettingInputArea {
	private List<TextField> textFieldsRectMarkAreasList;
	private static List<MarkAreasSetting> markAreasSettingList  = Main.getInstance().getMarkAreasSettingList();
	private AnchorPane anchorPane;
	
	protected ChoiceSettingInputArea(List<TextField> textFieldMarkAreaRectanglesList, AnchorPane anchorPane) {
		this.textFieldsRectMarkAreasList = textFieldMarkAreaRectanglesList;
		this.anchorPane = anchorPane;
		createInputArea();
	}

	private void createInputArea() {
		// TextFieldのためのcss
		String styleForTextField =
				"   -fx-font-size: 15;" + 
				"   -fx-padding: 1,1,1,1;" + 
				"   -fx-border-color: black;" + 
				"   -fx-border-width: 2;" + 
				"   -fx-border-radius: 0;" + 
				"   -fx-border: gone;" + 
				"   -fx-background-color: transparent;" + 
				"   -fx-text-fill: red;"+
				"-fx-prompt-text-fill: gray;";
		// マークエリア縦３４横３４
		int markAreasColumn =34;
		int markAreasRow =34;
		//　マークエリアの幅、高さ、一番左上の座標、隣りまでの距離、一個下までの距離
		double widthRect = MarkSheetSetting.widthRect;
		double heightRect = MarkSheetSetting.heightRect;
		double firstRectX = MarkSheetSetting.firstRectX;
		double firstRectY = MarkSheetSetting.firstRectY;
		double nextRightRect = MarkSheetSetting.nextRightRect;
		double nextBellowRect =MarkSheetSetting.nextBellowRect;		
		
		//　マークエリアを作成　３４×３４
		int count =0;
		for (int y=0;y<markAreasColumn;y++){
			for (int x=0;x<markAreasRow;x++) {
				TextField textFieldMarkAreaRectangle = new TextField();
				textFieldMarkAreaRectangle.setId(String.valueOf(count+1));
				textFieldsRectMarkAreasList.add(textFieldMarkAreaRectangle);
				if (markAreasSettingList.get(count).getQuestionNumber()<0) {
					textFieldMarkAreaRectangle.setDisable(true);
				}else {
					textFieldMarkAreaRectangle.setPromptText(String.valueOf(markAreasSettingList.get(count).getQuestionNumber()));	
					createEnterEquaolsTabKey(textFieldMarkAreaRectangle);
				}
				textFieldMarkAreaRectangle.setStyle(styleForTextField);
				textFieldMarkAreaRectangle.setAlignment(Pos.CENTER);
				textFieldMarkAreaRectangle.setPrefWidth(widthRect);
				textFieldMarkAreaRectangle.setPrefHeight(heightRect);
				AnchorPane.setLeftAnchor(textFieldMarkAreaRectangle, firstRectX + x * nextRightRect);
				AnchorPane.setTopAnchor(textFieldMarkAreaRectangle, firstRectY + y*nextBellowRect);
				anchorPane.getChildren().add(textFieldMarkAreaRectangle);
				count++;
			}
		}
		Platform.runLater(() -> anchorPane.setPrefHeight(anchorPane.getHeight()+20));
	}
	
	private void createEnterEquaolsTabKey(TextField textFieldMarkAreaRectangle) {
		textFieldMarkAreaRectangle.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode()==KeyCode.ENTER || event.getCode()==KeyCode.RIGHT) {
					textFieldMarkAreaRectangle.fireEvent(new KeyEvent(event.getEventType(), "", "", KeyCode.TAB,
					event.isShiftDown(), event.isControlDown(), event.isAltDown(), event.isMetaDown()));
				}else if (event.getCode()==KeyCode.LEFT) {
					textFieldMarkAreaRectangle.fireEvent(new KeyEvent(event.getEventType(), "", "", KeyCode.TAB,
					true, event.isControlDown(), event.isAltDown(), event.isMetaDown()));
				}
			}
		});
	}

}

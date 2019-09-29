package controller03;

import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkAreasSetting;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import setting.MarkSheetSetting;

public class AnswerSettingInputArea {
	private AnchorPane anchorPane;
	private List<Label> labelMarkAreaRectanglesList;
	private List<MarkAreasSetting> markAreasSettingList = Main.getInstance().getMarkAreasSettingList();
	private String styleForLabelMarkArea;

	protected AnswerSettingInputArea(AnchorPane anchorPane, List<Label> labelMarkAreaRectanglesList, String styleForLabelMarkArea) {
		this.anchorPane = anchorPane;
		this.labelMarkAreaRectanglesList = labelMarkAreaRectanglesList;
		this.styleForLabelMarkArea = styleForLabelMarkArea;
		
		createInputArea();
	}
	
	private void createInputArea() {
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
				Label LabelMarkAreaRectangle = new Label();
				labelMarkAreaRectanglesList.add(LabelMarkAreaRectangle);
				if (markAreasSettingList.get(count).getQuestionNumber()<0) {
					LabelMarkAreaRectangle.setDisable(true);
				}else {
					LabelMarkAreaRectangle.setText(markAreasSettingList.get(count).getAnswerChoice());	
					createEnterEquaolsTabKey(LabelMarkAreaRectangle);
				}
				LabelMarkAreaRectangle.setStyle(styleForLabelMarkArea);
				LabelMarkAreaRectangle.setAlignment(Pos.CENTER);
				LabelMarkAreaRectangle.setPrefWidth(widthRect);
				LabelMarkAreaRectangle.setPrefHeight(heightRect);
				AnchorPane.setLeftAnchor(LabelMarkAreaRectangle, firstRectX + x * nextRightRect);
				AnchorPane.setTopAnchor(LabelMarkAreaRectangle, firstRectY + y*nextBellowRect);
				anchorPane.getChildren().add(LabelMarkAreaRectangle);
				count++;
			}
		}
		Platform.runLater(() -> anchorPane.setPrefHeight(anchorPane.getHeight()+20));
	}
	
	private void createEnterEquaolsTabKey(Label LabelMarkAreaRectangle) {
		LabelMarkAreaRectangle.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode()==KeyCode.ENTER || event.getCode()==KeyCode.RIGHT) {
					LabelMarkAreaRectangle.fireEvent(new KeyEvent(event.getEventType(), "", "", KeyCode.TAB,
					event.isShiftDown(), event.isControlDown(), event.isAltDown(), event.isMetaDown()));
				}else if (event.getCode()==KeyCode.LEFT) {
					LabelMarkAreaRectangle.fireEvent(new KeyEvent(event.getEventType(), "", "", KeyCode.TAB,
					true, event.isControlDown(), event.isAltDown(), event.isMetaDown()));
				}
			}
		});
	}

}

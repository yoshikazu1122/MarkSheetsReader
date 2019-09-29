package controller01;

import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import setting.MarkSheetSetting;

public class QuestionNumberInputArea {
	//	private final String FONT_PATH ="./src/resources/MyricaM.TTC";

	private AnchorPane anchorPane;
	private List<Label> labelMarkAreaRectanglesList;
	private Spinner<Integer> spinnerForQuestionNumber;

	protected QuestionNumberInputArea( AnchorPane anchorPane, List<Label> labelMarkAreaRectanglesList, Spinner<Integer> spinnerForQuestionNumber) {
		this.anchorPane = anchorPane;
		this.labelMarkAreaRectanglesList = labelMarkAreaRectanglesList;
		this.spinnerForQuestionNumber = spinnerForQuestionNumber;

		//　マークエリアを設定するGUIを作る
		createMarkSheetSettingView();

		
	}

	private void createMarkSheetSettingView() {
		//　マークエリアの幅、高さ、一番左上の座標、隣りまでの距離、一個下までの距離
		double widthRect = MarkSheetSetting.widthRect;
		double heightRect = MarkSheetSetting.heightRect;
		double firstRectX = MarkSheetSetting.firstRectX;
		double firstRectY = MarkSheetSetting.firstRectY;
		double nextRightRect = MarkSheetSetting.nextRightRect;
		double nextBellowRect = MarkSheetSetting.nextBellowRect;

		//　マークエリアを作成　３４×３４
		for (int y=0;y<34;y++){
			for (int x=0;x<34;x++) {
				Label labelMarkAreaRectangle = new Label("");
				labelMarkAreaRectanglesList.add(labelMarkAreaRectangle);
				labelMarkAreaRectangle.setStyle("-fx-border-color:pink;");
				labelMarkAreaRectangle.setTextFill(Color.RED);
				labelMarkAreaRectangle.setPrefWidth(widthRect);
				labelMarkAreaRectangle.setPrefHeight(heightRect);
				labelMarkAreaRectangle.setAlignment(Pos.CENTER);
				//Fontをパッケージに入れたら起動するのにかなりの時間がかかるようになった。なぜだろう。
				//				try {
				//					labelMarkAreaRectangle.setFont(Font.loadFont(new FileInputStream(FONT_PATH),15));
				//				} catch (FileNotFoundException e) {
				//					System.out.println("Couldn't open the file");
				//				}
				AnchorPane.setLeftAnchor(labelMarkAreaRectangle, firstRectX + x * nextRightRect);
				AnchorPane.setTopAnchor(labelMarkAreaRectangle, firstRectY + y * nextBellowRect);
				anchorPane.getChildren().add(labelMarkAreaRectangle);

				//　マークエリア上をクリックしたとき、問題番号をセットする
				createTextOnMarkAreaPressed(labelMarkAreaRectangle);		
				createDragEventHandlers(labelMarkAreaRectangle);
			}
		}
		Platform.runLater(() -> anchorPane.setPrefHeight(anchorPane.getHeight()+20));
	}

	private void createTextOnMarkAreaPressed(Label labelMarkAreaRectangle) {
		//　マークエリア上をクリックしたとき、問題番号のtextを貼り付ける
		labelMarkAreaRectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton()==MouseButton.PRIMARY) {
					setLabelMarkAreaRectangleText(labelMarkAreaRectangle,MouseButton.PRIMARY);
				}else if (event.getButton()==MouseButton.SECONDARY) {
					setLabelMarkAreaRectangleText(labelMarkAreaRectangle,MouseButton.SECONDARY);
				}
			}
		});
	}

	private void createDragEventHandlers(Label labelMarkAreaRectangle) {
		EventHandler<MouseEvent> drag = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (labelMarkAreaRectangle.getBoundsInParent().contains(event.getX(), event.getY()) && event.getButton().equals(MouseButton.PRIMARY)) {
					setLabelMarkAreaRectangleText(labelMarkAreaRectangle,MouseButton.PRIMARY);
				}else if (labelMarkAreaRectangle.getBoundsInParent().contains(event.getX(), event.getY()) && event.getButton().equals(MouseButton.SECONDARY)) {
					setLabelMarkAreaRectangleText(labelMarkAreaRectangle,MouseButton.SECONDARY);
				}
			}
		};
		anchorPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, drag);
	}
	
	protected void setLabelMarkAreaRectangleText(Label labelMarkAreaRectangle, MouseButton mouseButton) {
		if (mouseButton==MouseButton.PRIMARY) {
			if (spinnerForQuestionNumber.getValue()>99) {
				labelMarkAreaRectangle.setFont(Font.font(MarkSheetSetting.markAreaFontSizeS));
			}else if(spinnerForQuestionNumber.getValue()<=99){
				labelMarkAreaRectangle.setFont(Font.font(MarkSheetSetting.markAreafontSizeM));
			}
			labelMarkAreaRectangle.setText(String.valueOf(spinnerForQuestionNumber.getValue()));
		}else if(mouseButton==MouseButton.SECONDARY) {
			labelMarkAreaRectangle.setText("");
		}
		
	}

}













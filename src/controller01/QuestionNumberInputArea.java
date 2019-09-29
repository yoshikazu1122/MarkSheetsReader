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

		//�@�}�[�N�G���A��ݒ肷��GUI�����
		createMarkSheetSettingView();

		
	}

	private void createMarkSheetSettingView() {
		//�@�}�[�N�G���A�̕��A�����A��ԍ���̍��W�A�ׂ�܂ł̋����A����܂ł̋���
		double widthRect = MarkSheetSetting.widthRect;
		double heightRect = MarkSheetSetting.heightRect;
		double firstRectX = MarkSheetSetting.firstRectX;
		double firstRectY = MarkSheetSetting.firstRectY;
		double nextRightRect = MarkSheetSetting.nextRightRect;
		double nextBellowRect = MarkSheetSetting.nextBellowRect;

		//�@�}�[�N�G���A���쐬�@�R�S�~�R�S
		for (int y=0;y<34;y++){
			for (int x=0;x<34;x++) {
				Label labelMarkAreaRectangle = new Label("");
				labelMarkAreaRectanglesList.add(labelMarkAreaRectangle);
				labelMarkAreaRectangle.setStyle("-fx-border-color:pink;");
				labelMarkAreaRectangle.setTextFill(Color.RED);
				labelMarkAreaRectangle.setPrefWidth(widthRect);
				labelMarkAreaRectangle.setPrefHeight(heightRect);
				labelMarkAreaRectangle.setAlignment(Pos.CENTER);
				//Font���p�b�P�[�W�ɓ��ꂽ��N������̂ɂ��Ȃ�̎��Ԃ�������悤�ɂȂ����B�Ȃ����낤�B
				//				try {
				//					labelMarkAreaRectangle.setFont(Font.loadFont(new FileInputStream(FONT_PATH),15));
				//				} catch (FileNotFoundException e) {
				//					System.out.println("Couldn't open the file");
				//				}
				AnchorPane.setLeftAnchor(labelMarkAreaRectangle, firstRectX + x * nextRightRect);
				AnchorPane.setTopAnchor(labelMarkAreaRectangle, firstRectY + y * nextBellowRect);
				anchorPane.getChildren().add(labelMarkAreaRectangle);

				//�@�}�[�N�G���A����N���b�N�����Ƃ��A���ԍ����Z�b�g����
				createTextOnMarkAreaPressed(labelMarkAreaRectangle);		
				createDragEventHandlers(labelMarkAreaRectangle);
			}
		}
		Platform.runLater(() -> anchorPane.setPrefHeight(anchorPane.getHeight()+20));
	}

	private void createTextOnMarkAreaPressed(Label labelMarkAreaRectangle) {
		//�@�}�[�N�G���A����N���b�N�����Ƃ��A���ԍ���text��\��t����
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













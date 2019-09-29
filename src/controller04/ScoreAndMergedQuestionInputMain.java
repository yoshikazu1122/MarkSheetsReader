package controller04;

import java.security.KeyStore.TrustedCertificateEntry;
import java.util.Arrays;
import java.util.List;

import com.springfire.jp.Main;

import datamodel.QuestionsSetting;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import setting.MarkSheetSetting;

public class ScoreAndMergedQuestionInputMain {
	private List<Integer> questionNumbersList;
	private String styleForLabel;
	private String styleForTextField;
	private int sumForLabel = 0;
	private HBox hBoxForSumLabel;

	protected double widthRect = MarkSheetSetting.widthRect+13;
	protected double heightRect = MarkSheetSetting.heightRect;
	protected double firstRectX = MarkSheetSetting.firstRectX+140;
	protected double firstRectY = MarkSheetSetting.firstRectY+40;
	protected double nextRightRect  = widthRect ;
	protected int startXForLabelX =140;
	protected Label labelForSum;
	protected boolean isCtrlKeyPressed = false;
	private List<Integer> questionNumberHavingCorrectAnswer;

	protected ScoreAndMergedQuestionInputMain(List<Integer> questionNumbersList, String styleForLabel, String styleForTextField,
			HBox hBoxForSumLabel, Label labelForSum, List<Integer> questionNumberHavingCorrectAnswer) {
		this.questionNumbersList = questionNumbersList;
		this.styleForLabel = styleForLabel;
		this.styleForTextField = styleForTextField;
		this.hBoxForSumLabel = hBoxForSumLabel;
		this.labelForSum = labelForSum;
		this.questionNumberHavingCorrectAnswer = questionNumberHavingCorrectAnswer;

	}
	
	protected void createAnswerLabel(AnchorPane anchorPane, List<Label> labelsList, double adjustForSingle, double adjustForMultiple) {
		List<QuestionsSetting> questionsSettingList = Main.getInstance().getQuestionsSettingList();
		int sum = 0;
		for (int i = 0; i < questionNumbersList.size(); i++) {
			List<String> answersList = questionsSettingList.get(i).getCorrectAnswersList();
			int answersListSize = answersList.size();
			Label label = new Label();
			label.setStyle(styleForLabel+"-fx-background-color:#fdd0db;");
			for (int j = 0; j < answersListSize; j++) {
				String answer = answersList.get(j);
				if (j>0) {
					answer = ","+answer;
				}
				label.setText(label.getText()+answer);
			}
			label.setPrefHeight(heightRect);
			answersListSize = answersListSize==0? 1:answersListSize;
			label.setPrefWidth(widthRect+widthRect*(answersListSize-1)*0.5);
			label.setAlignment(Pos.CENTER);
			AnchorPane.setLeftAnchor(label, firstRectX + i * nextRightRect+widthRect*sum*0.5);
			AnchorPane.setTopAnchor(label, firstRectY+adjustForMultiple);
			labelsList.add(label);
			anchorPane.getChildren().add(label);
			sum += answersListSize-1;
		}
		
		
	}

	protected void createLabelTitleForQuestionNumberScoreSum(AnchorPane anchorPane, double adjustForSingle, double adjustForMultiple) {
		//　AnchorPaneの中のラベル「問題番号」「配点」を配置する
		Label labelQuestionNumber = new Label("問題番号");
		labelQuestionNumber.setAlignment(Pos.CENTER_LEFT);
		AnchorPane.setTopAnchor(labelQuestionNumber, firstRectY+5+adjustForSingle);
		AnchorPane.setLeftAnchor(labelQuestionNumber, firstRectX-startXForLabelX);
		
		Label labelAnswer = new Label("正解");
		labelAnswer.setAlignment(Pos.CENTER_LEFT);
		AnchorPane.setTopAnchor(labelAnswer, firstRectY+heightRect*1+5+adjustForSingle);
		AnchorPane.setLeftAnchor(labelAnswer, firstRectX-startXForLabelX);

		Label labelPoint = new Label("配点");
		labelPoint.setAlignment(Pos.CENTER_LEFT);
		AnchorPane.setTopAnchor(labelPoint, firstRectY+heightRect*2+5+adjustForSingle+adjustForMultiple);
		AnchorPane.setLeftAnchor(labelPoint, firstRectX-startXForLabelX);

		anchorPane.getChildren().addAll(labelQuestionNumber,labelAnswer,labelPoint);
	}



	protected void createLabelRectangleInColumns(AnchorPane anchorPane, List<Label> labelsList) {
		//　AnchorPane内に問題番号のラベルを横一列に並べる
		int sum = 0;
		for (int i = 0; i < questionNumbersList.size(); i++) {
			List<String> answersList = Main.getInstance().getQuestionsSettingList().get(i).getCorrectAnswersList();
			int answersListSize = answersList.size()==0? 1:answersList.size();
			
			Label label = new Label();
			labelsList.add(label);
			label.setStyle(styleForLabel);
			label.setPrefHeight(heightRect);
			label.setPrefWidth(widthRect+widthRect*(answersListSize-1)*0.5);
			label.setAlignment(Pos.CENTER);

			label.setText(String.valueOf(questionNumbersList.get(i)));
			AnchorPane.setLeftAnchor(label, firstRectX + i * nextRightRect+widthRect*sum*0.5);
			AnchorPane.setTopAnchor(label, firstRectY);
			anchorPane.getChildren().add(label);
			
			sum += answersListSize-1;
		}

	}

	protected void createTextFieldToInputScoresOrMergeNumber(AnchorPane anchorPane, List<TextField> textFieldsList) {
		int sum = 0;
		for (int i = 0; i < questionNumbersList.size(); i++) {
			List<String> answersList = Main.getInstance().getQuestionsSettingList().get(i).getCorrectAnswersList();
			int answersListSize = answersList.size()==0? 1:answersList.size();
			TextField textField = new TextField();
			textField.setStyle(styleForTextField);
			textField.setPrefHeight(heightRect);
			textField.setPrefWidth(widthRect+widthRect*(answersListSize-1)*0.5);
			textField.setAlignment(Pos.CENTER);
			AnchorPane.setTopAnchor(textField, firstRectY+heightRect*2);
			AnchorPane.setLeftAnchor(textField, firstRectX + i * nextRightRect+widthRect*sum*0.5);
			anchorPane.getChildren().add(textField);
			textFieldsList.add(textField);
			sum += answersListSize-1;
		}
	}

	protected void createTextFieldProperty(AnchorPane anchorPane, List<TextField> textFieldsList, Label labelSumNumber) {
		for (int i = 0; i < textFieldsList.size(); i++) {
			TextField textField = textFieldsList.get(i);
			textField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue!=null && oldValue!=null) {
						sumForLabel = 0;
						if (!newValue.matches("\\d*")) {
							textField.setText(newValue.replaceAll("[^\\d]", ""));
							// 該当すればrepalceAllで新しい値から古い値に差し替えられるため、sumForLabel=0としなければ倍となる。
							sumForLabel = 0;
						}
						for (int i = 0; i < textFieldsList.size(); i++) {
							if (!textFieldsList.get(i).getText().isEmpty()) {
								try {
									sumForLabel += Integer.parseInt(textFieldsList.get(i).getText());
								} catch (NumberFormatException e) {
									System.out.println("Error for input string when Ctrl+V is pressed : "+e);
								}
							}
						}
						labelSumNumber.setText("合計："+String.valueOf(sumForLabel)+"点");
					}
				}
			});
		}
	}


	protected void createKeyListenersForSeveralFunctions(List<TextField> textFieldsList) {
		for (int i = 0; i < textFieldsList.size(); i++) {
			TextField textField = textFieldsList.get(i);
			textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode()==KeyCode.F && isCtrlKeyPressed) {
						final Clipboard cb = Clipboard.getSystemClipboard();
						String[] stringPieces= cb.getString().trim().split("\t");
						int index = textFieldsList.indexOf(textField);
						int end = index+stringPieces.length;
						end = end>textFieldsList.size()? textFieldsList.size():end; 
						int count =0;
						for (int j = index; j < end; j++) {
							if (textFieldsList.get(j).isDisable()) {
								break;
							}
							textFieldsList.get(j).setText(stringPieces[count++]);
						}
					}else	if (event.getCode()==KeyCode.CONTROL) {
						isCtrlKeyPressed=true;
					}else	if (event.getCode()==KeyCode.ENTER) {
						textField.fireEvent(new KeyEvent(event.getEventType(), "", "", KeyCode.TAB,
								event.isShiftDown(), event.isControlDown(), event.isAltDown(), event.isMetaDown()));
					}else if (event.getCode()==KeyCode.ENTER) {
						textField.fireEvent(new KeyEvent(event.getEventType(), "", "", KeyCode.TAB,
								true, event.isControlDown(), event.isAltDown(), event.isMetaDown()));
					}

				}
			});

			textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode()==KeyCode.CONTROL) {
						isCtrlKeyPressed=false;
					}

				}
			});

		}
	}

	protected void setDisableForNoAnswerNumber(List<TextField> textFieldsList,boolean merged) {
//		List<Integer> duplicateNumberList = Main.getInstance().getDuplicateNumberList();

		for (int i = 0; i < textFieldsList.size(); i++) {
			textFieldsList.get(i).setDisable(true);
		}

		int count =0;
		for (int i = 0; i < questionNumberHavingCorrectAnswer.size(); i++) {
//			if (merged) {
//				i += duplicateNumberList.get(count)-1; 
//			}
			textFieldsList.get(count++).setDisable(false);
		}


	}

}

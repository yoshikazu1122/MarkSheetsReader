package controller06;

import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkSheetsData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class Controller06DisplayMarkAreaData {
	@FXML private Label labelForSlider;
	@FXML private Slider sliderForThreshold;
	@FXML private TextArea textArea;
	@FXML private ImageView imagePlus;
	@FXML private ImageView imageMinus;
	
	private List<MarkSheetsData> markSheetDataArrayList = Main.getInstance().getMarkSheetDataArrayList();
	
	@FXML private void initialize() {
		sliderForThreshold.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue!=null) {
					labelForSlider.setText("読取値"+String.valueOf(newValue.intValue()));
				}
			}
		});
		labelForSlider.setText("読取値"+String.valueOf((int)sliderForThreshold.getValue()));
		
		// TextAreaにプラスとマイナスの画像を配置
		imagePlus.setImage(new Image("/controller06/resources/plus.png"));
		imageMinus.setImage(new Image("/controller06/resources/minus.png"));
		clickPlusMinusImage();
		
		//　TextAreaに読み取ったデータを出力する
		textArea.setFont(Font.font("monospace",13));
		for (int i = 0; i < markSheetDataArrayList.size(); i++) {
			createTextAreaContent(markSheetDataArrayList.get(i));
		}
		
	}
	
	private void createTextAreaContent(MarkSheetsData markSheetData) {
		//　ファイル名をセット
		textArea.appendText("ファイル名:"+markSheetData.getFilePath().getName()+"\n");
		//　円の情報をセット
		textArea.appendText("  四隅の円の中心点:　"+"左上("+Math.round(markSheetData.getCircleX0())+", "+Math.round(markSheetData.getCircleY0())
		+")  右上("+Math.round(markSheetData.getCircleX1())+", "+Math.round(markSheetData.getCircleY1())
		+")  左下("+Math.round(markSheetData.getCircleX2())+", "+Math.round(markSheetData.getCircleY2())
		+")  右下("+Math.round(markSheetData.getCircleX3())+", "+Math.round(markSheetData.getCircleY3())
		+")\n");
		
		int  markAreasRow = 34;
		int  markAreasColumn = 34;
		int count = 0;
		textArea.appendText("    ");
		for (int i = 0; i < markAreasRow; i++) {
			if ((i+1)<10) {
				textArea.appendText("[ "+(i+1)+"]");
			}else {
				textArea.appendText("["+(i+1)+"]");
			}

		}
		textArea.appendText("\n");
		for (int y = 0; y < markAreasRow; y++) {
			if (y<10-1) {
				textArea.appendText("[ "+(y+1)+"]");
			}else {
				textArea.appendText("["+(y+1)+"]");
			}
			for (int x = 0; x < markAreasColumn; x++) {
				if (markSheetData.getMarkAreaDataArrayList().get(count).getQuestionNumber()==-1) {
					textArea.appendText("   |");
				}else if (markSheetData.getMarkAreaDataArrayList().get(count).getQuestionNumber()>0) {			
					
					if (markSheetData.getMarkAreaDataArrayList().get(count).getRatioOfMarkArea()<10) {					
						textArea.appendText("   "+markSheetData.getMarkAreaDataArrayList().get(count).getRatioOfMarkArea());
					}else if (markSheetData.getMarkAreaDataArrayList().get(count).getRatioOfMarkArea()<100) {
						textArea.appendText("  "+markSheetData.getMarkAreaDataArrayList().get(count).getRatioOfMarkArea());
					}else if (markSheetData.getMarkAreaDataArrayList().get(count).getRatioOfMarkArea()==100) {
						textArea.appendText(" "+markSheetData.getMarkAreaDataArrayList().get(count).getRatioOfMarkArea());
					}			
				}
				count++;
			}
			textArea.appendText("\n");
		}
		textArea.appendText("----------------------------------------------------------------------------------------------------------------------------------------------\n\n");

	}
	
	private void clickPlusMinusImage() {
		imagePlus.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double fontsize = textArea.getFont().getSize();
				textArea.setFont(Font.font("monospace",fontsize+1));
			}
		});
		
		imageMinus.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				double fontsize = textArea.getFont().getSize();
				textArea.setFont(Font.font("monospace",fontsize-1));
			}
		});
	}
	

	@FXML private void handlePrev() {
		Main.getInstance().prevPage(0);
	}

	@FXML private void handleNext() {
		Main.getInstance().setSpinnerThreshold((int)sliderForThreshold.getValue());
		Main.getInstance().setFirstTimeScene6(true);
		Main.getInstance().nextPage(0);
	}
}

package controller01;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import imageProcessing.ImageProcessingTiltCorrect;;

public class QuestionSettingMenu {
	private AnchorPane anchorPane;
	private ImageView imageViewSettingBackgroundImage;
	private List<Label> labelMarkAreaRectanglesList;
	private Spinner<Integer> spinnerForQuestionNumber;

	protected QuestionSettingMenu(AnchorPane anchorPane, ImageView imageViewSettingBackgroundImage, List<Label> labelMarkAreaRectanglesList, Spinner<Integer> spinnerForQuestionNumber) {
		this.anchorPane = anchorPane;
		this.imageViewSettingBackgroundImage = imageViewSettingBackgroundImage;
		this.labelMarkAreaRectanglesList = labelMarkAreaRectanglesList;
		this.spinnerForQuestionNumber = spinnerForQuestionNumber;
		
		Platform.runLater(() -> createKeyListeners());
	}


	protected void handleSettingBackgroundImageButton() throws Exception {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(anchorPane.getScene().getWindow());
		if (file!=null) {
			ImageProcessingTiltCorrect background = new ImageProcessingTiltCorrect(file,true);
			MatOfByte byteMat = new MatOfByte();
			Imgcodecs.imencode(".bmp", background.getBackgroundMap(), byteMat);
			Image image = new Image(new ByteArrayInputStream(byteMat.toArray()),948,1680,false,true);
			imageViewSettingBackgroundImage.setImage(image);
			AnchorPane.setLeftAnchor(imageViewSettingBackgroundImage, 5.0);
			AnchorPane.setTopAnchor(imageViewSettingBackgroundImage, 0.0);
		}
	}

	protected void handleSettingResetButton() {
		for (int i = 0; i < labelMarkAreaRectanglesList.size(); i++) {
			labelMarkAreaRectanglesList.get(i).setText("");
		}
		// バックグラウンドに貼り付けたマークシートの原稿を削除する
		imageViewSettingBackgroundImage.setImage(null);
	}

	private void createKeyListeners() {
		anchorPane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if ( event.getCode().equals(KeyCode.S)){
					spinnerForQuestionNumber.getValueFactory().setValue(spinnerForQuestionNumber.getValue()+1);
				}else if (event.getCode().equals(KeyCode.D)){
					spinnerForQuestionNumber.getValueFactory().setValue(spinnerForQuestionNumber.getValue()-1);
				}
			}
		});
	}




}





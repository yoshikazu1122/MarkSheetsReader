package controller02;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ChoiceSettingMenu {
	private AnchorPane anchorPane;
	private boolean isShiftKeyPressed = false;
	private boolean isCtrlKeyPressed = false;
	private TextField clickedTextField = null;
	private String[] copyList;
	private List<TextField> textFieldMarkAreaRectanglesList;
	private List<TextField> textFieldsCopyPasteList = new LinkedList<TextField>();
	
	private String styleForTextField =
			"   -fx-font-size: 15;" + 
			"   -fx-padding: 1,1,1,1;" + 
			"   -fx-border-color: black;" + 
			"   -fx-border-width: 2;" + 
			"   -fx-border-radius: 0;" + 
			"   -fx-border: gone;" + 
			"   -fx-background-color: transparent;" + 
			"   -fx-text-fill: red;"+
			"-fx-prompt-text-fill: gray;";
	private String stylePressedForTextField = 
			"   -fx-font-size: 15;" + 
					"   -fx-padding: 1,1,1,1;" + 
					"   -fx-border-color: blue;" + 
					"   -fx-border-width: 2;" + 
					"   -fx-border-radius: 0;" + 
					"   -fx-border: gone;" + 
					"   -fx-background-color: transparent;" + 
					"   -fx-text-fill: red;"+
					"-fx-prompt-text-fill: gray;";


	protected ChoiceSettingMenu(List<TextField> textFieldsRectMarkAreasList, AnchorPane anchorPane) {
		this.anchorPane = anchorPane;
		this.textFieldMarkAreaRectanglesList = textFieldsRectMarkAreasList;
		createClickMarkAreaList();
		Platform.runLater(() -> createKeyLesteners());

	}

	private void createClickMarkAreaList() {
		for (int i = 0; i < textFieldMarkAreaRectanglesList.size(); i++) {
			TextField textFieldRectMarkArea = textFieldMarkAreaRectanglesList.get(i);
					textFieldRectMarkArea.setOnMousePressed(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if (event.getButton().equals(MouseButton.PRIMARY)) {
								if (textFieldsCopyPasteList.contains(textFieldRectMarkArea) && isShiftKeyPressed) {
									textFieldsCopyPasteList.remove(textFieldRectMarkArea);
									textFieldRectMarkArea.setStyle(styleForTextField);
								}else	if(!textFieldsCopyPasteList.contains(textFieldRectMarkArea) && isShiftKeyPressed){
									textFieldsCopyPasteList.add(textFieldRectMarkArea);
									textFieldRectMarkArea.setStyle(stylePressedForTextField);
								}
								clickedTextField = textFieldRectMarkArea;
							}
						}
					});
		}
	}

	protected void createKeyLesteners() {
		anchorPane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.SHIFT)) {
					isShiftKeyPressed = true;
				}else if(event.getCode()==KeyCode.CONTROL){
					isCtrlKeyPressed =true; 
				}else if(event.getCode()==KeyCode.F){
					if (isCtrlKeyPressed) {
						handlePaste();
					}
				}
			}
		});
		anchorPane.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.SHIFT)) {
					isShiftKeyPressed = false;
				}else if(event.getCode()==KeyCode.CONTROL){
					isCtrlKeyPressed =false; 
				}
			}
		});
	}

	protected void handlePaste() {
		if (clickedTextField!=null && copyList!=null) {
			int disableCount = 0;
			for (int i = 0; i <copyList.length+disableCount; i++) {
				String paste = copyList[i-disableCount];
				if (Integer.valueOf(clickedTextField.getId())-1+i>=1156) {
					break;
				}else	if (textFieldMarkAreaRectanglesList.get(Integer.valueOf(clickedTextField.getId())-1+i).isDisable()) {
					disableCount++;
				}else {
					textFieldMarkAreaRectanglesList.get(Integer.valueOf(clickedTextField.getId())-1+i).setText(paste);
				}
			}
		}
	}
	
	protected void handleCopy() {
		copyList = new String[textFieldsCopyPasteList.size()];
		for (int i = 0; i < copyList.length; i++) {
			copyList[i] = textFieldsCopyPasteList.get(i).getText();
		}
		for (int i = 0; i < textFieldsCopyPasteList.size(); i++) {
			textFieldsCopyPasteList.get(i).setStyle(styleForTextField);
		}
		textFieldsCopyPasteList.clear();
		
	}

}







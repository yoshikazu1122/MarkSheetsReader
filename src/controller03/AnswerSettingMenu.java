package controller03;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AnswerSettingMenu {
	private List<Label> labelMarkAreaRectanglesList;
	private String styleForLabelMarkArea;
	private String styleForSelectedLabelMarkArea;
	private TextField textFieldForSaveName;
	private String styleForDoubleLabelMarkArea;
	

	public AnswerSettingMenu(List<Label> labelMarkAreaRectanglesList, String styleForLabelMarkArea, String styleForSelectedLabelMarkArea,
			TextField textFieldForSaveName, String styleForDoubleLabelMarkArea) {
		this.labelMarkAreaRectanglesList = labelMarkAreaRectanglesList;
		this.styleForLabelMarkArea = styleForLabelMarkArea;
		this.styleForSelectedLabelMarkArea = styleForSelectedLabelMarkArea;
		this.textFieldForSaveName = textFieldForSaveName;
		this.styleForDoubleLabelMarkArea = styleForDoubleLabelMarkArea;
		createClickMarkAreaList();
		initializeTextField();
		
	}

	private void initializeTextField() {
		DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
		String localTimeNowTosave = String.valueOf(LocalDate.now().format(formatter)+LocalTime.now().getHour()+LocalTime.now().getMinute()+LocalTime.now().getSecond());
		textFieldForSaveName.setText("marksheet"+localTimeNowTosave);
		textFieldForSaveName.setAlignment(Pos.CENTER_LEFT);
	}

	private void createClickMarkAreaList() {
		for (int i = 0; i < labelMarkAreaRectanglesList.size(); i++) {
			Label labelMarkAreaRectangle = labelMarkAreaRectanglesList.get(i);
			labelMarkAreaRectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getButton().equals(MouseButton.PRIMARY) && labelMarkAreaRectangle.getStyle().equals(styleForLabelMarkArea)) {
						labelMarkAreaRectangle.setStyle(styleForSelectedLabelMarkArea);
					}else if (event.getButton().equals(MouseButton.PRIMARY) &&
							((labelMarkAreaRectangle.getStyle().equals(styleForSelectedLabelMarkArea)||(labelMarkAreaRectangle.getStyle().equals(styleForDoubleLabelMarkArea))))) {
						labelMarkAreaRectangle.setStyle(styleForLabelMarkArea);
					}
				}
			});
		}
	}
	


}

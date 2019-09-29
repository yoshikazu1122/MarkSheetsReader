package controller03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;

public class AnswerSettingOpenFile {
	private ListView<File> listViewForSaveFiles;
	private List<Label> labelMarkAreaRectanglesList;
	private String styleForLabelMarkArea;
	private String styleForSelectedLabelMarkArea;
	

	protected AnswerSettingOpenFile(ListView<File> listViewForSaveFiles, List<Label> labelMarkAreaRectanglesList,
			String styleForLabelMarkArea, String styleForSelectedLabelMarkArea) {
		this.listViewForSaveFiles = listViewForSaveFiles;
		this.labelMarkAreaRectanglesList = labelMarkAreaRectanglesList;
		this.styleForLabelMarkArea = styleForLabelMarkArea;
		this.styleForSelectedLabelMarkArea = styleForSelectedLabelMarkArea;
		
		initializeListViewForSaveFiles();
	}
	
	
	private void initializeListViewForSaveFiles() {
		File file = new File(System.getProperty("user.dir"));
		FilenameFilter fileter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		};
		File[] files = file.listFiles(fileter);
		listViewForSaveFiles.getItems().setAll(files);
		listViewForSaveFiles.setPrefWidth(ListView.USE_COMPUTED_SIZE);
		listViewForSaveFiles.setPrefHeight(ListView.USE_COMPUTED_SIZE);
		listViewForSaveFiles.getSelectionModel().selectFirst();
		listViewForSaveFiles.setCellFactory(param -> {
			ListCell<File> cell = new ListCell<File>() {
				@Override
				protected void updateItem(File item, boolean empty) {
					super.updateItem(item, empty);
					if (item==null || empty) {
						setText(null);
					}else {
						setText(item.getName());
					}
				}
			};
			return cell;
		});
	}
	
	protected void handleOpenFileButton() throws IOException {
		File file = listViewForSaveFiles.getSelectionModel().getSelectedItem();

		if (file==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("エラー");
			alert.setHeaderText("リストのファイル名を選択して下さい。");
			alert.showAndWait();
		}else {
			//一度クリックされたラベルをリセット
			for (int i = 0; i < labelMarkAreaRectanglesList.size(); i++) {
				Label labelMarkAreaRectangle = labelMarkAreaRectanglesList.get(i);
				labelMarkAreaRectangle.setStyle(styleForLabelMarkArea);
			}

			Path path = Paths.get(file.toURI());
			BufferedReader br = Files.newBufferedReader(path);

			String input;
			int count = 0;
			try {
				while((input=br.readLine())!=null) {
					String[] itemPiece = input.split("\t");
					if ( ! labelMarkAreaRectanglesList.get(count).isDisable() && ! itemPiece[3].equals("-1")) {
						labelMarkAreaRectanglesList.get(count).setStyle(styleForSelectedLabelMarkArea);;
					}
					count++;
				}
			} finally {
				if (br!=null) {
					br.close();
				}
			} 			
		}
	}
	
}

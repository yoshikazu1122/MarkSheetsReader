package controller02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChoiceSettingOpenFile {
	private ListView<File> listViewForSaveFiles;
	private List<TextField> textFieldsRectMarkAreasList;

	protected ChoiceSettingOpenFile(ListView<File> listViewForSaveFiles, List<TextField> textFieldsRectMarkAreasList) {
		this.listViewForSaveFiles = listViewForSaveFiles;
		this.textFieldsRectMarkAreasList = textFieldsRectMarkAreasList;

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

			Path path = Paths.get(file.toURI());
			BufferedReader br = Files.newBufferedReader(path);

			String input;
			int count = 0;
			try {
				while((input=br.readLine())!=null) {
					String[] itemPiece = input.split("\t");
					textFieldsRectMarkAreasList.get(count).setText(null);
					if (!textFieldsRectMarkAreasList.get(count).isDisable()) {
						textFieldsRectMarkAreasList.get(count).setText(itemPiece[2]);
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

package controller01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class QuestionSettingOpenFile{
	private ListView<File> listViewForSavedFiles;
	private List<Label> textFieldMarkAreaRectanglesList;
	private ImageView imageViewSettingBackgroundImage;

	protected QuestionSettingOpenFile(ListView<File> listViewForSavedFiles, List<Label> labelMarkAreaRectanglesList, ImageView imageViewSettingBackgroundImage) {
		this.listViewForSavedFiles = listViewForSavedFiles;
		this.textFieldMarkAreaRectanglesList = labelMarkAreaRectanglesList;
		this.imageViewSettingBackgroundImage = imageViewSettingBackgroundImage;

		//ListView�̒��Ƀ}�[�N�V�[�g�ݒ�t�@�C����\������
		initializeListViewForSaveFiles();
	}


	private void initializeListViewForSaveFiles() {
		// ���[�U�[�f�B���N�g���[���J��
		File file = new File(System.getProperty("user.dir"));
		//�@�g���q txt �t�@�C���݂̂��E��
		FilenameFilter fileter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		};
		File[] files = file.listFiles(fileter);
		// ListView�Ɏ擾�����t�@�C�����Z�b�g����
		listViewForSavedFiles.getItems().setAll(files);
		listViewForSavedFiles.setPrefWidth(ListView.USE_COMPUTED_SIZE);
		listViewForSavedFiles.setPrefHeight(ListView.USE_COMPUTED_SIZE);
		listViewForSavedFiles.getSelectionModel().selectFirst();
		//�@ListView�Ƀp�X�ł͂Ȃ��ăt�@�C���𖼂�\��������
		listViewForSavedFiles.setCellFactory(param -> {
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


	protected void handleFileOpenButton() throws IOException {
		File file = listViewForSavedFiles.getSelectionModel().getSelectedItem();

		if (file==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�G���[");
			alert.setHeaderText("���X�g�̃t�@�C������I�����ĉ������B");
			alert.showAndWait();
		}else {
			// ��x�A���x���ƃC���[�W�����Z�b�g����
			for (int i = 0; i < textFieldMarkAreaRectanglesList.size(); i++) {
				textFieldMarkAreaRectanglesList.get(i).setText("");
			}
			imageViewSettingBackgroundImage.setImage(null);

			Path path = Paths.get(file.toURI());
			BufferedReader br = Files.newBufferedReader(path);

			String input;
			int count = 0;
			try {
				while((input=br.readLine())!=null) {
					String[] itemPiece = input.split("\t");
					if (Integer.valueOf(itemPiece[1])>0) {
						textFieldMarkAreaRectanglesList.get(count).setText(itemPiece[1]);
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

	protected void handleSavedFileDeleteButton() {
		File filePath = listViewForSavedFiles.getSelectionModel().getSelectedItem();
		if (filePath!=null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("�ݒ�t�@�C���폜");
			alert.setContentText("�폜����F�@"+filePath.getName());
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get().equals(ButtonType.OK)) {
				deleteSaveData(filePath);
			}
		}
	}

	private void deleteSaveData(File filePath) {
		File deleteFile = new File(filePath.toString());			
		if (deleteFile.delete()) {
			initializeListViewForSaveFiles();
		}
	}

}













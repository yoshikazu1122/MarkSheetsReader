package controller05;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import com.springfire.jp.Main;

import datamodel.MarkAreasSetting;
import datamodel.MarkSheetsData;
import imageProcessing.ImageProcessingFindMarkArea;
import imageProcessing.ImageProcessingTiltCorrect;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class Controller05ImageProcessing {
	@FXML private Label labelFileCount;
	@FXML private ListView<File> listViewImageFiles;
	@FXML private ImageView imageViewOpendFiles;
	
	private ObservableList<File> observableListForOpenFIleListView = FXCollections.observableArrayList();
	private List<MarkSheetsData> markSheetDataArrayList = Main.getInstance().getMarkSheetDataArrayList();
	private List<MarkAreasSetting> markAreaSettingList = Main.getInstance().getMarkAreasSettingList();
	
	@FXML private void initialize() {
		File f1 = new File("C:/Users/kami/Desktop/java/scan/marksheet_01.jpg");
		File f2 = new File("C:/Users/kami/Desktop/java/scan/marksheet_02.jpg");
		File f3 = new File("C:/Users/kami/Desktop/java/scan/marksheet_03.jpg");
		observableListForOpenFIleListView.addAll(f1,f2,f3);
		listViewImageFiles.setItems(observableListForOpenFIleListView);
		
		//　画像を開く(TAB)
		createImageFilesToOpenView();
		
	}

	private void createImageFilesToOpenView() {
		listViewImageFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listViewImageFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {
			@Override
			public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
				ObservableList<File> files = listViewImageFiles.getSelectionModel().getSelectedItems();
				if (newValue!=null && files.get(0)!=null) {
					String PATH = files.get(0).toURI().toString();
					Image image = new Image(PATH,580,700,true,true);
					imageViewOpendFiles.setImage(image);
				}
			}
		});
	}

	@FXML private void handlePrev() {
		Main.getInstance().prevPage(0);
	}

	@FXML private void handleNext() {
		Main.getInstance().setFirstTimeScene5(true);
		Main.getInstance().nextPage(0);
	}

	@FXML private void handleOpenButton() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("ファイルを開：JPEG, PNG, IMG, etc...");
		List<File> choosenFiles = chooser.showOpenMultipleDialog(labelFileCount.getScene().getWindow());
		if (choosenFiles!=null) {
			observableListForOpenFIleListView.addAll(choosenFiles);
		}
		listViewImageFiles.setItems(observableListForOpenFIleListView);
		listViewImageFiles.getSelectionModel().selectFirst();
		labelFileCount.setText("ファイル数："+observableListForOpenFIleListView.size() );
	}

	@FXML private void handleDeleteButton() {
		List<File> deleteLest = listViewImageFiles.getSelectionModel().getSelectedItems();
		if (deleteLest!=null) {
			observableListForOpenFIleListView.removeAll(deleteLest);
		}
		labelFileCount.setText("ファイル数："+observableListForOpenFIleListView.size() );
	}

	@FXML private void handleImageProcessingButton() throws Exception {
		//以前に吐き出した画像を削除する
		String PATH1 = Paths.get(".").toAbsolutePath().normalize().toString()+"\\02DetectedCirclesImages\\";
		File file1 = new File(PATH1);
		File[] files1 = file1.listFiles();
		String PATH2 = Paths.get(".").toAbsolutePath().normalize().toString()+"\\03homographyImages\\";
		File file2 = new File(PATH2);
		File[] files2 = file2.listFiles();
		if (files1!=null && files1.length!=0) {
			for (int i = 0; i < files1.length; i++) {
				System.out.println("ファイル削除(02DetectedCirclesImages)"+files1[i].delete());
			}
		}
		if (files2!=null && files2.length!=0) {
			for (int i = 0; i < files2.length; i++) {
				System.out.println("ファイル削除(03homographyImages)"+files2[i].delete());
			}
		}

		
		// 傾きを修正する
		processingTiltCorrect();
		//　マークシートエリアの濃度を算出する
		processingFileMarkArea();
		//　新しいSceneを生成し、次の画面に進む
		Main.getInstance().setFirstTimeScene4(true);
		Main.getInstance().nextPage(0);
	}
	
	private void processingTiltCorrect() throws Exception {
		if (!observableListForOpenFIleListView.isEmpty()) {
			
			// 戻るボタンで戻ったときに残っているデータを消しておくために、いったんgetMarkSheetDataArrayListを空にする
			Main.getInstance().getMarkSheetDataArrayList().clear();
			
			for (int i = 0; i < observableListForOpenFIleListView.size(); i++) {
				File filePath = observableListForOpenFIleListView.get(i);
				new ImageProcessingTiltCorrect(filePath, false);
			}
		}
	}

	private void processingFileMarkArea() {
		String PATH = Paths.get(".").toAbsolutePath().normalize().toString()+"\\03homographyImages\\";
		File file = new File(PATH);
		File[] files = file.listFiles();
		if (files.length!=0) {
			for (int i = 0; i < files.length; i++) {
				MarkSheetsData markSheetData = markSheetDataArrayList.get(i);
				new ImageProcessingFindMarkArea(files[i],markAreaSettingList,markSheetData);
			}
		}
	}

}






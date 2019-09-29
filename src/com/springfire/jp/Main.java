package com.springfire.jp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;

import datamodel.MarkAreasSetting;
import datamodel.MarkSheetsData;
import datamodel.MergedQuestionSetting;
import datamodel.QuestionsSetting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import setting.MarkSheetSetting;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

	//　openCVを実装
	static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	//　Controllerからアクセスできるようにpublicとする
	public static Main singleton;
	//　各ControllerからアクセスできるようにGetterSetterを設定する
	private List<MarkAreasSetting> markAreasSettingList =new ArrayList<MarkAreasSetting>();
	private List<QuestionsSetting> questionsSettingList = new ArrayList<QuestionsSetting>();
	private List<MarkSheetsData> markSheetDataArrayList = new ArrayList<MarkSheetsData>();
	private List<MergedQuestionSetting> mergedQuestionSettingsList = new ArrayList<MergedQuestionSetting>();
	private int SpinnerThreshold;
	private List<Integer> duplicateNumberList = new ArrayList<Integer>();
	private List<Integer> questionNumbersList = new ArrayList<Integer>();
	private boolean isHandleMergeQuestionNumerButtonPusshed =false;

	//　画面遷移においてScene, Parent(root), fxml, controllerをセットで遷移させる	
	private String[] PAGE = {
			"/controller01/window01QuestionSetting.fxml",
			"/controller02/window02ChoiceSetting.fxml",
			"/controller03/window03AnswerSetting.fxml",
			"/controller04/window04ScoreAndMergedQuestion.fxml",
			"/controller05/window05ImageProcessing.fxml",
			"/controller06/window6DisplayMarkAreaData.fxml",
			"/controller07/window07ScoreResult.fxml",
			"/controller08/window08.fxml",
			"/controller09/window09.fxml"
	};
	private Parent[] PARENTS = new Parent[PAGE.length];
	private Scene[] SCENES= new Scene[PAGE.length];
	//　遷移するページ数
	private  int page_index =0;
	private  int page_num = 9;
	//　Sceneのサイズ
	private int width = 1200;
	private int height = 750;
	private Stage stage;
	//　trueのときのみ新しいシーンを作る。戻るをおしたとき、falseからtrueへとし、次に進むをおしたときに新しいSceneを生成する
	private boolean firstTimeScene0 = true;
	private boolean firstTimeScene1 = true;
	private boolean firstTimeScene2 = true;
	private boolean firstTimeScene3 = true;
	private boolean firstTimeScene4 = true;
	private boolean firstTimeScene5 = true;
	private boolean firstTimeScene6 = true;
	private boolean firstTimeScene7 = true;
	private boolean firstTimeScene8 = true;
	private boolean firstTimeScene9 = true;

	@Override
	public void start(Stage primaryStage) {
		try {
			//　この中でデータを管理する
			new MarkSheetSetting();
			singleton = this;
			stage = primaryStage;
			PARENTS[0] = FXMLLoader.load(getClass().getResource(PAGE[0]));
			SCENES[0] = new Scene(PARENTS[0] , width, height);
			stage.setTitle("マークシートリーダースイッターくん");
			stage.setScene(SCENES[0] );
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public  void nextPage(int page){
		page_index = page_index+1+page;
		page_index %= page_num;
		setPage();
	}

	public void prevPage(int page){
		page_index =page_index -1 -page;
		if (page_index<0){
			page_index += page_num;
		}
		page_index %= page_num;
		setPage();
	}

	private void setPage(){
		try {
			//　何度も新しいSceneが生成されないようにtrueのときに限り生成する。例えば戻るボタンから進むボタンのときはtrueにし新しいSceneを生成する。
			if (firstTimeScene0 && page_index==0) {
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
//				SCENES[page_index] .getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				firstTimeScene0 = false;
			}else if (firstTimeScene1 && page_index==1) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene1 = false;
			}else if (firstTimeScene2 && page_index==2) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene2 = false;
			}else if (firstTimeScene3 && page_index==3) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene3 = false;
			}else if (firstTimeScene4 && page_index==4) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene4 = false;
			}else if (firstTimeScene5 && page_index==5) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene5 = false;
			}else if (firstTimeScene6 && page_index==6) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				SCENES[page_index] .getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				firstTimeScene6 = false;
			}else if (firstTimeScene7 && page_index==7) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene7 = false;
			}else if (firstTimeScene8 && page_index==8) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene8 = false;
			}else if (firstTimeScene9 && page_index==9) {
				System.out.println("new Scene ->" + PAGE[page_index] +",  page_index="+page_index);
				PARENTS[page_index] = FXMLLoader.load(getClass().getResource(PAGE[page_index]));
				SCENES[page_index] = new Scene(PARENTS[page_index],width,height);
				firstTimeScene9 = false;
			}
			stage.setScene(SCENES[page_index]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Main getInstance() {
		return singleton;
	}
	public boolean isFirstTimeScene0() {
		return firstTimeScene0;
	}

	public void setFirstTimeScene0(boolean firstTimeScene0) {
		this.firstTimeScene0 = firstTimeScene0;
	}

	public void setFirstTimeScene1(boolean firstTimeScene1) {
		this.firstTimeScene1 = firstTimeScene1;
	}

	public void setFirstTimeScene2(boolean firstTimeScene2) {
		this.firstTimeScene2 = firstTimeScene2;
	}

	public void setFirstTimeScene3(boolean firstTimeScene3) {
		this.firstTimeScene3 = firstTimeScene3;
	}

	public void setFirstTimeScene4(boolean firstTimeScene4) {
		this.firstTimeScene4 = firstTimeScene4;
	}

	public void setFirstTimeScene5(boolean firstTimeScene5) {
		this.firstTimeScene5 = firstTimeScene5;
	}

	public void setFirstTimeScene6(boolean firstTimeScene6) {
		this.firstTimeScene6 = firstTimeScene6;
	}

	public void setFirstTimeScene7(boolean firstTimeScene7) {
		this.firstTimeScene7 = firstTimeScene7;
	}

	public void setFirstTimeScene8(boolean firstTimeScene8) {
		this.firstTimeScene8 = firstTimeScene8;
	}

	public void setFirstTimeScene9(boolean firstTimeScene9) {
		this.firstTimeScene9 = firstTimeScene9;
	}
	
	public List<MarkAreasSetting> getMarkAreasSettingList() {
		return markAreasSettingList;
	}

	public List<QuestionsSetting> getQuestionsSettingList() {
		return questionsSettingList;
	}
	public List<MarkSheetsData> getMarkSheetDataArrayList() {
		return markSheetDataArrayList;
	}

	public double getSpinnerThreshold() {
		return SpinnerThreshold;
	}

	public void setSpinnerThreshold(int spinnerThreshold) {
		SpinnerThreshold = spinnerThreshold;
	}

	
	public List<Integer> getDuplicateNumberList() {
		return duplicateNumberList;
	}

	public List<Integer> getQuestionNumbersList() {
		return questionNumbersList;
	}
	
	public void setHandleMergeQuestionNumerButtonPusshed(boolean isHandleMergeQuestionNumerButtonPusshed) {
		this.isHandleMergeQuestionNumerButtonPusshed = isHandleMergeQuestionNumerButtonPusshed;
	}

	public boolean isHandleMergeQuestionNumerButtonPusshed() {
		return isHandleMergeQuestionNumerButtonPusshed;
	}
	
	

	public List<MergedQuestionSetting> getMergedQuestionSettingsList() {
		return mergedQuestionSettingsList;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
	
}


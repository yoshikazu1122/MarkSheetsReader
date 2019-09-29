package datamodel;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import com.springfire.jp.Main;


public class StoreMarkSheetSetting {
	private static StoreMarkSheetSetting instance = new StoreMarkSheetSetting();
	private List<MarkAreasSetting> markAreaSettingList = Main.getInstance().getMarkAreasSettingList();
	
	private StoreMarkSheetSetting() {
		// TODO Auto-generated constructor stub
		
	}
	

	public static StoreMarkSheetSetting getInstance() {
		return instance;
	}
	
	public void storeSettingData(String enteredFilename) throws IOException {
		Path path = Paths.get(enteredFilename+".txt");
		BufferedWriter bw = Files.newBufferedWriter(path);
		try {
			Iterator<MarkAreasSetting> itr = markAreaSettingList.iterator();
			while (itr.hasNext()) {
				MarkAreasSetting mark = itr.next();
				bw.write(String.format("%s\t%s\t%s\t%s",
						mark.getMarkAreaID(),
						mark.getQuestionNumber(),
						mark.getAnswerChoice(), // 問題番号のないところはー１と入り、未入力の場合は「/」となる
						mark.getCorrectAnswer()));// 答えがないところはー１となる
				bw.newLine();
			}
		} finally {
			if (bw!=null) {
				bw.close();
			}
		}
	}


	
}

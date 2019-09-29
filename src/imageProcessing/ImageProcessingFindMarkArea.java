package imageProcessing;

import java.io.File;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import datamodel.MarkAreasData;
import datamodel.MarkAreasSetting;
import datamodel.MarkSheetsData;

public class ImageProcessingFindMarkArea {
	private File filePath;
	private List<MarkAreasSetting> markAreaSettingList;
	private MarkSheetsData markSheetData;
	private MarkAreasData areaData;

	public ImageProcessingFindMarkArea(File filePath, List<MarkAreasSetting> markAreaSettingList, MarkSheetsData markSheetData) {
		this.filePath = filePath;
		this.markAreaSettingList = markAreaSettingList;
		this.markSheetData = markSheetData;
				
		Mat src = Imgcodecs.imread(String.valueOf(filePath));
		findMarkedArea(src);
	}

	private void findMarkedArea(Mat src) {

		//　 一番最初に検出されるマークエリア
		double firstMarkX = 61;
		double firstMarkY = 187;
		//　 マークエリアは小さい四角形なので、その横と縦の長さ 
		double rectangleWidth = 15;
		double rectangleHeihgt = 42;
		//　 マークエリアの次の右側のマークエリアまでの距離と、その下のマークエリアまでの距離 
		double nextMarkWidth = 67.9;
		double nextMarkHeight = 90.3;
		
		//　 マークエリアを読みとるために二値化
		Mat binarySrc = new Mat();
		//　敷居値を設定する。この数字を大きくすれば、マークの薄いものは消えてしまい、小さくすれば消し残り読み取りダブルマークにつながる。
		//　findContoursを使って円を検出するときも二値化したが、敷居値が小さいため、鉛筆のマークが消えていた。
		double threshold =200;
		double maxValue=255;
		Imgproc.threshold(src, binarySrc, threshold, maxValue, Imgproc.THRESH_BINARY_INV);

		//　二値化反転画像を出力
//		new File("04BinaryInvForMarkArea").mkdir();
//		Imgcodecs.imwrite("./04BinaryInvForMarkArea/MarkArea_"+filePath.getName(),binarySrc);
		
		//　 マークシートの縦横の列の数
		int markAreasColumn =34;
		int markAreasRow = 34;
		
		//　マークエリアは縦３４横３４の合計１１５６ヵ所ある。
		//　マークエリアは、一番左上が１でそこから右に１ずつ増えていく。よって一番右下が一番最後のマークエリアで１１２６番目となる。
//		createNeedMarkAreaList(markAreasColumn,markAreasRow);
		
		//　 画像のマークエリアにナンバーをふる
		int count = 0;
		//　 マークエリアの特定
		for (int y = 0; y < markAreasRow; y++) {
			for (int x = 0; x < markAreasColumn; x++) {
				//　 マークエリアの描写
				count++;
				if (markAreaSettingList.get(count-1).getQuestionNumber()>0) { //｛問題番号＝｛不要必要、問題番号、濃度｝｝
					Imgproc.rectangle(src, new Point(firstMarkX+x*nextMarkWidth, firstMarkY+y*nextMarkHeight),
							new Point(firstMarkX+rectangleWidth+x*nextMarkWidth, firstMarkY+rectangleHeihgt+y*nextMarkHeight),
							new Scalar(255,0,255), 1);
					Imgproc.putText(src, String.valueOf(count), new Point(firstMarkX+x*nextMarkWidth-rectangleWidth/2,firstMarkY+y*nextMarkHeight+rectangleHeihgt/2),
							Imgproc.FONT_HERSHEY_PLAIN, 1, new Scalar(0, 0, 0), 1);
				}
				
				// 　マークエリアの範囲をMatを使って取得する
				Mat matOfMarkArea = new Mat(binarySrc, new Rect(new Point(firstMarkX+x*nextMarkWidth, firstMarkY+y*nextMarkHeight),
						new Point(firstMarkX+rectangleWidth+x*nextMarkWidth, firstMarkY+rectangleHeihgt+y*nextMarkHeight)));

				//　上記で取得したマークエリアの範囲（一つの四角形の内側）で、Matの黒色の濃度を調べる。
				//　 マークエリアの範囲の黒のピクセル数÷マークエリアの全範囲のピクセル数×１００としては、四角形の中のマーク率を１００分率で算出している。

				//　 マークエリアのすべてのピクセル数
				double total = matOfMarkArea.total()*255;
				//　 マークエリアのマークが検出された数
				double sumOfMarkArea = Core.sumElems(matOfMarkArea).val[0];
				//　 上記の二つから１００分率を算出
				int ratioOfMarkArea = (int)(sumOfMarkArea/total*100);
				//　 各マーク率をリストに保存する
				areaData = new MarkAreasData(markAreaSettingList.get(count-1).getQuestionNumber(),
						(int)sumOfMarkArea, ratioOfMarkArea, markAreaSettingList.get(count-1).getAnswerChoice(),
						markAreaSettingList.get(count-1).getCorrectAnswer());
				markSheetData.getMarkAreaDataArrayList().add(areaData);
			}
		}
		
		
		
		//　透視変換後の画像にマークエリアを描写ファイル出力
//		new File("05drawMarkAreasImages").mkdir();
//		Imgcodecs.imwrite("./05drawMarkAreasImages/MarkArea_"+filePath.getName(), src);
		
	}
		
}
	
	
	
	
	
	
	


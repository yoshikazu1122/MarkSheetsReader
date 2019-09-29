package imageProcessing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import com.springfire.jp.Main;

import datamodel.MarkSheetsData;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//言葉の定義
//マークエリア：　マークを読みとる場所は、横３４縦３４の合計１１５６か所である。その一つ一つをマークエリアと呼ぶ。
//マークナンバー：　マークナンバーは、各マークエリアの番号である。マークナンバーは一番左上が1番となり右に進みながら１増え、一番右下が１１５６番となる。
public class ImageProcessingTiltCorrect  {
	//　マークシート設定でバックグランドに使う画像を取得、バックグラウンドで使用する場合はコンストラクターでbackgroundToUseをtrueとする
	private Mat backgroundMap;
	private boolean backgroundToUse;

	//　ListViewから画像ファイルのパスを受け取る
	private final File filePath;

	//　四隅のマークシートの円の中心　０：左上、１右上、２左下、３右下
	private double circleX0;
	private double circleY0;
	private double circleX1;
	private double circleY1;
	private double circleX2;
	private double circleY2;
	private double circleX3;
	private double circleY3;

	private List<MarkSheetsData> markSheetDataArrayList = Main.getInstance().getMarkSheetDataArrayList();


	public ImageProcessingTiltCorrect(File filePath, boolean backgroundToUse) throws Exception {
		//　画像処理のプロセス
		//　画像をグレースケール→　二値化→　findContours（四隅の円検出）→　透視変換　→マークエリア濃度検出

		this.filePath = filePath;
		//　Controller２のGUIにバックグラウンドに貼り付ける場合はTrue。ただの画像処理はfalseとする。
		this.backgroundToUse = backgroundToUse;
		
		//　受け取った画像ファイル名をコンソールに出力
		System.out.println(String.valueOf(filePath.getName()));
		//　Mat srcに画像読みこみ、タイプはグレースケールを選択。
		Mat src = Imgcodecs.imread(String.valueOf(filePath), Imgcodecs.IMREAD_GRAYSCALE);
		//　グレースケールにしたデータを二値化し白黒反転
		Mat binaryMat = createBinaryImageInverseBlackWhite(src);
		//　四隅の円を検出し、四隅の円の中心の情報を取得する。rangeOfFindContoursの範囲で四隅の円を検出する。
		int rangeOfFindContours = 150;
		detectedFourCircles(binaryMat, rangeOfFindContours);
		//　四隅の円の中心と検出した範囲を四角でを描き画像ファイルを出力
		drawCircleAndRectangleOnImage(rangeOfFindContours);
		//　 透視変換
		src = Imgcodecs.imread(String.valueOf(filePath), Imgcodecs.IMREAD_COLOR);
		homographyTransformation(src);
	}
	
	public Mat getBackgroundMap() {
		return backgroundMap;
	}
	
	private Mat createBinaryImageInverseBlackWhite(Mat src) throws Exception {
		//　画像読み込み確認
		if (src.dataAddr()==0) {
			System.out.println(System.getProperties());
			createErrorDialog();
			throw new Exception("Couldn't open file");
		}else {
			//　画像を二値化する。typeでINVを選び白黒反転
			Mat binaryImage = new Mat();
			double threshold = 100;
			double maxValue = 255.0;
			Imgproc.threshold(src, binaryImage, threshold, maxValue, Imgproc.THRESH_BINARY_INV);
			//　二値画像ファイル出力
//			new File("01BinaryForCircleImages").mkdir();
//			Imgcodecs.imwrite("./01BinaryForCircleImages/Binary_"+filePath.getName(), binaryImage);	
			return binaryImage;
		}
	}

	private void detectedFourCircles(Mat binaryImage, int rangeOfFindContours) {
		//　輪郭を検出する。hierarychyはオプションで特に気にしなくてよさそう。
		Mat hierarchy = new Mat();
		//　findContoursで検出したオブジェクトをlistに入れる
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		//　白い場所の外の輪郭のみを検出
		Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		//　たくさんの輪郭が見つかるので、条件をつけ画像の四隅の輪郭を抽出する。範囲はrangeOfFindContoursとする。
		// 検出した円の値を取得し、後で四隅の円の中心を利用し、透視変換する。
		circleX0 = 0;
		circleY0 = rangeOfFindContours; 
		circleX1 = 0;
		circleY1 = rangeOfFindContours;
		circleX2 = 0;
		circleY2 = 0;
		circleX3 = 0;
		circleY3 = 0;

		//　四隅で検出されたものの中には、小さなゴミや鉛筆の跡がある。これらを除外するためにサイズの条件をつけて円を検出する。
		int circleSize = 20;
		//　forで contoursの中に入っている検出した円を取り出す。
		for (int i = 0; i < contours.size(); i++) {
			MatOfPoint pointMat = contours.get(i);
			//　円の中心のデーターを取得するため、pointMatをArrayの形に変換する。
			MatOfPoint2f pointMat2 = new MatOfPoint2f(pointMat.toArray());

			// bbox.centerで検出したものの中心を求めることができる
			RotatedRect bbox = Imgproc.minAreaRect(pointMat2);
			//　 検出されたものは、多数あるので、四隅にあるものを検出する。四隅にある小さいゴミ等を除外するために、circleSizeの条件を付けくわえる。
			if (bbox.center.x<rangeOfFindContours && bbox.center.y<rangeOfFindContours && contours.get(i).rows()>circleSize) {
				// 左上の円　（０）
				if (circleY0>bbox.center.y) {
					circleX0 = bbox.center.x;
					circleY0 = bbox.center.y;
				}
			}else if (bbox.center.x>binaryImage.width()-rangeOfFindContours && bbox.center.y<rangeOfFindContours && contours.get(i).rows()>circleSize) {
				//　右上の円　（１）
				if (circleY1>bbox.center.y) {
					circleX1 = bbox.center.x;
					circleY1 = bbox.center.y;
				}
			}else if (bbox.center.x<rangeOfFindContours && bbox.center.y>binaryImage.height()-rangeOfFindContours && contours.get(i).rows()>circleSize) {
				//　左下の円　（２）
				if (circleY2<bbox.center.y) {
					circleX2 = bbox.center.x;
					circleY2 = bbox.center.y;
				}
			}else if (bbox.center.x>binaryImage.width()-rangeOfFindContours && bbox.center.y>binaryImage.height()-rangeOfFindContours && contours.get(i).rows()>circleSize) {
				//　右下の円　（３）
				if (circleY3<bbox.center.y) {
					circleX3 = bbox.center.x;
					circleY3 = bbox.center.y;
				}
			}
		}
	}
	
	private void drawCircleAndRectangleOnImage(int rangeOfFindContours) {
		//検出した円の座標を表示
		System.out.println("  3: "+circleX3+", "+circleY3);
		System.out.println("  2: "+circleX2+", "+circleY2);
		System.out.println("  1: "+circleX1+", "+circleY1);
		System.out.println("  0: "+circleX0+", "+circleY0);
		//　四隅の円の中心を描く
		Mat src = Imgcodecs.imread(String.valueOf(filePath));
		Imgproc.circle(src, new Point(circleX0,circleY0), 2, new Scalar(255, 0, 255),5);
		Imgproc.circle(src, new Point(circleX1,circleY1), 2, new Scalar(255, 0, 255),5);
		Imgproc.circle(src, new Point(circleX2,circleY2), 2, new Scalar(255, 0, 255),5);
		Imgproc.circle(src, new Point(circleX3,circleY3), 2, new Scalar(255, 0, 255),5);
		//　四隅の円を検出する範囲を四角で描く
		Imgproc.rectangle(src, new Point(0, 0),
				new Point(rangeOfFindContours, rangeOfFindContours), new Scalar(255, 0, 255), 3);
		Imgproc.rectangle(src, new Point(src.width()-rangeOfFindContours, 0),
				new Point(src.width(), rangeOfFindContours), new Scalar(255, 0, 255), 3);
		Imgproc.rectangle(src, new Point(0, src.height()-rangeOfFindContours),
				new Point(rangeOfFindContours, src.height()), new Scalar(255, 0, 255), 3);
		Imgproc.rectangle(src, new Point(src.width()-rangeOfFindContours, src.height()-rangeOfFindContours),
				new Point(src.width(), src.height()), new Scalar(255, 0, 255), 3);

		//　四隅の円を検出した画像ファイル出力
//		new File("02DetectedCirclesImages").mkdir();
//		Imgcodecs.imwrite("./02DetectedCirclesImages/Circles_"+filePath.getName(), src);		
	}

	private Mat homographyTransformation(Mat src) {
		//　 変換前の４点を指定する
		List<Point> srcPoints = new ArrayList<Point>(4);
		srcPoints.add(new Point(circleX0, circleY0));
		srcPoints.add(new Point(circleX1, circleY1));
		srcPoints.add(new Point(circleX2, circleY2));
		srcPoints.add(new Point(circleX3, circleY3));
		//　 CV_32F以外では動かないので注意が必要！
		Mat srcPointsMat = Converters.vector_Point_to_Mat(srcPoints, CvType.CV_32F);

		//　 変換後の４点を指定する。円からmargin分離れた内側に画像を変換。
		double margin = 50;
		int width = 2375;
		int height = 3380;
		List<Point> dstPoints = new ArrayList<Point>(4);
		dstPoints.add(new Point(margin, margin)); //0 左上
		dstPoints.add(new Point(width-margin, margin)); //1　右上
		dstPoints.add(new Point(margin, height-margin)); //2　左下
		dstPoints.add(new Point(width-margin, height-margin)); //3　右上
		Mat dstPointsMat = Converters.vector_Point_to_Mat(dstPoints, CvType.CV_32F);

		//　 変換前後の座標をもとにgetPerspectiveTransformで変換行列を求める
		Mat mat = Imgproc.getPerspectiveTransform(srcPointsMat, dstPointsMat);
		//　 変換先
		Mat dst = new Mat(height,width,src.type());
		//　 変換行列用いて warpPerspective で画像を変換する。
		Imgproc.warpPerspective(src, dst, mat, dst.size());

		//　透視変換後の画像ファイル出力
		new File("03homographyImages").mkdir();
		Imgcodecs.imwrite("./03homographyImages/homography_"+filePath.getName(), dst);
		

		//true: マークシート設定画面のバックグラウンドに使う画像を取得
		//false:　マークシートデータをArrayListに追加
		if (backgroundToUse) {
			backgroundMap = new Mat(height,width,src.type());
			dst.copyTo(backgroundMap);
		}else {		
			MarkSheetsData newMarkSheet = new MarkSheetsData(filePath,circleX0,circleY0,circleX1,circleY1,circleX2,circleY2,circleX3,circleY3);
			markSheetDataArrayList.add(newMarkSheet);
		}

		return dst;
	}











	private void createErrorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText("Couldn't open file");
		alert.showAndWait();
	}



 





}

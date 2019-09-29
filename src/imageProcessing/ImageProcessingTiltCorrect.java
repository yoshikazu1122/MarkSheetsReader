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

//���t�̒�`
//�}�[�N�G���A�F�@�}�[�N��ǂ݂Ƃ�ꏊ�́A���R�S�c�R�S�̍��v�P�P�T�U�����ł���B���̈����}�[�N�G���A�ƌĂԁB
//�}�[�N�i���o�[�F�@�}�[�N�i���o�[�́A�e�}�[�N�G���A�̔ԍ��ł���B�}�[�N�i���o�[�͈�ԍ��オ1�ԂƂȂ�E�ɐi�݂Ȃ���P�����A��ԉE�����P�P�T�U�ԂƂȂ�B
public class ImageProcessingTiltCorrect  {
	//�@�}�[�N�V�[�g�ݒ�Ńo�b�N�O�����h�Ɏg���摜���擾�A�o�b�N�O���E���h�Ŏg�p����ꍇ�̓R���X�g���N�^�[��backgroundToUse��true�Ƃ���
	private Mat backgroundMap;
	private boolean backgroundToUse;

	//�@ListView����摜�t�@�C���̃p�X���󂯎��
	private final File filePath;

	//�@�l���̃}�[�N�V�[�g�̉~�̒��S�@�O�F����A�P�E��A�Q�����A�R�E��
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
		//�@�摜�����̃v���Z�X
		//�@�摜���O���[�X�P�[�����@��l�����@findContours�i�l���̉~���o�j���@�����ϊ��@���}�[�N�G���A�Z�x���o

		this.filePath = filePath;
		//�@Controller�Q��GUI�Ƀo�b�N�O���E���h�ɓ\��t����ꍇ��True�B�����̉摜������false�Ƃ���B
		this.backgroundToUse = backgroundToUse;
		
		//�@�󂯎�����摜�t�@�C�������R���\�[���ɏo��
		System.out.println(String.valueOf(filePath.getName()));
		//�@Mat src�ɉ摜�ǂ݂��݁A�^�C�v�̓O���[�X�P�[����I���B
		Mat src = Imgcodecs.imread(String.valueOf(filePath), Imgcodecs.IMREAD_GRAYSCALE);
		//�@�O���[�X�P�[���ɂ����f�[�^���l�����������]
		Mat binaryMat = createBinaryImageInverseBlackWhite(src);
		//�@�l���̉~�����o���A�l���̉~�̒��S�̏����擾����BrangeOfFindContours�͈̔͂Ŏl���̉~�����o����B
		int rangeOfFindContours = 150;
		detectedFourCircles(binaryMat, rangeOfFindContours);
		//�@�l���̉~�̒��S�ƌ��o�����͈͂��l�p�ł�`���摜�t�@�C�����o��
		drawCircleAndRectangleOnImage(rangeOfFindContours);
		//�@ �����ϊ�
		src = Imgcodecs.imread(String.valueOf(filePath), Imgcodecs.IMREAD_COLOR);
		homographyTransformation(src);
	}
	
	public Mat getBackgroundMap() {
		return backgroundMap;
	}
	
	private Mat createBinaryImageInverseBlackWhite(Mat src) throws Exception {
		//�@�摜�ǂݍ��݊m�F
		if (src.dataAddr()==0) {
			System.out.println(System.getProperties());
			createErrorDialog();
			throw new Exception("Couldn't open file");
		}else {
			//�@�摜���l������Btype��INV��I�є������]
			Mat binaryImage = new Mat();
			double threshold = 100;
			double maxValue = 255.0;
			Imgproc.threshold(src, binaryImage, threshold, maxValue, Imgproc.THRESH_BINARY_INV);
			//�@��l�摜�t�@�C���o��
//			new File("01BinaryForCircleImages").mkdir();
//			Imgcodecs.imwrite("./01BinaryForCircleImages/Binary_"+filePath.getName(), binaryImage);	
			return binaryImage;
		}
	}

	private void detectedFourCircles(Mat binaryImage, int rangeOfFindContours) {
		//�@�֊s�����o����Bhierarychy�̓I�v�V�����œ��ɋC�ɂ��Ȃ��Ă悳�����B
		Mat hierarchy = new Mat();
		//�@findContours�Ō��o�����I�u�W�F�N�g��list�ɓ����
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		//�@�����ꏊ�̊O�̗֊s�݂̂����o
		Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		//�@��������̗֊s��������̂ŁA���������摜�̎l���̗֊s�𒊏o����B�͈͂�rangeOfFindContours�Ƃ���B
		// ���o�����~�̒l���擾���A��Ŏl���̉~�̒��S�𗘗p���A�����ϊ�����B
		circleX0 = 0;
		circleY0 = rangeOfFindContours; 
		circleX1 = 0;
		circleY1 = rangeOfFindContours;
		circleX2 = 0;
		circleY2 = 0;
		circleX3 = 0;
		circleY3 = 0;

		//�@�l���Ō��o���ꂽ���̂̒��ɂ́A�����ȃS�~�≔�M�̐Ղ�����B���������O���邽�߂ɃT�C�Y�̏��������ĉ~�����o����B
		int circleSize = 20;
		//�@for�� contours�̒��ɓ����Ă��錟�o�����~�����o���B
		for (int i = 0; i < contours.size(); i++) {
			MatOfPoint pointMat = contours.get(i);
			//�@�~�̒��S�̃f�[�^�[���擾���邽�߁ApointMat��Array�̌`�ɕϊ�����B
			MatOfPoint2f pointMat2 = new MatOfPoint2f(pointMat.toArray());

			// bbox.center�Ō��o�������̂̒��S�����߂邱�Ƃ��ł���
			RotatedRect bbox = Imgproc.minAreaRect(pointMat2);
			//�@ ���o���ꂽ���̂́A��������̂ŁA�l���ɂ�����̂����o����B�l���ɂ��鏬�����S�~�������O���邽�߂ɁAcircleSize�̏�����t�����킦��B
			if (bbox.center.x<rangeOfFindContours && bbox.center.y<rangeOfFindContours && contours.get(i).rows()>circleSize) {
				// ����̉~�@�i�O�j
				if (circleY0>bbox.center.y) {
					circleX0 = bbox.center.x;
					circleY0 = bbox.center.y;
				}
			}else if (bbox.center.x>binaryImage.width()-rangeOfFindContours && bbox.center.y<rangeOfFindContours && contours.get(i).rows()>circleSize) {
				//�@�E��̉~�@�i�P�j
				if (circleY1>bbox.center.y) {
					circleX1 = bbox.center.x;
					circleY1 = bbox.center.y;
				}
			}else if (bbox.center.x<rangeOfFindContours && bbox.center.y>binaryImage.height()-rangeOfFindContours && contours.get(i).rows()>circleSize) {
				//�@�����̉~�@�i�Q�j
				if (circleY2<bbox.center.y) {
					circleX2 = bbox.center.x;
					circleY2 = bbox.center.y;
				}
			}else if (bbox.center.x>binaryImage.width()-rangeOfFindContours && bbox.center.y>binaryImage.height()-rangeOfFindContours && contours.get(i).rows()>circleSize) {
				//�@�E���̉~�@�i�R�j
				if (circleY3<bbox.center.y) {
					circleX3 = bbox.center.x;
					circleY3 = bbox.center.y;
				}
			}
		}
	}
	
	private void drawCircleAndRectangleOnImage(int rangeOfFindContours) {
		//���o�����~�̍��W��\��
		System.out.println("  3: "+circleX3+", "+circleY3);
		System.out.println("  2: "+circleX2+", "+circleY2);
		System.out.println("  1: "+circleX1+", "+circleY1);
		System.out.println("  0: "+circleX0+", "+circleY0);
		//�@�l���̉~�̒��S��`��
		Mat src = Imgcodecs.imread(String.valueOf(filePath));
		Imgproc.circle(src, new Point(circleX0,circleY0), 2, new Scalar(255, 0, 255),5);
		Imgproc.circle(src, new Point(circleX1,circleY1), 2, new Scalar(255, 0, 255),5);
		Imgproc.circle(src, new Point(circleX2,circleY2), 2, new Scalar(255, 0, 255),5);
		Imgproc.circle(src, new Point(circleX3,circleY3), 2, new Scalar(255, 0, 255),5);
		//�@�l���̉~�����o����͈͂��l�p�ŕ`��
		Imgproc.rectangle(src, new Point(0, 0),
				new Point(rangeOfFindContours, rangeOfFindContours), new Scalar(255, 0, 255), 3);
		Imgproc.rectangle(src, new Point(src.width()-rangeOfFindContours, 0),
				new Point(src.width(), rangeOfFindContours), new Scalar(255, 0, 255), 3);
		Imgproc.rectangle(src, new Point(0, src.height()-rangeOfFindContours),
				new Point(rangeOfFindContours, src.height()), new Scalar(255, 0, 255), 3);
		Imgproc.rectangle(src, new Point(src.width()-rangeOfFindContours, src.height()-rangeOfFindContours),
				new Point(src.width(), src.height()), new Scalar(255, 0, 255), 3);

		//�@�l���̉~�����o�����摜�t�@�C���o��
//		new File("02DetectedCirclesImages").mkdir();
//		Imgcodecs.imwrite("./02DetectedCirclesImages/Circles_"+filePath.getName(), src);		
	}

	private Mat homographyTransformation(Mat src) {
		//�@ �ϊ��O�̂S�_���w�肷��
		List<Point> srcPoints = new ArrayList<Point>(4);
		srcPoints.add(new Point(circleX0, circleY0));
		srcPoints.add(new Point(circleX1, circleY1));
		srcPoints.add(new Point(circleX2, circleY2));
		srcPoints.add(new Point(circleX3, circleY3));
		//�@ CV_32F�ȊO�ł͓����Ȃ��̂Œ��ӂ��K�v�I
		Mat srcPointsMat = Converters.vector_Point_to_Mat(srcPoints, CvType.CV_32F);

		//�@ �ϊ���̂S�_���w�肷��B�~����margin�����ꂽ�����ɉ摜��ϊ��B
		double margin = 50;
		int width = 2375;
		int height = 3380;
		List<Point> dstPoints = new ArrayList<Point>(4);
		dstPoints.add(new Point(margin, margin)); //0 ����
		dstPoints.add(new Point(width-margin, margin)); //1�@�E��
		dstPoints.add(new Point(margin, height-margin)); //2�@����
		dstPoints.add(new Point(width-margin, height-margin)); //3�@�E��
		Mat dstPointsMat = Converters.vector_Point_to_Mat(dstPoints, CvType.CV_32F);

		//�@ �ϊ��O��̍��W�����Ƃ�getPerspectiveTransform�ŕϊ��s������߂�
		Mat mat = Imgproc.getPerspectiveTransform(srcPointsMat, dstPointsMat);
		//�@ �ϊ���
		Mat dst = new Mat(height,width,src.type());
		//�@ �ϊ��s��p���� warpPerspective �ŉ摜��ϊ�����B
		Imgproc.warpPerspective(src, dst, mat, dst.size());

		//�@�����ϊ���̉摜�t�@�C���o��
		new File("03homographyImages").mkdir();
		Imgcodecs.imwrite("./03homographyImages/homography_"+filePath.getName(), dst);
		

		//true: �}�[�N�V�[�g�ݒ��ʂ̃o�b�N�O���E���h�Ɏg���摜���擾
		//false:�@�}�[�N�V�[�g�f�[�^��ArrayList�ɒǉ�
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

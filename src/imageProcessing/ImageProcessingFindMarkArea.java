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

		//�@ ��ԍŏ��Ɍ��o�����}�[�N�G���A
		double firstMarkX = 61;
		double firstMarkY = 187;
		//�@ �}�[�N�G���A�͏������l�p�`�Ȃ̂ŁA���̉��Əc�̒��� 
		double rectangleWidth = 15;
		double rectangleHeihgt = 42;
		//�@ �}�[�N�G���A�̎��̉E���̃}�[�N�G���A�܂ł̋����ƁA���̉��̃}�[�N�G���A�܂ł̋��� 
		double nextMarkWidth = 67.9;
		double nextMarkHeight = 90.3;
		
		//�@ �}�[�N�G���A��ǂ݂Ƃ邽�߂ɓ�l��
		Mat binarySrc = new Mat();
		//�@�~���l��ݒ肷��B���̐�����傫������΁A�}�[�N�̔������̂͏����Ă��܂��A����������Ώ����c��ǂݎ��_�u���}�[�N�ɂȂ���B
		//�@findContours���g���ĉ~�����o����Ƃ�����l���������A�~���l�����������߁A���M�̃}�[�N�������Ă����B
		double threshold =200;
		double maxValue=255;
		Imgproc.threshold(src, binarySrc, threshold, maxValue, Imgproc.THRESH_BINARY_INV);

		//�@��l�����]�摜���o��
//		new File("04BinaryInvForMarkArea").mkdir();
//		Imgcodecs.imwrite("./04BinaryInvForMarkArea/MarkArea_"+filePath.getName(),binarySrc);
		
		//�@ �}�[�N�V�[�g�̏c���̗�̐�
		int markAreasColumn =34;
		int markAreasRow = 34;
		
		//�@�}�[�N�G���A�͏c�R�S���R�S�̍��v�P�P�T�U��������B
		//�@�}�[�N�G���A�́A��ԍ��オ�P�ł�������E�ɂP�������Ă����B����Ĉ�ԉE������ԍŌ�̃}�[�N�G���A�łP�P�Q�U�ԖڂƂȂ�B
//		createNeedMarkAreaList(markAreasColumn,markAreasRow);
		
		//�@ �摜�̃}�[�N�G���A�Ƀi���o�[���ӂ�
		int count = 0;
		//�@ �}�[�N�G���A�̓���
		for (int y = 0; y < markAreasRow; y++) {
			for (int x = 0; x < markAreasColumn; x++) {
				//�@ �}�[�N�G���A�̕`��
				count++;
				if (markAreaSettingList.get(count-1).getQuestionNumber()>0) { //�o���ԍ����o�s�v�K�v�A���ԍ��A�Z�x�p�p
					Imgproc.rectangle(src, new Point(firstMarkX+x*nextMarkWidth, firstMarkY+y*nextMarkHeight),
							new Point(firstMarkX+rectangleWidth+x*nextMarkWidth, firstMarkY+rectangleHeihgt+y*nextMarkHeight),
							new Scalar(255,0,255), 1);
					Imgproc.putText(src, String.valueOf(count), new Point(firstMarkX+x*nextMarkWidth-rectangleWidth/2,firstMarkY+y*nextMarkHeight+rectangleHeihgt/2),
							Imgproc.FONT_HERSHEY_PLAIN, 1, new Scalar(0, 0, 0), 1);
				}
				
				// �@�}�[�N�G���A�͈̔͂�Mat���g���Ď擾����
				Mat matOfMarkArea = new Mat(binarySrc, new Rect(new Point(firstMarkX+x*nextMarkWidth, firstMarkY+y*nextMarkHeight),
						new Point(firstMarkX+rectangleWidth+x*nextMarkWidth, firstMarkY+rectangleHeihgt+y*nextMarkHeight)));

				//�@��L�Ŏ擾�����}�[�N�G���A�͈̔́i��̎l�p�`�̓����j�ŁAMat�̍��F�̔Z�x�𒲂ׂ�B
				//�@ �}�[�N�G���A�͈̔͂̍��̃s�N�Z�������}�[�N�G���A�̑S�͈͂̃s�N�Z�����~�P�O�O�Ƃ��ẮA�l�p�`�̒��̃}�[�N�����P�O�O�����ŎZ�o���Ă���B

				//�@ �}�[�N�G���A�̂��ׂẴs�N�Z����
				double total = matOfMarkArea.total()*255;
				//�@ �}�[�N�G���A�̃}�[�N�����o���ꂽ��
				double sumOfMarkArea = Core.sumElems(matOfMarkArea).val[0];
				//�@ ��L�̓����P�O�O�������Z�o
				int ratioOfMarkArea = (int)(sumOfMarkArea/total*100);
				//�@ �e�}�[�N�������X�g�ɕۑ�����
				areaData = new MarkAreasData(markAreaSettingList.get(count-1).getQuestionNumber(),
						(int)sumOfMarkArea, ratioOfMarkArea, markAreaSettingList.get(count-1).getAnswerChoice(),
						markAreaSettingList.get(count-1).getCorrectAnswer());
				markSheetData.getMarkAreaDataArrayList().add(areaData);
			}
		}
		
		
		
		//�@�����ϊ���̉摜�Ƀ}�[�N�G���A��`�ʃt�@�C���o��
//		new File("05drawMarkAreasImages").mkdir();
//		Imgcodecs.imwrite("./05drawMarkAreasImages/MarkArea_"+filePath.getName(), src);
		
	}
		
}
	
	
	
	
	
	
	


package indoorPositioning;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class IndoorEnvironement {
	
	public static Rect[] detectVehicle(Mat outmat) {

		Rect[] rectArr = new Rect[2];
		Mat v = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(outmat, contours, v, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		double maxArea = -1;
		int maxAreaIdx = -1;
		Rect r = null;

		for (int idx = 0; idx < contours.size(); idx++) {
			Mat contour = contours.get(idx);
			double contourarea = Imgproc.contourArea(contour);
			if (contourarea > maxArea) {
				maxArea = contourarea;
				maxAreaIdx = idx;
				r = Imgproc.boundingRect(contours.get(maxAreaIdx));
			}
		}

		if (maxAreaIdx != -1) {
			rectArr[0] = r;
			contours.remove(maxAreaIdx);
			maxArea = -1;
			maxAreaIdx = -1;
			r = null;
		}

		for (int idx = 0; idx < contours.size(); idx++) {
			Mat contour = contours.get(idx);
			double contourarea = Imgproc.contourArea(contour);
			if (contourarea > maxArea) {
				maxArea = contourarea;
				maxAreaIdx = idx;
				r = Imgproc.boundingRect(contours.get(maxAreaIdx));
			}
		}

		rectArr[1] = r;

		v.release();

		return rectArr;

	}

	public static ObjectCircle detectRedVehicle(Mat frame, Mat hsv_image)
	{
		ObjectCircle oc = null;
		List<Mat> lhsv = new ArrayList<Mat>(3);
		Mat thresholded = new Mat();
		Mat thresholded2 = new Mat();
		Mat array255 = new Mat(480, 640, CvType.CV_8UC1);
		array255.setTo(new Scalar(255));
		Mat distance = new Mat(480, 640, CvType.CV_8UC1);
		Scalar hsv_min = new Scalar(50, 38, 50, 0);
		Scalar hsv_max = new Scalar(255, 75, 255, 0);
		Scalar hsv_min2 = new Scalar(50, 38, 50, 0);
		Scalar hsv_max2 = new Scalar(255, 75, 255, 0);
		Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);
		Core.inRange(hsv_image, hsv_min2, hsv_max2,
		thresholded2);
		Core.bitwise_or(thresholded, thresholded2,
		thresholded);

		Core.split(hsv_image, lhsv);

		Mat S = lhsv.get(1);
		Mat V = lhsv.get(2);

		Core.subtract(array255, S, S);
		Core.subtract(array255, V, V);

		S.convertTo(S, CvType.CV_32F);
		V.convertTo(V, CvType.CV_32F);

		Core.magnitude(S, V, distance);

		Core.inRange(distance, new Scalar(0.0), new Scalar(200.0), thresholded2);

		Core.bitwise_and(thresholded, thresholded2, thresholded);
		Imgproc.GaussianBlur(thresholded, thresholded, new Size(9, 9), 0, 0);
		
		Rect[] rectArr = detectVehicle(thresholded);
		
		if(rectArr[0] != null && rectArr[1] != null)
		{
			Point center1 = new Point(0.5 * (rectArr[0].tl().x + rectArr[0].br().x), 0.5 * (rectArr[0].tl().y + rectArr[0].br().y));
			Point center2 = new Point(0.5 * (rectArr[1].tl().x + rectArr[1].br().x), 0.5 * (rectArr[1].tl().y + rectArr[1].br().y));
			double temDistance = Math.sqrt(Math.pow((center2.x - center1.x),2) + Math.pow((center2.y - center1.y),2));
			Point tempCenter = new Point(0.5 * (center1.x + center2.x), 0.5 * (center1.y + center2.y));
			oc = new ObjectCircle(tempCenter, (int) (0.5 * temDistance), new Point[]{center1,center2});
		}
		
		return oc;
	}
	
	public static ObjectCircle detectGreenVehicle(Mat frame, Mat hsv_image)
	{
		ObjectCircle oc = null;
		
		return oc;
	}
	
	public static ObjectCircle detectBlueVehicle(Mat frame, Mat hsv_image)
	{
		ObjectCircle oc = null;
		List<Mat> lhsv = new ArrayList<Mat>(3);
		Mat thresholded = new Mat();
		Mat array255 = new Mat(480, 640, CvType.CV_8UC1);
		array255.setTo(new Scalar(255));
		Mat distance = new Mat(480, 640, CvType.CV_8UC1);
		Scalar hsv_min = new Scalar(110, 50, 50, 0);
		Scalar hsv_max = new Scalar(130, 255, 255, 0);
		Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);


		Core.split(hsv_image, lhsv);

		Mat S = lhsv.get(1);
		Mat V = lhsv.get(2);

		Core.subtract(array255, S, S);
		Core.subtract(array255, V, V);

		S.convertTo(S, CvType.CV_32F);
		V.convertTo(V, CvType.CV_32F);

		Core.magnitude(S, V, distance);

		Core.inRange(distance, new Scalar(0.0), new Scalar(200.0), thresholded);

		Core.bitwise_and(thresholded, thresholded, thresholded);
		Imgproc.GaussianBlur(thresholded, thresholded, new Size(9, 9), 0, 0);
		
		Rect[] rectArr = detectVehicle(thresholded);
		
		if(rectArr[0] != null && rectArr[1] != null)
		{
			Point center1 = new Point(0.5 * (rectArr[0].tl().x + rectArr[0].br().x), 0.5 * (rectArr[0].tl().y + rectArr[0].br().y));
			Point center2 = new Point(0.5 * (rectArr[1].tl().x + rectArr[1].br().x), 0.5 * (rectArr[1].tl().y + rectArr[1].br().y));
			double temDistance = Math.sqrt(Math.pow((center2.x - center1.x),2) + Math.pow((center2.y - center1.y),2));
			Point tempCenter = new Point(0.5 * (center1.x + center2.x), 0.5 * (center1.y + center2.y));
			oc = new ObjectCircle(tempCenter, (int) (0.5 * temDistance), new Point[]{center1,center2});
		}
		
		return oc;
	}
}

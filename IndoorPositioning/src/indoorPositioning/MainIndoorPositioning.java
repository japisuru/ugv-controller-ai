package indoorPositioning;

import java.awt.Component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import navigation.RobotNavigator;

public class MainIndoorPositioning {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	static Mat frame = null;

	public static void main(String arg[]) {

		JFrame jframe = new JFrame("HUMAN MOTION DETECTOR FPS");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(640, 480);
		jframe.setVisible(true);

//		JFrame jframe2 = new JFrame("DETECTED BLOB");
//		jframe2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JLabel vidpanel2 = new JLabel();
//		jframe2.setContentPane(vidpanel2);
//		jframe2.setSize(640, 480);
//		jframe2.setVisible(true);

		VideoCapture capture = new VideoCapture("http://192.168.8.101:8080/video?x.mjpeg");
		//VideoCapture capture = new VideoCapture("http://192.168.1.112:8080/video?x.mjpeg");
		//VideoCapture capture = new VideoCapture("/home/isuru/MSC-AI/Project/ImageProcessing/test_new.mp4");

		Mat frame = new Mat();
		Mat hsv_image = new Mat();

		Mat thresholded = new Mat();
		Mat thresholded2 = new Mat();

		Mat circles = new Mat();

		Mat array255 = new Mat(480, 640, CvType.CV_8UC1);
		array255.setTo(new Scalar(255));

		Mat distance = new Mat(480, 640, CvType.CV_8UC1);
		List<Mat> lhsv = new ArrayList<Mat>(3);

		// green
		//Scalar g_hsv_min = new Scalar(50, 50, 50, 0);
		//Scalar g_hsv_max = new Scalar(70, 255, 255, 0);
		Scalar g_hsv_min = new Scalar(88, 186, 85, 0);
		Scalar g_hsv_max = new Scalar(99, 255, 200, 0);

		// blue
		Scalar b_hsv_min = new Scalar(110, 53, 176, 0);
		Scalar b_hsv_max = new Scalar(130, 255, 255, 0);

		// red

//		Scalar r_hsv_min = new Scalar(0, 58, 125, 0);
//        Scalar r_hsv_max = new Scalar(6, 255, 255, 0);
//	    Scalar r_hsv_min2 = new Scalar(175, 50, 50, 0);
//	    Scalar r_hsv_max2 = new Scalar(179, 255, 255, 0);
		
		Scalar r_hsv_min = new Scalar(0, 139, 130, 0);
        Scalar r_hsv_max = new Scalar(15, 174, 178, 0);
	    Scalar r_hsv_min2 = new Scalar(175, 50, 50, 0);
	    Scalar r_hsv_max2 = new Scalar(179, 255, 255, 0);

		// pink
		Scalar p_hsv_min = new Scalar(144, 22, 177, 0);
		Scalar p_hsv_max = new Scalar(173, 42, 255, 0);
		
		// yellow
		Scalar y_hsv_min = new Scalar(15, 130, 146, 0);
		Scalar y_hsv_max = new Scalar(31, 255, 255, 0);

		///////////////////////////////////////////////////////////////////////////////////

		int min_minimum = 0;
		int min_maximum = 255;
		int min_initial = 0;
		JSlider min_SlideBar = new JSlider(JSlider.HORIZONTAL, min_minimum, min_maximum, min_initial);

		min_SlideBar.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int level = (int) source.getValue();
				// Mat output = imageProcessor.blur(image, level);
				// updateView(output);
			}
		});

		int max_minimum = 0;
		int max_maximum = 10;
		int max_initial = 0;
		JSlider max_SlideBar = new JSlider(JSlider.HORIZONTAL, max_minimum, max_maximum, max_initial);

		////////////////////////////////////////////////////////////////////////////////////

		Size sz = new Size(640, 480);

		Boolean isVideoEnable = true;
		int frameNum = 0;
		RobotNavigator rn = new RobotNavigator();

		if (isVideoEnable) {
			capture.read(frame);
			if (capture.isOpened()) {
				while (true) {
					capture.read(frame);
					if (!frame.empty()) {

						Imgproc.resize(frame, frame, sz);
						//Highgui.imwrite("/home/isuru/MSC-AI/Project/ImageProcessing/test/original.jpg", frame);
						Imgproc.cvtColor(frame, hsv_image, Imgproc.COLOR_BGR2HSV);

						Core.inRange(hsv_image, b_hsv_min, b_hsv_max, thresholded);
						Rect[] rectArr = detectVehicles(thresholded);
						draw(rectArr, frame);

						Core.inRange(hsv_image, g_hsv_min, g_hsv_max, thresholded);
						Rect[] rectArrGreen = detectVehicles(thresholded);
						draw(rectArrGreen, frame);
						
						Core.inRange(hsv_image, p_hsv_min, p_hsv_max, thresholded);	
						//Highgui.imwrite("/home/isuru/MSC-AI/Project/ImageProcessing/test/thresh.jpg", thresholded);
						List<Rect> lstPink = detectObstacles(thresholded);
						draw(lstPink,frame);
						//Highgui.imwrite("/home/isuru/MSC-AI/Project/ImageProcessing/test/final.jpg", frame);
						
						Core.inRange(hsv_image, y_hsv_min, y_hsv_max, thresholded);					
						List<Rect> lst = detectObstacles(thresholded);
						draw(lst,frame);

						Core.inRange(hsv_image, r_hsv_min, r_hsv_max, thresholded);
//						Core.inRange(hsv_image, r_hsv_min2, r_hsv_max2, thresholded2);
//						Core.bitwise_or(thresholded, thresholded2, thresholded);
//						
//
//						Core.split(hsv_image, lhsv);
//
//						Mat S = lhsv.get(1);
//						Mat V = lhsv.get(2);
//
//						Core.subtract(array255, S, S);
//						Core.subtract(array255, V, V);
//
//						S.convertTo(S, CvType.CV_32F);
//						V.convertTo(V, CvType.CV_32F);
//
//						Core.magnitude(S, V, distance);
//						
//						Core.inRange(distance, new Scalar(0.0), new
//						Scalar(200.0), thresholded2);
//
//						Core.bitwise_and(thresholded, thresholded2,
//						thresholded);
						
						rectArr = detectVehicles(thresholded);
						draw(rectArr, frame);
						
						

						/*
						 * ObjectCircle oc_red =
						 * IndoorEnvironement.detectRedVehicle(frame,
						 * hsv_image); if(oc_red != null) { Core.ellipse(frame,
						 * oc_red.getPoint(), new Size(oc_red.getRadius(),
						 * oc_red.getRadius()), 0,0, 360, new Scalar(255, 0,
						 * 255), 4, 8, 0); }
						 * 
						 */

						 //ImageIcon image2 = new
						 //ImageIcon(Mat2bufferedImage(thresholded));
						 //vidpanel2.setIcon(image2);
						 //vidpanel2.repaint();

						ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
						vidpanel.setIcon(image);
						vidpanel.repaint();
						
						if(frameNum % 20 == 0 && rectArrGreen.length == 2 && lstPink.size() == 1 && lstPink.get(0) != null && rectArrGreen[0] != null && rectArrGreen[1] != null)
						{
							rn.navigate(2,rectArrGreen[1].x,rectArrGreen[1].y,rectArrGreen[0].x,rectArrGreen[0].y,lstPink.get(0).x,lstPink.get(0).y);	
						}
							
						lstPink.clear();
						frameNum++;
						

					}
				}
			}
		} else {
			// frame =
			// Highgui.imread("/home/isuru/MSC-AI/Project/ImageProcessing/test.jpg",
			// Highgui.CV_LOAD_IMAGE_COLOR);
			// if (!frame.empty()) {
			//
			// Imgproc.resize(frame, frame, sz);
			//
			// Imgproc.cvtColor(frame, hsv_image, Imgproc.COLOR_BGR2HSV);
			// Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);
			// //Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);
			// //Core.bitwise_or(thresholded, thresholded2, thresholded);
			//
			// Core.split(hsv_image, lhsv);
			//
			// Mat S = lhsv.get(1);
			// Mat V = lhsv.get(2);
			//
			// Core.subtract(array255, S, S);
			// Core.subtract(array255, V, V);
			//
			// S.convertTo(S, CvType.CV_32F);
			// V.convertTo(V, CvType.CV_32F);
			//
			// Core.magnitude(S, V, distance);
			//
			// Core.inRange(distance, new Scalar(0.0), new Scalar(200.0),
			// thresholded2);
			//
			// Core.bitwise_and(thresholded, thresholded2, thresholded);
			// Imgproc.GaussianBlur(thresholded, thresholded, new Size(9, 9), 0,
			// 0);
			// Imgproc.HoughCircles(thresholded, circles,
			// Imgproc.CV_HOUGH_GRADIENT, 2, thresholded.height() / 4, 500,
			// 50, 0, 0);
			// int rows = circles.rows();
			// int elemSize = (int) circles.elemSize();
			// float[] data2 = new float[rows * elemSize / 4];
			// if (data2.length > 0) {
			// circles.get(0, 0, data2);
			// for (int i = 0; i < data2.length; i = i + 3) {
			// Point center = new Point(data2[i], data2[i + 1]);
			// Core.ellipse(frame, center, new Size((double) data2[i + 2],
			// (double) data2[i + 2]), 0, 0, 360,
			// new Scalar(255, 0, 255), 4, 8, 0);
			// }
			// Rect r = detect(thresholded);
			// if (r != null) {
			// Core.rectangle(frame, r.tl(), r.br(), new Scalar(0, 255, 0), 2);
			// }
			// Core.putText(frame,
			// "( x = " + 0.5 * (r.tl().x + r.br().x) + ", y = " + 0.5 *
			// (r.tl().y + r.br().y) + " )",
			// new Point(0.5 * (r.tl().x + r.br().x), 0.5 * (r.tl().y +
			// r.br().y)), 1, 1,
			// new Scalar(255, 255, 255));
			// }
			//
			// ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
			// vidpanel.setIcon(image);
			// vidpanel.repaint();
			//
			// }
		}

	}

	public static BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		Highgui.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}
	
	public static List<Rect> detectObstacles(Mat outmat) {
		Imgproc.GaussianBlur(outmat, outmat, new Size(9, 9), 0, 0);

		List<Rect> rectList = new ArrayList<Rect>();
		Mat v = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(outmat, contours, v, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		double minSize = 100;
		Rect r = null;

		for (int idx = 0; idx < contours.size(); idx++) {
			Mat contour = contours.get(idx);
			double contourarea = Imgproc.contourArea(contour);
			if (contourarea > minSize) {
				r = Imgproc.boundingRect(contours.get(idx));
				rectList.add(r);
			}
		}
		
		return rectList;
	}

	public static Rect[] detectVehicles(Mat outmat) {

		Imgproc.GaussianBlur(outmat, outmat, new Size(9, 9), 0, 0);

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

	public static void draw(List<Rect> list, Mat frame)
	{
		for (Rect rect : list) {
			Core.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);

			Core.putText(frame,
					"( x = " + 0.5 * (rect.tl().x + rect.br().x) + ", y = "
							+ 0.5 * (rect.tl().y + rect.br().y) + " )",
					new Point(0.5 * (rect.tl().x + rect.br().x), 0.5 * (rect.tl().y + rect.br().y)),
					1, 1, new Scalar(255, 255, 255));
		}
	}
	
	public static void draw(Rect[] rectArr, Mat frame) {
		Rect max = rectArr[0];
		Rect secondMax = rectArr[1];
		if (max != null) {
			Core.rectangle(frame, max.tl(), max.br(), new Scalar(0, 255, 0), 2);

			Core.putText(frame,
					"( x = " + 0.5 * (max.tl().x + max.br().x) + ", y = " + 0.5 * (max.tl().y + max.br().y) + " )",
					new Point(0.5 * (max.tl().x + max.br().x), 0.5 * (max.tl().y + max.br().y)), 1, 1,
					new Scalar(255, 255, 255));
		}

		if (secondMax != null) {
			Core.rectangle(frame, secondMax.tl(), secondMax.br(), new Scalar(0, 255, 0), 2);

			Core.putText(frame,
					"( x = " + 0.5 * (secondMax.tl().x + secondMax.br().x) + ", y = "
							+ 0.5 * (secondMax.tl().y + secondMax.br().y) + " )",
					new Point(0.5 * (secondMax.tl().x + secondMax.br().x), 0.5 * (secondMax.tl().y + secondMax.br().y)),
					1, 1, new Scalar(255, 255, 255));
		}

		if (max != null && secondMax != null) {
			Core.arrowedLine(frame, new Point(0.5 * (max.tl().x + max.br().x), 0.5 * (max.tl().y + max.br().y)),
					new Point(0.5 * (secondMax.tl().x + secondMax.br().x), 0.5 * (secondMax.tl().y + secondMax.br().y)),
					new Scalar(0, 255, 0));
		}
	}
}
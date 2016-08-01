package indoorPositioning;

import org.opencv.core.Point;

public class ObjectCircle {
	private Point point;
	private int radius;
	private Point [] direction;
	
	public ObjectCircle(Point point, int radius)
	{
		this.point = point;
		this.radius = radius;
	}
	
	public ObjectCircle(Point point, int radius, Point [] direction)
	{
		this.point = point;
		this.radius = radius;
		this.direction = direction;
	}
	
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public Point [] getDirection() {
		return direction;
	}
	public void setDirection(Point [] direction) {
		this.direction = direction;
	}

}

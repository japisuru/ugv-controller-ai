package math;

import terrain.Position;

public class Line {
	
	private Position start;
	private Position end;
	double slope;
	
	public Line(Position start, Position end)
	{
		this.start = start;
		this.end = end;
		this.slope = (this.end.getY() - this.start.getY()) / (this.end.getX() - this.start.getX());
	}

	public double getSlope()
	{
		return slope;
	}
	
	public Position calculatePointOnLine(double distanceFromStart)
	{
		return new Position(distanceFromStart * Math.cos(slope), distanceFromStart * Math.sin(slope), 0);
	}
}

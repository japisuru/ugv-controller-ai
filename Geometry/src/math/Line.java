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
		if(this.end.getX() - this.start.getX() == 0)
			this.slope = Math.PI / 2;
		else
			this.slope = (this.end.getY() - this.start.getY()) / (this.end.getX() - this.start.getX());
	}

	public double getSlope()
	{
		return slope;
	}
	
	public Position calculatePointOnLine(double distanceFromStart)
	{
		return new Position(start.getX() + (distanceFromStart * Math.cos(slope)), start.getY() + (distanceFromStart * Math.sin(slope)), 0);
	}
	
	public String toString() 
	{ 
        return "Start: " + start.getX() + "," + start.getY() + "," + start.getZ() + " --- " + "End: " + end.getX() + "," + end.getY() + "," + end.getZ() + " --- " + "Slope: " + slope;
    }
}

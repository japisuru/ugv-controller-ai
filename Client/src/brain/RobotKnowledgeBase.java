package brain;

import java.util.ArrayList;

import terrain.Position;

public class RobotKnowledgeBase 
{
	double visibilityDistance;
	double visibilityAngle;
	
	private ArrayList<Position> obstacles = new ArrayList<Position>();
	private ArrayList<Position> neighbors = new ArrayList<Position>();
	
	private Position currentPosition;
	private Position initialPosition;
	private Position targetPosition;
	
	public RobotKnowledgeBase(double visibilityDistance, double visibilityAngle, Position initialPosition, Position targetPosition)
	{
		this.visibilityDistance = visibilityDistance;
		this.visibilityAngle = visibilityAngle;
	}
	
	public void addObstacle(Position position)
	{
		for(Position pos : obstacles)
		{
			if(pos.equals(position))
				return;
		}
		obstacles.add(position);
	}
	
	public void removeObstacle(Position position)
	{
		obstacles.remove(position);
	}
	
	public ArrayList<Position> getVisibleObstacles()
	{
		//have to write a algorithm to get only visible obstacles. Upto now it's returning all obstacles.
		return obstacles;
	}
	
	public void addNeighbor(Position position)
	{
		neighbors.add(position);
	}
	
	public void removeNeighbor(Position position)
	{
		neighbors.remove(position);
	}
	
	public void removeAllNeighbors()
	{
		neighbors.clear();
	}
	
	public ArrayList<Position> getVisibleNeighbors()
	{
		//have to write a algorithm to get only visible neighbors. Upto now it's returning all obstacles.
		return neighbors;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public Position getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(Position initialPosition) {
		this.initialPosition = initialPosition;
	}

	public Position getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(Position targetPosition) {
		this.targetPosition = targetPosition;
	}
}

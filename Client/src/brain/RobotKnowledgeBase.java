package brain;

import java.util.ArrayList;

import terrain.Position;

public class RobotKnowledgeBase 
{
	int agentId;
	
	double visibilityDistance;
	double visibilityAngle;
	
	private ArrayList<Position> obstacles = new ArrayList<Position>();
	private ArrayList<Position> neighbors = new ArrayList<Position>();
	
	private Position currentPosition;
	private Position initialPosition;
	private Position targetPosition;
	
	private ArrayList<Position> targetPositions = new ArrayList<Position>();
	
	public RobotKnowledgeBase(int agentId, double visibilityDistance, double visibilityAngle, Position initialPosition, ArrayList<Position> targetPositions)
	{
		this.agentId = agentId;
		this.visibilityDistance = visibilityDistance;
		this.visibilityAngle = visibilityAngle;
		this.initialPosition = initialPosition;
		this.currentPosition = initialPosition;
		this.setTargetPositions(targetPositions);
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Created knowledge base");
	}
	
	public void addObstacle(Position position)
	{
		for(Position pos : obstacles)
		{
			if(pos.equals(position))
				return;
		}
		obstacles.add(position);
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Added obstacle");
	}
	
	public void removeObstacle(Position position)
	{
		System.out.println("Removed obstacles");
		obstacles.remove(position);
	}
	
	public ArrayList<Position> getVisibleObstacles()
	{
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Returened obstacles");
		//have to write a algorithm to get only visible obstacles. Upto now it's returning all obstacles.
		return obstacles;
	}
	
	public void addNeighbor(Position position)
	{
		neighbors.add(position);
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Added neighbor");
	}
	
	public void removeNeighbor(Position position)
	{
		neighbors.remove(position);
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Removed neghbor");
	}
	
	public void removeAllNeighbors()
	{
		neighbors.clear();
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Removed all neghbors");
	}
	
	public ArrayList<Position> getVisibleNeighbors()
	{
		//have to write a algorithm to get only visible neighbors. Upto now it's returning all obstacles.
		ArrayList<Position> newNeighors = new ArrayList<Position>();
		for(Position pos : neighbors)
		{
			if(pos.getDistance(currentPosition) < 50)
			{
				newNeighors.add(pos);
			}
		}
		return newNeighors;
	}

	public Position getCurrentPosition() {
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Returned current position");
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Setted position");
	}

	public Position getInitialPosition() {
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Returned initial position");
		return initialPosition;
	}

	public void setInitialPosition(Position initialPosition) {
		this.initialPosition = initialPosition;
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Setted initial positon");
	}

	public ArrayList<Position> getTargetPositions() {
		return targetPositions;
	}

	public void setTargetPositions(ArrayList<Position> targetPositions) {
		this.targetPositions = targetPositions;
	}

	public Position getTargetPosition() {
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Returned target positon");
		return targetPosition;
	}

	public void setTargetPosition(Position targetPosition) {
		this.targetPosition = targetPosition;
		System.out.println(agentId + " --> " + "RobotKnowledgeBase" + " --> " + "Setted target positon");
	}
}

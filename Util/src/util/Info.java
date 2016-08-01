package util;

import java.util.ArrayList;

import terrain.Position;

public class Info {
	
	public static Position[] currentPositions = { new Position(100.0, 2.0, 0.0), new Position(130.0, 2.0, 0.0),
			new Position(170.0, 2.0, 0.0), new Position(250.0, 2.0, 0.0), new Position(280.0, 2.0, 0.0),
			new Position(300.0, 2.0, 0.0) };
	
	//public static Position targetPosition = new Position(500.0, 780, 0.0);
	public static Position[] targetPositions = {new Position(500.0, 780, 0.0), new Position(700.0, 780, 0.0) };
	
	public static int numOfRobotAgents = currentPositions.length;
	
	public static String serverHost = "localhost";
	
	public static int serverPort = 1978;
	
	public static Position[] obstaclePositions = {new Position(600.0, 600, 0.0), 
			new Position(300.0, 200, 0.0), new Position(500.0, 200, 0.0) 
			,new Position(300.0, 250, 0.0), new Position(470.0, 250, 0.0)
			,new Position(350.0, 280, 0.0), new Position(470.0, 280, 0.0)
			, new Position(350.0, 300, 0.0), new Position(450.0, 300, 0.0)
			, new Position(350.0, 340, 0.0), new Position(450.0, 340, 0.0)
			, new Position(400.0, 370, 0.0), new Position(450.0, 370, 0.0)
			, new Position(400.0, 400, 0.0), new Position(450.0, 400, 0.0), new Position(480.0, 400, 0.0)};

}

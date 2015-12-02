package util;

import terrain.Position;

public class Info {
	
	public static Position[] currentPositions = { new Position(100.0, 2.0, 0.0), new Position(130.0, 2.0, 0.0),
			new Position(170.0, 2.0, 0.0), new Position(250.0, 2.0, 0.0), new Position(280.0, 2.0, 0.0),
			new Position(300.0, 2.0, 0.0) };
	
	public static Position targetPosition = new Position(500.0, 780, 0.0);
	
	public static int numOfRobotAgents = currentPositions.length;
	
	public static String serverHost = "localhost";
	
	public static int serverPort = 1978;

}

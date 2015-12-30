package brain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import terrain.Position;
import util.MyFormatter;

public class ThinkingMemory {

	private Logger logger;
	private FileHandler fh;
	
	public ThinkingMemory(String fileName)
	{
		try {
			fh = new FileHandler("/home/isuru/MSC-AI/Project/Memory/" + fileName + ".log");
			logger = Logger.getAnonymousLogger();
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			
			MyFormatter formatter = new MyFormatter();  
	        fh.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
          
	}
	
	public void memorize(Position currentPosition, Position futurePosition, Position initialPosition, Position targetPosition, ArrayList<Position> neighborPositions, ArrayList<Position> obstaclePositions)
	{
		String tempNeigbors = "[";
		for(Position pos : neighborPositions)
		{
			if(tempNeigbors.equals("["))
			{
				tempNeigbors = tempNeigbors + pos.toString();
			}
			else
			{
				tempNeigbors = tempNeigbors + " " + pos.toString();
			}			
		}
		tempNeigbors = tempNeigbors + "]";
		
		String tempObstacles = "[";
		for(Position pos : obstaclePositions)
		{
			if(tempObstacles.equals("["))
			{
				tempObstacles = tempObstacles + pos.toString();
			}
			else
			{
				tempObstacles = tempObstacles + " " + pos.toString();
			}			
		}
		tempObstacles = tempObstacles + "]";
		
		logger.info(currentPosition.toString() + "_" + futurePosition.toString() + "_" + initialPosition.toString() + "_" + targetPosition.toString() + "_" + tempNeigbors + "_" + tempObstacles);
	}
}

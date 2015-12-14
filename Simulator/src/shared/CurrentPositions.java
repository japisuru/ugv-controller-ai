package shared;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import terrain.Position;
import util.Info;

public class CurrentPositions {

	public static ConcurrentMap concurrentMap = new ConcurrentHashMap();
	
	public static void init()
	{
		for (int i = 0; Info.currentPositions.length > i; i++) 
		{
			concurrentMap.put(i, Info.currentPositions[i]);
		}		
	}
}

package terrain;

import java.util.ArrayList;

public class PositionUtil 
{
	
	public static Position getCenter(ArrayList<Position> positions)
	{
		double meanX = 0, meanY = 0, meanZ = 0;
        for(int i = 0; i < positions.size(); i++)
        {
            meanX = meanX + positions.get(i).getX();
            meanY = meanY + positions.get(i).getY();
            meanZ = meanZ + positions.get(i).getZ();
        }
        return new Position(meanX/positions.size(), meanY/positions.size(), meanZ/positions.size());
	}
	
	public static Position getCenter(Position[] positions)
	{
		double meanX = 0, meanY = 0, meanZ = 0;
        for(int i = 0; i < positions.length; i++)
        {
            meanX = meanX + positions[i].getX();
            meanY = meanY + positions[i].getY();
            meanZ = meanZ + positions[i].getZ();
        }
        return new Position(meanX/positions.length, meanY/positions.length, meanZ/positions.length);
	}
}


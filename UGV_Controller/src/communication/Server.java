package communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

import terrain.Position;

public interface Server extends Remote{
	
	public boolean update(int id, Position positionOfObstacle) throws RemoteException;
}

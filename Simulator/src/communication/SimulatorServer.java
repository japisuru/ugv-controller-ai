package communication;

import java.util.concurrent.atomic.AtomicIntegerArray;

import processing.MyProcessingSketch;
import terrain.Position;
import java.rmi.*;

public interface SimulatorServer extends Remote{
	
	public boolean update(int id, Position position) throws RemoteException;

}

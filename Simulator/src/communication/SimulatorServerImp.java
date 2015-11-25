package communication;

import java.rmi.RemoteException;

import processing.MyProcessingSketch;
import terrain.Position;

public class SimulatorServerImp implements SimulatorServer{

	@Override
	public boolean update(int id, Position position) throws RemoteException {
		MyProcessingSketch.currentPositions[id] = position;
		return true;
	}

}

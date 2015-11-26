package main;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import processing.core.PApplet;


public class Main {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        
		PApplet.main(new String[] { "--present", "processing.MyProcessingSketch" });
	}

}

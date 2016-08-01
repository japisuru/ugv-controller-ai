package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.Simulator;
import shared.CurrentPositions;
import util.Info;



public class AgentHandlingServer {

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        
        CurrentPositions.init();
        Simulator.start();
        
        try {
            serverSocket = new ServerSocket(Info.serverPort);
            System.out.println("AgentHandlingServer -> Server created");
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
            	System.out.println("AgentHandlingServer -> Waiting for clients");
                socket = serverSocket.accept();
                System.out.println("AgentHandlingServer -> A client connected");
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            
            // new threa for a client
            
            new AgentHandler(socket).start();
        }
        
    }
}

package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.Info;



public class AgentHandlingServer {

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        
        try {
            serverSocket = new ServerSocket(Info.serverPort);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new threa for a client
            new AgentHandler(socket).start();
        }
    }
}

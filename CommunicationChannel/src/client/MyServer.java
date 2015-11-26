package client;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer extends Thread {

	public MyServer() {

	}

	public void run() {
		try {
			ServerSocket ss = new ServerSocket(6666);
			while(true)
			{
			Socket s = ss.accept();// establishes connection
			DataInputStream dis = new DataInputStream(s.getInputStream());
			String str = (String) dis.readUTF();
			System.out.println("message= " + str);
			ss.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}

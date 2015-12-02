package multiagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import jade.core.Agent;
import terrain.Position;
import util.Info;

public class RobotAgent extends Agent {
	

    PrintWriter out;
    int agentId;
    int data;

	public RobotAgent(int agentID) throws UnknownHostException, IOException 
	{
		this.agentId = agentID;
		Socket socket = new Socket(Info.serverHost, Info.serverPort);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		new Thread()
		{	
			public void run()
			{
				String line;
				while(true)
				{
					try {
						line = in.readLine();
						data = 0;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			}
		};
		
		out = new PrintWriter(socket.getOutputStream(), true);

	}

	protected void setup() {
		System.out.println("Hello World. ");
		System.out.println("My name is " + getLocalName());
	}
	
	private void write(Position position)
	{
		out.println(agentId + "-" + position.getX() + "," + position.getY() + "," + position.getZ() );
	}
}

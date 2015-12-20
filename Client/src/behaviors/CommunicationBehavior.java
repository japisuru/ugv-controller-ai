package behaviors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import brain.RobotKnowledgeBase;
import genetic.PathPlanner;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import multiagent.RobotAgent;
import terrain.Position;
import util.Info;
import util.MyLog;

public class CommunicationBehavior extends SimpleBehaviour {

	private int agentId;
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	RobotAgent agent;
	RobotKnowledgeBase rkb;
	//PrintWriter out;
	MyLog mylog;
	PathPlanner pathPlanner;
	int collectedCount;
	BufferedReader in = null;
	BufferedWriter brout = null;

	public CommunicationBehavior(MyLog myLog, RobotAgent agent, int agentId, RobotKnowledgeBase rkb)
			throws UnknownHostException, IOException {
		super(agent);
		this.mylog = myLog;
		this.agent = agent;
		this.agentId = agentId;
		this.rkb = rkb;
		this.pathPlanner = new PathPlanner();
		this.collectedCount = 0;
		init();
		// mylog.log("testLogging message");
		mylog.log("Inialized");
	}

	public void init() throws UnknownHostException, IOException {

		new Thread() {
			public void run() {
				Socket socket;
				try {
					socket = new Socket(Info.serverHost, Info.serverPort);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					//out = new PrintWriter(socket.getOutputStream(), true);
					brout = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					mylog.log("Before seperate thread");
					mylog.log("Inside seperate thread");
					String line;
					while (true) {
						try {
							line = in.readLine();

							if (line != null) {
								String[] spt1 = line.split("_");
								sleep(1000);
								System.out.println(
										agentId + " --> " + "CommunicationBehavior --> " + spt1[0] + " received");

								if (spt1[0] == "Obstacle") {
									mylog.log("Received Obstacle from sever");
									mylog.testLog("Received Obstacle from sever");
									updateObstacles(spt1[1]);
									//calculateNextPosition();
								}
								if (spt1[0] == "Completed")
								{
									mylog.log("Received Completed from server");
									mylog.testLog("Received Completed from server");
								}
								
								calculateNextPosition();
							}

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}.start();

	}

	private void updateObstacles(String msg) {
		String[] spt2 = msg.split(",");
		Position newObstacle = new Position(Double.parseDouble(spt2[0]), Double.parseDouble(spt2[1]),
				Double.parseDouble(spt2[2]));
		rkb.addObstacle(newObstacle);
		mylog.log("Updated obstacles");
	}

	void calculateNextPosition() {
		// ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
		// aclMessage.setContent("Collect-" + agentId + "-Empty");
		// aclMessage.addReceiver(new AID("agent" + agentId, AID.ISLOCALNAME));
		// myAgent.send(aclMessage);
		// mylog.log("CommunicationBehavior -> Sent Collect to " + agentId);

		ACLMessage reqMessage = new ACLMessage(ACLMessage.INFORM);
		reqMessage.setContent("PositionRequest_" + agentId + "_Empty");
		for (int i = 0; i < Info.numOfRobotAgents; i++) {
			if (i != agentId) {
				AID driver = new AID("agent" + i + "@" + myAgent.getHap(), AID.ISGUID);
				reqMessage.addReceiver(driver);
				mylog.log("Sent position request to " + i);
			}
		}
		mylog.log("Sent position request to all others");
		this.rkb.removeAllNeighbors();
		myAgent.send(reqMessage);
	}

	public void sendPositionToSimulator(Position position) {
		try {
			brout.write("SimulateAgent" + "_" + agentId + "_" + position.getX() + "," + position.getY() + ","
					+ position.getZ());
			brout.newLine();
	        brout.flush();
	        mylog.log("Sent new position to simulator: " + position.toString());
			mylog.testLog("Sent new position to simulator: " + position.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void sendPositionToOthers(Position position) {

		ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
		aclMessage.setContent("PositionUpdate" + "_" + agentId + "_" + position.getX() + "," + position.getY() + ","
				+ position.getZ());

		for (int i = 0; i < Info.numOfRobotAgents; i++)
			if (i != agentId)
				aclMessage.addReceiver(new AID("agent" + i, AID.ISLOCALNAME));

		myAgent.send(aclMessage);
		mylog.log("Sent new position to other agents");
	}

	@Override
	public void action() {
		ACLMessage aclMessage = myAgent.receive(mt);

		if (aclMessage != null) {
			String data[] = aclMessage.getContent().split("_");
			String event = data[0];
			int sender = Integer.parseInt(data[1]);
			String content = data[2];

			mylog.log("ACTION" + " --> " + event + " _ " + sender  + " _ " + content);

			if (event.compareTo("PositionResponse") == 0) 
			{
				mylog.log("Received PositionResponse from " + sender);
				
				this.rkb.addNeighbor(new Position(content));
				collectedCount++;
				if (collectedCount % (Info.numOfRobotAgents - 1) == 0) 
				{
					pathPlanner.calculateNextPosition(rkb);
					mylog.testLog("Calculated next position");
					sendPositionToSimulator(rkb.getCurrentPosition());
					
					mylog.log("Calculated next position");
				}				
			}
			if (event.compareTo("PositionRequest") == 0) 
			{
				mylog.log("Recieved PositionRequest from " + sender);
				ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
				reply.setContent("PositionResponse_" + agentId + "_" + rkb.getCurrentPosition().toString());
				reply.addReceiver(new AID("agent" + sender + "@" + myAgent.getHap(), AID.ISGUID));
				myAgent.send(reply);
				mylog.log("Sent Position response to " + sender);
			}
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}

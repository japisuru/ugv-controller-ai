package behaviors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import brain.RobotKnowledgeBase;
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
	PrintWriter out;
	MyLog mylog;

	public CommunicationBehavior(MyLog myLog, RobotAgent agent, int agentId, RobotKnowledgeBase rkb)
			throws UnknownHostException, IOException {
		super(agent);
		this.mylog = myLog;
		this.agent = agent;
		this.agentId = agentId;
		this.rkb = rkb;
		init();
		//mylog.log("testLogging message");
		mylog.log("CommunicationBehavior -> Inialized");
	}

	public void init() throws UnknownHostException, IOException {

		new Thread() {
			public void run() {
				Socket socket;
				try {
					socket = new Socket(Info.serverHost, Info.serverPort);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream(), true);
					mylog.log("CommunicationBehavior --> Before seperate thread");
					mylog.log("CommunicationBehavior --> Inside seperate thread");
					String line;
					while (true) {
						try {
							line = in.readLine();

							if (line != null) {
								String[] spt1 = line.split("-");

								System.out.println(agentId + " --> " + "CommunicationBehavior --> " + spt1[0] + " received");

								if (spt1[0] == "Obstacle") {
									mylog.log("CommunicationBehavior --> Received Obstacle from sever");
									updateObstacles(spt1[1]);
									calculateNextPosition();
								}
								if (spt1[0] == "Completed")
									mylog.log("CommunicationBehavior --> Received Completed from server");
								calculateNextPosition();
							}

						} catch (IOException e) {
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
		mylog.log("CommunicationBehavior -> Updated obstacles");
	}

	void calculateNextPosition() {
		ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
		aclMessage.setContent("Collect-" + agentId + "-Empty");
		aclMessage.addReceiver(new AID("agent" + agentId, AID.ISLOCALNAME));
		myAgent.send(aclMessage);
		mylog.log("CommunicationBehavior -> Sent Collect to " + agentId);
	}

	public void sendPositionToSimulator(Position position) {
		out.println("SimulateAgent" + "-" + agentId + "-" + position.getX() + "," + position.getY() + "," + position.getZ());
		mylog.log("CommunicationBehavior -> Sent new position to simulator");
	}

	public void sendPositionToOthers(Position position) {

		ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
		aclMessage.setContent("PositionUpdate" + "-" +agentId + "-" + position.getX() + "," + position.getY() + "," + position.getZ());

		for (int i = 0; i < Info.numOfRobotAgents; i++)
			if (i != agentId)
				aclMessage.addReceiver(new AID("agent" + i, AID.ISLOCALNAME));

		myAgent.send(aclMessage);
		mylog.log("CommunicationBehavior -> Sent new position to other agents");
	}

	@Override
	public void action() {
		ACLMessage aclMessage = myAgent.receive(mt);

		if (aclMessage != null) 
		{
			String data[] = aclMessage.getContent().split("-");
			String event = data[0];
			int sender = Integer.parseInt(data[1]);
			String content = data[2];
			
			if (event.compareTo("SendResult") == 0) 
			{
				mylog.log("CommunicationBehavior -> Received SendResult from " + sender);
				//sendPositionToOthers(rkb.getCurrentPosition()); don't want to sent. because when some agent is calculating new position, always they will request neighbor's positions
				sendPositionToSimulator(rkb.getCurrentPosition());
			}
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}

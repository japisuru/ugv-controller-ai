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

public class CommunicationBehavior extends SimpleBehaviour {

	private int agentId;
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	RobotAgent agent;
	RobotKnowledgeBase rkb;
	PrintWriter out;

	public CommunicationBehavior(RobotAgent agent, int agentId, RobotKnowledgeBase rkb) {
		super(agent);
		this.agent = agent;
		this.agentId = agentId;
		this.rkb = rkb;
	}

	public void init() throws UnknownHostException, IOException {
		Socket socket = new Socket(Info.serverHost, Info.serverPort);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		new Thread() {
			public void run() {
				String line;
				while (true) {
					try {
						line = in.readLine();

						String[] spt1 = line.split("-");
						if (spt1[0] == "Obstacle") {
							updateObstacles(line);
							calculateNextPosition();
						}
						if (spt1[0] == "Completed")
							calculateNextPosition();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		};
		out = new PrintWriter(socket.getOutputStream(), true);
	}

	private void updateObstacles(String msg) {
		String[] spt2 = msg.split(",");
		Position newObstacle = new Position(Double.parseDouble(spt2[0]), Double.parseDouble(spt2[1]),
				Double.parseDouble(spt2[2]));
		rkb.addObstacle(newObstacle);
	}

	void calculateNextPosition() {
		ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
		aclMessage.setContent("Collect-");
		aclMessage.addReceiver(new AID("agent" + agentId, AID.ISLOCALNAME));
		myAgent.send(aclMessage);
	}

	public void sendPositionToSimulator(Position position) {
		out.println(agentId + "-" + position.getX() + "," + position.getY() + "," + position.getZ());
	}

	public void sendPositionToOthers(Position position) {

		ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
		aclMessage.setContent(agentId + "-" + position.getX() + "," + position.getY() + "," + position.getZ());

		for (int i = 0; i < Info.numOfRobotAgents; i++)
			if (i != agentId)
				aclMessage.addReceiver(new AID("agent" + i, AID.ISLOCALNAME));

		myAgent.send(aclMessage);
	}

	@Override
	public void action() {

		ACLMessage aclMessage = myAgent.receive(mt);
		String data[] = aclMessage.getContent().split("-");
		if (data[0].compareTo("SendResult") == 0) {
			sendPositionToOthers(rkb.getCurrentPosition());
			sendPositionToSimulator(rkb.getCurrentPosition());
		}

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}

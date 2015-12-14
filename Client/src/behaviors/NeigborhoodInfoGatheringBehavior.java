package behaviors;

import brain.RobotKnowledgeBase;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import multiagent.RobotAgent;
import terrain.Position;
import util.Info;
import util.MyLog;

public class NeigborhoodInfoGatheringBehavior extends SimpleBehaviour {

	private int agentId;
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	RobotAgent agent;
	int collectedCount = 0;
	RobotKnowledgeBase rkb;
	MyLog mylog;

	public NeigborhoodInfoGatheringBehavior(MyLog myLog, RobotAgent agent, int agentId, RobotKnowledgeBase rkb) {
		super(agent);
		this.mylog = myLog;
		this.agent = agent;
		this.agentId = agentId;
		this.rkb = rkb;
		mylog.log("NeigborhoodInfoGatheringBehavior -> Inialized");
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

			if (event.compareTo("Collect") == 0) 
			{
				mylog.log("NeigborhoodInfoGatheringBehavior -> Received Collect message from " + sender);
				ACLMessage reqMessage = new ACLMessage(ACLMessage.INFORM);
				reqMessage.setContent("PositionRequest-" + agentId + "-Empty");
				for (int i = 0; i < Info.numOfRobotAgents; i++) 
				{
					if (i != agentId) 
					{
						AID driver = new AID("agent" + i + "@" + myAgent.getHap(), AID.ISGUID);
						reqMessage.addReceiver(driver);
						mylog.log("NeigborhoodInfoGatheringBehavior -> Sent position request to " + i);
					}
				}
				mylog.log("NeigborhoodInfoGatheringBehavior -> Sent position request to all others");
				this.rkb.removeAllNeighbors();
				myAgent.send(reqMessage);			
				collectedCount = 0;
			}
			if (event.compareTo("PositionRequest") == 0) 
			{
				mylog.log("NeigborhoodInfoGatheringBehavior -> Recieved PositionRequest from " + sender);
				ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
				reply.setContent("PositionResponse-" + agentId + "-" + rkb.getCurrentPosition().toString());
				reply.addReceiver(new AID("agent" + sender + "@" + myAgent.getHap(), AID.ISGUID));
				myAgent.send(reply);
				mylog.log("NeigborhoodInfoGatheringBehavior -> Sent Position response to " + sender);
			}
			if (event.compareTo("PositionResponse") == 0) 
			{
				mylog.log("NeigborhoodInfoGatheringBehavior -> Received PositionResponse from " + sender);
				this.rkb.addNeighbor(new Position(content));
				collectedCount++;
				if (collectedCount == Info.numOfRobotAgents - 1) 
				{
					ACLMessage reqMessage = new ACLMessage(ACLMessage.INFORM);
					reqMessage.setContent("CalculateNextPosition-" + agentId + "-Empty");
					AID driver = new AID("agent" + agentId + "@" + myAgent.getHap(), AID.ISGUID);
					reqMessage.addReceiver(driver);
					myAgent.send(reqMessage);
					mylog.log("NeigborhoodInfoGatheringBehavior -> Sent CalculateNextPosition to " + agentId);
				}				
			}
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

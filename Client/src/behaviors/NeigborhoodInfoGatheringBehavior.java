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
		mylog.log("Inialized");
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

			mylog.log("ACTION" + " --> " + event + " - " + sender  + " - " + content);
			
			if (event.compareTo("PositionRequest") == 0) 
			{
				mylog.log("Recieved PositionRequest from " + sender);
				ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
				reply.setContent("PositionResponse-" + agentId + "-" + rkb.getCurrentPosition().toString());
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

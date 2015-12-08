package behaviors;

import brain.RobotKnowledgeBase;
import genetic.PathPlanner;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import multiagent.RobotAgent;
import util.Info;

public class RobotWalkingBehavior extends SimpleBehaviour {

	PathPlanner pathPlanner;
	RobotAgent agent;
	int agentId;
	RobotKnowledgeBase rkb;

	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

	public RobotWalkingBehavior(RobotAgent a, int agentId, RobotKnowledgeBase rkb) {
		super(a);
		agent = a;
		this.agentId = agentId;
		this.rkb = rkb;
		pathPlanner = new PathPlanner();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {

		ACLMessage aclMessage = myAgent.receive(mt);		
		String data [] = aclMessage.getContent().split("-");
		
		if (data[0].compareTo("CalculateNextPosition") == 0) 
		{
			pathPlanner.calculateNextPosition(rkb.getCurrentPosition(), rkb.getInitialPosition(), rkb.getTargetPosition());
		}
		
		ACLMessage reqMessage = new ACLMessage(ACLMessage.INFORM);
		reqMessage.setContent("SendResult-");
		AID driver = new AID("agent" + agentId + "@" + myAgent.getHap(), AID.ISGUID);
		reqMessage.addReceiver(driver);				
		myAgent.send(reqMessage);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

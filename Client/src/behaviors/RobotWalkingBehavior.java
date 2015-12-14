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
import util.MyLog;

public class RobotWalkingBehavior extends SimpleBehaviour {

	PathPlanner pathPlanner;
	RobotAgent agent;
	int agentId;
	RobotKnowledgeBase rkb;
	MyLog mylog;

	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

	public RobotWalkingBehavior(MyLog myLog, RobotAgent a, int agentId, RobotKnowledgeBase rkb) {
		super(a);
		this.mylog = myLog;
		agent = a;
		this.agentId = agentId;
		this.rkb = rkb;
		pathPlanner = new PathPlanner();
		mylog.log("RobotWalkingBehavior -> Inialized");
		// TODO Auto-generated constructor stub
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

			if (event.compareTo("CalculateNextPosition") == 0) 
			{
				mylog.log("RobotWalkingBehavior -> Received CalculateNextPosition from " + sender);
				pathPlanner.calculateNextPosition(rkb.getCurrentPosition(), rkb.getInitialPosition(),
						rkb.getTargetPosition());
				mylog.log("RobotWalkingBehavior -> Calculated next position");
				
				ACLMessage reqMessage = new ACLMessage(ACLMessage.INFORM);
				reqMessage.setContent("SendResult-" + agentId + "-Empty");
				AID driver = new AID("agent" + agentId + "@" + myAgent.getHap(), AID.ISGUID);
				reqMessage.addReceiver(driver);
				myAgent.send(reqMessage);
				mylog.log("RobotWalkingBehavior -> Sent SendResult to " + agentId);
			}		
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

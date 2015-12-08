package behaviors;

import brain.RobotKnowledgeBase;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import multiagent.RobotAgent;
import terrain.Position;
import util.Info;

public class NeigborhoodInfoGatheringBehavior extends SimpleBehaviour{

	private int agentId;
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	RobotAgent agent;
	int collectedCount = 0;
	RobotKnowledgeBase rkb;
	
	public NeigborhoodInfoGatheringBehavior(RobotAgent agent, int agentId, RobotKnowledgeBase rkb)
	{
		super(agent);
		this.agent = agent;
		this.agentId = agentId;
		this.rkb = rkb;
	}
	
	@Override
	public void action() {
		
		ACLMessage aclMessage = myAgent.receive(mt);
		
		String data [] = aclMessage.getContent().split("-");
		
		if (data[0].compareTo("Collect") == 0) {
			ACLMessage reqMessage = new ACLMessage(ACLMessage.INFORM);
			reqMessage.setContent("PositionRequest-");
			for (int i = 0; i < Info.numOfRobotAgents; i++) {
				if (i != agentId) 
				{
					AID driver = new AID("agent" + i + "@" + myAgent.getHap(), AID.ISGUID);
					reqMessage.addReceiver(driver);				
				}
			}
			myAgent.send(reqMessage);
			this.rkb.removeAllNeighbors();
			collectedCount = 0;
		}
		if (data[0].compareTo("PositionRequest") == 0) 
		{
			ACLMessage reply = new ACLMessage( ACLMessage.INFORM );
            reply.setContent( "PositionResponse-" + agentId + "-" + rkb.getCurrentPosition().toString() );
            reply.addReceiver( aclMessage.getSender() );
            myAgent.send(reply);
		}
		if (data[0].compareTo("PositionResponse") == 0) 
		{
			this.rkb.addNeighbor(new Position(data[1]));
			collectedCount++;
			if(collectedCount == Info.numOfRobotAgents - 1)
			{
				ACLMessage reqMessage = new ACLMessage(ACLMessage.INFORM);
				reqMessage.setContent("CalculateNextPosition-");
				AID driver = new AID("agent" + agentId + "@" + myAgent.getHap(), AID.ISGUID);
				reqMessage.addReceiver(driver);				
				myAgent.send(reqMessage);
			}
		}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}

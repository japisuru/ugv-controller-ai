package multiagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import behaviors.CommunicationBehavior;
import behaviors.NeigborhoodInfoGatheringBehavior;
import behaviors.RobotWalkingBehavior;
import brain.RobotKnowledgeBase;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import terrain.Position;
import util.Info;

public class RobotAgent extends Agent {

	private int agentId;

	RobotKnowledgeBase rkb;

	protected void setup() {
		Object[] args = getArguments();
		String aid = String.valueOf(args[0]);
		agentId = Integer.parseInt(aid);

		rkb = new RobotKnowledgeBase(1, 0.5, Info.currentPositions[agentId], Info.targetPosition);
		
		addBehaviour(new NeigborhoodInfoGatheringBehavior(this, agentId, rkb));
		addBehaviour(new RobotWalkingBehavior(this, agentId, rkb));
		addBehaviour(new CommunicationBehavior(this, agentId, rkb));
		// addBehaviour(new RobotAgentBrain(this,2000));
	}
}

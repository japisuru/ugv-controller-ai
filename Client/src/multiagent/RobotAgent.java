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
import util.MyLog;

public class RobotAgent extends Agent {

	private int agentId;

	RobotKnowledgeBase rkb;

	protected void setup() {
		Object[] args = getArguments();
		String aid = String.valueOf(args[0]);
		agentId = Integer.parseInt(aid);

		//MyLog mylog = new MyLog(agentId + "_agent", "");
		
		rkb = new RobotKnowledgeBase(agentId, 1, 0.5, Info.currentPositions[agentId], Info.targetPosition);
			
		//addBehaviour(new RobotWalkingBehavior(mylog, this, agentId, rkb));
		try {
			addBehaviour(new CommunicationBehavior(new MyLog(agentId + "_agent_CommunicationBehavior", ""), this, agentId, rkb));
			//addBehaviour(new NeigborhoodInfoGatheringBehavior(new MyLog(agentId + "_agent_NeigborhoodInfoGatheringBehavior", ""), this, agentId, rkb));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// addBehaviour(new RobotAgentBrain(this,2000));
		System.out.println(agentId + " --> " + "RobotAgent" + " --> " + "Started and added all behaviors for agent " + agentId);
	}
}

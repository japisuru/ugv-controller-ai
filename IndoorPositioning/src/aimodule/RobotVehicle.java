package aimodule;

import java.util.ArrayList;

import brain.RobotKnowledgeBase;
import brain.ThinkingMemory;
import genetic.PathPlanner;
import navigation.RobotNavigator;
import terrain.Position;

public class RobotVehicle {
	private RobotKnowledgeBase rkb;
	private int agentId;
	private double visibilityDistance = 1;
	private double visibilityAngle = 0.5;;
	private PathPlanner pathPlanner;
	private Position temporaryTarget;
	private RobotNavigator rn;

	public RobotVehicle(int agentId) {
		this.agentId = agentId;
		this.rkb = new RobotKnowledgeBase(agentId, visibilityDistance, visibilityAngle, null, null,
				new ArrayList<Position>(), new ThinkingMemory(agentId + ""));
		this.rkb.setCurrentPosition(null);
		this.pathPlanner = new PathPlanner();
		rn = new RobotNavigator();
	}

	public void think(Position currentPosition, Position targetPosition, ArrayList<Position> obstaclePositions,
			ArrayList<Position> otherVehicles) {
		if (currentPosition != null && targetPosition != null) {
			
			this.rkb.removeAllNeighbors();
			for (Position pos : otherVehicles) {
				this.rkb.addNeighbor(pos);
			}

			this.rkb.removeAllObstacles();
			for (Position pos : obstaclePositions) {
				this.rkb.addObstacle(pos);
			}
			
			if (this.rkb.getInitialPosition() == null || this.rkb.getCurrentPosition() == null) {
				this.rkb.setInitialPosition(currentPosition);
			} else {
				this.rkb.setInitialPosition(this.rkb.getCurrentPosition());
			}

			this.rkb.setCurrentPosition(currentPosition);

			this.rkb.setTargetPosition(targetPosition);

			pathPlanner.calculateNextPosition(this.rkb);

			this.temporaryTarget = this.rkb.getCurrentPosition();
		}
	}

	public void navigate(int r_f_x, int r_f_y, int r_b_x, int r_b_y) {
		rn.navigate(this.agentId, r_f_x, r_f_y, r_b_x, r_b_y, (int) this.temporaryTarget.getX(),
				(int) this.temporaryTarget.getY());
	}

}

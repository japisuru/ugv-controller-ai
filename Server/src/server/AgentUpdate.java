package server;

import javax.naming.InterruptedNamingException;

public class AgentUpdate {
	
	private int agentId;
	private double x;
	private double y;
	private double z;
	
	public AgentUpdate(String msg)
	{
		String [] spt1 = msg.split("_");
		agentId = Integer.parseInt(spt1[1]);
		String [] spt2 = spt1[2].split(",");
		x = Double.parseDouble(spt2[0]);
		y = Double.parseDouble(spt2[1]);
		z = Double.parseDouble(spt2[2]);
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

}

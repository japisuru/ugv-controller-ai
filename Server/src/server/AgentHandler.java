package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import math.Line;
import shared.CurrentPositions;
import terrain.Position;
import util.MyLog;

public class AgentHandler extends Thread {
    protected Socket socket;
    private int clientId;
    BufferedReader brinp = null;
    BufferedWriter brout = null;
    private MyLog myLog = null;

    public AgentHandler(Socket clientSocket) {
        this.socket = clientSocket;
    }
    
    public void isObstacleDetected(Position position) throws IOException
    {
    	brout.write("Obstacle--" + position.getX() + "," + position.getY() + "," + position.getZ());
    	brout.newLine();               	
    	brout.flush();
    	System.out.println("AgentHandler -> Sent Obstacle to agent");
    }
    
    private void update(int agentId, Position position) throws InterruptedException
    {
    	Position currentPosition = (Position) CurrentPositions.concurrentMap.get(agentId);
    	Line line = new Line(currentPosition,position);
    	
    	if(myLog == null)
    	{
    		myLog = new MyLog("Server/" + agentId + "_agent_Server", "");
    	}	
    	
    	myLog.log("Line --> " + line.toString());
    	
    	while(true)
    	{
    		currentPosition = (Position) CurrentPositions.concurrentMap.get(agentId);
    		
    		if(currentPosition.getDistance(position) < 1.0)
    		{
    			CurrentPositions.concurrentMap.put(agentId, position );
    			return;
    		}
    		else
    		{
    			CurrentPositions.concurrentMap.put(agentId,line.calculatePointOnLine(1.0));
    		}
    		
    		myLog.log("Intermediate future Position: " + ((Position) CurrentPositions.concurrentMap.get(agentId)).toString());
    		
    		System.out.println("AgentHandler -> Position updated");
    		sleep(1000);
    	}
    }
    
    public void run() {
    	System.out.println("AgentHandler -> Server instance started for one client");
        InputStream inp = null;
        OutputStream out = null;
        
        try {
            inp = socket.getInputStream();
            out = socket.getOutputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            brout = new BufferedWriter(new OutputStreamWriter(out));
            sleep(2000);
            brout.write("Completed--");     	
            brout.newLine();
            brout.flush();
        	System.out.println("AgentHandler -> Sent initial Completed to agents");
        } catch (IOException | InterruptedException e) {
        	System.out.println("AgentHandler -> Initial sent failure");
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
//                    out.writeBytes(line + "\n\r");
//                    out.flush();
                	System.out.println("AgentHandler -> Received updated position from a agent");
                	AgentUpdate agentUpdate = new AgentUpdate(line);
                	
                	update(agentUpdate.getAgentId(), new Position(agentUpdate.getX(), agentUpdate.getY(), agentUpdate.getZ()));  
                	
                	System.out.println("AgentHandler -> Updated new position in shared position Map");
                	brout.write("Completed--");
                	brout.newLine();               	
                	brout.flush();
                	System.out.println("AgentHandler -> Sent Completed to agent");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
//        	try {
//				sleep(2000);
//				brout.write("Completed-Current");
//				brout.newLine();
//				System.out.println("AgentHandler -> Sent Completed to agents");
//				out.flush();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        	
        }
    }
}

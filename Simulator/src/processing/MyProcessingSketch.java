package processing;

import math.MathUtil;
import terrain.Position;
import terrain.PositionUtil;
import util.Info;
import processing.core.*;
import shared.CurrentPositions;

public class MyProcessingSketch extends PApplet {

	float x = 100;
	float y = 100;
	float angle1 = (float) 0.0;
	float segLength = 50;
	
	public void settings() {
		size(1400, 800);
	}

	public void setup() {
		// frameRate(1);
		strokeWeight((float) 1.0);
		stroke(255, 100);
		CurrentPositions.init();
	}

	public void draw() {
		
		background(0);

		if (!CurrentPositions.concurrentMap.isEmpty()) {
			System.out.println("*******************************************************************");
			for (int i = 0; i < CurrentPositions.concurrentMap.size(); i++) {

				x = (float) ((Position) (CurrentPositions.concurrentMap.get(i))).getX();
				y = (float) ((Position) (CurrentPositions.concurrentMap.get(i))).getY();

				System.out.print(x + "," + y + "  <-->  ");
				// segment(x, y, angle1);
				ellipseMode(CENTER);
				fill(192, 255, 62, 40);
				ellipse(x, y, 50, 50);			
				
				fill(255,255,0);
				ellipse(x, y, 20, 20);


				fill(0,255,0);
				for(Position pos : Info.targetPositions)
				{
					ellipse((float) pos.getX(), (float) pos.getY(), 20, 20);
				}
				
				fill(255,0,0);
				for(Position pos : Info.obstaclePositions)
				{
					ellipse((float) pos.getX(), (float) pos.getY(), 20, 20);
				}
			}
			System.out.println();
			System.out.println("*******************************************************************");
		}		
	}

	void segment(float x, float y, float a) {
		pushMatrix();
		translate(x, y);
		rotate(a);
		line(0, 0, segLength, 0);
		popMatrix();
	}
}

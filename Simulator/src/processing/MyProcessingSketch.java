package processing;

import math.MathUtil;
import terrain.Position;
import terrain.PositionUtil;
import util.Info;
import processing.core.*;

public class MyProcessingSketch extends PApplet {

	float x = 100;
	float y = 100;
	float angle1 = (float) 0.0;
	float segLength = 50;

	Position[] previousePositions = null;
	public static Position[] currentPositions = new Position[6];

	public void update() {
		previousePositions = currentPositions;
	}

	public void settings() {
		size(1400, 800);
	}

	public void setup() {
		// frameRate(1);
		strokeWeight((float) 20.0);
		stroke(255, 100);
	}

	public void draw() {
		
		background(0);

		if (currentPositions != null && previousePositions != null) {
			System.out.println("*******************************************************************");
			for (int i = 0; i < currentPositions.length; i++) {

				float dx = (float) currentPositions[i].getX() - (float) previousePositions[i].getX();
				float dy = (float) currentPositions[i].getY() - (float) previousePositions[i].getY();

				angle1 = atan2(dy, dx);

				x = (float) currentPositions[i].getX();
				y = (float) currentPositions[i].getY();

				System.out.print(x + "," + y + "  <-->  ");
				// segment(x, y, angle1);
				ellipse(x, y, 2, 2);

				// color c = color(255, 0, 0);
				fill(random(255));
				ellipse((float) Info.targetPosition.getX(), (float) Info.targetPosition.getY(), 2, 2);
			}
			System.out.println();
			System.out.println("*******************************************************************");
			update();
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

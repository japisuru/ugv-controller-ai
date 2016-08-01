package processing;

import math.MathUtil;
import terrain.Position;
import terrain.PositionUtil;
import util.Info;
import processing.core.*;
import shared.CurrentPositions;

public class KnowledgeBaseSketch extends PApplet {

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

		ellipseMode(CENTER);
		fill(192, 255, 62, 40);
		ellipse(x, y, 50, 50);
				
	}

	void segment(float x, float y, float a) {
		pushMatrix();
		translate(x, y);
		rotate(a);
		line(0, 0, segLength, 0);
		popMatrix();
	}
}

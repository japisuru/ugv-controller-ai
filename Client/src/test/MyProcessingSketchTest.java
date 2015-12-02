package test;

import terrain.PositionUtil;

import processing.core.PApplet;

public class MyProcessingSketchTest  extends PApplet {

	public void settings() {
		size(1400, 800);
	}
	
	public void setup() {
		// size(640, 360);
		frameRate(1);
		strokeWeight((float) 20.0);
		stroke(255, 100);
	}

	public void draw() {
		background(0);
		ellipse(300, 300, 1f, 1f);
		ellipse(320, 300, 1f, 1f);
	}
}

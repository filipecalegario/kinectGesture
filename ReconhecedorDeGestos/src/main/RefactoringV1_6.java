package main;

import java.awt.event.KeyEvent;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import architecture.SimpleSkeletonAnalyzer;
import basic.Utils;
import body.Skeleton;

/**
 * @author filipecalegario
 * 
 */
public class RefactoringV1_6 extends PApplet {

	SimpleSkeletonAnalyzer skeletonAnalyzer;
	boolean drawSkeleton = false;

	public void setup() {

		size(screenHeight * 4 / 3 / 2, screenHeight / 2, PConstants.P3D);
		skeletonAnalyzer = new SimpleSkeletonAnalyzer(this);
		frameRate(60);
	}

	public void draw() {
		//background(0);
		 fill(0,0,0,50);
		 rect(0,0,width,height);
		
		for (Skeleton s: skeletonAnalyzer.getSkels().values()) {
			drawSkeletonLines(s);
		}
		
		fill(255, 0, 0);
		ellipse((frameCount * 15) % width, 20, 20, 20);
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (key == ' ') {

		}

	}

	private void drawSkeletonLines(Skeleton s) {
		PVector head = Utils.convertToScreen(this, s.headCoords);
		PVector neck = Utils.convertToScreen(this, s.neckCoords);
		PVector lShoulder = Utils.convertToScreen(this, s.lShoulderCoords);
		PVector rShoulder = Utils.convertToScreen(this, s.rShoulderCoords);
		PVector torso = Utils.convertToScreen(this, s.torsoCoords);
		PVector lElbow = Utils.convertToScreen(this, s.lElbowCoords);
		PVector rElbow = Utils.convertToScreen(this, s.rElbowCoords);
		PVector lHand = Utils.convertToScreen(this, s.lHandCoords);
		PVector rHand = Utils.convertToScreen(this, s.rHandCoords);
		PVector lHip = Utils.convertToScreen(this, s.lHipCoords);
		PVector rHip = Utils.convertToScreen(this, s.rHipCoords);
		fill(255, 50);
		ellipseMode(CENTER);
		ellipse(head.x, head.y, 2 * Utils.BALL_SIZE / s.headCoords.z, 2 * Utils.BALL_SIZE
				/ s.headCoords.z);
		stroke(255);
		// strokeWeight(3);
		linePVector(head, neck);
		linePVector(rShoulder, lShoulder);
		linePVector(lShoulder, torso);
		linePVector(rShoulder, torso);
		linePVector(rShoulder, rElbow);
		linePVector(rElbow, rHand);
		linePVector(lShoulder, torso);
		linePVector(lShoulder, lElbow);
		linePVector(lElbow, lHand);
	}
	
	public void linePVector(PVector v1, PVector v2) {
		line(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#000000",
		// "--present",
				"--stop-color=#cccccc", "main.RefactoringV1_6" });
	}
}

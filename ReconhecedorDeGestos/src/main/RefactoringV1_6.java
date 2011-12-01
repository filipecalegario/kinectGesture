package main;

import interaction.InteractionStatus;

import java.awt.event.KeyEvent;

import music.MusicModule;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import architecture.SimpleSkeletonAnalyzer;
import basic.Ellipse;
import basic.Utils;
import body.Skeleton;

/**
 * @author filipecalegario
 */
public class RefactoringV1_6 extends PApplet {

	MusicModule musicModule;
	SimpleSkeletonAnalyzer skeletonAnalyzer;
	InteractionStatus iStatus;
	
	// Interactions variables
	private boolean rectDrawStatus;
	private boolean lockDist;
	
	private float lastValue;
	private float lastSpeed;
	private float lastAcc;

	public void setup() {
		size(screenHeight * 4 / 3 / 2, screenHeight / 2, PConstants.P3D);
		skeletonAnalyzer = new SimpleSkeletonAnalyzer(this);
		musicModule = new MusicModule();
		frameRate(60);
		this.iStatus = InteractionStatus.DIRECTIONS;
	}

	public void draw() {
		background(0);

		for (Skeleton s : skeletonAnalyzer.getSkels().values()) {
			drawSkeletonLines(s);
			interaction(s);
		}

		noStroke();
		fill(255, 0, 0);
		ellipse((frameCount * 15) % width, 20, 20, 20);
	}

	public void interaction(Skeleton s) {
		switch (iStatus) {
		case HAND_ANGLE:
			interaction_handAngle(s);
			break;
		case DIRECTIONS:
			interaction_directions(s);
			break;
		default:
			break;
		}
	}

	public void interaction_handAngle(Skeleton s) {
		PVector leftHand = Utils.convertToScreen(this, s.lHandCoords);
		PVector rightHand = Utils.convertToScreen(this, s.rHandCoords);

		float dist = PVector.dist(s.lHandCoords, s.rHandCoords);
		float angle = Utils.calculateAngle(s.lHandCoords, s.rHandCoords);

		float rectX = leftHand.x - (leftHand.x - rightHand.x) / 2;
		float rectY = leftHand.y - (leftHand.y - rightHand.y) / 2;
		float rectLength = PVector.dist(leftHand, rightHand);
		float rectHeight = 30;
		int note = Math.round(map(rectY, 0, height, 60, 80));

		if (dist < 0.05 && lockDist == false) {
			if (rectDrawStatus == false) {
				rectDrawStatus = true;
				musicModule.sendNoteOn(note, 100);
			} else {
				rectDrawStatus = false;
				musicModule.sendNoteOff(note, 100);
			}
			lockDist = true;

		} else if (dist > 0.06) {
			lockDist = false;
		}

		float mappedAngle = map(angle, -PI / 2, PI / 2, 0, 1);
		float colorHue = mappedAngle * 100;
		// sendCC(3, mappedAngle);

		if (rectDrawStatus == true) {
			noStroke();
			pushMatrix();
			translate(rectX, rectY);
			rotateZ(angle);
			colorMode(HSB, 100);
			fill(colorHue, 100, 100);
			colorMode(RGB, 255);
			rect(-rectLength / 2, -rectHeight / 2, rectLength, rectHeight);
			popMatrix();

			// float mappedLength = (map(rectLength, 10, 350, 0, 1));
			// sendCC(1, mappedLength);
		}
	}

	public void interaction_directions(Skeleton s){
		float value = s.lHandCoords.z*100;
		if(abs(value - lastValue) < 10){
			value = lastValue;
		}
		strokeWeight(10);
		float speed = value - lastValue;
		float acc = speed - lastSpeed;
		
		//Utils.writeOnScreen(this, "Acc=" + acc, 10, 80, color(255,0,255), 36);
		
		//stroke(255,0,0);
		//point(frameCount%width, speed * 100);
		
		
		float speedS = (speed + 1) * 10;
		if(abs(speedS) > 30){
			noStroke();
			if(speed < 0){
				fill(255,0,0);
				Utils.writeOnScreen(this, "FRENTE", color(0,255,255));
			} else {
				fill(0,255,0);
				Utils.writeOnScreen(this, "TRçS", color(0,255,255));
			}
			ellipse(50,50,50,50);
		} 
		
		stroke(255,0,255);
		point(frameCount%width, speedS + height/2);
		stroke(0,255,255);
		point(frameCount%width, acc * 100 + height/2);
		
		//Utils.writeOnScreen(this, "Speed=" + speedS, 10, 40, color(255,0,0), 36);

		if(acc * 100 > 50){
			
			if(speed < 0){
			} else {
			}
		}

		lastValue = value;
		lastSpeed = speed;
		lastAcc = acc;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (key) {
		case 'a':
		case 'A':
			
			break;

		default:
			break;
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
		strokeWeight(1);
		stroke(255);
		linePVector(head, neck);
		linePVector(rShoulder, lShoulder);
		linePVector(lShoulder, torso);
		linePVector(rShoulder, torso);
		linePVector(rShoulder, rElbow);
		linePVector(rElbow, rHand);
		linePVector(lShoulder, torso);
		linePVector(lShoulder, lElbow);
		linePVector(lElbow, lHand);
		strokeWeight(15);
		point(rHand.x, rHand.y, rHand.z);
		point(lHand.x, lHand.y, lHand.z);
		point(head.x, head.y, head.z);
		strokeWeight(5);
		point(rShoulder.x, rShoulder.y, rShoulder.z);
		point(lShoulder.x, lShoulder.y, lShoulder.z);

		// ellipse(rHand.x, rHand.y, 40, 40);
		// ellipse(lHand.x, lHand.y, 40, 40);
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

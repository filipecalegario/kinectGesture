package main;

import game.GameModule;
import game.GameResult;
import game.GameStatus;

import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Vector;

import old_architecture.GestureEvent;
import old_architecture.Hit;
import old_architecture.HitGestureEvent;
import old_architecture.SpeedGestureEvent;

import music.MusicModule;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;
import architecture.HandsTogetherListener;
import architecture.HitListener;
import architecture.SkeletonAnalyzer;
import basic.Ellipse;
import basic.Rectangle;
import basic.RectangleRepository;
import basic.Utils;
import body.Joint;
import body.Skeleton;

/**
 * @author filipecalegario
 * 
 */
public class RefactoringV1_5 extends PApplet {

	int bgColor = color(0, 0, 0, 0);

	SkeletonAnalyzer skeletonAnalyzer;
	HitListener hl;
	HandsTogetherListener htl;
	GameModule game;
	MusicModule music;

	public void setup() {

		size(screenHeight * 4 / 3 / 2, screenHeight / 2, PConstants.P3D);

		// Skeleton and Music configuration
		skeletonAnalyzer = new SkeletonAnalyzer(this);
		music = new MusicModule();

		// Game setup
		game = new GameModule(this, music);
		// rr = new RectangleRepository(this);

		this.hl = new HitListener(this, skeletonAnalyzer);
		this.hl.registerOnAction("hitOnAction", this);
		this.hl.registerOffAction("hitOffAction", this);
		this.skeletonAnalyzer.addListener(this.hl);
		
		this.htl = new HandsTogetherListener(this, skeletonAnalyzer);
		this.htl.registerOnAction("handsOnAction", this);
		this.htl.registerOffAction("handsOffAction", this);
		//this.htl.registerUpdateAction("handsUpdateAction", this);
		this.skeletonAnalyzer.addListener(this.htl);
	}

	public void handsOnAction() {
		System.out.println("HANDS ON");
	}

	public void handsOffAction() {
		System.out.println("HANDS OFF");
	}

	public void handsUpdateAction(Object[] oargs) {
		System.out.println("ANGLE = " + (Float) oargs[0]);
	}

	public void hitOnAction() {
		System.out.println("HIT ON");
	}

	public void hitOffAction() {
		System.out.println("HIT OFF");
	}

	public void draw() {
		background(0);
		//skeletonAnalyzer.drawSkeletons(true);
		// drawInteraction(s);
		// drawRectangles();
		game.updateGame();
	}

	// private void drawInteraction(Skeleton s) {
	//
	// PVector leftHand = Utils.convertToScreen(this, s.lHandCoords);
	// PVector rightHand = Utils.convertToScreen(this, s.rHandCoords);
	//
	// float dist = PVector.dist(s.lHandCoords, s.rHandCoords);
	// float angle = Utils.calculateAngle(s.lHandCoords, s.rHandCoords);
	//
	// float rectX = leftHand.x - (leftHand.x - rightHand.x) / 2;
	// float rectY = leftHand.y - (leftHand.y - rightHand.y) / 2;
	// float rectLength = PVector.dist(leftHand, rightHand);
	// float rectHeight = 30;
	//
	// int note = Math.round(map(rectY, 0, height, 60, 80));
	//		
	// if (dist < 0.05 && lockDist == false) {
	// if (rectDrawStatus == false) {
	// rectDrawStatus = true;
	// music.sendNoteOn(note, 100);
	// } else {
	// rectDrawStatus = false;
	// music.sendNoteOff(note, 100);
	// }
	// lockDist = true;
	//
	// } else if (dist > 0.06) {
	// lockDist = false;
	// }
	//
	// float mappedAngle = map(angle, -PI / 2, PI / 2, 0, 1);
	// float colorHue = mappedAngle * 100;
	//
	// if (rectDrawStatus == true) {
	// pushMatrix();
	// translate(rectX, rectY);
	// rotateZ(angle);
	// colorMode(HSB, 100);
	// fill(colorHue, 100, 100);
	// colorMode(RGB, 255);
	// rect(-rectLength / 2, -rectHeight / 2, rectLength, rectHeight);
	// popMatrix();
	// }
	//
	// float currentDistanceHandTorso = PVector.dist((s.lHandCoords),
	// (s.lShoulderCoords));
	// float abs = Math.abs(currentDistanceHandTorso - lastDistanceHandTorso);
	//
	// if (abs > 0.03f && !forwardLock) {
	// Rectangle userRectangle = new Rectangle(rectLength, rectHeight,
	// angle, new PVector(rectX, rectY), color(127));
	// rr.add(userRectangle);
	// forwardLock = true;
	// game.checkSimilarity(userRectangle);
	// counter = 0;
	// }
	// if (counter == 10) {
	// counter = 0;
	// forwardLock = false;
	// }
	// counter++;
	// lastDistanceHandTorso = currentDistanceHandTorso;
	//
	// }

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == 72) {
			HorrivelGlobal.calibration += 0.001f;
		} else if (e.getKeyCode() == 71) {
			HorrivelGlobal.calibration -= 0.001f;
		}
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#000000",
		// "--present",
				"--stop-color=#cccccc", "main.RefactoringV1_5" });
	}
}

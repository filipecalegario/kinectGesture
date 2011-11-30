package body;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PVector;
import basic.Utils;

public class Skeleton {
	
	private PApplet main;
	private boolean drawHands;

	public Vector<PVector> body;

	public PVector headCoords = new PVector();
	public PVector neckCoords = new PVector();
	public PVector rCollarCoords = new PVector();
	public PVector rShoulderCoords = new PVector();
	public PVector rElbowCoords = new PVector();
	public PVector rWristCoords = new PVector();
	public PVector rHandCoords = new PVector();
	public PVector rFingerCoords = new PVector();
	public PVector lCollarCoords = new PVector();
	public PVector lShoulderCoords = new PVector();
	public PVector lElbowCoords = new PVector();
	public PVector lWristCoords = new PVector();
	public PVector lHandCoords = new PVector();
	public PVector lFingerCoords = new PVector();
	public PVector torsoCoords = new PVector();
	public PVector rHipCoords = new PVector();
	public PVector rKneeCoords = new PVector();
	public PVector rAnkleCoords = new PVector();
	public PVector rFootCoords = new PVector();
	public PVector lHipCoords = new PVector();
	public PVector lKneeCoords = new PVector();
	public PVector lAnkleCoords = new PVector();
	public PVector lFootCoords = new PVector();

	public int id;
	public float colors[] = { 255, 0, 0 };// The color of this skeleton
	
	private PVector head;
	private PVector neck;
	private PVector lShoulder;
	private PVector rShoulder;
	private PVector torso;
	private PVector lElbow;
	private PVector rElbow;
	private PVector lHand;
	private PVector rHand;
	private PVector lHip;
	private PVector rHip;


	public Skeleton(PApplet main, int id) {
		this.main = main;
		body = new Vector<PVector>();
		createSkeleton();
		convertToScreen();
		this.id = id;
		colors[0] = 255;
		colors[1] = 0;
		colors[2] = 0;
		
	}

	private void createSkeleton() {
		body.add(headCoords);
		body.add(neckCoords);
		body.add(rCollarCoords);
		body.add(rShoulderCoords);
		body.add(rElbowCoords);
		body.add(rWristCoords);
		body.add(rHandCoords);
		body.add(rFingerCoords);
		body.add(lCollarCoords);
		body.add(lShoulderCoords);
		body.add(lElbowCoords);
		body.add(lWristCoords);
		body.add(lHandCoords);
		body.add(lFingerCoords);
		body.add(torsoCoords);
		body.add(rHipCoords);
		body.add(rKneeCoords);
		body.add(rAnkleCoords);
		body.add(rFootCoords);
		body.add(lHipCoords);
		body.add(lKneeCoords);
		body.add(lAnkleCoords);
		body.add(lFootCoords);
	}

	private void convertToScreen() {
		this.head = Utils.convertToScreen(main, this.headCoords);
		this.neck = Utils.convertToScreen(main, this.neckCoords);
		this.lShoulder = Utils.convertToScreen(main, this.lShoulderCoords);
		this.rShoulder = Utils.convertToScreen(main, this.rShoulderCoords);
		this.torso = Utils.convertToScreen(main, this.torsoCoords);
		this.lElbow = Utils.convertToScreen(main, this.lElbowCoords);
		this.rElbow = Utils.convertToScreen(main, this.rElbowCoords);
		this.lHand = Utils.convertToScreen(main, this.lHandCoords);
		this.rHand = Utils.convertToScreen(main, this.rHandCoords);
		this.lHip = Utils.convertToScreen(main, this.lHipCoords);
		this.rHip = Utils.convertToScreen(main, this.rHipCoords);
	}

	public void drawHands(boolean b) {
		this.drawHands = b;
	}
	
//	/**
//	 * Draws the current pointer position to the canvas.
//	 * <p>Can be registered for automatic drawing using drawPointer().</p>
//	 */
//	public void draw() {
//		convertToScreen();
//		main.fill(255, 50);
//		main.ellipseMode(main.CENTER);
//		main.ellipse(head.x, head.y, 2 * Utils.BALL_SIZE / this.headCoords.z, 2 * Utils.BALL_SIZE / this.headCoords.z);
//		main.stroke(255);
//		//strokeWeight(3);
//		Utils.linePVector(main, head, neck);
//		Utils.linePVector(main, rShoulder, lShoulder);
//		Utils.linePVector(main, lShoulder, torso);
//		Utils.linePVector(main, rShoulder, torso);
//		Utils.linePVector(main, rShoulder, rElbow);
//		Utils.linePVector(main, rElbow, rHand);
//		Utils.linePVector(main, lShoulder, torso);
//		Utils.linePVector(main, lShoulder, lElbow);
//		Utils.linePVector(main, lElbow, lHand);
//		if (drawHands) {
//			main.fill(255);
//			main.noStroke();
//			main.ellipseMode(main.CENTER);
//			main.ellipse(lHand.x, lHand.y, 50, 50);
//			main.ellipse(rHand.x, rHand.y, 50, 50);
//		}
//	}
	
//	/**
//	 * Sets whether the Wiimote pointer should be drawn or not.
//	 * 
//	 * @param d whether or not to draw the pointer
//	 */
//	public void drawSkeleton(boolean d) {
//		if (d) {
//			main.registerDraw(this);
//		} else {
//			main.unregisterDraw(this);
//		}
//	}
}
package main;

import interaction.InteractionStatus;
import interaction.InteractionVariables;

import java.awt.event.KeyEvent;

import music.MusicModule;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;
import architecture.SimpleSkeletonAnalyzer;
import basic.Utils;
import body.Joint;
import body.Skeleton;

/**
 * @author filipecalegario
 */
public class RefactoringV1_6 extends PApplet {

	MusicModule musicModule;
	SimpleSkeletonAnalyzer skeletonAnalyzer;
	InteractionStatus iStatus;

	PFont font;

	// Interactions variables
	private boolean rectDrawStatus;
	private boolean lockDist;

	InteractionVariables iv;
	
	int iSIndex;

	public void setup() {
		size(screenHeight * 4 / 3 / 2, screenHeight / 2, PConstants.P3D);
		skeletonAnalyzer = new SimpleSkeletonAnalyzer(this);
		musicModule = new MusicModule();
		frameRate(60);
		this.iStatus = InteractionStatus.ARM_ANGLE;
		this.iv = new InteractionVariables();
		font = loadFont("CourierNew36.vlw");
	}

	public void draw() {
		background(0);
		writeOnScreen("status: " + iStatus.name(), 10, height - 20, color(255,
				0, 255), 15);

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
		case ARM_ANGLE:
			interaction_armAngle(s);
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

	public void interaction_directions(Skeleton s) {

		float positionX = s.lHandCoords.x;
		float positionY = s.lHandCoords.y;
		float positionZ = s.lHandCoords.z;

		float speedX = positionX - iv.lastPositionX;
		float speedY = positionY - iv.lastPositionY;
		float speedZ = positionZ - iv.lastPositionZ;

		float absSpeedX = Math.abs(speedX);
		float absSpeedY = Math.abs(speedY);
		float absSpeedZ = Math.abs(speedZ);

		iv.setMaxSpeedX(absSpeedX);
		iv.setMinSpeedX(absSpeedX);
		iv.setMaxSpeedY(absSpeedY);
		iv.setMinSpeedY(absSpeedY);
		iv.setMaxSpeedZ(absSpeedZ);
		iv.setMinSpeedZ(absSpeedZ);

		// writeOnScreen("Speed X =[" + iv.getMinSpeedX() + "][" +
		// iv.getMaxSpeedX() + "]", 10, 40, color(255,0,0), 20);
		// writeOnScreen("Speed Y =[" + iv.getMinSpeedY() + "][" +
		// iv.getMaxSpeedY() + "]", 10, 60, color(0,255,0), 20);
		// writeOnScreen("Speed Z =[" + iv.getMinSpeedZ() + "][" +
		// iv.getMaxSpeedZ() + "]", 10, 80, color(0,255,255), 20);

		if (absSpeedX > 0.01) {
			if (speedX > 0) {
				writeOnScreen("RIGHT!", color(255, 0, 255));
			} else {
				writeOnScreen("LEFT!", color(255, 0, 255));
			}
		}

		if (absSpeedY > 0.01) {
			if (speedY > 0) {
				writeOnScreen("UP!", color(255, 0, 255));
			} else {
				writeOnScreen("DOWN!", color(255, 0, 255));
			}
		}

		if (absSpeedZ > 0.02) {
			if (speedZ > 0) {
				writeOnScreen("BACKWARD!", color(255, 0, 255));
			} else {
				writeOnScreen("FORWARD!", color(255, 0, 255));
			}
		}

		// END =====================
		iv.lastPositionX = positionX;
		iv.lastPositionY = positionY;
		iv.lastPositionZ = positionZ;

	}

	public void interaction_armAngle(Skeleton s) {

		float leftDegree = degreesTwoVectors(s.lHandCoords, s.lElbowCoords, s.lShoulderCoords);
		float rightDegree = degreesTwoVectors(s.rHandCoords, s.rElbowCoords, s.rShoulderCoords);
		
		boolean leftCheck = checkAngle(80, leftDegree, 7);
		boolean rightCheck = checkAngle(80, rightDegree, 7);
		
		PVector screenLElbow = Utils.convertToScreen(this, s.lElbowCoords);
		PVector screenRElbow = Utils.convertToScreen(this, s.rElbowCoords);
		
		drawDetection(screenLElbow, leftCheck);
		drawDetection(screenRElbow, rightCheck);
		
	}
	
	private void drawDetection(PVector point, boolean draw){
		if (draw) {
			noStroke();
			fill(0,255,0);
			ellipseMode(CENTER);
			ellipse(point.x, point.y, 20, 20);
			//writeOnScreen("DETECTOU!", color(0, 255, 0));
		}
	}
	
	private float degreesTwoVectors(PVector a, PVector center, PVector b){
		PVector aCenter = PVector.sub(a, center);
		PVector bCenter = PVector.sub(b, center);
		float theta = PVector.angleBetween(aCenter, bCenter);
		return degrees(theta);
	}
	
	private boolean checkAngle(float reference, float degrees, float tolerance){
		float result = Math.abs(reference - degrees);
		return result < tolerance;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// println(key);
		switch (key) {
		case 'a':
		case 'A':
			iStatus = InteractionStatus.HAND_ANGLE;
			break;
		case 's':
		case 'S':
			iStatus = InteractionStatus.DIRECTIONS;
			break;
		case 'r':
			iv.resetSpeeds();
			break;
		case ' ':
			iStatus = InteractionStatus.values()[iSIndex++%InteractionStatus.values().length];
			break;
		case CODED:
			switch (keyCode) {
			case RIGHT:
				System.out.println("Speed X =[" + iv.getMinSpeedX() + "]["
						+ iv.getMaxSpeedX() + "]");
				System.out.println("Speed Y =[" + iv.getMinSpeedY() + "]["
						+ iv.getMaxSpeedY() + "]");
				System.out.println("Speed Z =[" + iv.getMinSpeedZ() + "]["
						+ iv.getMaxSpeedZ() + "]");
				break;
			default:
				break;
			}
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

	public void writeOnScreen(String txt, float x, float y, int color, int size) {
		textFont(font, size);
		fill(color);
		text(txt, x, y);
	}

	public void writeOnScreen(String txt, int color) {
		textFont(font, 50);
		float textWidth = textWidth(txt);
		float textHeight = textAscent();
		float x = width / 2 - textWidth / 2;
		float y = height / 2 - textHeight / 2;
		// fill(127, 50);
		// rect(x, y, textWidth, textHeight);
		fill(color);
		text(txt, x, y);
	}
	
	public PVector getJointFromSkeleton(Skeleton s, Joint j){
		PVector result = null;
		switch (j) {
		case HEAD:
			result = s.headCoords;
			break;
		case NECK:
			result = s.neckCoords;
			break;
		case R_SHOULDER:
			result = s.rShoulderCoords;
			break;
		case R_ELBOW:
			result = s.rElbowCoords;
			break;
		case R_HAND:
			result = s.rHandCoords;
			break;
		case L_SHOULDER:
			result = s.lShoulderCoords;
			break;
		case L_ELBOW:
			result = s.lElbowCoords;
			break;
		case L_HAND:
			result = s.lHandCoords;
			break;
		case TORSO:
			result = s.torsoCoords;
			break;
		case R_HIP:
			result = s.rHipCoords;
			break;
		case R_KNEE:
			result = s.rKneeCoords;
			break;
		case R_FOOT:
			result = s.rFootCoords;
			break;
		case L_HIP:
			result = s.lHipCoords;
			break;
		case L_KNEE:
			result = s.lKneeCoords;
			break;
		case L_FOOT:
			result = s.lFootCoords;
			break;
		default:
			break;
		}
		return result;
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#000000",
		// "--present",
				"--stop-color=#cccccc", "main.RefactoringV1_6" });
	}
}

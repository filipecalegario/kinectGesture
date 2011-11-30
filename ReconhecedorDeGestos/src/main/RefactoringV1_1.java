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
public class RefactoringV1_1 extends PApplet {

	private static final int Z_MULTIPLIER = 100;
	OscP5 oscP5;
	NetAddress myRemoteLocation;
	int ballSize = 10;
	Hashtable<Integer, Skeleton> skels = new Hashtable<Integer, Skeleton>();
	PFont font;
	int bgColor = color(0, 0, 0, 0);
	int placeColor = 127;
	Vector<Ellipse> ellipses;
	RectangleRepository rr;
	boolean rectDrawStatus = false;
	boolean lockDist = false;
	Rectangle gameRectangle;
	int statusCounter = 0;

	float maxAngle = 0;
	float minAngle = Float.MAX_VALUE;

	float lastDistanceHandTorso;
	boolean forwardLock = false;
	int counter;
	boolean drawResult;

	GameResult gr;
	GameStatus gameStatus;
	GameModule gm;
	
	MusicModule musicModule;

	public void setup() {

		size(screenHeight * 4 / 3 / 2, screenHeight / 2, PConstants.P3D);
		
		// Details
		font = loadFont("CourierNew36.vlw");
		textFont(font, 36);
		ellipses = new Vector<Ellipse>();
		
		// Network configuration
		oscP5 = new OscP5(this, 7110);
		myRemoteLocation = new NetAddress("127.0.0.1", 9999);
		musicModule = new MusicModule();

		// Game setup
		gameRectangle = new Rectangle();
		gr = GameResult.OK;
		gameStatus = GameStatus.STATUS1;
		gm = new GameModule(this, musicModule);
		rr = new RectangleRepository(this);
	}

	public void draw() {
		background(0);
		for (Skeleton s : skels.values()) {
			//s.runGestures();
			// drawBody(s);
//			drawSkeletonLines(s);
			// drawEllipses();
			//s.drawHands(true);
			//s.drawSkeleton(true);
			drawInteraction(s);
			drawRectangles();
		}
		gm.updateGame();
	}

//	private void updateGame() {
//		switch (gameStatus) {
//		case STATUS1:
//			PVector rectPoint = new PVector(width / 2, 50);
//			float angle = random(-PI / 2, PI / 2);
//			float rectLength = random(50, 250);
//			float rectHeight = 30;
//			gameRectangle = new Rectangle(rectLength, rectHeight, angle, rectPoint, color(127, 0, 127));
//			gameStatus = GameStatus.STATUS2;
//			statusCounter = 0;
//			break;
//		case STATUS2:
//			gameRectangle.render(this);
//			if(statusCounter == 100){
//				writeOnScreen("MISS", color(255,0,0));
//				gm.incrementCounter(GameResult.MISS, 1);
//				gameStatus = GameStatus.STATUS1;
//			}
//			statusCounter++;
//			break;
//		case STATUS3:
//			switch (gr) {
//			case PERFECT:
//				writeOnScreen("PERFECT", color(0,255,0));
//				break;
//			case OK:
//				writeOnScreen("GOOD", color(0,0,255));
//				break;
//			case TERRIBLE:
//				writeOnScreen("TERRIBLE", color(255,0,0));
//				break;
//
//			default:
//				break;
//			}
//			if(statusCounter == 50){
//				gameStatus =  GameStatus.STATUS1;
//			}
//			statusCounter++;
//			break;
//
//		default:
//			break;
//		}
//		gm.process();
//	}

//	private void writeOnScreen(String txt, int color){
//		textFont(font, 50);
//		float textWidth = textWidth(txt);
//		float textHeight = textAscent();
//		float x = width / 2 - textWidth / 2;
//		float y = height / 2 - textHeight / 2;
//		//fill(127, 50);
//		//rect(x, y, textWidth, textHeight);
//		fill(color);
//		text(txt, x, y);
//	}
	
	private void drawRectangles() {
		rr.process();
		rr.draw();
	}

//	private void drawHands(Skeleton s) {
//		fill(255);
//		noStroke();
//		PVector leftHand = this.convertToScreen(s.lHandCoords);
//		PVector rightHand = this.convertToScreen(s.rHandCoords);
//		ellipseMode(CENTER);
//		ellipse(leftHand.x, leftHand.y, 50, 50);
//		ellipse(rightHand.x, rightHand.y, 50, 50);
//	}

	private void drawInteraction(Skeleton s) {

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
				ellipses.add(new Ellipse(rectX, rectY, 50, 50));
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

		//sendCC(2, map(rectY, 0, height, 0, 1));

		float mappedAngle = map(angle, -PI / 2, PI / 2, 0, 1);
		float colorHue = mappedAngle * 100;
		//sendCC(3, mappedAngle);

		if (rectDrawStatus == true) {
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

		float currentDistanceHandTorso = PVector.dist((s.lHandCoords),
				(s.lShoulderCoords));
		float abs = Math.abs(currentDistanceHandTorso - lastDistanceHandTorso);

		if (abs > 0.03f && !forwardLock) {
			Rectangle userRectangle = new Rectangle(rectLength, rectHeight,
					angle, new PVector(rectX, rectY), color(127));
			rr.add(userRectangle);
			forwardLock = true;
			gm.checkSimilarity(userRectangle);
			counter = 0;
		}
		if (counter == 10) {
			counter = 0;
			forwardLock = false;
		}
		counter++;
		lastDistanceHandTorso = currentDistanceHandTorso;

	}

//	private void checkSimilarity(Rectangle userRectangle, Rectangle gameRectangle2) {
//		
//		float perfectTolerance = PI * 0.05f;
//		float goodTolerance = PI * 0.20f;
//		float diff = Math.abs(userRectangle.getAngle() - gameRectangle2.getAngle());
//		
//		if(diff < perfectTolerance){
//			gr = GameResult.PERFECT;
//		} else if (diff < goodTolerance){
//			gr = GameResult.OK;
//		} else {
//			gr = GameResult.TERRIBLE;
//		}
//		
//		statusCounter = 0;
//		gameStatus = GameStatus.STATUS3;
//	}

//	private void drawEllipses() {
//		fill(255,50);
//		noStroke();
//		for (Ellipse e : ellipses) {
//			ellipse(e.x, e.y, e.w, e.h);
//		}
//	}

//	private void drawBody(Skeleton s) {
//		ambientLight(64, 64, 64);
//		lightSpecular(255, 255, 255);
//		directionalLight(224, 224, 224, .5f, 1, -1);
//		PVector previous = null;
//		for (PVector vec : s.body) {
//			float posX = vec.x * width;
//			float posY = vec.y * height;
//			float posZ = -vec.z * Z_MULTIPLIER;
//			pushMatrix();
//			fill(255, 0, 0);
//			translate(posX, posY, posZ);
//			 sphere(2 * ballSize / vec.z);
//			noStroke();
//			ellipse(0, 0, 2 * ballSize / vec.z, 2 * ballSize / vec.z);
//			popMatrix();
//		}
//	}

//	private void drawSkeletonLines(Skeleton s) {
//		PVector head = convertToScreen(s.headCoords);
//		PVector neck = convertToScreen(s.neckCoords);
//		PVector lShoulder = convertToScreen(s.lShoulderCoords);
//		PVector rShoulder = convertToScreen(s.rShoulderCoords);
//		PVector torso = convertToScreen(s.torsoCoords);
//		PVector lElbow = convertToScreen(s.lElbowCoords);
//		PVector rElbow = convertToScreen(s.rElbowCoords);
//		PVector lHand = convertToScreen(s.lHandCoords);
//		PVector rHand = convertToScreen(s.rHandCoords);
//		PVector lHip = convertToScreen(s.lHipCoords);
//		PVector rHip = convertToScreen(s.rHipCoords);
//		fill(255, 50);
//		ellipseMode(CENTER);
//		ellipse(head.x, head.y, 2 * ballSize / s.headCoords.z, 2 * ballSize
//				/ s.headCoords.z);
//		stroke(255);
//		//strokeWeight(3);
//		linePVector(head, neck);
//		linePVector(rShoulder, lShoulder);
//		linePVector(lShoulder, torso);
//		linePVector(rShoulder, torso);
//		linePVector(rShoulder, rElbow);
//		linePVector(rElbow, rHand);
//		linePVector(lShoulder, torso);
//		linePVector(lShoulder, lElbow);
//		linePVector(lElbow, lHand);
//	}

//	private void linePVector(PVector v1, PVector v2) {
//		line(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
//	}
//
//	private PVector convertToScreen(PVector v1) {
//		return new PVector(v1.x * width, v1.y * height, -v1.z * Z_MULTIPLIER);
//	}

	public void oscEvent(OscMessage msg) {
		if (msg.checkAddrPattern("/joint") && msg.checkTypetag("sifff")) {
			Integer id = msg.get(1).intValue();
			Skeleton s = skels.get(id);
			if (s == null) {
				s = new Skeleton(this, id);
				skels.put(id, s);
			}
			if (msg.get(0).stringValue().equals("head")) {
				s.headCoords.x = msg.get(2).floatValue();
				s.headCoords.y = msg.get(3).floatValue();
				s.headCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("neck")) {
				s.neckCoords.x = msg.get(2).floatValue();
				s.neckCoords.y = msg.get(3).floatValue();
				s.neckCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_collar")) {
				s.rCollarCoords.x = msg.get(2).floatValue();
				s.rCollarCoords.y = msg.get(3).floatValue();
				s.rCollarCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_shoulder")) {
				s.rShoulderCoords.x = msg.get(2).floatValue();
				s.rShoulderCoords.y = msg.get(3).floatValue();
				s.rShoulderCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_elbow")) {
				s.rElbowCoords.x = msg.get(2).floatValue();
				s.rElbowCoords.y = msg.get(3).floatValue();
				s.rElbowCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_wrist")) {
				s.rWristCoords.x = msg.get(2).floatValue();
				s.rWristCoords.y = msg.get(3).floatValue();
				s.rWristCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_hand")) {
				s.rHandCoords.x = msg.get(2).floatValue();
				s.rHandCoords.y = msg.get(3).floatValue();
				s.rHandCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_finger")) {
				s.rFingerCoords.x = msg.get(2).floatValue();
				s.rFingerCoords.y = msg.get(3).floatValue();
				s.rFingerCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_collar")) {
				s.lCollarCoords.x = msg.get(2).floatValue();
				s.lCollarCoords.y = msg.get(3).floatValue();
				s.lCollarCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_shoulder")) {
				s.lShoulderCoords.x = msg.get(2).floatValue();
				s.lShoulderCoords.y = msg.get(3).floatValue();
				s.lShoulderCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_elbow")) {
				s.lElbowCoords.x = msg.get(2).floatValue();
				s.lElbowCoords.y = msg.get(3).floatValue();
				s.lElbowCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_wrist")) {
				s.lWristCoords.x = msg.get(2).floatValue();
				s.lWristCoords.y = msg.get(3).floatValue();
				s.lWristCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_hand")) {
				s.lHandCoords.x = msg.get(2).floatValue();
				s.lHandCoords.y = msg.get(3).floatValue();
				s.lHandCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_finger")) {
				s.lFingerCoords.x = msg.get(2).floatValue();
				s.lFingerCoords.y = msg.get(3).floatValue();
				s.lFingerCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("torso")) {
				s.torsoCoords.x = msg.get(2).floatValue();
				s.torsoCoords.y = msg.get(3).floatValue();
				s.torsoCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_hip")) {
				s.rHipCoords.x = msg.get(2).floatValue();
				s.rHipCoords.y = msg.get(3).floatValue();
				s.rHipCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_knee")) {
				s.rKneeCoords.x = msg.get(2).floatValue();
				s.rKneeCoords.y = msg.get(3).floatValue();
				s.rKneeCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_ankle")) {
				s.rAnkleCoords.x = msg.get(2).floatValue();
				s.rAnkleCoords.y = msg.get(3).floatValue();
				s.rAnkleCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_foot")) {
				s.rFootCoords.x = msg.get(2).floatValue();
				s.rFootCoords.y = msg.get(3).floatValue();
				s.rFootCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_hip")) {
				s.lHipCoords.x = msg.get(2).floatValue();
				s.lHipCoords.y = msg.get(3).floatValue();
				s.lHipCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_knee")) {
				s.lKneeCoords.x = msg.get(2).floatValue();
				s.lKneeCoords.y = msg.get(3).floatValue();
				s.lKneeCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_ankle")) {
				s.lAnkleCoords.x = msg.get(2).floatValue();
				s.lAnkleCoords.y = msg.get(3).floatValue();
				s.lAnkleCoords.z = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_foot")) {
				s.lFootCoords.x = msg.get(2).floatValue();
				s.lFootCoords.y = msg.get(3).floatValue();
				s.lFootCoords.z = msg.get(4).floatValue();
			}
		} else if (msg.checkAddrPattern("/new_user") && msg.checkTypetag("i")) {
			// A new user is in front of the kinect... Tell him to do the
			// calibration pose!
			println("New user with ID = " + msg.get(0).intValue());
		} else if (msg.checkAddrPattern("/new_skel") && msg.checkTypetag("i")) {
			// New skeleton calibrated! Lets create it!
			Integer id = msg.get(0).intValue();
			Skeleton s = new Skeleton(this, id);
			skels.put(id, s);
		} else if (msg.checkAddrPattern("/lost_user") && msg.checkTypetag("i")) {
			// Lost user/skeleton
			Integer id = msg.get(0).intValue();
			println("Lost user " + id);
			skels.remove(id);
		}
	}

//	public void gestureEvent(GestureEvent event) {
//		if (event instanceof SpeedGestureEvent) {
//			SpeedGestureEvent sge = (SpeedGestureEvent) event;
//			float speed = sge.getSpeed();
//			// println("Speed: " + sge.getJoint().name() + " - " + speed);
//			bgColor = color((speed * 20), 255, 255);
//		} else if (event instanceof HitGestureEvent) {
//			HitGestureEvent hge = (HitGestureEvent) event;
//			Joint joint = hge.getJoint();
//			Hit hit = hge.getHit();
//			// println("Hit: " + joint + " - " + hit);
//			switch (joint) {
//			case L_ELBOW:
//				switch (hit) {
//				case UP:
//					System.out.println("ELBOW UP!");
//					break;
//
//				default:
//					// System.out.println("AAAaaa!");
//					break;
//				}
//				break;
//			case L_HAND:
//				text("HAND!= " + hit.name(), 30, 30);
//				switch (hit) {
//				case LEFT:
//					System.out.println("HAND LEFT!");
//					break;
//				case FORWARD:
//					System.out.println("HAND FW");
//					break;
//				case BACK:
//					System.out.println("HAND BK");
//					break;
//				default:
//					break;
//				}
//				break;
//
//			default:
//				break;
//			}
//		}
//	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == 72) {
			HorrivelGlobal.calibration += 0.001f;
		} else if (e.getKeyCode() == 71) {
			HorrivelGlobal.calibration -= 0.001f;
		}
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#666666", 
				//"--present",
				"--stop-color=#cccccc", "main.RefactoringV1_1" });
	}
}

package main;

import interaction.InteractionStatus;
import interaction.InteractionVariables;

import java.awt.Font;
import java.awt.event.KeyEvent;

import music.MusicModule;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import architecture.SimpleSkeletonAnalyzer;
import basic.Constants;
import basic.Utils;
import body.Joint;
import body.Skeleton;
import controlP5.*;

import processing.opengl.*;

/**
 * @author filipecalegario
 */
public class RefactoringV1_7 extends PApplet {

	MusicModule musicModule;
	SimpleSkeletonAnalyzer skeletonAnalyzer;
	InteractionStatus iStatus;

	PFont font;

	ControlP5 controlP5;
	PImage img;
	PVector pos;
	RadioButton r;
	RadioButton l;
	RadioButton out;
	Slider slider1;
	Slider slider2;
	Numberbox nb1;
	Numberbox nb2;

	// Interactions variables
	private boolean rectDrawStatus;
	private boolean lockDist;

	InteractionVariables iv;

	int iSIndex;

	float offset = 285;
	Joint currentJoint = Joint.L_HAND;

	int eventSelected = Constants.POSITION;
	private int outputSelected = 0;

	float displayWidth = 800;
	float displayHeight = 600;

	float trackedValue = 0;
	private int rHandColor;
	private int lHandColor;
	private int headColor;
	private int rShouldColor;
	private int lShouldColor;
	private int neckColor;
	private int torsoColor;
	private int rElbowColor;
	private int lElbowColor;

	PVector[] tracking;
	private float outputValue;
	private boolean drawTracking = true;

	public void setup() {
		size((int) (offset + displayWidth + 200), (int) displayHeight, OPENGL);

		skeletonAnalyzer = new SimpleSkeletonAnalyzer(this);
		musicModule = new MusicModule();
		frameRate(60);
		this.iStatus = InteractionStatus.VALUES;
		this.iv = new InteractionVariables();
		font = loadFont("CourierNew36.vlw");

		controlP5 = new ControlP5(this);
		setupControlP5();
	}

	public void draw() {
		background(0);

		stroke(255);
		line(offset, 0, offset, height);

		stroke(255);
		strokeWeight(1);
		line(offset + displayWidth, 0, offset + displayWidth, height);

		// fill(255, 0,0, 50);
		// noStroke();
		// rect(0, 0, 285, height);
		writeOnScreen("status: " + iStatus.name(), offset + displayWidth - 200,
				height - 20, color(255, 0, 255), 15);

		noStroke();
		fill(255, 0, 0);
		ellipse(((frameCount * 15) % displayWidth) + offset, 20, 20, 20);
		fill(255);
		noStroke();
		rect(0, 0, 285, height);
		rect(offset + displayWidth, 0, 200, height);
		fill(127);
		rect(1098, 381, 165, 200);

		for (Skeleton s : skeletonAnalyzer.getSkels().values()) {
			drawSkeletonLines(s);
			interaction(s);
			drawTracking(s, currentJoint, drawTracking );
		}

		output();
		image(img, pos.x, pos.y);
	}

	public void output() {
		switch (outputSelected) {
		case Constants.OUTPUT_TRIGGER_MORE:
			if(outputValue > slider1.value()/100.0f){
				writeResultTab("TRIGGERED", color(0, 0, 0), 25);
			} else {
				writeResultTab("NOTHING", color(0, 0, 0), 25);
			}
			break;
		case Constants.OUTPUT_TRIGGER_LESS:
			if(outputValue < slider1.value()/100.0f){
				writeResultTab("TRIGGERED", color(0, 0, 0), 25);
			} else {
				writeResultTab("NOTHING", color(0, 0, 0), 25);
			}
			break;
		case Constants.OUTPUT_TRACK_MAP:
			float drawValue = map(outputValue, 0, 1, nb1.value(), nb2.value());
			writeResultTab("" + drawValue, color(0,0,0), 25);
			break;

		default:
			break;
		}
	}

	public void interaction(Skeleton s) {
		switch (iStatus) {
		case HAND_ANGLE:
			interaction_handAngle(s);
			break;
		case DIRECTIONS:
			interaction_directions(s, currentJoint);
			break;
		case ARM_ANGLE:
			interaction_armAngle(s);
			break;
		case VALUES:
			interaction_values(s, currentJoint);
			break;
		default:
			break;
		}
	}

	public void interaction_handAngle(Skeleton s) {
		PVector leftHand = convertToScreen(s.lHandCoords);
		PVector rightHand = convertToScreen(s.rHandCoords);

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
		outputValue = mappedAngle;
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

	public void interaction_directions(Skeleton s, Joint j) {

		PVector tracked = getJointFromSkeleton(s, j);

		float positionX = tracked.x;
		float positionY = tracked.y;
		float positionZ = tracked.z;

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
				writeOnScreen("RIGHT", color(255, 0, 255));
			} else {
				writeOnScreen("LEFT", color(255, 0, 255));
			}
		}

		if (absSpeedY > 0.01) {
			if (speedY > 0) {
				writeOnScreen("DOWN", color(255, 0, 255));
			} else {
				writeOnScreen("UP", color(255, 0, 255));
			}
		}

		if (absSpeedZ > 0.02) {
			if (speedZ > 0) {
				writeOnScreen("BACKWARD", color(255, 0, 255));
			} else {
				writeOnScreen("FORWARD", color(255, 0, 255));
			}
		}

		// END =====================
		iv.lastPositionX = positionX;
		iv.lastPositionY = positionY;
		iv.lastPositionZ = positionZ;

	}

	public void interaction_armAngle(Skeleton s) {

		float leftDegree = degreesTwoVectors(s.lHandCoords, s.lElbowCoords,
				s.lShoulderCoords);
		float rightDegree = degreesTwoVectors(s.rHandCoords, s.rElbowCoords,
				s.rShoulderCoords);

		boolean leftCheck = checkAngle(80, leftDegree, 7);
		boolean rightCheck = checkAngle(80, rightDegree, 7);

		if(leftCheck && rightCheck){
			writeResultTab("TRIGGERED", color(0, 0, 0), 25);
		} else {
			writeResultTab("NOTHING", color(0, 0, 0), 25);
		}
		
		PVector screenLElbow = convertToScreen(s.lElbowCoords);
		PVector screenRElbow = convertToScreen(s.rElbowCoords);

		drawDetection(screenLElbow, leftCheck);
		drawDetection(screenRElbow, rightCheck);
		

	}

	public void interaction_values(Skeleton s, Joint j) {
	
		//fill(255, 0, 0);
		//ellipse(50, 50, 200, 200);
	
		String event = "";
		float value = 0;
	
		switch (eventSelected) {
		case Constants.POSITION:
			event = "Position X";
			value = getJointFromSkeleton(s, j).x;
			outputValue = value;
			break;
		case Constants.VELOCITY:
			event = "Velocity";
			PVector tracked = getJointFromSkeleton(s, j);
	
			float positionX = tracked.x;
			float positionY = tracked.y;
			float positionZ = tracked.z;
	
			float speedX = positionX - iv.lastPositionX;
			float speedY = positionY - iv.lastPositionY;
			float speedZ = positionZ - iv.lastPositionZ;
			
			value = speedX;
			outputValue = value;
			
			// END =====================
			iv.lastPositionX = positionX;
			iv.lastPositionY = positionY;
			iv.lastPositionZ = positionZ;
			
			break;
		case Constants.POSE:
			event = "Pose";
			value = getJointFromSkeleton(s, j).x;
			break;
		case Constants.DISTANCE_TO_HEAD:
			event = "Dist to Head";
			PVector a1 = getJointFromSkeleton(s, j);
			PVector b1 = getJointFromSkeleton(s, Joint.HEAD);
			float dist1 = PVector.dist(a1, b1);
			value = dist1;
			outputValue = value;
			break;
		case Constants.DISTANCE_TO_TORSO:
			event = "Dist to Torso";
			PVector a2 = getJointFromSkeleton(s, j);
			PVector b2 = getJointFromSkeleton(s, Joint.TORSO);
			float dist2 = PVector.dist(a2, b2);
			outputValue = dist2;
			break;
		case Constants.ANGLE_BTW_HANDS:
			// Nunca entrar� aqui, porque o controlEvent j� muda o interaction para HAND_ANGLE
			event = "Angle Head";
			value = getJointFromSkeleton(s, j).x;
			break;
		case Constants.ANGLE_BTW_TORSO:
			event = "Angle Torso";
			value = getJointFromSkeleton(s, j).x;
			break;
	
		default:
			break;
		}
	
		writeOnScreen(event + " = " + value, displayWidth / 2 + offset, 50,
				color(255, 0, 0), 20);
	}

	private void drawDetection(PVector point, boolean draw) {
		if (draw) {
			noStroke();
			fill(0, 255, 0);
			ellipseMode(CENTER);
			ellipse(point.x, point.y, 20, 20);
			// writeOnScreen("DETECTOU!", color(0, 255, 0));
		}
	}

	private float degreesTwoVectors(PVector a, PVector center, PVector b) {
		PVector aCenter = PVector.sub(a, center);
		PVector bCenter = PVector.sub(b, center);
		float theta = PVector.angleBetween(aCenter, bCenter);
		return degrees(theta);
	}

	private boolean checkAngle(float reference, float degrees, float tolerance) {
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
			iStatus = InteractionStatus.values()[iSIndex++
					% InteractionStatus.values().length];
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
		case 'w':
			r.activate(1);
			break;
		default:
			break;
		}
	}

	private void drawSkeletonLines(Skeleton s) {
		// pushMatrix();
		// translate(offset - 105, 0);
		PVector head = convertToScreen(s.headCoords);
		PVector neck = convertToScreen(s.neckCoords);
		PVector lShoulder = convertToScreen(s.lShoulderCoords);
		PVector rShoulder = convertToScreen(s.rShoulderCoords);
		PVector torso = convertToScreen(s.torsoCoords);
		PVector lElbow = convertToScreen(s.lElbowCoords);
		PVector rElbow = convertToScreen(s.rElbowCoords);
		PVector lHand = convertToScreen(s.lHandCoords);
		PVector rHand = convertToScreen(s.rHandCoords);
		PVector lHip = convertToScreen(s.lHipCoords);
		PVector rHip = convertToScreen(s.rHipCoords);
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

		organizeColors();

		plotPoint(rHand, rHandColor, 15);
		plotPoint(lHand, lHandColor, 15);
		plotPoint(head, headColor, 15);
		plotPoint(rShoulder, rShouldColor, 10);
		plotPoint(lShoulder, lShouldColor, 10);
		plotPoint(neck, neckColor, 10);
		plotPoint(torso, torsoColor, 10);
		plotPoint(rElbow, rElbowColor, 10);
		plotPoint(lElbow, lElbowColor, 10);

		// strokeWeight(15);
		// point(rHand.x, rHand.y, rHand.z);
		// point(lHand.x, lHand.y, lHand.z);
		// point(head.x, head.y, head.z);
		// strokeWeight(10);
		// point(rShoulder.x, rShoulder.y, rShoulder.z);
		// point(lShoulder.x, lShoulder.y, lShoulder.z);
		// point(neck.x, neck.y, neck.z);
		// point(torso.x, torso.y, torso.z);
		// point(rElbow.x, rElbow.y, rElbow.z);
		// point(lElbow.x, lElbow.y, lElbow.z);
		// popMatrix();
		// ellipse(rHand.x, rHand.y, 40, 40);
		// ellipse(lHand.x, lHand.y, 40, 40);
	}

	public void plotPoint(PVector p, int color, int weight) {
		stroke(color);
		strokeWeight(weight);
		point(p.x, p.y, p.z);
	}

	public void organizeColors() {
		int selectedColor = color(255, 0, 0);
		int notSelectedColor = color(255);
		if (currentJoint.equals(Joint.HEAD)) {
			headColor = selectedColor;
		} else {
			headColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.NECK)) {
			neckColor = selectedColor;
		} else {
			neckColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.R_SHOULDER)) {
			rShouldColor = selectedColor;
		} else {
			rShouldColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.R_ELBOW)) {
			rElbowColor = selectedColor;
		} else {
			rElbowColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.R_HAND)) {
			rHandColor = selectedColor;
		} else {
			rHandColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.L_SHOULDER)) {
			lShouldColor = selectedColor;
		} else {
			lShouldColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.L_ELBOW)) {
			lElbowColor = selectedColor;
		} else {
			lElbowColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.L_HAND)) {
			lHandColor = selectedColor;
		} else {
			lHandColor = notSelectedColor;
		}

		if (currentJoint.equals(Joint.TORSO)) {
			torsoColor = selectedColor;
		} else {
			torsoColor = notSelectedColor;
		}

	}

	public void linePVector(PVector v1, PVector v2) {
		line(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
	}

	public void drawTracking(Skeleton s, Joint j, boolean b) {
		if (b) {
			if (tracking == null) {
				tracking = new PVector[40];
			}
			PVector v = convertToScreen(getJointFromSkeleton(s, j));
			tracking[frameCount % tracking.length] = v;
			for (int i = 0; i < tracking.length; i++) {
				PVector p = tracking[i];
				if (p != null) {
					plotPoint(p, color(0, 255, 0), 10);
				}
			}
		}

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
		float x = (800 / 2 - textWidth / 2) + offset;
		float y = height / 2 - textHeight / 2;
		// fill(127, 50);
		// rect(x, y, textWidth, textHeight);
		fill(color);
		text(txt, x, y);
	}

	public void writeResultTab(String txt, int color, int size) {
		textFont(font, size);
		float textWidth = textWidth(txt);
		float textHeight = textAscent();
		float x = (165 / 2 - textWidth / 2) + 1098;
		float y = 200 / 2 - textHeight / 2 + 381 + 25;
		// fill(127, 50);
		// rect(x, y, textWidth, textHeight);
		fill(color);
		text(txt, x, y);
	}

	public PVector getJointFromSkeleton(Skeleton s, Joint j) {
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

	/**
	 * 
	 */
	public void setupControlP5() {
		img = loadImage("drawing.png");

		pos = new PVector(20, 30);

		r = controlP5.addRadioButton("radioButton", (int) pos.x, (int) pos.y);
		r.setColorForeground(color(120));
		r.setColorActive(color(255, 0, 255));
		r.setColorLabel(color(0));

		out = controlP5.addRadioButton("output",
				(int) (offset + displayWidth + 25), 50);
		out.setColorForeground(color(120));
		out.setColorActive(color(255, 0, 0));
		out.setColorLabel(color(0));

		Label label = out.captionLabel();
		label.setFont(Font.TRUETYPE_FONT);

		slider1 = controlP5.addSlider("slider1", 0, 100, 100, 1110, 80, 100, 20);
		// slider1.setNumberOfTickMarks(100);
		slider1.setLabel("LIMIT");
		slider1.setSliderMode(Slider.FLEXIBLE);
		slider1.setColorForeground(color(120));
		slider1.setColorActive(color(200));
		slider1.setColorLabel(color(0));

		slider2 = controlP5
				.addSlider("slider2", 0, 100, 0, 1110, 200, 100, 20);
		// slider1.setNumberOfTickMarks(100);
		slider2.setLabel("LIMIT");
		slider2.setSliderMode(Slider.FLEXIBLE);
		slider2.setColorForeground(color(120));
		slider2.setColorActive(color(200));
		slider2.setColorLabel(color(0));
		// l.setItemHeight(15);
		// l.setBarHeight(15);

		// l.captionLabel().toUpperCase(true);
		// l.captionLabel().set("Options");
		// l.captionLabel().style().marginTop = 3;
		// l.valueLabel().style().marginTop = 3;

		nb1 = controlP5.addNumberbox("nb1", 0, 1110, 315, 50, 20);
		nb2 = controlP5.addNumberbox("nb2", 100, 1180, 315, 50, 20);

		// nb1.setDirection(Controller.HORIZONTAL);
		nb1.setMultiplier(-1.0f);
		nb1.setMin(0.0f);
		nb1.setMax(100.0f);
		nb2.setMultiplier(-1.0f);
		nb2.setMin(0.0f);
		nb2.setMax(100.0f);

		Toggle t1 = r.addItem("head", 1);
		Toggle t2 = r.addItem("neck", 2);
		Toggle t3 = r.addItem("r_shoulder", 3);
		Toggle t4 = r.addItem("r_elbow", 4);
		Toggle t5 = r.addItem("r_hand", 5);
		Toggle t6 = r.addItem("l_shoulder", 6);
		Toggle t7 = r.addItem("l_elbow", 7);
		Toggle t8 = r.addItem("l_hand", 8);
		Toggle t9 = r.addItem("torso", 9);
		Toggle t10 = r.addItem("r_hip", 10);
		Toggle t11 = r.addItem("r_knee", 11);
		Toggle t12 = r.addItem("r_foot", 12);
		Toggle t13 = r.addItem("l_hip", 13);
		Toggle t14 = r.addItem("l_knee", 14);
		Toggle t15 = r.addItem("l_foot", 15);

		t1.setPosition(105, 45);
		t2.setPosition(105, 78);
		t3.setPosition(72, 81);
		t4.setPosition(32, 144);
		t5.setPosition(36, 215);
		t6.setPosition(144, 82);
		t7.setPosition(185, 139);
		t8.setPosition(188, 201);
		t8.setState(true);
		t9.setPosition(108, 180);
		t10.setPosition(81, 221);
		t11.setPosition(66, 318);
		t12.setPosition(79, 401);
		t13.setPosition(141, 219);
		t14.setPosition(165, 309);
		t15.setPosition(155, 398);

		Toggle out1 = out.addItem("[TRIGGER] IF MORE THAN", 500);
		Toggle out2 = out.addItem("[TRIGGER] IF LESS THAN", 501);
		Toggle out3 = out.addItem("[TRACK] MAP BETWEEN", 502);

		out1.setPosition(0, 0);
		out2.setPosition(0, 120);
		out3.setPosition(0, 240);

		// l = controlP5.addMultiList("myList", (int) pos.x + 55,
		// (int) pos.y + 450, 100, 12);

		l = controlP5.addRadioButton("myList", (int) pos.x + 55,
				(int) pos.y + 450);
		l.setColorForeground(color(120));
		l.setColorActive(color(0, 255, 0));
		l.setColorLabel(color(255));
		// r.setItemsPerRow(5);
		l.setSpacingColumn(50);

		addToRadioButton(l, "Position", Constants.POSITION);
		addToRadioButton(l, "Velocity", Constants.VELOCITY);
		addToRadioButton(l, "Pose", Constants.POSE);
		addToRadioButton(l, "Distance to...", Constants.DISTANCE_TO_HEAD);
		addToRadioButton(l, "Angle between...", Constants.ANGLE_BTW_HANDS);

		// l.add("position", Constants.POSITION);
		// l.add("velocity", Constants.VELOCITY);
		// l.add("acceleration", Constants.ACCELERATION);
		// MultiListButton distance = l.add("distance to...", 999);
		// MultiListButton angle = l.add("angle between...", 998);
		// distance.add("head_", Constants.DISTANCE_TO_HEAD).setLabel("head");
		// distance.add("torso_",
		// Constants.DISTANCE_TO_TORSO).setLabel("torso");
		// angle.add("head__", Constants.ANGLE_BTW_HANDS).setLabel("hands");
		// angle.add("torso__", Constants.ANGLE_BTW_TORSO).setLabel("torso");
	}

	void addToRadioButton(RadioButton theRadioButton, String theName,
			int theValue) {
		Toggle t = theRadioButton.addItem(theName, theValue);
		t.captionLabel().setColorBackground(color(80));
		t.captionLabel().style().movePadding(2, 0, -1, 2);
		t.captionLabel().style().moveMargin(-2, 0, 0, -3);
		t.captionLabel().style().backgroundWidth = 90;
	}

	public void controlEvent(ControlEvent theEvent) {
		// print("got an event from "+theEvent.group().name()+"\t");
		// for (int i=0;i<theEvent.group().arrayValue().length;i++) {
		// print(int(theEvent.group().arrayValue()[i]));
		// }
		// println("\t "+theEvent.group().value());
		// myColorBackground = color(int(theEvent.group().value()*10));
		// println(theEvent.controller().name()+" = "+theEvent.value());
		int value = 0;
		if (theEvent.isGroup()) {
			// println(theEvent.controller().name()+" = "+theEvent.value());
			String name = theEvent.group().name();
			int value2 = (int) theEvent.group().value();
			if (name.equals("radioButton")) {
				// println(theEvent.group().name()+" = "+value2);
				value = (int) value2;
				// println(value);
				Joint joint = Joint.values()[value - 1];
				currentJoint = joint;
				println(joint.name());
			} else if (name.equals("output")) {
				println(theEvent.group().value());
				outputSelected = value2;
			} else if (name.equals("myList")) {
				println(value2);
				eventSelected = value2;
				if (eventSelected == Constants.ANGLE_BTW_HANDS) {
					this.iStatus = InteractionStatus.HAND_ANGLE;
					this.drawTracking = false;
				} else if(eventSelected == Constants.POSE) {
					this.iStatus = InteractionStatus.ARM_ANGLE;
					this.drawTracking = false;
					out.deactivateAll();
				} else {
					this.iStatus = InteractionStatus.VALUES;
					this.drawTracking = true;
				}
			}

		} else if (theEvent.isController()) {
			String name = theEvent.controller().name();

			if (name.equals("slider1")) {

			} else if (name.equals("slider2")) {

			}
		}
		// println(theEvent);
	}

	@Override
	public void mousePressed() {
		// println(mouseX + ", " + mouseY);
	}

	public PVector convertToScreen(PVector v1) {
		return new PVector(offset + (v1.x * displayWidth),
				v1.y * displayHeight, -v1.z * Utils.Z_MULTIPLIER);
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#000000",
		// "--present",
				"--stop-color=#cccccc", "main.RefactoringV1_7" });
	}
}

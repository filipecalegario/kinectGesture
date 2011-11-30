//package main;
//
//import java.util.Hashtable;
//import java.util.Vector;
//
//import old_architecture.GestureEvent;
//import old_architecture.Hit;
//import old_architecture.HitGestureEvent;
//import old_architecture.SpeedGestureEvent;
//
//import netP5.NetAddress;
//import oscP5.OscMessage;
//import oscP5.OscP5;
//import processing.core.PApplet;
//import processing.core.PConstants;
//import processing.core.PFont;
//import processing.core.PVector;
//import basic.Ellipse;
//import basic.Utils;
//import body.Joint;
//import body.Skeleton;
//
///**
// * @author filipecalegario
// *
// */
//public class RefactoringWithOrient extends PApplet {
//
//	private static final int Z_MULTIPLIER = 100;
//	OscP5 oscP5;
//	NetAddress myRemoteLocation;
//	int ballSize = 10;
//	Hashtable<Integer, Skeleton> skels = new Hashtable<Integer, Skeleton>();
//	PFont font;
//	int bgColor = color(0, 0, 0, 0);
//	int placeColor = 127;
//	Vector<Ellipse> ellipses;
//	boolean rectDrawStatus = false;
//	boolean lockDist = false;
//
//	float maxAngle = 0;
//	float minAngle = Float.MAX_VALUE;
//	
//	public void setup() {
//		size(600 * 4 / 3, 600, PConstants.P3D);
//		ellipses = new Vector<Ellipse>();
//		font = loadFont("CourierNew36.vlw");
//		textFont(font, 36);
//		oscP5 = new OscP5(this, 7110);
//		myRemoteLocation = new NetAddress("127.0.0.1", 9999);
//		// hint(ENABLE_OPENGL_4X_SMOOTH);
//		noStroke();
//	}
//
//	public void draw() {
//		//sendSimpleMessage("/teste", (int)mouseX);
//		background(0);
//		ambientLight(64, 64, 64);
//		lightSpecular(255, 255, 255);
//		directionalLight(224, 224, 224, .5f, 1, -1);
//		for (Skeleton s : skels.values()) {
//			s.runGestures();
//			//drawBody(s);
//			drawSkeletonLines(s);
//			drawEllipses();
//			drawHands(s);
//		}
//	}
//
//	private void drawHands(Skeleton s){
//		fill(255);
//		float leftX = s.lHandCoords.x * width;
//		float leftY = s.lHandCoords.y * height;
//		float rightX = s.rHandCoords.x * width;
//		float rightY = s.rHandCoords.y * height;
//		ellipse(leftX, leftY, 50, 50);
//		ellipse(rightX, rightY, 50, 50);
//		
//		PVector sub = PVector.sub(s.lHandCoords, s.rHandCoords);
//		PVector base = new PVector(1.0f, 0.0f, 0.0f);
//		
//		float dist = PVector.dist(s.lHandCoords, s.rHandCoords);
//		
//		float posX = leftX - (leftX-rightX)/2;
//		float posY = leftY - (leftY-rightY)/2;
//		int note = Math.round(map(posY, 0, height, 60, 80));
//		
//		sendCC(2, map(posY, 0, height, 0, 1));
//		
//		if(dist < 0.05 && lockDist == false){
//			if(rectDrawStatus == false){
//				ellipses.add(new Ellipse(posX, posY, 50, 50));
//				rectDrawStatus = true;
//				//sendSimpleMessage("/hpm/1/note", note);
//				sendNoteOn(note, 100);
//			} else {
//				rectDrawStatus = false;
//				sendNoteOff(note, 100);
//			}
//			lockDist = true;
//			
//		} else if (dist > 0.06){
//			lockDist = false;
//		}
//		
//		//text("angle= " + PVector.angleBetween(s.lHandCoords, s.rHandCoords), 30, 30);
//		//text("angle= " + PVector.angleBetween(sub, base), 30, 30);
//		float angle = Utils.calculateAngle(s.lHandCoords, s.rHandCoords);
//		float rectLength = PVector.dist(this.convertToScreen(s.lHandCoords), this.convertToScreen(s.rHandCoords));
//		float rectHeight = 30;
//		if(rectDrawStatus == true){
//			pushMatrix();
//			translate(posX, posY);
//			rotate(angle);
//			fill(placeColor);
//			rect(-rectLength/2,-rectHeight/2, rectLength, rectHeight);
//			popMatrix();
//			
//			float mappedLength = (map(rectLength, 10, 350, 0, 1));
//			sendCC(1, mappedLength);
//		}
//		PVector torsoScreen = (s.lHandCoords);
////		text("Z do Torso= " + torsoScreen.z, 30, 30);
//		
//		maxAngle = Math.max(angle, maxAngle);
//		minAngle = Math.min(angle, minAngle);
//
//		float mappedAngle = map(angle, -1.57f, 1.57f, 0, 1);
//		sendCC(3, mappedAngle);
//		
//		text("ANGLE= " + angle, 30, 30);
//		text("MAX= " + maxAngle, 30, 60);
//		text("MIN= " + minAngle, 30, 90);
//		text("MAP= " + mappedAngle, 30, 120);
//		stroke(255);
//		line(leftX, leftY, rightX, rightY);
//	}
//
//	private void sendNoteOn(int pitch, int velocity){
//		OscMessage msg = new OscMessage("/hpm/1/note");
//		msg.add(pitch);
//		msg.add(velocity);
//		msg.add(1);
//		oscP5.send(msg, myRemoteLocation);
//	}
//	
//	private void sendNoteOff(int pitch, int velocity){
//		OscMessage msg = new OscMessage("/hpm/1/note");
//		msg.add(pitch);
//		msg.add(velocity);
//		msg.add(0);
//		oscP5.send(msg, myRemoteLocation);
//	}
//	
//	/**
//	 * @param cc Controller Number [0 - 127]
//	 * @param value [0.0 - 1.0]
//	 */
//	private void sendCC(int cc, float value){
//		OscMessage msg = new OscMessage("/hpm/1/cc/" + cc);
//		msg.add(value);
//		oscP5.send(msg, myRemoteLocation);
//	}
//	
//	private void sendSimpleMessage(String addr){
//		OscMessage msg = new OscMessage(addr);
//		oscP5.send(msg, myRemoteLocation);
//	}
//	
//	private void sendSimpleMessage(String addr, float value){
//		OscMessage msg = new OscMessage(addr);
//		msg.add(value);
//		if(addr.equals("/hpm/1/note")){
//			msg.add(100);
//		}
//		oscP5.send(msg, myRemoteLocation);
//	}
//	
//	private void drawEllipses(){
//		for (Ellipse e : ellipses) {
//			ellipse(e.x,e.y,e.w,e.h);
//		}
//	}
//
//	private void drawBody(Skeleton s) {
//		PVector previous = null;
//		for (PVector vec : s.body) {
//			float posX = vec.x * width;
//			float posY = vec.y * height;
//			float posZ = -vec.z * Z_MULTIPLIER;
//			pushMatrix();
//			fill(255,0,0);
//			translate(posX, posY, posZ);
//			//sphere(2 * ballSize / vec.z);
//			noStroke();
//			ellipse(0,0,2 * ballSize / vec.z, 2 * ballSize / vec.z);
//			popMatrix();
//		}
//	}
//	
//	private void drawSkeletonLines(Skeleton s){
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
//		ellipseMode(CENTER);
//		ellipse(head.x, head.y, 2 * ballSize / s.headCoords.z, 2 * ballSize / s.headCoords.z);
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
//	
//	private void linePVector(PVector v1, PVector v2){
//		line(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
//	}
//	
//	private PVector convertToScreen(PVector v1){
//		return new PVector(v1.x * width, v1.y * height, -v1.z * Z_MULTIPLIER);
//	}
//	
//	public void oscEvent(OscMessage msg) {
//		if (msg.checkAddrPattern("/joint") && msg.checkTypetag("sifff")) {
//			Integer id = msg.get(1).intValue();
//			Skeleton s = skels.get(id);
//			if (s == null) {
//				s = new Skeleton(this, id);
//				skels.put(id, s);
//			}
//			if (msg.get(0).stringValue().equals("head")) {
//				s.headCoords.x = msg.get(2).floatValue();
//				s.headCoords.y = msg.get(3).floatValue();
//				s.headCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("neck")) {
//				s.neckCoords.x = msg.get(2).floatValue();
//				s.neckCoords.y = msg.get(3).floatValue();
//				s.neckCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_collar")) {
//				s.rCollarCoords.x = msg.get(2).floatValue();
//				s.rCollarCoords.y = msg.get(3).floatValue();
//				s.rCollarCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_shoulder")) {
//				s.rShoulderCoords.x = msg.get(2).floatValue();
//				s.rShoulderCoords.y = msg.get(3).floatValue();
//				s.rShoulderCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_elbow")) {
//				s.rElbowCoords.x = msg.get(2).floatValue();
//				s.rElbowCoords.y = msg.get(3).floatValue();
//				s.rElbowCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_wrist")) {
//				s.rWristCoords.x = msg.get(2).floatValue();
//				s.rWristCoords.y = msg.get(3).floatValue();
//				s.rWristCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_hand")) {
//				s.rHandCoords.x = msg.get(2).floatValue();
//				s.rHandCoords.y = msg.get(3).floatValue();
//				s.rHandCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_finger")) {
//				s.rFingerCoords.x = msg.get(2).floatValue();
//				s.rFingerCoords.y = msg.get(3).floatValue();
//				s.rFingerCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_collar")) {
//				s.lCollarCoords.x = msg.get(2).floatValue();
//				s.lCollarCoords.y = msg.get(3).floatValue();
//				s.lCollarCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_shoulder")) {
//				s.lShoulderCoords.x = msg.get(2).floatValue();
//				s.lShoulderCoords.y = msg.get(3).floatValue();
//				s.lShoulderCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_elbow")) {
//				s.lElbowCoords.x = msg.get(2).floatValue();
//				s.lElbowCoords.y = msg.get(3).floatValue();
//				s.lElbowCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_wrist")) {
//				s.lWristCoords.x = msg.get(2).floatValue();
//				s.lWristCoords.y = msg.get(3).floatValue();
//				s.lWristCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_hand")) {
//				s.lHandCoords.x = msg.get(2).floatValue();
//				s.lHandCoords.y = msg.get(3).floatValue();
//				s.lHandCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_finger")) {
//				s.lFingerCoords.x = msg.get(2).floatValue();
//				s.lFingerCoords.y = msg.get(3).floatValue();
//				s.lFingerCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("torso")) {
//				s.torsoCoords.x = msg.get(2).floatValue();
//				s.torsoCoords.y = msg.get(3).floatValue();
//				s.torsoCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_hip")) {
//				s.rHipCoords.x = msg.get(2).floatValue();
//				s.rHipCoords.y = msg.get(3).floatValue();
//				s.rHipCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_knee")) {
//				s.rKneeCoords.x = msg.get(2).floatValue();
//				s.rKneeCoords.y = msg.get(3).floatValue();
//				s.rKneeCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_ankle")) {
//				s.rAnkleCoords.x = msg.get(2).floatValue();
//				s.rAnkleCoords.y = msg.get(3).floatValue();
//				s.rAnkleCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("r_foot")) {
//				s.rFootCoords.x = msg.get(2).floatValue();
//				s.rFootCoords.y = msg.get(3).floatValue();
//				s.rFootCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_hip")) {
//				s.lHipCoords.x = msg.get(2).floatValue();
//				s.lHipCoords.y = msg.get(3).floatValue();
//				s.lHipCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_knee")) {
//				s.lKneeCoords.x = msg.get(2).floatValue();
//				s.lKneeCoords.y = msg.get(3).floatValue();
//				s.lKneeCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_ankle")) {
//				s.lAnkleCoords.x = msg.get(2).floatValue();
//				s.lAnkleCoords.y = msg.get(3).floatValue();
//				s.lAnkleCoords.z = msg.get(4).floatValue();
//			} else if (msg.get(0).stringValue().equals("l_foot")) {
//				s.lFootCoords.x = msg.get(2).floatValue();
//				s.lFootCoords.y = msg.get(3).floatValue();
//				s.lFootCoords.z = msg.get(4).floatValue();
//			}
//		} else if (msg.checkAddrPattern("/new_user") && msg.checkTypetag("i")) {
//			// A new user is in front of the kinect... Tell him to do the
//			// calibration pose!
//			println("New user with ID = " + msg.get(0).intValue());
//		} else if (msg.checkAddrPattern("/new_skel") && msg.checkTypetag("i")) {
//			// New skeleton calibrated! Lets create it!
//			Integer id = msg.get(0).intValue();
//			Skeleton s = new Skeleton(this, id);
//			skels.put(id, s);
//		} else if (msg.checkAddrPattern("/lost_user") && msg.checkTypetag("i")) {
//			// Lost user/skeleton
//			Integer id = msg.get(0).intValue();
//			println("Lost user " + id);
//			skels.remove(id);
//		} 
////		else if (msg.checkAddrPattern("/orient") && msg.checkTypetag("sifffffffff    ")){
////			if (msg.get(0).stringValue().equals("r_hand")) {
////				float x1 = msg.get(2).floatValue();
////				float y1 = msg.get(3).floatValue();
////				float z1 = msg.get(4).floatValue();
////				//float x2 = msg.get(5).floatValue();
////				//float y2 = msg.get(6).floatValue();
////				//float z2 = msg.get(7).floatValue();
////				//float x3 = msg.get(8).floatValue();
////				//float y3 = msg.get(9).floatValue();
////				//float z3 = msg.get(10).floatValue();
////				//System.out.println("Orient:" + " (" +  x1 + ", " + y1 + ", " + z1 + ") " + " (" +  x2 + ", " + y2 + ", " + z2 + ") "+ " (" +  x3 + ", " + y3 + ", " + z3 + ") " );
////				System.out.println("Orient:" + " (" +  x1 + ", " + y1 + ", " + z1 + ") ");
////			}
////		}
//	}
//	
//	public void gestureEvent(GestureEvent event){
//		if(event instanceof SpeedGestureEvent){
//			SpeedGestureEvent sge = (SpeedGestureEvent)event;
//			float speed = sge.getSpeed();
//			//println("Speed: " + sge.getJoint().name() + " - " + speed);
//			bgColor = color((speed*20), 255, 255);
//		} else
//		if(event instanceof HitGestureEvent){
//			HitGestureEvent hge = (HitGestureEvent) event;
//			Joint joint = hge.getJoint();
//			Hit hit = hge.getHit();
//			//println("Hit: " + joint + " - " + hit);
//		}
//	}
//
//	static public void main(String args[]) {
//		PApplet.main(new String[] { "--bgcolor=#666666",
//				"--stop-color=#cccccc", "Refactoring" });
//	}
//}


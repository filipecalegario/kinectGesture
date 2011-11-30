package architecture;

import java.util.Hashtable;
import java.util.Vector;

import oscP5.OscP5;
import processing.core.PApplet;
import body.Skeleton;

public class SkeletonAnalyzer {

	private PApplet main;
	private OscP5 client;
	public Hashtable<Integer, Skeleton> skels;
	private Vector<RegisteredAction> onActions = new Vector<RegisteredAction>();
	private Vector<RegisteredAction> offActions = new Vector<RegisteredAction>();
	private Vector<RegisteredAction> updateActions = new Vector<RegisteredAction>();
	private Vector<SkeletonListener> listeners = new Vector<SkeletonListener>();
	
	public SkeletonAnalyzer(PApplet parent){
		this.main = parent;
		this.client = new OscP5(this, 7110);
		
		this.skels = skels = new Hashtable<Integer, Skeleton>();
		
		this.client.plug(this, "newSkeleton", "/new_skel");
		this.client.plug(this, "lostSkeleton", "/lost_user");
		this.client.plug(this, "jointReceived", "/joint");
	}
	
	public void addListener(SkeletonListener sl){
		this.listeners.add(sl);
	}
	
	public void lostSkeleton(int id){
		skels.remove(id);
	}
	
	public void newSkeleton(int id){
		Skeleton s = new Skeleton(main, id);
		skels.put(id, s);
	}
	
	public void jointReceived(String joint, int id, float x, float y, float z){
		//System.out.println(skels.size());
		Skeleton s = skels.get(id);
		if (s == null) {
			s = new Skeleton(main, id);
			skels.put(id, s);
		}
		if (joint.equals("head")) {
			s.headCoords.x = x;
			s.headCoords.y = y;
			s.headCoords.z = z;
		} else if (joint.equals("neck")) {
			s.neckCoords.x = x;
			s.neckCoords.y = y;
			s.neckCoords.z = z;
		} else if (joint.equals("r_collar")) {
			s.rCollarCoords.x = x;
			s.rCollarCoords.y = y;
			s.rCollarCoords.z = z;
		} else if (joint.equals("r_shoulder")) {
			s.rShoulderCoords.x = x;
			s.rShoulderCoords.y = y;
			s.rShoulderCoords.z = z;
		} else if (joint.equals("r_elbow")) {
			s.rElbowCoords.x = x;
			s.rElbowCoords.y = y;
			s.rElbowCoords.z = z;
		} else if (joint.equals("r_wrist")) {
			s.rWristCoords.x = x;
			s.rWristCoords.y = y;
			s.rWristCoords.z = z;
		} else if (joint.equals("r_hand")) {
			s.rHandCoords.x = x;
			s.rHandCoords.y = y;
			s.rHandCoords.z = z;
		} else if (joint.equals("r_finger")) {
			s.rFingerCoords.x = x;
			s.rFingerCoords.y = y;
			s.rFingerCoords.z = z;
		} else if (joint.equals("r_collar")) {
			s.lCollarCoords.x = x;
			s.lCollarCoords.y = y;
			s.lCollarCoords.z = z;
		} else if (joint.equals("l_shoulder")) {
			s.lShoulderCoords.x = x;
			s.lShoulderCoords.y = y;
			s.lShoulderCoords.z = z;
		} else if (joint.equals("l_elbow")) {
			s.lElbowCoords.x = x;
			s.lElbowCoords.y = y;
			s.lElbowCoords.z = z;
		} else if (joint.equals("l_wrist")) {
			s.lWristCoords.x = x;
			s.lWristCoords.y = y;
			s.lWristCoords.z = z;
		} else if (joint.equals("l_hand")) {
			s.lHandCoords.x = x;
			s.lHandCoords.y = y;
			s.lHandCoords.z = z;
		} else if (joint.equals("l_finger")) {
			s.lFingerCoords.x = x;
			s.lFingerCoords.y = y;
			s.lFingerCoords.z = z;
		} else if (joint.equals("torso")) {
			s.torsoCoords.x = x;
			s.torsoCoords.y = y;
			s.torsoCoords.z = z;
		} else if (joint.equals("r_hip")) {
			s.rHipCoords.x = x;
			s.rHipCoords.y = y;
			s.rHipCoords.z = z;
		} else if (joint.equals("r_knee")) {
			s.rKneeCoords.x = x;
			s.rKneeCoords.y = y;
			s.rKneeCoords.z = z;
		} else if (joint.equals("r_ankle")) {
			s.rAnkleCoords.x = x;
			s.rAnkleCoords.y = y;
			s.rAnkleCoords.z = z;
		} else if (joint.equals("r_foot")) {
			s.rFootCoords.x = x;
			s.rFootCoords.y = y;
			s.rFootCoords.z = z;
		} else if (joint.equals("l_hip")) {
			s.lHipCoords.x = x;
			s.lHipCoords.y = y;
			s.lHipCoords.z = z;
		} else if (joint.equals("l_knee")) {
			s.lKneeCoords.x = x;
			s.lKneeCoords.y = y;
			s.lKneeCoords.z = z;
		} else if (joint.equals("l_ankle")) {
			s.lAnkleCoords.x = x;
			s.lAnkleCoords.y = y;
			s.lAnkleCoords.z = z;
		} else if (joint.equals("l_foot")) {
			s.lFootCoords.x = x;
			s.lFootCoords.y = y;
			s.lFootCoords.z = z;
		}
		update();
	}
	
	public void update(){
		for (SkeletonListener sl : listeners) {
			sl.update();
		}
	}
	
//	public void draw(){
//		for (Skeleton s : this.skels.values()) {
//			s.drawSkeleton(true);
//			s.drawHands(true);
//		}
//	}
	
//	/**
//	 * Sets whether the Wiimote pointer should be drawn or not.
//	 * 
//	 * @param d whether or not to draw the pointer
//	 */
//	public void drawSkeletons(boolean d) {
//		if (d) {
//			main.registerDraw(this);
//		} else {
//			main.unregisterDraw(this);
//		}
//	}

	public void registerOnAction(String string,
			Object o) {
		this.onActions.add(new RegisteredAction(string, o));
	}
	
	public void registerOffAction(String string,
			Object o) {
		this.offActions.add(new RegisteredAction(string, o));
	}

	public void registerUpdateAction(String string,
			Object o) {
		this.updateActions.add(new RegisteredAction(string, o));
	}
	
}

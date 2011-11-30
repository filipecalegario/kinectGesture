package main;
import java.util.Hashtable;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import processing.core.PVector;
import body.Skeleton;

public class PUTAQUEPARIU extends PApplet {

OscP5 oscP5;

int ballSize = 10;
Hashtable<Integer, Skeleton> skels = new Hashtable<Integer, Skeleton>();

public void setup() {
    size(screen.height*4/3/2, screen.height/2, P3D); //Keep 4/3 aspect ratio, since it matches the kinect's.
    oscP5 = new OscP5(this, 7110);
    //hint(ENABLE_OPENGL_4X_SMOOTH);
    noStroke();
}

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


public void draw()
{
  background(0);  
  ambientLight(64, 64, 64);
  lightSpecular(255,255,255);
  directionalLight(224,224,224, .5f, 1, -1);

  for (Skeleton s: skels.values()) {
	  //s.drawHands(true);
	  //s.drawSkeleton(true);
	  //s.draw();
    fill(s.colors[0], s.colors[1], s.colors[2]);
    for (PVector j: s.body) {
      pushMatrix();
      translate(j.x*width, j.y*height, -j.z*300);
      sphere(2 * ballSize/j.z);
      popMatrix();
    }
  }
  fill(255,0,0);
	ellipse((frameCount*15)%width, 20, 20, 20);
}

static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#666666", "--stop-color=#cccccc", "main.PUTAQUEPARIU" });
  }
}

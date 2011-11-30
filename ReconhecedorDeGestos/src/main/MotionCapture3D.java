package main;
import processing.core.*; 
import processing.xml.*; 

import javax.media.j3d.*;

import processing.opengl.*; 
import oscP5.*; 
import netP5.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class MotionCapture3D extends PApplet {

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
    // We have received joint coordinates, let's find out which skeleton/joint and save the values ;)
    Integer id = msg.get(1).intValue();
    Skeleton s = skels.get(id);
    if (s == null) {
      s = new Skeleton(id);
      skels.put(id, s);
    }
    if (msg.get(0).stringValue().equals("head")) {
      s.headCoords[0] = msg.get(2).floatValue();
      s.headCoords[1] = msg.get(3).floatValue();
      s.headCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("neck")) {
      s.neckCoords[0] = msg.get(2).floatValue();
      s.neckCoords[1] = msg.get(3).floatValue();
      s.neckCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_collar")) {
      s.rCollarCoords[0] = msg.get(2).floatValue();
      s.rCollarCoords[1] = msg.get(3).floatValue();
      s.rCollarCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_shoulder")) {
      s.rShoulderCoords[0] = msg.get(2).floatValue();
      s.rShoulderCoords[1] = msg.get(3).floatValue();
      s.rShoulderCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_elbow")) {
      s.rElbowCoords[0] = msg.get(2).floatValue();
      s.rElbowCoords[1] = msg.get(3).floatValue();
      s.rElbowCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_wrist")) {
      s.rWristCoords[0] = msg.get(2).floatValue();
      s.rWristCoords[1] = msg.get(3).floatValue();
      s.rWristCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_hand")) {
      s.rHandCoords[0] = msg.get(2).floatValue();
      s.rHandCoords[1] = msg.get(3).floatValue();
      s.rHandCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_finger")) {
      s.rFingerCoords[0] = msg.get(2).floatValue();
      s.rFingerCoords[1] = msg.get(3).floatValue();
      s.rFingerCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_collar")) {
      s.lCollarCoords[0] = msg.get(2).floatValue();
      s.lCollarCoords[1] = msg.get(3).floatValue();
      s.lCollarCoords[2] = msg.get(4).floatValue();
    }  
    else if (msg.get(0).stringValue().equals("l_shoulder")) {
      s.lShoulderCoords[0] = msg.get(2).floatValue();
      s.lShoulderCoords[1] = msg.get(3).floatValue();
      s.lShoulderCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("l_elbow")) {
      s.lElbowCoords[0] = msg.get(2).floatValue();
      s.lElbowCoords[1] = msg.get(3).floatValue();
      s.lElbowCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("l_wrist")) {
      s.lWristCoords[0] = msg.get(2).floatValue();
      s.lWristCoords[1] = msg.get(3).floatValue();
      s.lWristCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("l_hand")) {
      s.lHandCoords[0] = msg.get(2).floatValue();
      s.lHandCoords[1] = msg.get(3).floatValue();
      s.lHandCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("l_finger")) {
      s.lFingerCoords[0] = msg.get(2).floatValue();
      s.lFingerCoords[1] = msg.get(3).floatValue();
      s.lFingerCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("torso")) {
      s.torsoCoords[0] = msg.get(2).floatValue();
      s.torsoCoords[1] = msg.get(3).floatValue();
      s.torsoCoords[2] = msg.get(4).floatValue();
    }
    else if (msg.get(0).stringValue().equals("r_hip")) {
      s.rHipCoords[0] = msg.get(2).floatValue();
      s.rHipCoords[1] = msg.get(3).floatValue();
      s.rHipCoords[2] = msg.get(4).floatValue();
    } 
    else if (msg.get(0).stringValue().equals("r_knee")) {
      s.rKneeCoords[0] = msg.get(2).floatValue();
      s.rKneeCoords[1] = msg.get(3).floatValue();
      s.rKneeCoords[2] = msg.get(4).floatValue();
    } 
    else if (msg.get(0).stringValue().equals("r_ankle")) {
      s.rAnkleCoords[0] = msg.get(2).floatValue();
      s.rAnkleCoords[1] = msg.get(3).floatValue();
      s.rAnkleCoords[2] = msg.get(4).floatValue();
    } 
    else if (msg.get(0).stringValue().equals("r_foot")) {
      s.rFootCoords[0] = msg.get(2).floatValue();
      s.rFootCoords[1] = msg.get(3).floatValue();
      s.rFootCoords[2] = msg.get(4).floatValue();
    } 
    else if (msg.get(0).stringValue().equals("l_hip")) {
      s.lHipCoords[0] = msg.get(2).floatValue();
      s.lHipCoords[1] = msg.get(3).floatValue();
      s.lHipCoords[2] = msg.get(4).floatValue();
    } 
    else if (msg.get(0).stringValue().equals("l_knee")) {
      s.lKneeCoords[0] = msg.get(2).floatValue();
      s.lKneeCoords[1] = msg.get(3).floatValue();
      s.lKneeCoords[2] = msg.get(4).floatValue();
    } 
    else if (msg.get(0).stringValue().equals("l_ankle")) {
      s.lAnkleCoords[0] = msg.get(2).floatValue();
      s.lAnkleCoords[1] = msg.get(3).floatValue();
      s.lAnkleCoords[2] = msg.get(4).floatValue();
    } 
    else if (msg.get(0).stringValue().equals("l_foot")) {
      s.lFootCoords[0] = msg.get(2).floatValue();
      s.lFootCoords[1] = msg.get(3).floatValue();
      s.lFootCoords[2] = msg.get(4).floatValue();
    } 
  }
  else if (msg.checkAddrPattern("/new_user") && msg.checkTypetag("i")) {
    // A new user is in front of the kinect... Tell him to do the calibration pose!
    println("New user with ID = " + msg.get(0).intValue());
  }
  else if(msg.checkAddrPattern("/new_skel") && msg.checkTypetag("i")) {
    //New skeleton calibrated! Lets create it!
    Integer id = msg.get(0).intValue();
    Skeleton s = new Skeleton(id);
    skels.put(id, s);
  }
  else if(msg.checkAddrPattern("/lost_user") && msg.checkTypetag("i")) {
    //Lost user/skeleton
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
    fill(s.colors[0], s.colors[1], s.colors[2]);
    for (float[] j: s.allCoords) {
      pushMatrix();
      translate(j[0]*width, j[1]*height, -j[2]*300);
      sphere(2 * ballSize/j[2]);
      popMatrix();
    }
  }
  fill(255,0,0);
	ellipse((frameCount*15)%width, 20, 20, 20);
}
class Skeleton {
  // We just use this class as a structure to store the joint coordinates sent by OSC.
  // The format is {x, y, z}, where x and y are in the [0.0, 1.0] interval, 
  // and z is in the [0.0, 7.0] interval.
  float headCoords[] = new float[3];
  float neckCoords[] = new float[3];
  float rCollarCoords[] = new float[3];
  float rShoulderCoords[] = new float[3];
  float rElbowCoords[] = new float[3];
  float rWristCoords[] = new float[3];
  float rHandCoords[] = new float[3];
  float rFingerCoords[] = new float[3];
  float lCollarCoords[] = new float[3];
  float lShoulderCoords[] = new float[3];
  float lElbowCoords[] = new float[3];
  float lWristCoords[] = new float[3];
  float lHandCoords[] = new float[3];
  float lFingerCoords[] = new float[3];
  float torsoCoords[] = new float[3];
  float rHipCoords[] = new float[3];
  float rKneeCoords[] = new float[3];
  float rAnkleCoords[] = new float[3];
  float rFootCoords[] = new float[3];
  float lHipCoords[] = new float[3];
  float lKneeCoords[] = new float[3];
  float lAnkleCoords[] = new float[3];
  float lFootCoords[] = new float[3];
  float[] allCoords[] = {headCoords, neckCoords, rCollarCoords, rShoulderCoords, rElbowCoords, rWristCoords,
                       rHandCoords, rFingerCoords, lCollarCoords, lShoulderCoords, lElbowCoords, lWristCoords,
                       lHandCoords, lFingerCoords, torsoCoords, rHipCoords, rKneeCoords, rAnkleCoords,
                       rFootCoords, lHipCoords, lKneeCoords, lAnkleCoords, lFootCoords};
                    
  int id; //here we store the skeleton's ID as assigned by OpenNI and sent through OSC.
  float colors[] = {255, 255, 255};// The color of this skeleton

  Skeleton(int id) {
    this.id = id;
    colors[0] = random(128, 255);
    colors[1] = random(128, 255);
    colors[2] = random(128, 255);
  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#666666", "--stop-color=#cccccc", "main.MotionCapture3D" });
  }
}

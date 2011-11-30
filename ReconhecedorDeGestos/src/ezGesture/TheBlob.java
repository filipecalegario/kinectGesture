package ezGesture;

import processing.core.*;
import processing.xml.*;

import net.silentlycrashing.gestures.*;
import net.silentlycrashing.gestures.preset.*;

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

public class TheBlob extends PApplet {

	/**
	 * A simple example of the <a
	 * href="http://www.silentlycrashing.net/ezgestures/">ezGestures</a> library
	 * presets.
	 * <p>
	 * Interact with the blob:<br>
	 * &nbsp;&nbsp;- twirl clockwise to make the blob rotate right<br>
	 * &nbsp;&nbsp;- twirl counter-clockwise to make the blob rotate left<br>
	 * &nbsp;&nbsp;- shake horizontally to make the blob grow
	 * </p>
	 * <p>
	 * by Elie Zananiri<br>
	 * <a href="http://www.silentlycrashing.net/">silentlyCrashing::net</a>
	 * </p>
	 * <p>
	 * Thanks go out to <a href="http://www.ricardmarxer.com/">Ricard Marxer
	 * Pi&ntilde;&oacute;n</a> for helping create the blob.
	 * </p>
	 */

	int NUM_PTS = 30;

	float DEFAULT_ROTATION = 0.001f;
	float ROTATION_INC = 0.001f;
	float rotation = 0;
	float currRotation;
	int rotationState;

	float MIN_SCALE = 0.2f;
	float SCALE_INC = 0.002f;
	float currScale;
	boolean scaleUp = false;

	GestureAnalyzer brain;
	ConcurrentGestureListener cwTwirlEar;
	ConcurrentGestureListener ccwTwirlEar;
	ConcurrentGestureListener hShakeEar;

	public void setup() {
		// init the applet
		size(400, 400);
		smooth();
		stroke(153, 204, 102);
		strokeWeight(2);
		fill(153, 204, 102, 100);

		currScale = MIN_SCALE;
		currRotation = DEFAULT_ROTATION;

		// init the gesture listeners
		// brain = new MouseGestureAnalyzer(this);
		OSCeletonGestureAnalyzer osc = new OSCeletonGestureAnalyzer(this);
		osc.drawPointer(true);
		brain = osc;
		cwTwirlEar = new ConcurrentCWTwirlListener(this, brain);
		cwTwirlEar.registerOnAction("setRotationRight", this);
		cwTwirlEar.registerOffAction("setRotationDefault", this);
		ccwTwirlEar = new ConcurrentCCWTwirlListener(this, brain);
		ccwTwirlEar.registerOnAction("setRotationLeft", this);
		ccwTwirlEar.registerOffAction("setRotationDefault", this);
		hShakeEar = new ConcurrentHShakeListener(this, brain);
		hShakeEar.registerOnAction("toggleScaleUp", this);
		hShakeEar.registerOffAction("toggleScaleUp", this);
	}

	public void draw() {
		background(255);

		rotation += currRotation;

		noStroke();
		fill(255, 0, 0, 80);
		// draw the blob
		pushMatrix();
		translate(width / 2, height / 2);
		rotate(rotation);
		translate(-width / 2, -height / 2);

		beginShape();
		for (int i = 0; i < NUM_PTS + 3; i++) {
			curveVertex(getX(i % NUM_PTS), getY(i % NUM_PTS));
		}
		endShape();
		popMatrix();

		// set the transformation values for the next frame
		if (scaleUp)
			incScale();
		else
			resetScale();

		if (rotationState == 0)
			resetRotation();
		else if (rotationState == 1)
			incRotation();
		else
			decRotation();
	}

	public float getX(int i) {
		return 100 * (noise(frameCount / 200.0f, i) + currScale)
				* sin(2 * PI * i / NUM_PTS) + width / 2;
	}

	public float getY(int i) {
		return 100 * (noise(frameCount / 200.0f, i) + currScale)
				* cos(2 * PI * i / NUM_PTS) + height / 2;
	}

	public void setRotationDefault() {
		rotationState = 0;
	}

	public void setRotationRight() {
		rotationState = 1;
	}

	public void setRotationLeft() {
		rotationState = 2;
	}

	public void incRotation() {
		currRotation += ROTATION_INC;
	}

	public void decRotation() {
		currRotation -= ROTATION_INC;
	}

	public void resetRotation() {
		if (currRotation > DEFAULT_ROTATION)
			decRotation();
		else if (currRotation < DEFAULT_ROTATION)
			incRotation();
	}

	public void toggleScaleUp() {
		scaleUp = !scaleUp;
	}

	public void incScale() {
		currScale += SCALE_INC;
	}

	public void resetScale() {
		if (currScale > MIN_SCALE)
			currScale -= SCALE_INC;
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#666666",
				"--stop-color=#cccccc", "ezGesture.TheBlob" });
	}
}

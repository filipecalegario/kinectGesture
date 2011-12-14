package main;

import processing.core.*;
import processing.xml.*;

import architecture.SimpleSkeletonAnalyzer;
import controlP5.*;

import interaction.InteractionStatus;
import interaction.InteractionVariables;

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

import music.MusicModule;

public class SkeletonRadioButton extends PApplet {

	/**
	 * ControlP5 RadioButton by andreas schlegel, 2009
	 */

	MusicModule musicModule;
	SimpleSkeletonAnalyzer skeletonAnalyzer;
	InteractionStatus iStatus;

	PFont font;

	ControlP5 controlP5;
	PImage img;
	PVector pos;
	RadioButton r;
	MultiList l;

	//int myColorBackground = color(0, 0, 0);
	
	// Interactions variables
	private boolean rectDrawStatus;
	private boolean lockDist;

	InteractionVariables iv;

	int iSIndex;

	public void setup() {
		size(1085, 600);
		smooth();
		setupControlP5();
	}

	private void setupControlP5() {
		img = loadImage("drawing.png");
		controlP5 = new ControlP5(this);
		pos = new PVector(20, 30);

		r = controlP5.addRadioButton("radioButton", (int) pos.x, (int) pos.y);
		r.setColorForeground(color(120));
		r.setColorActive(color(255, 0, 255));
		r.setColorLabel(color(0));

		l = controlP5.addMultiList("myList", (int) pos.x + 55,
				(int) pos.y + 450, 100, 12);
		// l.setItemHeight(15);
		// l.setBarHeight(15);

		// l.captionLabel().toUpperCase(true);
		// l.captionLabel().set("Options");
		// l.captionLabel().style().marginTop = 3;
		// l.valueLabel().style().marginTop = 3;

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
		t9.setPosition(108, 180);
		t10.setPosition(81, 221);
		t11.setPosition(66, 318);
		t12.setPosition(79, 401);
		t13.setPosition(141, 219);
		t14.setPosition(165, 309);
		t15.setPosition(155, 398);

		l.add("position", 100);
		l.add("velocity", 101);
		l.add("acceleration", 102);
		MultiListButton distance = l.add("distance to...", 103);
		MultiListButton angle = l.add("angle between...", 104);
		distance.add("head_", 105).setLabel("head");
		distance.add("torso_", 106).setLabel("torso");
		angle.add("head__", 107).setLabel("head");
		angle.add("torso__", 108).setLabel("torso");

		// l.setColorBackground(color(255, 128));
		// l.setColorActive(color(0, 0, 255, 128));
	}

	public void draw() {
		background(255);
		image(img, pos.x, pos.y);
	}

	public void keyPressed() {
		if (key == ' ') {
			r.deactivateAll();
		} else if (key == 'a') {
			r.activate("50");
		} else if (key >= '0' && key < '5') {
			// convert a key-number (48-52) to an int between 0 and 4
			int n = PApplet.parseInt(key) - 48;
			r.activate(n);
		}
	}

	public void mousePressed() {
		println(mouseX + ", " + mouseY);
	}

	public void controlEvent(ControlEvent theEvent) {
		// print("got an event from "+theEvent.group().name()+"\t");
		// for (int i=0;i<theEvent.group().arrayValue().length;i++) {
		// print(int(theEvent.group().arrayValue()[i]));
		// }
		// println("\t "+theEvent.group().value());
		// myColorBackground = color(int(theEvent.group().value()*10));
		// println(theEvent.controller().name()+" = "+theEvent.value());
		println(theEvent);
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#FFFFFF",
				"main.SkeletonRadioButton" });
	}
}

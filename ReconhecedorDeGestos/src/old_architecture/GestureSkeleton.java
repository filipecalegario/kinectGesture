package old_architecture;

import java.util.Vector;

import processing.core.PApplet;
import body.Joint;
import body.Skeleton;


public class GestureSkeleton {
	
	private PApplet main;
	private Skeleton skeleton;
	private Vector<GestureJointAnalyzer> analyzers;
	
	public GestureSkeleton(PApplet main, Skeleton skeleton){
		this.main = main;
		this.skeleton = skeleton;
		this.analyzers = new Vector<GestureJointAnalyzer>();
		addAnalyzers();
	}
	
	private void addAnalyzers(){
		this.analyzers.add(new GestureJointAnalyzer(this.main, Joint.L_HAND, skeleton.lHandCoords, 15));
		this.analyzers.add(new GestureJointAnalyzer(this.main, Joint.L_ELBOW, skeleton.lElbowCoords, 5));
	}
	
	public void runAnalyzers(){
		for (GestureJointAnalyzer analyzer : this.analyzers) {
			analyzer.analyze();
		}
	}

}

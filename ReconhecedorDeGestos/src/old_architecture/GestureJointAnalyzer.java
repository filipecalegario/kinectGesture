package old_architecture;


import java.lang.reflect.Method;

import main.HorrivelGlobal;
import processing.core.PApplet;
import processing.core.PVector;
import basic.Utils;
import body.Joint;

public class GestureJointAnalyzer {

	public Joint joint;

	private PVector old;
	private PVector current;
	private PApplet main;
	public Method eventMethod;
	private UnidimensionalAnalysis uaX;
	private UnidimensionalAnalysis uaY;
	private UnidimensionalAnalysis uaZ;
	private float limit;
	private float calibration;

	public GestureJointAnalyzer(PApplet main, Joint joint, PVector current, float limit) {
		this.joint = joint;
		this.current = current;
		this.old = new PVector(0, 0, 0);
		this.main = main;
		this.uaX = new UnidimensionalAnalysis();
		this.uaY = new UnidimensionalAnalysis();
		this.uaZ = new UnidimensionalAnalysis();
		this.limit = limit;
		this.calibration = 5;
		try {
			// Looking for a method called "myEvent", with one argument of
			// PEvent type
			eventMethod = main.getClass().getMethod("gestureEvent",
					new Class[] { GestureEvent.class });
		} catch (Exception e) {
			System.out.println("Method not in parent class? " + e);
		}
	}
	
	
	
	public float getCalibration() {
		return calibration;
	}



	public void setCalibration(float calibration) {
		this.calibration = calibration;
	}



	private void updateUA(PVector current){
		this.uaX.setCurrentValue(current.x);
		this.uaY.setCurrentValue(current.y);
		this.uaZ.setCurrentValue(current.z);
	}

	public void analyze() {
		PVector old_mod = new PVector(this.old.x * main.width, this.old.y
				* main.height, this.old.z * 100);
		PVector current_mod = new PVector(this.current.x * main.width,
				this.current.y * main.height, this.current.z * 100);
		float speed = PVector.dist(old_mod, current_mod);
		if (speed > 10) {
			SpeedGestureEvent ge = new SpeedGestureEvent(this.joint, speed);
			// callMethod(ge);
		}
		
//		if(speed > 10){
//			UnidimensionalEvent ue = this.uaX.analyse(current_mod.x);
//			
//			switch (ue) {
//			case PEAKDOWN:
//				System.out.println("<<<<<<<=======");
//				break;
//			case PEAKUP:
//				System.out.println("=======>>>>>>>");
//				break;
//				
//			default:
//				break;
//			}
//			
//		}
		
		
		PVector oldNorm = Utils.copyPVector(old);
		PVector currentNorm = Utils.copyPVector(current);
		oldNorm.normalize();
		currentNorm.normalize();

		float deltaX = oldNorm.x - currentNorm.x;
		float absDeltaX = Math.abs(deltaX);
		float deltaY = oldNorm.y - currentNorm.y;
		float absDeltaY = Math.abs(deltaY);
		float deltaZ = oldNorm.z - currentNorm.z;
		float absDeltaZ = Math.abs(deltaZ);
		
		//main.println("DELTAS: " + absDeltaX + ", " + absDeltaY + ", " + absDeltaZ);
		//main.println(currentNorm.x);
		
		Hit hit = Hit.NONE;

		HitGestureEvent ge = new HitGestureEvent(this.joint, hit);

		if (speed > limit) {

			if(absDeltaX > absDeltaY && absDeltaX > absDeltaZ){
				if(deltaX > 0){
					hit = Hit.LEFT;
				} else {
					hit = Hit.RIGHT;
				}
			} else if (absDeltaY > absDeltaX && absDeltaY > absDeltaZ){
				if(deltaY > 0){
					hit = Hit.UP;
				} else {
					hit = Hit.DOWN;
				}
			} else if (absDeltaZ > absDeltaX && absDeltaZ > absDeltaY){
				if(deltaZ > 0){
					hit = Hit.BACK;
				} else {
					hit = Hit.FORWARD;
				}
			}
			
			//System.out.println(ge);
		} else {
			hit = Hit.NONE;
		}
		ge.setHit(hit);
		//callMethod(ge);

		if (absDeltaZ > HorrivelGlobal.calibration) {
			Hit hit2;
			if (deltaZ > 0) {
				hit2 = Hit.FORWARD;
			} else {
				hit2 = Hit.BACK;
			}
			GestureEvent ge1 = new HitGestureEvent(this.joint, hit2);
			callMethod(ge1);
		}
		
		this.old = Utils.copyPVector(this.current);
	}
	
	private void callMethod(GestureEvent ge) {
		// As long as the method exists
		if (eventMethod != null) {
			try {
				// Call the method with this object as the argument!
				eventMethod.invoke(main, new GestureEvent[] { ge });
			} catch (Exception e) {
				// Error handling
				System.err
						.println("I couldn't invoke that method for some reason.");
				e.printStackTrace();
				eventMethod = null;
			}
		}
	}

}

package old_architecture;
import body.Joint;


public class SpeedGestureEvent extends GestureEvent {
	
	private Joint joint;
	private float speed;
	
	public SpeedGestureEvent(Joint joint, float speed) {
		super();
		this.joint = joint;
		this.speed = speed;
	}

	public Joint getJoint() {
		return joint;
	}

	public void setJoint(Joint joint) {
		this.joint = joint;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
}

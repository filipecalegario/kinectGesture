package old_architecture;

import body.Joint;


public class HitGestureEvent extends GestureEvent{

	private Joint joint;
	private Hit hit;
	
	public HitGestureEvent(Joint joint, Hit hit) {
		super();
		this.joint = joint;
		this.hit = hit;
	}

	public Joint getJoint() {
		return joint;
	}
	public void setJoint(Joint joint) {
		this.joint = joint;
	}
	public Hit getHit() {
		return hit;
	}
	public void setHit(Hit hit) {
		this.hit = hit;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Hit: " + joint.name() + " - " + hit.name();
	}
	
}

package basic;

import processing.core.PApplet;
import processing.core.PVector;

public class Rectangle {

	private float length;
	private float height;
	private float angle;
	private PVector point;
	private int color;
	private float speed;
	private int lifeCycle;

	public Rectangle() {

	}

	public Rectangle(float length, float height, float angle,
			PVector middlePoint, int color) {
		super();
		this.length = length;
		this.height = height;
		this.angle = angle;
		this.point = middlePoint;
		this.color = color;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public PVector getPoint() {
		return point;
	}

	public void setMiddlePoint(PVector middlePoint) {
		this.point = middlePoint;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(int lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public void render(PApplet main) {
		float mappedAngle = PApplet.map(this.angle, -PApplet.PI / 2, PApplet.PI / 2, 0, 1);
		float colorHue = mappedAngle * 100;

		main.noStroke();
		main.pushMatrix();
		main.translate(this.point.x, this.point.y);
		main.rotateZ(this.angle);
		main.colorMode(PApplet.HSB, 100);
		main.fill(colorHue, 100, 100, this.lifeCycle * 5);
		main.colorMode(PApplet.RGB, 255);
		main.rect(-this.length / 2, -this.height / 2, this.length, this.height);
		main.popMatrix();
	}

}

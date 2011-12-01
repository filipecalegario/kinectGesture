package interaction;

public class InteractionVariables {

	public float lastPositionX; 
	public float lastPositionY; 
	public float lastPositionZ;
	
	private float maxSpeedX;
	private float minSpeedX;
	private float maxSpeedY;
	private float minSpeedY;
	private float maxSpeedZ;
	private float minSpeedZ;
	
	public InteractionVariables() {
		resetSpeeds();		
	}
	
	public float getMaxSpeedX() {
		return maxSpeedX;
	}
	public void setMaxSpeedX(float maxSpeedX) {
		this.maxSpeedX = Math.max(this.maxSpeedX, maxSpeedX);
	}
	public float getMinSpeedX() {
		return minSpeedX;
	}
	public void setMinSpeedX(float minSpeedX) {
		this.minSpeedX = Math.min(this.minSpeedX, minSpeedX);
	}
	public float getMaxSpeedY() {
		return maxSpeedY;
	}
	public void setMaxSpeedY(float maxSpeedY) {
		this.maxSpeedY = Math.max(this.maxSpeedY, maxSpeedY);
	}
	public float getMinSpeedY() {
		return minSpeedY;
	}
	public void setMinSpeedY(float minSpeedY) {
		this.minSpeedY = Math.min(this.minSpeedY, minSpeedY);
	}
	public float getMaxSpeedZ() {
		return maxSpeedZ;
	}
	public void setMaxSpeedZ(float maxSpeedZ) {
		this.maxSpeedZ = Math.max(this.maxSpeedZ, maxSpeedZ);
	}
	public float getMinSpeedZ() {
		return minSpeedZ;
	}
	public void setMinSpeedZ(float minSpeedZ) {
		this.minSpeedZ = Math.min(this.minSpeedZ, minSpeedZ);
	}

	public void resetSpeeds() {
		this.maxSpeedX = 0;
		this.maxSpeedY = 0;
		this.maxSpeedZ = 0;
		
		this.minSpeedX = Float.MAX_VALUE;
		this.minSpeedY = Float.MAX_VALUE;
		this.minSpeedZ = Float.MAX_VALUE;
	}
	
}

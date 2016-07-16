package de.riedelgames.gameobjects;

public abstract class GameObject {
	
	protected float x, y, velX, velY;
	
	public GameObject(float x, float y, float velX, float velY){
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}
	
	
	
}

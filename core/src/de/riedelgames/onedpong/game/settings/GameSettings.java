package de.riedelgames.onedpong.game.settings;

import de.riedelgames.gameobjects.deadline.DeadlineType;
import de.riedelgames.onedpong.game.velocity.VelocityMod;

public class GameSettings {
	
	/** Window Settings */
	private int windowWidth;
	private int windowHeight;
	private boolean fullScreenMod;
	
	/** Game Settings */
	private int pointsToBePlayed;
	private float ballStartVelocity;

	
	/** Deadline Settings */
	private DeadlineType deadlineType;
	private float deadlineConstantDecreaseValue;
	private float hitAreaSize;
	
	/** VelocityMod Settings */
	private VelocityMod velocityMod;
	private float velocityConstantIncreaseValue;
	private float velocityParabolicHitpointMax;
	private float velocityParabolicHitpointMin;
	
	
	/** dummy game settings */
	public GameSettings(int i) {
		setWindowWidth(640);
		setWindowHeight(480);
		setFullScreenMod(true);
		pointsToBePlayed = 5;
		deadlineType = DeadlineType.constantDecrease;
		velocityMod = VelocityMod.parabolicHitPoint;
		deadlineConstantDecreaseValue = 0.01f;
		velocityConstantIncreaseValue = 0.1f;
		setBallStartVelocity(1.0f);
		setHitAreaSize(0.4f);
		velocityParabolicHitpointMin = 0.1f;
		velocityParabolicHitpointMax = 5.0f;
	}
	
	
	
	public int getWindowWidth() {
		return windowWidth;
	}



	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}



	public int getWindowHeight() {
		return windowHeight;
	}



	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}



	public boolean isFullScreenMod() {
		return fullScreenMod;
	}



	public void setFullScreenMod(boolean fullScreenMod) {
		this.fullScreenMod = fullScreenMod;
	}



	public float getBallStartVelocity() {
		return ballStartVelocity;
	}



	public void setBallStartVelocity(float ballStartVelocity) {
		this.ballStartVelocity = ballStartVelocity;
	}



	public float getDeadlineConstantDecreaseValue() {
		return deadlineConstantDecreaseValue;
	}



	public float getHitAreaSize() {
		return hitAreaSize;
	}



	public void setHitAreaSize(float hitAreaSize) {
		this.hitAreaSize = hitAreaSize;
	}



	public VelocityMod getVelocityMod() {
		return velocityMod;
	}



	public float getVelocityConstantIncreaseValue() {
		return velocityConstantIncreaseValue;
	}



	public void setVelocityConstantIncreaseValue(float velocityConstantIncreaseValue) {
		this.velocityConstantIncreaseValue = velocityConstantIncreaseValue;
	}



	public void setVelocityMod(VelocityMod velocityMod) {
		this.velocityMod = velocityMod;
	}



	public float getVelocityParabolicHitpointMax() {
		return velocityParabolicHitpointMax;
	}



	public void setVelocityParabolicHitpointMax(float velocityParabolicHitpointMax) {
		this.velocityParabolicHitpointMax = velocityParabolicHitpointMax;
	}



	public float getVelocityParabolicHitpointMin() {
		return velocityParabolicHitpointMin;
	}



	public void setVelocityParabolicHitpointMin(float velocityParabolicHitpointMin) {
		this.velocityParabolicHitpointMin = velocityParabolicHitpointMin;
	}
}

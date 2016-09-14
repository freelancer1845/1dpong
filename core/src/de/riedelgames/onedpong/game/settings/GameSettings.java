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
	
	
	public GameSettings() {};
	
	/** dummy game settings */
	public GameSettings(int i) {
		setWindowWidth(640);
		setWindowHeight(480);
		setFullScreenMod(false);
		setPointsToBePlayed(5);
		setDeadlineType(DeadlineType.constantDecrease);
		velocityMod = VelocityMod.Increase;
		setDeadlineConstantDecreaseValue(0.01f);
		velocityConstantIncreaseValue = 0.1f;
		setBallStartVelocity(2.0f);
		setHitAreaSize(0.4f);
		velocityParabolicHitpointMin = 0.1f;
		velocityParabolicHitpointMax = 4.5f;
		
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

	public DeadlineType getDeadlineType() {
		return deadlineType;
	}

	public void setDeadlineType(DeadlineType deadlineType) {
		this.deadlineType = deadlineType;
	}

	public float getDeadlineConstantDecreaseValue() {
		return deadlineConstantDecreaseValue;
	}

	public void setDeadlineConstantDecreaseValue(float deadlineConstantDecreaseValue) {
		this.deadlineConstantDecreaseValue = deadlineConstantDecreaseValue;
	}

	public float getHitAreaSize() {
		return hitAreaSize;
	}

	public void setHitAreaSize(float hitAreaSize) {
		this.hitAreaSize = hitAreaSize;
	}

	public int getPointsToBePlayed() {
		return pointsToBePlayed;
	}

	public void setPointsToBePlayed(int pointsToBePlayed) {
		this.pointsToBePlayed = pointsToBePlayed;
	}
}

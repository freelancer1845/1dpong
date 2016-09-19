package de.riedelgames.onedpong.game.settings;

import de.riedelgames.gameobjects.deadline.DeadlineType;
import de.riedelgames.onedpong.game.velocity.VelocityMod;

public class GameSettings {

    /** Window Settings. */
    private int windowWidth;
    private int windowHeight;
    private boolean fullScreenMod;

    /** Game Settings. */
    private int pointsToBePlayed;
    private float ballStartVelocity;

    /** Deadline Settings. */
    private DeadlineType deadlineType;
    private float deadlineConstantDecreaseValue;
    private float hitAreaSize;

    /** VelocityMod Settings. */
    private VelocityMod velocityMod;
    private float velocityConstantIncreaseValue;
    private float velocityHitPointMax;
    private float velocityHitPointMin;

    public GameSettings() {
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

    public float getVelocityHitpointMax() {
        return velocityHitPointMax;
    }

    public void setVelocityHitpointMax(float velocityHitpointMax) {
        this.velocityHitPointMax = velocityHitpointMax;
    }

    public float getVelocityHitpointMin() {
        return velocityHitPointMin;
    }

    public void setVelocityHitpointMin(float velocityHitpointMin) {
        this.velocityHitPointMin = velocityHitpointMin;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GameSettings)) {
            return false;
        }
        GameSettings otherSettings = (GameSettings) obj;

        boolean isEqual = true;

        isEqual &= this.fullScreenMod == otherSettings.isFullScreenMod();
        isEqual &= this.ballStartVelocity == otherSettings.getBallStartVelocity();
        isEqual &= this.deadlineConstantDecreaseValue == otherSettings.getDeadlineConstantDecreaseValue();
        isEqual &= this.deadlineType == otherSettings.getDeadlineType();
        isEqual &= this.hitAreaSize == otherSettings.getHitAreaSize();
        isEqual &= this.pointsToBePlayed == otherSettings.getPointsToBePlayed();
        isEqual &= this.velocityConstantIncreaseValue == otherSettings.getVelocityConstantIncreaseValue();
        isEqual &= this.velocityMod == otherSettings.getVelocityMod();
        isEqual &= this.velocityHitPointMax == otherSettings.getVelocityHitpointMax();
        isEqual &= this.velocityHitPointMin == otherSettings.getVelocityHitpointMin();
        isEqual &= this.windowHeight == otherSettings.windowHeight;
        isEqual &= this.windowWidth == otherSettings.getWindowWidth();

        return isEqual;
    }

}

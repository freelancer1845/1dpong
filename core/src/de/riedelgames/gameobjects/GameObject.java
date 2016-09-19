package de.riedelgames.gameobjects;

public abstract class GameObject {

    protected float posX;

    protected float posY;

    protected float velX;

    protected float velY;

    /**
     * Constructs a basic gameObject.
     * 
     * @param posX
     *            position
     * @param posY
     *            position
     * @param velX
     *            velocity
     * @param velY
     *            velocity
     */
    public GameObject(float posX, float posY, float velX, float velY) {
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
    }

    public float getX() {
        return posX;
    }

    public void setX(float posX) {
        this.posX = posX;
    }

    public float getY() {
        return posY;
    }

    public void setY(float posY) {
        this.posY = posY;
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

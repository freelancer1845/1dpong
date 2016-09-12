package de.riedelgames.gameobjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import de.riedelgames.onedpong.game.GameConstants;

public class Ball extends GameObject {

    private Sprite sprite;
    private float width, height;

    private ParticleEffect pe;
    private ParticleEmitter firstParticleEmitter;
    private float[] particleColor;
    private Random randColor;

    public Ball() {
        super(0, 0, 0, 0);
        width = GameConstants.GAME_WORLD_WIDTH / 35;
        height = width;

        sprite = new Sprite(new Texture("lightcircle.png"));

        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        sprite.setColor(new Color(0.6f, 0.6f, 1f, 1));

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("Ball.party"), Gdx.files.internal(""));
        firstParticleEmitter = pe.getEmitters().first();
        firstParticleEmitter.setPosition(x, y);
        firstParticleEmitter.setContinuous(true);
        particleColor = new float[] { 0.000f, 0.749f, 1.000f };
        randColor = new Random();
        firstParticleEmitter.getTint().setColors(particleColor);
        pe.start();

    }

    public Ball(float x, float y) {
        super(x, y, 0, 0);
        width = GameConstants.GAME_WORLD_WIDTH / 22;
        height = width;

        sprite = new Sprite(new Texture("lightcircle.png"));

        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        sprite.setColor(Color.BLUE);

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("Ball.party"), Gdx.files.internal(""));
        firstParticleEmitter = pe.getEmitters().first();
        firstParticleEmitter.setPosition(x, y);
        firstParticleEmitter.setContinuous(true);
        particleColor = new float[] { 0.000f, 0.749f, 1.000f };
        randColor = new Random();
        firstParticleEmitter.getTint().setColors(particleColor);
        pe.start();

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void update(float deltaTime) {

        this.x += velX * deltaTime;
        this.y += velY * deltaTime;

        if (checkForMapEnd()) {
            // clampToWorld();
            sprite.setColor(1, 0.6f, 0.6f, 1);
        }

        sprite.setPosition(x, y);
        firstParticleEmitter.setPosition(x + width / 2, y + height / 2);
        pe.update(deltaTime);

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
        pe.draw(batch);
    }

    public void dispose() {
        sprite.getTexture().dispose();
        pe.dispose();
    }

    @Override
    public void setVelX(float velX) {
        if (this.velX != 0) {
            if (Math.signum(this.velX) != Math.signum(velX)) {
                changeColorOnReflection();
            }
        }
        this.velX = velX;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private boolean checkForMapEnd() {
        if (this.x > GameConstants.GAME_WORLD_WIDTH) {
            // x = GameScreen.GAME_WORLD_WIDTH;
            return true;
        } else if (this.x < -sprite.getWidth()) {
            // x = sprite.getWidth();
            return true;
        } else if (this.y < 0 || this.y > GameConstants.GAME_WORLD_HEIGHT - sprite.getHeight()) {
            y = GameConstants.GAME_WORLD_HEIGHT - sprite.getHeight();
            return true;
        } else if (this.y < 0) {
            y = 0;
            return true;
        } else {
            return false;
        }
    }

    private void clampToWorld() {
        if (this.x > GameConstants.GAME_WORLD_WIDTH) {
            x = GameConstants.GAME_WORLD_WIDTH;
        } else if (this.x + sprite.getWidth() < 0) {
            x = -sprite.getWidth();
        } else if (this.y < 0 || this.y > GameConstants.GAME_WORLD_HEIGHT + sprite.getHeight()) {
            y = GameConstants.GAME_WORLD_HEIGHT + sprite.getHeight();
        } else if (this.y < 0) {
            y = 0;
        } else {
        }
    }

    public void rotateEmitterByDegree(ParticleEmitter pe, float degree) {
        float currentHighMin = pe.getAngle().getHighMin();
        float currentHighMax = pe.getAngle().getHighMax();
        float currentLowMin = pe.getAngle().getLowMin();
        float currentLowMax = pe.getAngle().getLowMax();
        pe.getAngle().setHigh(currentHighMin + degree, currentHighMax + degree);
        pe.getAngle().setLow(currentLowMin + degree, currentLowMax + degree);
    }

    public void setEmitterRotationByDegree(float degree) {
        float currentHighMin = firstParticleEmitter.getAngle().getHighMin();
        float currentHighMax = firstParticleEmitter.getAngle().getHighMax();
        float currentLowMin = firstParticleEmitter.getAngle().getLowMin();
        float currentLowMax = firstParticleEmitter.getAngle().getLowMax();
        float meanHigh = (currentHighMax + currentHighMin) / 2f;
        float meanLow = (currentLowMax + currentLowMin) / 2f;

        firstParticleEmitter.getAngle().setHigh(degree + (currentHighMin - meanHigh),
                degree + (currentHighMax - meanHigh));
        firstParticleEmitter.getAngle().setLow(degree + (currentLowMin - meanLow), degree + (currentLowMax - meanLow));
    }

    private void changeColorOnReflection() {
        float sum = 0;
        for (int i = 0; i < this.particleColor.length; i++) {
            float colorValue = 0;
            do {
                sum -= colorValue;
                colorValue = randColor.nextFloat();
                sum += colorValue;
            } while (sum > 1);
            this.particleColor[i] = colorValue;
            shuffleArray(this.particleColor);
        }
    }

    private void shuffleArray(float[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            float a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}

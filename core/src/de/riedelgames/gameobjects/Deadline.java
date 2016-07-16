package de.riedelgames.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.gameobjects.deadline.ConstantDecreaseMod;
import de.riedelgames.gameobjects.deadline.ConstantMod;
import de.riedelgames.gameobjects.deadline.DeadlineMod;
import de.riedelgames.gameobjects.deadline.DeadlineType;
import de.riedelgames.onedpong.game.GameStatus;
import de.riedelgames.onedpong.game.settings.GameSettings;

public class Deadline extends GameObject {

	private int direction;
	
	private DeadlineMod deadlineMod;
	
	private Sprite sprite;
	
	public Deadline(float x, float y, float width, float height,
			DeadlineType deadlineType, int direction){
		super(x, y, width, height);
		sprite = new Sprite(new Texture(Gdx.files.internal("redDot.png")));
		sprite.setX(x);
		sprite.setY(y);
		sprite.setSize(width, height);
		setUpMode(deadlineType);
		this.direction = direction;
	}
	
	public void update(GameStatus gameStatus) {
		deadlineMod.update(gameStatus);
	}
	
	public void draw(SpriteBatch batch){
		sprite.setX(this.x);
		sprite.setY(this.y);
		sprite.draw(batch);
	}
	
	public void dispose(){
		sprite.getTexture().dispose();
	}
	
	private void setUpMode(DeadlineType deadlineType) {
		switch(deadlineType) {
		case constantDecrease:
			deadlineMod = new ConstantDecreaseMod(this);
			break;
		case constant:
			deadlineMod = new ConstantMod();
			break;
		}
	}
	
	public int getDirection() {
		return direction;
	}

	
}

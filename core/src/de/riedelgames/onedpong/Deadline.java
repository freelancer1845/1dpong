package de.riedelgames.onedpong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Deadline extends Sprite {

	public Deadline(float x, float y, float width, float height){
		super(new Texture(Gdx.files.internal("redDot.png")));
		super.setX(x);
		super.setY(y);
		super.setSize(width, height);
	}
	
	public void draw(SpriteBatch batch){
		super.draw(batch);
	}
	
	public void dispose(){
		super.getTexture().dispose();
	}

	
}

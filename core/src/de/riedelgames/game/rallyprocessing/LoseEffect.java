package de.riedelgames.game.rallyprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

public class LoseEffect extends ParticleEffect{
	
	public LoseEffect(float x, float y) {
		super.load(Gdx.files.internal("loseEffect.party"), Gdx.files.internal(""));
		ParticleEmitter firstEmitter = super.getEmitters().first();
		firstEmitter.setPosition(x, y);
		start();
	}
	
}

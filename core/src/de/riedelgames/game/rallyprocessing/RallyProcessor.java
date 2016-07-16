package de.riedelgames.game.rallyprocessing;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.onedpong.game.GameStatus;

public interface RallyProcessor {

	/** updates the Rally Processor */
	public abstract void update(GameStatus gameStatus, float deltaTime);
	
	/** returns the status set */
	public abstract EnumSet<RallyProcessStatus> getRallyProcessStatusSet();
	
	/** draws the effects */
	public abstract void draw(SpriteBatch batch);
}

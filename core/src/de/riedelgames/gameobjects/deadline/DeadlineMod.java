package de.riedelgames.gameobjects.deadline;

import de.riedelgames.onedpong.game.GameStatus;

public interface DeadlineMod {
	
	/** updates the Deadline depending on the current game situation */
	public abstract void update(GameStatus gameStatus);
	
	/** returns the deadlinetype */
	public abstract DeadlineType getType();

}

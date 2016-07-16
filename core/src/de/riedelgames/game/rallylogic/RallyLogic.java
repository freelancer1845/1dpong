package de.riedelgames.game.rallylogic;

import java.util.EnumSet;
import de.riedelgames.onedpong.game.GameStatus;

public interface RallyLogic {
	
	/** This updates the rally, i.e. does all the logic concerning the rally */
	public abstract void update(GameStatus gameStatus, float deltaTime) throws RallyException;
	
	/** This gets the status of the Rally (a set of enums) */
	public abstract EnumSet<RallyStatus> getRallyStatusSet();
	
	/** Set the Rally Status */
	public abstract void setRallyStatusSet(EnumSet<RallyStatus> rallyStatusSet);
	
	/** Add a rally status */
	public abstract void addRallyStatus(RallyStatus rallyStatus);
	
	/** Remove a rally status */
	public abstract void removeRallyStatus(RallyStatus rallyStatus);
}

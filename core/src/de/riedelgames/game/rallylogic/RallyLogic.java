package de.riedelgames.game.rallylogic;

import java.util.EnumSet;
import de.riedelgames.onedpong.game.GameStatus;

public interface RallyLogic {

    /**
     * This starts the Rally Logic calculations with a given TickRate (0 =
     * maximal) and a {@link GameStatus} that will be updated. You can also the
     * paused logic with that.
     */
    public void start(int tickrate, GameStatus gameStatus);

    /**
     * Sets the desired Tickrate of the RallyLogic.
     */
    public void setTickrate(int tickrate);

    /**
     * Pauses the Rally Logic.
     */
    public void pause();

    /**
     * Stops and disposes the RallyLogic.
     */
    public void dispose();

    /** This gets the status of the Rally (a set of enums) */
    public EnumSet<RallyStatus> getRallyStatusSet();

    /** Set the Rally Status */
    public void setRallyStatusSet(EnumSet<RallyStatus> rallyStatusSet);

    /** Add a rally status */
    public void addRallyStatus(RallyStatus rallyStatus);

    /** Remove a rally status */
    public void removeRallyStatus(RallyStatus rallyStatus);
}

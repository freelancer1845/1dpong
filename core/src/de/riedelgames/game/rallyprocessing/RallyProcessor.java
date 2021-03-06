package de.riedelgames.game.rallyprocessing;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.onedpong.game.GameStatus;

public interface RallyProcessor {

    /** updates the Rally Processor */
    public void update(GameStatus gameStatus, float deltaTime);

    /** returns the status set */
    public EnumSet<RallyProcessStatus> getRallyProcessStatusSet();

    /** draws the effects */
    public void draw(SpriteBatch batch);

}

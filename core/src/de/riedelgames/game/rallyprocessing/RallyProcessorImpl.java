package de.riedelgames.game.rallyprocessing;

import java.util.EnumSet;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.game.rallylogic.RallyStatus;
import de.riedelgames.onedpong.game.GameStatus;

public class RallyProcessorImpl implements RallyProcessor {

    private EnumSet<RallyStatus> rallyStatusSet = null;

    private EnumSet<RallyProcessStatus> rallyProcessStatusSet;

    private ParticleEffect loseEffect = null;

    public RallyProcessorImpl(EnumSet<RallyStatus> rallyStatusSet) {
        this.rallyStatusSet = rallyStatusSet;
        rallyProcessStatusSet = EnumSet.allOf(RallyProcessStatus.class);
    }

    @Override
    public void update(GameStatus gameStatus, float deltaTime) {

        if (rallyProcessStatusSet.contains(RallyProcessStatus.WAITING_FOR_RALLY)) {
            return;
        } else {
            if (rallyProcessStatusSet.contains(RallyProcessStatus.PLAYING_LOSE_ANIMATION)) {
                playLoseAnimation(gameStatus, deltaTime);
            } else if (rallyProcessStatusSet.contains(RallyProcessStatus.SETTING_UP_RALLY)) {
                setUpRally(gameStatus);
            } else if (rallyProcessStatusSet.contains(RallyProcessStatus.UPDATING_INFORMATION)) {
                updateInformation(gameStatus);
            } else {
                rallyProcessStatusSet.add(RallyProcessStatus.WAITING_FOR_RALLY);
                rallyProcessStatusSet.add(RallyProcessStatus.SETTING_UP_RALLY);
                rallyProcessStatusSet.add(RallyProcessStatus.PLAYING_LOSE_ANIMATION);
                rallyProcessStatusSet.add(RallyProcessStatus.UPDATING_INFORMATION);
                rallyStatusSet.add(RallyStatus.RALLY_IDELING);
                rallyStatusSet.remove(RallyStatus.RALLY_STOPPED);
            }
        }
    }

    private void setUpRally(GameStatus gameStatus) {
        if (rallyStatusSet.contains(RallyStatus.LEFT_PLAYER_WON)) {
            rallyStatusSet.add(RallyStatus.RIGHT_PLAYER_SERVE);
            gameStatus.getBall().setX(gameStatus.getRightDeadline().getX());
        } else if (rallyStatusSet.contains(RallyStatus.RIGHT_PLAYER_WON)) {
            rallyStatusSet.add(RallyStatus.LEFT_PLAYER_SERVE);
            gameStatus.getBall().setX(gameStatus.getLeftDeadline().getX() - gameStatus.getBall().getWidth());
        }
        rallyProcessStatusSet.remove(RallyProcessStatus.SETTING_UP_RALLY);
    }

    private void updateInformation(GameStatus gameStatus) {
        if (rallyStatusSet.contains(RallyStatus.LEFT_PLAYER_WON)) {
            gameStatus.getLeftPlayer().setScore(gameStatus.getLeftPlayer().getScore() + 1);
        } else if (rallyStatusSet.contains(RallyStatus.RIGHT_PLAYER_WON)) {
            gameStatus.getRightPlayer().setScore(gameStatus.getRightPlayer().getScore() + 1);
        }
        gameStatus.setRallyLength(0);
        rallyStatusSet.remove(RallyStatus.LEFT_PLAYER_WON);
        rallyStatusSet.remove(RallyStatus.RIGHT_PLAYER_WON);
        rallyProcessStatusSet.remove(RallyProcessStatus.UPDATING_INFORMATION);
    }

    private void playLoseAnimation(GameStatus gameStatus, float deltaTime) {
        if (loseEffect == null) {
            loseEffect = new LoseEffect(gameStatus.getBall().getX(), gameStatus.getBall().getY());
        }
        loseEffect.update(deltaTime);
        if (loseEffect.isComplete()) {
            rallyProcessStatusSet.remove(RallyProcessStatus.PLAYING_LOSE_ANIMATION);
            loseEffect.dispose();
            loseEffect = null;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (loseEffect != null) {
            loseEffect.draw(batch);
        }
    }

    @Override
    public EnumSet<RallyProcessStatus> getRallyProcessStatusSet() {
        return rallyProcessStatusSet;
    }

}

package de.riedelgames.game.rallyprocessing;

import java.util.EnumSet;

import de.riedelgames.game.rallylogic.RallyLogic;
import de.riedelgames.game.rallylogic.RallyLogicImpl;
import de.riedelgames.game.rallylogic.RallyStatus;
import de.riedelgames.onedpong.game.GameStatus;

public class RallyProcessorImpl implements RallyProcessor{

	private static EnumSet<RallyStatus> rallyStatusSet = null;
	
	private static EnumSet<RallyProcessStatus> rallyProcessStatusSet;
	
	private static RallyProcessorImpl instance = null;
	
	public static RallyProcessor getInstance() {
		if (instance == null) {
			instance = new RallyProcessorImpl();
			rallyProcessStatusSet = EnumSet.allOf(RallyProcessStatus.class);
			rallyStatusSet = RallyLogicImpl.getInstance().getRallyStatusSet();
			return instance;
		} else {
			return instance;
		}
	}
	
	
	@Override
	public void update(GameStatus gameStatus, float deltaTime) {

		if (rallyProcessStatusSet.contains(RallyProcessStatus.WAITING_FOR_RALLY)) {
			return;
		} else {
			if (rallyProcessStatusSet.contains(RallyProcessStatus.PLAYING_LOSE_ANIMATION)) {
				playLoseAnimation(gameStatus, deltaTime);
			}
			if (rallyProcessStatusSet.contains(RallyProcessStatus.SETTING_UP_RALLY)) {
				setUpRally(gameStatus);
			}
			if (rallyProcessStatusSet.contains(RallyProcessStatus.UPDATING_INFORMATION)) {
				updateInformation(gameStatus);
			}
			if (!rallyProcessStatusSet.contains(RallyProcessStatus.PLAYING_LOSE_ANIMATION)
				&& !rallyProcessStatusSet.contains(RallyProcessStatus.SETTING_UP_RALLY)
				&& !rallyProcessStatusSet.contains(RallyProcessStatus.UPDATING_INFORMATION)) {
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
			gameStatus.getBall().setX(gameStatus.getLeftDeadline().getX());
		}
		rallyProcessStatusSet.remove(RallyProcessStatus.SETTING_UP_RALLY);
	}
	
	private void updateInformation(GameStatus gameStatus) {
		if (rallyStatusSet.contains(RallyStatus.LEFT_PLAYER_WON)) {
			gameStatus.getLeftPlayer().setScore(gameStatus.getLeftPlayer().getScore() + 1);
		} else if (rallyStatusSet.contains(RallyStatus.RIGHT_PLAYER_WON)) {
			gameStatus.getRightPlayer().setScore(gameStatus.getLeftPlayer().getScore() + 1);
		}
		gameStatus.setRallyLength(0);
		rallyStatusSet.remove(RallyStatus.LEFT_PLAYER_WON);
		rallyStatusSet.remove(RallyStatus.RIGHT_PLAYER_WON);
		rallyProcessStatusSet.remove(RallyProcessStatus.UPDATING_INFORMATION);
	}
	
	private void playLoseAnimation(GameStatus gameStatus, float deltaTime) {
		rallyProcessStatusSet.remove(RallyProcessStatus.PLAYING_LOSE_ANIMATION);
	}


	@Override
	public EnumSet<RallyProcessStatus> getRallyProcessStatusSet() {
		return rallyProcessStatusSet;
	}

}

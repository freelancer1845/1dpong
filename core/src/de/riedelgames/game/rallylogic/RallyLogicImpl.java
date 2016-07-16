package de.riedelgames.game.rallylogic;

import java.util.EnumSet;
import java.util.Random;

import de.riedelgames.gameobjects.Ball;
import de.riedelgames.onedpong.Player;
import de.riedelgames.onedpong.game.GameConstants;
import de.riedelgames.onedpong.game.GameStatus;
import de.riedelgames.onedpong.game.velocity.VelocityLogic;

public class RallyLogicImpl implements RallyLogic {

	private static RallyLogicImpl instance = null;
	
	/** neutral server needed variables */
	private boolean serveStarted = false;
	private double serveStartTime;
	private double serveExecuteTime;
	
	private static EnumSet<RallyStatus> rallyStatusSet;
	
	private RallyLogicImpl(){};
	
	public static RallyLogic getInstance() {
		if (instance == null) {
			instance = new RallyLogicImpl();
			rallyStatusSet = EnumSet.noneOf(RallyStatus.class);
			return instance;
		} else {
			return instance;
		}
	}
	
	@Override
	public void update(GameStatus gameStatus, float deltaTime) throws RallyException {
		if (rallyStatusSet.contains(RallyStatus.RALLY_STOPPED)) {
			gameStatus.getBall().update(deltaTime);
			return;
		} else if (rallyStatusSet.contains(RallyStatus.RALLY_RUNNING)) {
			rallyRunningLogic(gameStatus, deltaTime);
		} else if (rallyStatusSet.contains(RallyStatus.RALLY_IDELING)) {
			rallyIdleLogic(gameStatus);
		} else {
			throw new RallyException("Rally is in undefined state.");
		}
		if (rallyStatusSet.contains(RallyStatus.RALLY_STOPPED)) {
			gameStatus.getBall().setVelX(0);
		}
		gameStatus.getBall().update(deltaTime);
		gameStatus.getLeftDeadline().update(gameStatus);
		gameStatus.getRightDeadline().update(gameStatus);
	}
	
	private void rallyRunningLogic(GameStatus gameStatus, float deltaTime){
		if (checkForPlayerInput(gameStatus)) {
			handlePlayerHitMistakes(gameStatus);
			if (!isRallyRunning()) return;
			if (acceptInput(gameStatus)) {
				VelocityLogic.update(gameStatus, deltaTime);
			}
		}
		handleBallLeavingMap(gameStatus);
	}
	
	private boolean checkForPlayerInput(GameStatus gameStatus) {
		if (gameStatus.getLeftPlayer().isKeyDown()) {
			return true;
		} else if (gameStatus.getRightPlayer().isKeyDown()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void handlePlayerHitMistakes(GameStatus gameStatus) {
		/** left Playey */
		if (gameStatus.getBall().getVelX() < 0) {
			if (gameStatus.getLeftPlayer().isKeyDown()
				&& gameStatus.getBall().getX() > gameStatus.getLeftDeadline().getX()) {
				rallyStatusSet.add(RallyStatus.RIGHT_PLAYER_WON);
				rallyStatusSet.add(RallyStatus.RALLY_STOPPED);
				rallyStatusSet.remove(RallyStatus.RALLY_RUNNING);
			}
		}
		/** right Player */
		else {
			if (gameStatus.getRightPlayer().isKeyDown()
				&& gameStatus.getBall().getX() < gameStatus.getRightDeadline().getX()) {
				rallyStatusSet.add(RallyStatus.LEFT_PLAYER_WON);
				rallyStatusSet.add(RallyStatus.RALLY_STOPPED);
				rallyStatusSet.remove(RallyStatus.RALLY_RUNNING);
			}
		}
	}
	
	private boolean acceptInput(GameStatus gameStatus) {
		if (gameStatus.getLeftPlayer().isKeyDown()
			&& gameStatus.getBall().getVelX() < 0) {
			return true;
		} else if (gameStatus.getRightPlayer().isKeyDown()
				&& gameStatus.getBall().getVelX() > 0) {
			return true;
		}
		return false;
	}
	
	private void handleBallLeavingMap(GameStatus gameStatus) {
		Ball ball = gameStatus.getBall();
		if (ball.getX() + ball.getWidth() < 0) {
			rallyStatusSet.add(RallyStatus.RIGHT_PLAYER_WON);
			rallyStatusSet.add(RallyStatus.RALLY_STOPPED);
			rallyStatusSet.remove(RallyStatus.RALLY_RUNNING);
		} else if (ball.getX() > GameConstants.GAME_WORLD_WIDTH) {
			rallyStatusSet.add(RallyStatus.LEFT_PLAYER_WON);
			rallyStatusSet.add(RallyStatus.RALLY_STOPPED);
			rallyStatusSet.remove(RallyStatus.RALLY_RUNNING);
		}
	}
	
	private void rallyIdleLogic(GameStatus gameStatus) throws RallyException {
		if (gameStatus.getBall().getVelX() != 0) {
			throw new RallyException("Rally is ideling but ball is moving!");
		}
		if (rallyStatusSet.contains(RallyStatus.LEFT_PLAYER_SERVE)
				&& !rallyStatusSet.contains(RallyStatus.RIGHT_PLAYER_SERVE)) {
			serveBall(gameStatus, gameStatus.getLeftPlayer());
			
		} else if (rallyStatusSet.contains(RallyStatus.RIGHT_PLAYER_SERVE)
				&& !rallyStatusSet.contains(RallyStatus.LEFT_PLAYER_SERVE)) {
			serveBall(gameStatus, gameStatus.getRightPlayer());
			
		} else if (rallyStatusSet.contains(RallyStatus.NEUTRAL_SERVE)
				&& !rallyStatusSet.contains(RallyStatus.LEFT_PLAYER_SERVE)
				&& !rallyStatusSet.contains(RallyStatus.RIGHT_PLAYER_SERVE)) {
			neutralServe(gameStatus);
			
		} else {
			throw new RallyException("Rally is ideling without server.");
		}
	}
	
	private void serveBall(GameStatus gameStatus, Player player) {
		if (!serveStarted) {
			serveStartTime = System.nanoTime() / 1000000000;
			serveStarted = true;
		} else {
			double currentTime = System.nanoTime() / 1000000000;
			if (currentTime - serveStartTime > 3) {
				if (player.isKeyDown()) {
					if (rallyStatusSet.contains(RallyStatus.LEFT_PLAYER_SERVE)) {
						gameStatus.getBall().setVelX(gameStatus.getGameSettings().getBallStartVelocity());
						rallyStatusSet.remove(RallyStatus.LEFT_PLAYER_SERVE);
						
					} else {
						gameStatus.getBall().setVelX(-gameStatus.getGameSettings().getBallStartVelocity());
						rallyStatusSet.remove(RallyStatus.RIGHT_PLAYER_SERVE);
					}
					rallyStatusSet.remove(RallyStatus.RALLY_IDELING);
					rallyStatusSet.add(RallyStatus.RALLY_RUNNING);
					gameStatus.setRallyLength(1);
					serveStarted = false;
				}
			}
		}
		
	}
	
	private void neutralServe(GameStatus gameStatus) {
		if (!serveStarted) {
			gameStatus.getBall().setX(GameConstants.GAME_WORLD_WIDTH / 2);
			serveStartTime = System.nanoTime() / 1000000;
			Random rand = new Random();
			serveExecuteTime = serveStartTime + rand.nextInt(3000) + 2000;
			serveStarted = true;
		} else {
			double currentTime = System.nanoTime() / 1000000;
			if (currentTime > serveExecuteTime) {
				Random rand = new Random();
				if (rand.nextBoolean()) {
					gameStatus.getBall().setVelX(gameStatus.getGameSettings().getBallStartVelocity());
				} else {
					gameStatus.getBall().setVelX(-gameStatus.getGameSettings().getBallStartVelocity());
				}
				serveStarted = false;
				rallyStatusSet.remove(RallyStatus.NEUTRAL_SERVE);
				rallyStatusSet.remove(RallyStatus.RALLY_IDELING);
				rallyStatusSet.add(RallyStatus.RALLY_RUNNING);
				gameStatus.setRallyLength(0);
			}
		}
		
	}
	
	public boolean isRallyRunning() {
		return rallyStatusSet.contains(RallyStatus.RALLY_RUNNING);
	}

	@Override
	public EnumSet<RallyStatus> getRallyStatusSet() {
		return rallyStatusSet;
	}

	@Override
	public void setRallyStatusSet(EnumSet<RallyStatus> rallyStatusSet) {
		RallyLogicImpl.rallyStatusSet = rallyStatusSet;
	}

	@Override
	public void addRallyStatus(RallyStatus rallyStatus) {
		RallyLogicImpl.rallyStatusSet.add(rallyStatus);
		
	}

	@Override
	public void removeRallyStatus(RallyStatus rallyStatus) {
		RallyLogicImpl.rallyStatusSet.remove(rallyStatus);
		
	}

}

package de.riedelgames.onedpong.game.velocity;

import de.riedelgames.onedpong.game.GameConstants;
import de.riedelgames.onedpong.game.GameStatus;

public class VelocityLogic {
	
	private VelocityLogic(){};
	
	public static void update(GameStatus gameStatus, float deltaTime) {
		if(gameStatus.getGameSettings().getVelocityMod() == VelocityMod.Increase){
			increaseLogic(gameStatus);
		} else if(gameStatus.getGameSettings().getVelocityMod() == VelocityMod.ParabolicHitPoint){
			parabolicHitPointLogic(gameStatus);
		} else if(gameStatus.getGameSettings().getVelocityMod() == VelocityMod.Constant) {
			constantLogic(gameStatus);
		} else if (gameStatus.getGameSettings().getVelocityMod() == VelocityMod.LinearHitPoint) {
			linearHitPointLogic(gameStatus);
		}
		gameStatus.setRallyLength(gameStatus.getRallyLength() + 1);
	}
	
	
	private static void increaseLogic(GameStatus gameStatus) {
		gameStatus.getBall().setVelX((Math.abs(gameStatus.getBall().getVelX())
				+ (gameStatus.getGameSettings().getVelocityConstantIncreaseValue())) * Math.signum(gameStatus.getBall().getVelX() * -1));
	}
	
	private static void parabolicHitPointLogic(GameStatus gameStatus) {
		float positionInDeadline;
		float deadLinePosition;
		float ballWidth = gameStatus.getBall().getWidth();
		float ballPosition;
		float max = gameStatus.getGameSettings().getVelocityHitpointMax();
		float min = gameStatus.getGameSettings().getVelocityHitpointMin();
		max = 4.0f;
		min = 0.4f;
		float velX;
		if (gameStatus.getBall().getVelX() <= 0) {
			ballPosition = gameStatus.getBall().getX();
			deadLinePosition = gameStatus.getLeftDeadline().getX();
			positionInDeadline =1 - (ballPosition + ballWidth) / (deadLinePosition + ballWidth);
			velX = 1;
		} else {
			ballPosition = gameStatus.getBall().getX() + ballWidth;
			deadLinePosition = gameStatus.getRightDeadline().getX();
			positionInDeadline = ballPosition / (GameConstants.GAME_WORLD_WIDTH + ballWidth - deadLinePosition) - deadLinePosition / (GameConstants.GAME_WORLD_WIDTH + ballWidth- deadLinePosition);
			velX = -1;
		}
		
		velX = velX * ((min - max) * 4 * positionInDeadline * positionInDeadline + (max - min) * 4 * positionInDeadline + min);
		gameStatus.getBall().setVelX(velX);
		
	}
	
	private static void constantLogic(GameStatus gameStatus) {
		gameStatus.getBall().setVelX(-gameStatus.getBall().getVelX());
	}
	
	private static void linearHitPointLogic(GameStatus gameStatus) {
		float positionInDeadline;
		float deadLinePosition;
		float ballWidth = gameStatus.getBall().getWidth();
		float ballPosition;
		float max = gameStatus.getGameSettings().getVelocityHitpointMax();
		float min = gameStatus.getGameSettings().getVelocityHitpointMin();
		float velX;
		if (gameStatus.getBall().getVelX() <= 0) {
			ballPosition = gameStatus.getBall().getX();
			deadLinePosition = gameStatus.getLeftDeadline().getX();
			positionInDeadline =1 - ballPosition / deadLinePosition;
			velX = 1;
		} else {
			ballPosition = gameStatus.getBall().getX() + ballWidth;
			deadLinePosition = gameStatus.getRightDeadline().getX();
			positionInDeadline = ballPosition / (GameConstants.GAME_WORLD_WIDTH - deadLinePosition) - deadLinePosition / (GameConstants.GAME_WORLD_WIDTH - deadLinePosition);
			velX = -1;
		}
		velX = (velX * positionInDeadline * (max - min)) + min;
		
		gameStatus.getBall().setVelX(velX);
	}
}

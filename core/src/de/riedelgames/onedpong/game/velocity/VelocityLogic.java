package de.riedelgames.onedpong.game.velocity;

import com.badlogic.gdx.Gdx;

import de.riedelgames.onedpong.game.GameConstants;
import de.riedelgames.onedpong.game.GameStatus;

public class VelocityLogic {
	
	private VelocityLogic(){};
	
	public static void update(GameStatus gameStatus, float deltaTime) {
		if(gameStatus.getGameSettings().getVelocityMod() == VelocityMod.constantIncrease){
			gameStatus.getBall().setVelX((Math.abs(gameStatus.getBall().getVelX())
					+ (gameStatus.getGameSettings().getVelocityConstantIncreaseValue())) * Math.signum(gameStatus.getBall().getVelX() * -1));
		} else if(gameStatus.getGameSettings().getVelocityMod() == VelocityMod.parabolicHitPoint){
			if(gameStatus.getBall().getVelX() <= 0){
				float max = gameStatus.getGameSettings().getVelocityParabolicHitpointMax();
				float min = gameStatus.getGameSettings().getVelocityParabolicHitpointMin();
				float k = gameStatus.getLeftDeadline().getX();
				float d = gameStatus.getBall().getWidth();
				float a = -((4*(max - min))/(d + k)/(d + k));
				float b = -((4*(d - k)*(max - min))/(d + k)/(d + k));
				float c = (4*d*k*(max - min))/(d + k)/(d + k) + min;
			    float x = (gameStatus.getBall().getX());
			    float velX = a*x*x+b*x+c;
			    gameStatus.getBall().setVelX(velX);
	
			} else {
				float max = gameStatus.getGameSettings().getVelocityParabolicHitpointMax();
				float min = gameStatus.getGameSettings().getVelocityParabolicHitpointMin();
				float k = gameStatus.getLeftDeadline().getX();
				float d = gameStatus.getBall().getWidth();
				float a = -((4*(max - min))/(d + k)/(d + k));
				float b = -((4*(d - k)*(max - min))/(d + k)/(d + k));
				float c = (4*d*k*(max - min))/(d + k)/(d + k) + min;
			    float x = (GameConstants.GAME_WORLD_WIDTH - gameStatus.getBall().getX())-gameStatus.getBall().getWidth();
			    float velX = a*x*x+b*x+c;
			    gameStatus.getBall().setVelX(-velX);

			}
		}
		gameStatus.setRallyLength(gameStatus.getRallyLength() + 1);
	}
}

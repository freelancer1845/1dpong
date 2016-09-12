package de.riedelgames.onedpong.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.gameobjects.Ball;
import de.riedelgames.gameobjects.Deadline;
import de.riedelgames.gameobjects.Player;
import de.riedelgames.onedpong.game.settings.GameSettings;

public class GameStatus {
	
	/** Game Settings*/
	private GameSettings gameSettings;
	
	/** Players */
	private Player leftPlayer;
	private Player rightPlayer;
	
	/** Ball */
	private Ball ball;
	
	/** Deadlines */
	private Deadline leftDeadline;
	private Deadline rightDeadline;
	
	private int rallyLength;
	
	
	public void draw(SpriteBatch batch) {
		ball.draw(batch);
		leftDeadline.draw(batch);
		rightDeadline.draw(batch);
	}
	
	public void dispose() {
		ball.dispose();
		leftDeadline.dispose();
		rightDeadline.dispose();
	}
	
	
	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public Player getLeftPlayer() {
		return leftPlayer;
	}

	public void setLeftPlayer(Player leftPlayer) {
		this.leftPlayer = leftPlayer;
	}

	public Player getRightPlayer() {
		return rightPlayer;
	}

	public void setRightPlayer(Player rightPlayer) {
		this.rightPlayer = rightPlayer;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public Deadline getLeftDeadline() {
		return leftDeadline;
	}

	public void setLeftDeadline(Deadline leftDeadline) {
		this.leftDeadline = leftDeadline;
	}

	public Deadline getRightDeadline() {
		return rightDeadline;
	}

	public void setRightDeadline(Deadline rightDeadline) {
		this.rightDeadline = rightDeadline;
	}

	public int getRallyLength() {
		return rallyLength;
	}

	public void setRallyLength(int rallyLength) {
		this.rallyLength = rallyLength;
	}

}

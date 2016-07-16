package de.riedelgames.gameobjects.deadline;

import de.riedelgames.gameobjects.Deadline;
import de.riedelgames.onedpong.game.GameStatus;
import de.riedelgames.onedpong.game.settings.GameSettings;

public class ConstantDecreaseMod implements DeadlineMod{

	private final DeadlineType deadlineType = DeadlineType.constantDecrease;
	
	private Deadline deadline;
	
	public ConstantDecreaseMod(Deadline deadline) {
		this.deadline = deadline;
	}
	
	@Override
	public void update(GameStatus gameStatus) {
		if (deadline.getDirection() > 0) {
			deadline.setX(deadline.getX() + gameStatus.getGameSettings().getConstantDecreaseValue());
		} else if (deadline.getDirection() < 0) {
			deadline.setX(deadline.getX() - gameStatus.getGameSettings().getConstantDecreaseValue());
		}
	}


	@Override
	public DeadlineType getType() {
		return deadlineType;
	}
}

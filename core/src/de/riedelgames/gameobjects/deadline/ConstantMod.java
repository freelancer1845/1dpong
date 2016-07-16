package de.riedelgames.gameobjects.deadline;

import de.riedelgames.onedpong.game.GameStatus;

public class ConstantMod implements DeadlineMod{

	private final DeadlineType deadlineType = DeadlineType.constant;
	
	@Override
	public void update(GameStatus gameStatus) {

	}

	@Override
	public DeadlineType getType() {
		return deadlineType;
	}


}

package de.riedelgames.game.rallylogic;

public class RallyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5960006328333403527L;

	public RallyException(String message) {
		super(message);
	}
	
	public RallyException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}

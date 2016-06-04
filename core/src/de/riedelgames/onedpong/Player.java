package de.riedelgames.onedpong;

public class Player {
	
	private int score = 0;
	private String name = "Unknown Player";
	boolean keyDown = false;
	private int playerKey;
	
	Player(int playerKey){
		this.playerKey = playerKey;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isKeyDown(){
		return keyDown;
	}
	public void setKeyDown(){
		this.keyDown = true;
	}
	public void unsetKeyDown(){
		this.keyDown = false;
	}
	public int getPlayerKey() {
		return playerKey;
	}
	public void setPlayerKey(int playerKey) {
		this.playerKey = playerKey;
	}
}

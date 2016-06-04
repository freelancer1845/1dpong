package de.riedelgames.onedpong;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameLogic implements InputProcessor{
	
	private static Player leftPlayer;
	private static Player rightPlayer;
	
	public static Ball ball;
	private static ParticleEffect loseEffect;
	private static float leftDeadline, rightDeadline;
	private static Deadline leftDeadlineObject, rightDeadlineObject;
	
	private static boolean gameRunning;
	private static boolean gameLost = false;
	private static VelocityMod velocityMod;
	private static DeadlineMod deadlineMod;
	private static int rallyLength = 0;
	
	private static Properties gameSettings;
	private static float deadlineConstantDecreaseValue;
	private static float velocityConstantIncreaseValue;
	private static float velocityParabolicHitpointMin, velocityParabolicHitpointMax;
	
	public GameLogic(){
		// Standard Logic Variables
		gameSettings = new Properties();
		try {
            loadAndSetGameSettings("config.cfg");
        } catch (FileNotFoundException e) {
            fallbackSettings();
            e.printStackTrace();
        } catch (IOException e) {
            Gdx.app.log("Error: ", e.getMessage());
            e.printStackTrace();
        }
		
		
		
		Gdx.input.setInputProcessor(this);
		
		leftPlayer = new Player(Input.Keys.A);
		rightPlayer = new Player(Input.Keys.D);
		
		ball = new Ball();
		ball.setPosition(GameScreen.GAME_WORLD_WIDTH / 2, GameScreen.GAME_WORLD_HEIGHT / 2f - ball.getHeight() / 2f);
		ball.setVelX(1f);
		loseEffect = new ParticleEffect();
		loseEffect.load(Gdx.files.internal("loseEffect.party"), Gdx.files.internal(""));
		
		
		leftDeadlineObject = new Deadline(leftDeadline,
				GameScreen.GAME_WORLD_HEIGHT/ 2 - GameScreen.GAME_WORLD_HEIGHT / 20,
				GameScreen.GAME_WORLD_WIDTH / 1000,
				GameScreen.GAME_WORLD_HEIGHT / 20);
		rightDeadlineObject = new Deadline(rightDeadline,
				GameScreen.GAME_WORLD_HEIGHT/ 2 - GameScreen.GAME_WORLD_HEIGHT / 20,
				GameScreen.GAME_WORLD_WIDTH / 1000,
				GameScreen.GAME_WORLD_HEIGHT / 20);
	
	}
	
	public static void update(float deltaTime){
		
		if(gameRunning){
			int tempRallyLength = rallyLength;
			ball.update(deltaTime);
			if(checkForPlayerMistake()){
				gameLost = true;
				loseEffect.getEmitters().first().setPosition(ball.getX() + ball.getWidth() / 2f, ball.getY() + ball.getHeight() / 2f);
				loseEffect.start();
				// TODO: Handle the Mistake of the player
			}
			else {
				respondToPlayerInput(); // also increases rally length 
				if(tempRallyLength != rallyLength){
					velocityLogic();
					if(rallyLength != 0 && rallyLength % 2 == 0){
						deadlineLogic();
					}
				}
			}
			// Gdx.app.log("Update", "Current leftDeadline" + leftDeadline);
		}
		if(gameLost){
			ball.setVelX(0);
			loseEffect.update(deltaTime);
			if(loseEffect.isComplete()){
				gameLost = false;
				ball.setVelX(1f);
			}
		}
	}
	
	public static void draw(SpriteBatch batch){
		rightDeadlineObject.draw(batch);
		leftDeadlineObject.draw(batch);
		ball.draw(batch);
		if(gameLost){
			loseEffect.draw(batch);
		}
	}
	
	public static void dispose(){
		rightDeadlineObject.dispose();
		leftDeadlineObject.dispose();
		ball.dispose();
		loseEffect.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == leftPlayer.getPlayerKey()){
			leftPlayer.setKeyDown();
		}
		else if(keycode == rightPlayer.getPlayerKey()){
			rightPlayer.setKeyDown();
		}
		else if(keycode == Input.Keys.ESCAPE){
			Gdx.app.exit();
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == leftPlayer.getPlayerKey()){
			leftPlayer.unsetKeyDown();
		}
		else if(keycode == rightPlayer.getPlayerKey()){
			rightPlayer.unsetKeyDown();
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private static boolean checkForPlayerMistake(){
		if(ballAtEnd()){
			//Gdx.app.log("PlayerError", "Ball at End");
			return false;
		}
		else if(playerPressedToEarly()){
			//Gdx.app.log("PlayerError", "One Player pressed to early");
			return true;
		}
		else{
			return false;
		}
		
		
	}
	
	private static boolean ballAtEnd(){
		if(ball.getX() == 0 || ball.getX() == GameScreen.GAME_WORLD_WIDTH - ball.getWidth()) return true;
		else return false;
	}
	
	private static boolean playerPressedToEarly(){
		if (ball.getVelX() < 0 && leftPlayer.isKeyDown() && ball.getX() > leftDeadline) return true;
		else if (ball.getVelX() > 0 && rightPlayer.isKeyDown() && ball.getX() + ball.getWidth() < rightDeadline) return true;
		else return false;
	}
	
	private static void respondToPlayerInput(){
		if (ball.getVelX() < 0 && leftPlayer.isKeyDown()){
			ball.setVelX(ball.getVelX() * -1);
			rallyLength++;
		}
		else if (ball.getVelX() > 0 && rightPlayer.isKeyDown()){
			ball.setVelX(ball.getVelX() * -1);
			rallyLength++;
		}
	}

	public static boolean isGameRunning() {
		return gameRunning;
	}

	public static void setGameRunning(boolean gameRunning) {
		GameLogic.gameRunning = gameRunning;
	}
	
	public static void velocityLogic(){
		if(velocityMod == VelocityMod.constantIncrease){
			ball.setVelX((Math.abs(ball.getVelX()) + (velocityConstantIncreaseValue)) * Math.signum(ball.getVelX()));
		}
		else if(velocityMod == VelocityMod.parabolicHitPoint){
			if(ball.getVelX() >= 0){
			    float b = 8f / 3f * (velocityParabolicHitpointMax - velocityParabolicHitpointMin);
			    float a =  - b / 2;
			    float x = (ball.getX() + ball.getWidth()) / leftDeadline;
			    float velX = a * x * x + b * x + velocityParabolicHitpointMin;
			    ball.setVelX(velX);
			} else {
			    float b = 8f / 3f * (velocityParabolicHitpointMax - velocityParabolicHitpointMin);
                float a =  - b / 2;
                float x =  (GameScreen.GAME_WORLD_WIDTH - (ball.getX() + ball.getWidth())) / leftDeadline;
                float velX = a * x * x + b * x + velocityParabolicHitpointMin;
                ball.setVelX(-velX);
			}
		}
	}
	
	public static void deadlineLogic(){
		if(deadlineMod == DeadlineMod.constantDecrease){
			leftDeadline = leftDeadline - (deadlineConstantDecreaseValue);
			rightDeadline = rightDeadline + (deadlineConstantDecreaseValue);
		}
		
		
		leftDeadlineObject.setX(leftDeadline);
		rightDeadlineObject.setX(rightDeadline);
	}

	public static VelocityMod getVelocityMod() {
		return velocityMod;
	}

	public static void setVelocityMod(VelocityMod velocityMod) {
		GameLogic.velocityMod = velocityMod;
	}

	public static void loadAndSetGameSettings(String path) throws IOException,FileNotFoundException{
		BufferedInputStream stream;
		stream = openFileStream(path);

		gameSettings.load(stream);
		stream.close();

		//VelocityModSettings
		switch (Integer.parseInt(gameSettings.getProperty("velocityDefaultMod", "1"))){
		    case 0:
		        velocityMod = VelocityMod.constant;
		        break;
		    case 1:
		        velocityMod = VelocityMod.constantIncrease;
		        break;
		    case 2:
                velocityMod = VelocityMod.parabolicIncrease;
                break;
		    case 3:
		        velocityMod = VelocityMod.linearHitPoint;
	            break;
		    case 4:
                velocityMod = VelocityMod.parabolicHitPoint;
                break;
	        default:
	            velocityMod = VelocityMod.constantIncrease;
	            break;
		}
		velocityConstantIncreaseValue = Float.parseFloat(gameSettings.getProperty("velocityConstantIncreaseValue", "0.1"));
		velocityParabolicHitpointMin = Float.parseFloat(gameSettings.getProperty("velocityParabolicHitpointMin",  "1"));
		velocityParabolicHitpointMax = Float.parseFloat(gameSettings.getProperty("velocityParabolicHitpointMax", "5.0"));
		//DeadlineModSettings
		switch(Integer.parseInt(gameSettings.getProperty("deadlineDefaultMod", "0"))){
		    case 0:
		        deadlineMod = DeadlineMod.constant;
		        break;
		    case 1:
		        deadlineMod = DeadlineMod.constantDecrease;
		        break;
	        default:
	            deadlineMod = DeadlineMod.constant;
	            break;
		}
		float hitAreaSize = Float.parseFloat(gameSettings.getProperty("hitAreaSize", "0.125"));
		deadlineConstantDecreaseValue = Float.parseFloat(gameSettings.getProperty("deadlineConstantDecreaseValue", "0.01"));
		
		//ScreenSettings
		GameScreen.windowHeight = Integer.parseInt(gameSettings.getProperty("windowHeight", "480"));
		GameScreen.windowWidth = Integer.parseInt(gameSettings.getProperty("windowWidth", "640"));
		GameScreen.fullScreenMod = Boolean.parseBoolean(gameSettings.getProperty("runInFullscreen", "false"));
		// finalize
		leftDeadline = GameScreen.GAME_WORLD_WIDTH * hitAreaSize;
		rightDeadline = GameScreen.GAME_WORLD_WIDTH - (GameScreen.GAME_WORLD_WIDTH * hitAreaSize);
	}
	
	private static void fallbackSettings(){
	    try {
            loadAndSetGameSettings("defaultSettings.cfg");
        } catch (FileNotFoundException e) {
            Gdx.app.error("Error: ", "DefaultSettings file not found!!!");
            e.printStackTrace();
        } catch (IOException e) {
            Gdx.app.error("Error: ", e.getMessage());
            e.printStackTrace();
        }
	}
	
	
	private static BufferedInputStream openFileStream(String Path) throws FileNotFoundException{
		BufferedInputStream stream = new BufferedInputStream(Gdx.files.internal(Path).read());
		return stream;
	}
	
}

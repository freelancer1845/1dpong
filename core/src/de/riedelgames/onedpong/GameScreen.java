package de.riedelgames.onedpong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameScreen implements Screen {
	
	public final OneDPong game;
	//private final GameLogic gameLogic;
	static final float GAME_WORLD_WIDTH = 2f;
	static final float GAME_WORLD_HEIGHT = GAME_WORLD_WIDTH * 9 / 16;
	
	public static boolean fullScreenMod = false;
	public static int windowWidth = 640;
	public static int windowHeight = 480;
	// Basic Setup of Background
	// private Viewport viewport;
	private OrthographicCamera camera;
	private Sprite background;
	
	
	public GameScreen(OneDPong game){
		this.game = game;
		new GameLogic();  // hack to start the Constructor of GmaeLogic (all static)
		
		// Basic initialization of Background
		background = new Sprite(new Texture("background.png"));
		background.setPosition(0, 0);
		background.setSize(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
		camera = new OrthographicCamera(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
		//camera.setToOrtho(false);
		camera.update();
		//viewport = new ScreenViewport(camera);
		//viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
		
		GameLogic.setGameRunning(true);
	}
	

	@Override
	public void show() {
		if(fullScreenMod){
		    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		else {
		    Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
		}
	}

	@Override
	public void render(float delta) {
		
		camera.update();
		GameLogic.update(delta);
		
		game.batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// draw background
		game.batch.begin();
		background.draw(game.batch);
		GameLogic.draw(game.batch);
		game.batch.end();

	}

	@Override
	public void resize(int width, int height) {
		//camera.setToOrtho(false);
		//viewport.update(width, height);
		camera.viewportWidth = GAME_WORLD_WIDTH;
		camera.viewportHeight = GAME_WORLD_HEIGHT * width/height;
		camera.update();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		GameLogic.dispose();
	}
	
	

}

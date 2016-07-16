package pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Network.NetworkHandler;
import de.riedelgames.onedpong.OneDPong;

public class StartScreen implements Screen {

	private final OneDPong game;
	private NetworkHandler networkHandler;
	private final Stage stage;
	private final Table table;
	
	public static boolean fullScreenMod = false;
	public static int windowWidth = 640;
	public static int windowHeight = 480;
	
	private OrthographicCamera camera;
	private Sprite background;
	
	
	public StartScreen(OneDPong game){
		this.game = game;
		
		background = new Sprite(new Texture("background.png"));
		background.setPosition(0, 0);
		background.setSize(windowWidth, windowHeight);
		
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		table.setDebug(true);
		

		
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		background.draw(game.batch);
		game.batch.end();
		
		stage.act(delta);
		stage.draw();
		// draw background
		
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
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
		background.getTexture().dispose();
		stage.dispose();
	}


}

package pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.riedelgames.onedpong.OneDPong;

public class SettingsScreen implements Screen {

	private final OneDPong game;
	private OrthographicCamera camera;
	// private Sprite background;
	
	private final Stage stage;
	private final Table rootTable;
	
	public SettingsScreen(OneDPong game){
		this.game = game;
		
		//background = new Sprite(new Texture("background.png"));
		//background.setPosition(0, 0);
		// background.setSize(windowWidth, windowHeight);
		
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.left().top().pad(20);
		stage.addActor(rootTable);
		rootTable.setDebug(true);
		
		fillTable();

	}
	
	private void fillTable() {
		Label label = new Label("Test", StartScreen.standardStyle);
		rootTable.add(label);
	}
	
	private HorizontalGroup newSettingGroup(String guiName, String identifier) {
		HorizontalGroup returnGroup = new HorizontalGroup();
		Label nameLabel = new Label(guiName, StartScreen.standardStyle);
		nameLabel.setFontScale(0.6f);
		
		return returnGroup;
	}
	
	@Override
	public void show() {
		if(StartScreen.fullScreenMod){
		    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		else {
		    Gdx.graphics.setWindowedMode(StartScreen.windowWidth, StartScreen.windowHeight);
		}
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		// background.draw(game.batch);
		game.batch.end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		stage.dispose();
	}

}

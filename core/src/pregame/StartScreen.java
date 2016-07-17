package pregame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Network.NetworkHandler;
import de.riedelgames.onedpong.OneDPong;

public class StartScreen implements Screen {

	private final OneDPong game;
	private NetworkHandler networkHandler;
	private final Stage stage;
	private final Table rootTable;
	private Table menu;
	private Table connectedClientsTable;
	private List<GuiClient> guiClients;
	private double updateTimer = -1;
	
	public static boolean fullScreenMod = false;
	public static int windowWidth = 640;
	public static int windowHeight = 480;
	
	private OrthographicCamera camera;
	private Sprite background;
	
	public static LabelStyle standardStyle = new LabelStyle(new BitmapFont(Gdx.files.getFileHandle("font/square_unique.fnt", Files.FileType.Internal)), Color.WHITE);
	
	
	public StartScreen(OneDPong game){
		this.game = game;
		
		background = new Sprite(new Texture("background.png"));
		background.setPosition(0, 0);
		background.setSize(windowWidth, windowHeight);
		
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.center().top();
		stage.addActor(rootTable);
		rootTable.setDebug(true);
		
		fillTable();

	}
	
	private void fillTable() {
		
		Label gameName = new Label("One D Pong", standardStyle);
		gameName.setName("GameName");
		gameName.setFontScale(1.4f);
		rootTable.add(gameName).padTop(25).padBottom(50).expandX();
		rootTable.row().center();
		
		addMenuEntries();
		addConnectedClientsView();
		
	}
	
	private void addMenuEntries() {
		menu = new Table();
		menu.setDebug(true);
		rootTable.add(menu);
		
		Label startGame = new Label("Start Game", standardStyle);
		startGame.setName("startGame");
		startGame.setFontScale(0.8f);
		menu.add(startGame).padBottom(20);
		menu.row().left();
		
		Label settings = new Label("Settings", standardStyle);
		settings.setName("settings");
		settings.setFontScale(0.8f);
		menu.add(settings).padBottom(20);
		menu.row().left();
		
		Label quit = new Label("Quit", standardStyle);
		quit.setName("quit");
		quit.setFontScale(0.8f);
		menu.add(quit);
		rootTable.row().left();
	}
	
	private void addConnectedClientsView() {
		connectedClientsTable = new Table();
		connectedClientsTable.setDebug(true);
		rootTable.add(connectedClientsTable).padTop(50).padLeft(20).expandX();
		connectedClientsTable.left().top();
		Label connectedClientsLabel = new Label("Connected Clients", standardStyle);
		connectedClientsLabel.setName("connectedClientsLabel");
		connectedClientsLabel.setFontScale(0.6f);
		connectedClientsTable.add(connectedClientsLabel).padBottom(10);
		
		guiClients = new ArrayList<GuiClient>();
		// guiClients.add(new GuiClient("Dummy", "192.168.2.101"));
	}
	
	private void updateConnectedClientsList(){
		if (updateTimer == -1) {
			updateTimer = System.nanoTime() / 1000000000;
		} else {
			double currentTime = System.nanoTime() / 1000000000;
			if (currentTime - updateTimer > 0) {
				connectedClientsTable.clearChildren();
				Label connectedClientsLabel = new Label("Connected Clients", standardStyle);
				connectedClientsLabel.setName("connectedClientsLabel");
				connectedClientsLabel.setFontScale(0.6f);
				connectedClientsTable.add(connectedClientsLabel).padBottom(10);
				for (GuiClient guiClient : guiClients) {
					connectedClientsTable.row().left();
					Label name = new Label(guiClient.getName(), standardStyle);
					name.setFontScale(0.5f);
					name.setName("guiClient");
					connectedClientsTable.add(name).left().padLeft(5).padBottom(5);
					Label ip = new Label(guiClient.getIp(), standardStyle);
					ip.setFontScale(0.5f);
					ip.setName("guiClient");
					connectedClientsTable.add(ip).padLeft(5).padBottom(5).right();
					connectedClientsTable.row().left();
				}
				if (connectedClientsTable.getChildren().size / 2 < 2) {
					connectedClientsTable.row().left();
					Label noClient = new Label("Waiting for player...", standardStyle);
					noClient.setFontScale(0.5f);
					connectedClientsTable.add(noClient).padLeft(5);
				}
				updateTimer = -1;
			}
		}
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
		
		updateConnectedClientsList();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		// background.draw(game.batch);
		game.batch.end();
		
		stage.act(delta);
		stage.draw();
		// draw background
		
		
	}

	@Override
	public void resize(int width, int height) {
		//stage.getViewport().update(width, height);
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

	private class GuiClient {
		
		private String name;
		private String ip;
		
		public GuiClient(String name, String ip) {
			this.name = name;
			this.ip = ip;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
		
	}
	
}

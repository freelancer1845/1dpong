package de.riedelgames.onedpong;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Network.NetworkHandler;
import de.riedelgames.onedpong.game.GameScreen;
import de.riedelgames.onedpong.game.settings.GameSettings;
import de.riedelgames.onedpong.game.settings.GameSettingsPersistenceHandler;
import pregame.SettingsScreen;
import pregame.StartScreen;


public class OneDPong extends Game	{
	
	public LinkedList<Player> playerList = new LinkedList<Player>();
	private NetworkHandler networkHandler;
	
	public static final Player leftPlayer = new Player(Input.Keys.A);
	public static final Player rightPlayer = new Player(Input.Keys.D);
	
	public SpriteBatch batch;
	
	@Override
	public void create () {
		
		//playerList.add(new Player(Input.Keys.A));
		//playerList.add(new Player(1));
		//networkHandler = new NetworkHandler(4000, playerList);
		//Thread test = new Thread(networkHandler);
		//test.start();
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(this, GameSettingsPersistenceHandler.loadSettings()));
		//this.setScreen(new GameScreen(this, new GameSettings(1)));
	}

	@Override
	public void render () {
		super.render();

	}
	
	public void dispose(){
		// networkHandler.dispose();
		batch.dispose();
	}
}

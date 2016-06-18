package de.riedelgames.onedpong;

import pregame.StartScreen;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Network.NetworkHandler;


public class OneDPong extends Game	{
	
	public LinkedList<Player> playerList = new LinkedList<Player>();
	
	public SpriteBatch batch;
	
	@Override
	public void create () {
		
		playerList.add(new Player(Input.Keys.A));
		playerList.add(new Player(1));
		Thread test = new Thread(new NetworkHandler(4000, playerList));
		test.start();
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();

	}
	
	public void dispose(){
		batch.dispose();
	}
}

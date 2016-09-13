package de.riedelgames.onedpong;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.gameobjects.Player;
import de.riedelgames.onedpong.game.GameScreen;
import de.riedelgames.onedpong.game.settings.GameSettings;
import de.riedelgames.onedpong.game.settings.GameSettingsPersistenceHandler;
import de.riedelgames.onedpong.network.NetworkHandler;
import de.riedelgames.onedpong.pregame.SettingsScreen;
import de.riedelgames.onedpong.pregame.StartScreen;

public class OneDPong extends Game {

    public LinkedList<Player> playerList = new LinkedList<Player>();
    private NetworkHandler networkHandler;

    public static final Player leftPlayer = new Player(Input.Keys.A);
    public static final Player rightPlayer = new Player(Input.Keys.L);

    public SpriteBatch batch;

    @Override
    public void create() {

        // playerList.add(new Player(Input.Keys.A));
        // playerList.add(new Player(1));
        batch = new SpriteBatch();
        // this.setScreen(new GameScreen(this,
        // GameSettingsPersistenceHandler.loadSettings()));
        this.setScreen(new StartScreen(this));
    }

    @Override
    public void render() {
        super.render();

    }

    public void dispose() {
        // networkHandler.dispose();
        batch.dispose();
    }

    public InputProcessor getScreenAsInputProcessor() {
        return (InputProcessor) this.getScreen();
    }
}

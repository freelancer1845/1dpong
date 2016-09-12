package de.riedelgames.onedpong.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.riedelgames.game.rallylogic.RallyException;
import de.riedelgames.game.rallylogic.RallyLogic;
import de.riedelgames.game.rallylogic.RallyLogicImpl;
import de.riedelgames.game.rallylogic.RallyStatus;
import de.riedelgames.game.rallyprocessing.RallyProcessStatus;
import de.riedelgames.game.rallyprocessing.RallyProcessor;
import de.riedelgames.game.rallyprocessing.RallyProcessorImpl;
import de.riedelgames.gameobjects.Ball;
import de.riedelgames.gameobjects.Deadline;
import de.riedelgames.gameobjects.deadline.DeadlineType;
import de.riedelgames.onedpong.OneDPong;
import de.riedelgames.onedpong.game.hud.Hud;
import de.riedelgames.onedpong.game.settings.GameSettings;
import de.riedelgames.onedpong.network.NetworkHandler;
import pregame.StartScreen;

public class GameScreen implements Screen, InputProcessor {

    public final OneDPong game;
    // private final GameLogic gameLogic;
    private final GameStatus gameStatus;
    private final GameSettings gameSettings;
    // Basic Setup of Background
    // private Viewport viewport;
    private OrthographicCamera camera;
    private Sprite background;

    private Hud hud;
    private RallyLogic rallyLogic;
    private RallyProcessor rallyProcessor;

    public GameScreen(OneDPong game, GameSettings gameSettings) {
        this.game = game;

        // Basic initialization of Background
        background = new Sprite(new Texture("background.png"));
        background.setPosition(0, 0);
        background.setSize(GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        camera = new OrthographicCamera(GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        // camera.setToOrtho(false);
        camera.update();
        // viewport = new ScreenViewport(camera);
        // viewport = new FitViewport(camera.viewportWidth,
        // camera.viewportHeight, camera);
        this.gameStatus = new GameStatus();
        gameStatus.setRightPlayer(OneDPong.rightPlayer);
        gameStatus.setLeftPlayer(OneDPong.leftPlayer);
        gameStatus.setLeftDeadline(new Deadline(gameSettings.getHitAreaSize(), GameConstants.GAME_WORLD_HEIGHT / 2,
                0.01f, 0.5f, DeadlineType.constant, -1));
        gameStatus.setRightDeadline(new Deadline(GameConstants.GAME_WORLD_WIDTH - gameSettings.getHitAreaSize(),
                GameConstants.GAME_WORLD_HEIGHT / 2, 0.01f, 0.5f, DeadlineType.constant, 1));
        gameStatus.setBall(new Ball());
        gameStatus.getBall().setY(GameConstants.GAME_WORLD_HEIGHT / 2 - gameStatus.getBall().getWidth() / 2);
        this.gameSettings = gameSettings;
        gameStatus.setGameSettings(gameSettings);
        rallyLogic = RallyLogicImpl.getInstance();
        rallyProcessor = RallyProcessorImpl.getInstance();
        hud = new Hud(gameStatus);
        rallyLogic.addRallyStatus(RallyStatus.RALLY_IDELING);
        rallyLogic.addRallyStatus(RallyStatus.NEUTRAL_SERVE);
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void show() {
        if (gameSettings.isFullScreenMod()) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(gameSettings.getWindowWidth(), gameSettings.getWindowHeight());
        }
    }

    @Override
    public void render(float delta) {

        try {
            camera.update();
            NetworkHandler.getInstance().fireKeyEvents(this);
            rallyLogic.update(gameStatus, delta);
            if (rallyLogic.getRallyStatusSet().contains(RallyStatus.RALLY_STOPPED)) {
                rallyProcessor.getRallyProcessStatusSet().remove(RallyProcessStatus.WAITING_FOR_RALLY);
            }
            rallyProcessor.update(gameStatus, delta);
            hud.update(gameStatus);

            game.batch.setProjectionMatrix(camera.combined);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // draw background
            game.batch.begin();
            background.draw(game.batch);
            gameStatus.draw(game.batch);
            rallyProcessor.draw(game.batch);
            game.batch.end();
            hud.draw();
        } catch (RallyException e) {
            Gdx.app.log("Rally Exception: ", e.getMessage());
            game.setScreen(new StartScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        // camera.setToOrtho(false);
        // viewport.update(width, height);
        camera.viewportWidth = GameConstants.GAME_WORLD_WIDTH;
        camera.viewportHeight = GameConstants.GAME_WORLD_HEIGHT * width / height;
        camera.update();
        hud.getViewport().update(width, height);

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
        gameStatus.dispose();
        background.getTexture().dispose();
        hud.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            gameStatus.getLeftPlayer().setKeyDown();
            return true;
        } else if (keycode == Input.Keys.DOWN) {
            gameStatus.getRightPlayer().setKeyDown();
            return true;
        } else if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP) {
            gameStatus.getLeftPlayer().unsetKeyDown();
            return true;
        } else if (keycode == Input.Keys.DOWN) {
            gameStatus.getRightPlayer().unsetKeyDown();
            return true;
        }
        return false;
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

}

package de.riedelgames.onedpong.pregame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.riedelgames.onedpong.OneDPong;
import de.riedelgames.onedpong.game.GameScreen;
import de.riedelgames.onedpong.game.settings.GameSettings;
import de.riedelgames.onedpong.game.settings.GameSettingsPersistenceHandler;
import de.riedelgames.onedpong.network.NetworkHandler;
import de.riedelgames.onedpong.network.NetworkServerClient;

public class StartScreen implements Screen, InputProcessor {

    private static final String[] BUTTON_IDS = { "Button_start_game_id", "Button_settings_id", "Button_quit_id" };

    private final OneDPong game;
    private NetworkHandler networkHandler;
    private final Stage stage;

    private final Skin skin;

    private final Table rootTable;
    private Table menu;
    private Table connectedClientsTable;
    private List<GuiClient> guiClients;
    private double updateTimer = 0;

    public boolean fullScreenMod = false;
    public int windowWidth = 640;
    public int windowHeight = 480;

    private OrthographicCamera camera;
    private Sprite background;

    public static LabelStyle standardStyle = new LabelStyle(
            new BitmapFont(Gdx.files.getFileHandle("font/square_unique.fnt", Files.FileType.Internal)), Color.WHITE);

    /** Font that will be used. */
    private BitmapFont standardFont = SkinProvider.getStandardFont();

    public StartScreen(OneDPong game) {
        this.game = game;
        networkHandler = NetworkHandler.getInstance();
        networkHandler.setVisible(true);
        networkHandler.startServer();

        GameSettings gameSettings = GameSettingsPersistenceHandler.loadSettings();
        windowWidth = gameSettings.getWindowWidth();
        windowHeight = gameSettings.getWindowHeight();
        fullScreenMod = gameSettings.isFullScreenMod();
        this.show();

        background = new Sprite(new Texture("background.png"));
        background.setPosition(0, 0);
        background.setSize(-1, -1);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = SkinProvider.getSkin();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center().top();
        stage.addActor(rootTable);
        rootTable.setDebug(true);
        stage.addListener(new MenuTraverseListener());

        fillTable();

    }

    private void fillTable() {
        LabelStyle headerStyle = new LabelStyle(standardFont, Color.WHITE);
        headerStyle.background = skin.getDrawable("header");
        Label gameName = new Label("One D Pong", headerStyle);
        gameName.setName("GameName");
        gameName.setFontScale(Gdx.graphics.getWidth() / 1000.0f);
        gameName.setAlignment(Align.center);
        float heightToWidth = gameName.getHeight() / gameName.getWidth();
        float width = Gdx.graphics.getWidth() / 1.5f;
        rootTable.add(gameName).padTop(Gdx.graphics.getHeight() / 40.0f).padBottom(Gdx.graphics.getHeight() / 40.0f)
                .width(width).height(width * heightToWidth);
        rootTable.row().center();

        addMenuEntries();
        addConnectedClientsView();

    }

    private void addMenuEntries() {
        menu = new Table();
        menu.setDebug(false);
        rootTable.add(menu);

        float fontScale = Gdx.graphics.getWidth() / 1000.0f / 1.4f;

        TextButton startGame = new TextButton("Start Game", skin);
        startGame.getLabel().setFontScale(fontScale);
        startGame.setChecked(true);
        startGame.setName(BUTTON_IDS[0]);
        float width = Gdx.graphics.getWidth() / 3.0f;
        float bottomPad = Gdx.graphics.getHeight() / 20.0f;
        float height = startGame.getLabel().getHeight() / startGame.getLabel().getWidth() * width;

        menu.add(startGame).padBottom(bottomPad).width(width).height(height);
        menu.row().left();

        TextButton settings = new TextButton("Settings", skin);
        settings.getLabel().setFontScale(fontScale);
        settings.setName(BUTTON_IDS[1]);
        menu.add(settings).padBottom(bottomPad).width(width).height(height);
        menu.row().left();

        TextButton quit = new TextButton("Quit", skin);
        quit.getLabel().setFontScale(fontScale);
        quit.setName(BUTTON_IDS[2]);
        menu.add(quit).width(width).height(height);
        rootTable.row().left();
    }

    private void addConnectedClientsView() {
        connectedClientsTable = new Table();
        connectedClientsTable.setBackground(skin.getDrawable("connectedPlayersTable"));
        connectedClientsTable.setDebug(false);
        float backgroundRatio = connectedClientsTable.getBackground().getTopHeight()
                / connectedClientsTable.getBackground().getLeftWidth();
        rootTable.add(connectedClientsTable).width(Gdx.graphics.getWidth() * 2.0f / 3).expandX()
                .height(Gdx.graphics.getHeight() / 4.0f);
        connectedClientsTable.left().top();
        guiClients = new ArrayList<GuiClient>();

    }

    private void updateConnectedClientsList() {
        guiClients.clear();
        // guiClients.add(new GuiClient("Player 1ssssssssssssssssssssssssssss",
        // "192.168.2.101"));
        // guiClients.add(new GuiClient("Player 2", "192.168.2.102"));
        for (NetworkServerClient serverClient : networkHandler.getNetworkClients()) {
            guiClients.add(new GuiClient(serverClient.getPlayer().getName(), serverClient.getIp()));
        }
        if (updateTimer == -1) {
            updateTimer = System.nanoTime() / 1000000000;
        } else {
            double currentTime = System.nanoTime() / 1000000000;
            if (currentTime - updateTimer > 0) {
                connectedClientsTable.clearChildren();
                Label connectedClientsLabel = new Label("Connected Clients", standardStyle);
                connectedClientsLabel.setName("connectedClientsLabel");
                connectedClientsLabel.setFontScale(connectedClientsTable.getHeight() / 300.0f * 1.2f);
                connectedClientsTable.add(connectedClientsLabel).padBottom(connectedClientsTable.getHeight() / 12.0f)
                        .padLeft(connectedClientsTable.getWidth() / 14.0f)
                        .padTop(connectedClientsTable.getHeight() / 4.5f).left().expandX();
                for (GuiClient guiClient : guiClients) {
                    connectedClientsTable.row().left();
                    Label name = new Label(guiClient.getName(), standardStyle);
                    name.setFontScale(connectedClientsTable.getHeight() / 300.0f * 0.9f);
                    name.setName("guiClient");
                    if (name.getPrefWidth() > connectedClientsTable.getWidth() / 1.8f) {
                        name.setText(name.getText().substring(0, 25) + "...");
                    }
                    connectedClientsTable.add(name).left().padLeft(connectedClientsTable.getWidth() / 10.0f)
                            .padBottom(connectedClientsTable.getHeight() / 8.6f);
                    Label ip = new Label(guiClient.getIp(), standardStyle);
                    ip.setFontScale(connectedClientsTable.getHeight() / 300.0f * 0.9f);
                    ip.setName("guiClient");
                    connectedClientsTable.add(ip).padBottom(connectedClientsTable.getHeight() / 8.6f).right()
                            .padRight(connectedClientsTable.getWidth() / 13.0f);
                    connectedClientsTable.row().left();
                }
                if (connectedClientsTable.getChildren().size / 2 < 2) {
                    connectedClientsTable.row().left();
                    Label noClient = new Label("Waiting for player...", standardStyle);
                    noClient.setFontScale(connectedClientsTable.getHeight() / 300.0f * 0.9f);
                    connectedClientsTable.add(noClient).padLeft(connectedClientsTable.getWidth() / 10.0f);
                }
                updateTimer = -1;
            }
        }
    }

    @Override
    public void show() {
        if (fullScreenMod) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
        }
    }

    @Override
    public void render(float delta) {
        processNetworkInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        // background.draw(game.batch);
        game.batch.end();
        // System.out.println("Size: " + connectedClientsTable.getWidth() + " -
        // " + connectedClientsTable.getHeight());
        stage.act(delta);
        stage.draw();
        // draw background
        updateConnectedClientsList();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        windowWidth = width;
        windowHeight = height;
        rootTable.clear();
        rootTable.center().top();
        updateTimer = 0;
        fillTable();

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

    private class MenuTraverseListener extends InputListener {

        private int activeButton = 0;

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Input.Keys.DOWN) {
                if (activeButton < BUTTON_IDS.length - 1) {
                    activeButton += 1;
                } else {
                    activeButton = 0;
                }
            } else if (keycode == Input.Keys.UP) {
                if (activeButton != 0) {
                    activeButton -= 1;
                } else {
                    activeButton = BUTTON_IDS.length - 1;
                }
            } else if (keycode == Input.Keys.ENTER) {
                handleEnter();
            } else if (keycode == Input.Keys.ESCAPE) {
                Gdx.app.exit();
            }
            setActiveButtonChecked();
            return true;
        }

        private void setActiveButtonChecked() {
            for (int i = 0; i < BUTTON_IDS.length; i++) {
                TextButton button = rootTable.findActor(BUTTON_IDS[i]);
                if (i != activeButton) {
                    button.setChecked(false);
                } else {
                    button.setChecked(true);
                }
            }
        }

        private void handleEnter() {
            if (BUTTON_IDS[activeButton] == BUTTON_IDS[0]) {
                if (guiClients.size() == 0) {
                    game.setScreen(new GameScreen(game, GameSettingsPersistenceHandler.loadSettings(), false));
                }
                // if (guiClients.size() > 1) {
                // game.setScreen(new GameScreen(game,
                // GameSettingsPersistenceHandler.loadSettings(), true));
                // } else {
                // game.setScreen(new GameScreen(game,
                // GameSettingsPersistenceHandler.loadSettings(), false));
                // }
                if (guiClients.size() == 2) {
                    game.setScreen(new GameScreen(game, GameSettingsPersistenceHandler.loadSettings(), true));
                }
            } else if (BUTTON_IDS[activeButton] == BUTTON_IDS[1]) {
                game.setScreen(new SettingsScreen(game));
            } else if (BUTTON_IDS[activeButton] == BUTTON_IDS[2]) {
                Gdx.app.exit();
            }
        }

    }

    private void processNetworkInput() {
        networkHandler.fireKeyEvents(stage);
    }

    @Override
    public boolean keyDown(int keycode) {
        stage.keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        stage.keyUp(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        stage.keyTyped(character);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        stage.touchDown(screenX, screenY, pointer, button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        stage.touchUp(screenX, screenY, pointer, button);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        stage.touchDragged(screenX, screenY, pointer);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        stage.mouseMoved(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        stage.scrolled(amount);
        return true;
    }

}

package pregame;

import java.io.IOException;
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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
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
    private final TextureAtlas textureAtlas;
    private BitmapFont standardFont = new BitmapFont(
            Gdx.files.getFileHandle("font/square_unique.fnt", Files.FileType.Internal));

    private final Table rootTable;
    private Table menu;
    private Table connectedClientsTable;
    private List<GuiClient> guiClients;
    private double updateTimer = -1;

    public static boolean fullScreenMod = true;
    public static int windowWidth = 640;
    public static int windowHeight = 480;

    private OrthographicCamera camera;
    private Sprite background;

    public static LabelStyle standardStyle = new LabelStyle(
            new BitmapFont(Gdx.files.getFileHandle("font/square_unique.fnt", Files.FileType.Internal)), Color.WHITE);

    public StartScreen(OneDPong game) {
        this.game = game;
        networkHandler = NetworkHandler.getInstance();

        background = new Sprite(new Texture("background.png"));
        background.setPosition(0, 0);
        background.setSize(windowWidth, windowHeight);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        textureAtlas = new TextureAtlas(Gdx.files.internal("ui/uiElements.atlas"));
        skin = new Skin();
        skin.addRegions(textureAtlas);
        skin.load(Gdx.files.internal("ui/skin.json"));

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center().top();
        stage.addActor(rootTable);
        rootTable.setDebug(false);
        stage.addListener(new MenuTraverseListener());

        fillTable();

    }

    private void fillTable() {
        LabelStyle headerStyle = new LabelStyle(standardFont, Color.WHITE);
        headerStyle.background = skin.getDrawable("header");
        Label gameName = new Label("One D Pong", headerStyle);
        gameName.setName("GameName");
        gameName.setFontScale(0.8f);
        gameName.setAlignment(Align.center);
        rootTable.add(gameName).padTop(25).padBottom(30).width(479).height(78);
        rootTable.row().center();

        addMenuEntries();
        addConnectedClientsView();

    }

    private void addMenuEntries() {
        menu = new Table();
        menu.setDebug(false);
        rootTable.add(menu);

        TextButton startGame = new TextButton("Start Game", skin);
        startGame.getLabel().setFontScale(0.6f);
        startGame.setChecked(true);
        startGame.setName(BUTTON_IDS[0]);
        menu.add(startGame).padBottom(20).width(200).height(53);
        menu.row().left();

        TextButton settings = new TextButton("Settings", skin);
        settings.getLabel().setFontScale(0.6f);
        settings.setName(BUTTON_IDS[1]);
        menu.add(settings).padBottom(20).width(200).height(53);
        menu.row().left();

        TextButton quit = new TextButton("Quit", skin);
        quit.getLabel().setFontScale(0.6f);
        quit.setName(BUTTON_IDS[2]);
        menu.add(quit).width(200).height(53);
        rootTable.row().left();
    }

    private void addConnectedClientsView() {
        connectedClientsTable = new Table();
        connectedClientsTable.setBackground(skin.getDrawable("connectedPlayersTable"));
        connectedClientsTable.setDebug(false);
        rootTable.add(connectedClientsTable).padTop(20).padLeft(20).width(500).expandX();
        connectedClientsTable.left().top();
        Label connectedClientsLabel = new Label("Connected Clients", standardStyle);
        connectedClientsLabel.setName("connectedClientsLabel");
        connectedClientsLabel.setFontScale(0.5f);
        connectedClientsTable.add(connectedClientsLabel).padBottom(10).padLeft(35).padTop(28).left().expandX();

        guiClients = new ArrayList<GuiClient>();
        guiClients.add(new GuiClient("Dummysdssssssssssssssssss", "192.168.2.101"));
        // guiClients.add(new GuiClient("Dummy2", "192.168.2.102"));
    }

    private void updateConnectedClientsList() {
        guiClients.clear();
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
                connectedClientsLabel.setFontScale(0.5f);
                connectedClientsTable.add(connectedClientsLabel).padBottom(10).padLeft(35).padTop(28).left().expandX();
                for (GuiClient guiClient : guiClients) {
                    connectedClientsTable.row().left();
                    Label name = new Label(guiClient.getName(), standardStyle);
                    name.setFontScale(0.45f);
                    name.setName("guiClient");
                    if (name.getPrefWidth() > 270) {
                        name.setText(name.getText().substring(0, 25) + "...");
                    }
                    connectedClientsTable.add(name).left().padLeft(40).padBottom(10);
                    Label ip = new Label(guiClient.getIp(), standardStyle);
                    ip.setFontScale(0.45f);
                    ip.setName("guiClient");
                    connectedClientsTable.add(ip).padLeft(35).padBottom(10).right().padRight(30);
                    connectedClientsTable.row().left();
                }
                if (connectedClientsTable.getChildren().size / 2 < 2) {
                    connectedClientsTable.row().left();
                    Label noClient = new Label("Waiting for player...", standardStyle);
                    noClient.setFontScale(0.45f);
                    connectedClientsTable.add(noClient).padLeft(40).padRight(30);
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
        updateConnectedClientsList();

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

    }

    @Override
    public void resize(int width, int height) {
        // stage.getViewport().update(width, height);
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
                for (int i = 0; i < BUTTON_IDS.length; i++) {
                    TextButton button = rootTable.findActor(BUTTON_IDS[i]);
                    if (i != activeButton) {
                        button.setChecked(false);
                    } else {
                        button.setChecked(true);
                    }
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
                game.setScreen(new GameScreen(game, GameSettingsPersistenceHandler.loadSettings()));
            } else if (BUTTON_IDS[activeButton] == BUTTON_IDS[1]) {
                game.setScreen(new SettingsScreen(game));
            } else if (BUTTON_IDS[activeButton] == BUTTON_IDS[2]) {
                Gdx.app.exit();
            }
        }

    }

    private void processNetworkInput() {
        if (!networkHandler.getNetworkClients().isEmpty()) {
            NetworkServerClient networkServerClient = networkHandler.getNetworkClients().iterator().next();
            List<Integer> keysDownList = networkServerClient.getPlayer().getKeysDown();
            synchronized (keysDownList) {
                for (Integer keyCode : keysDownList) {
                    stage.keyDown(keyCode);
                }
            }
        }

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

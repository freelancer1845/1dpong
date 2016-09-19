package de.riedelgames.onedpong.pregame;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.riedelgames.onedpong.OneDPong;
import de.riedelgames.onedpong.game.settings.GameSettings;
import de.riedelgames.onedpong.game.settings.GameSettingsPersistenceHandler;
import de.riedelgames.onedpong.game.velocity.VelocityMod;
import de.riedelgames.onedpong.network.NetworkHandler;

public class SettingsScreen implements Screen {

    /** Settings Name. */
    private static final String POINTS_TO_BE_PLAYED = "Points To Be Played";

    /** Settings Name. */
    private static final String START_VELOCITY = "Start Velocity";

    /** Settings Name. */
    private static final String HIT_AREA_SIZE = "Hit Area Size";

    /** Settings Name. */
    private static final String VELOCITY_MOD = "Velocity Mod";

    /** Settings Name. */
    private static final String FULL_SCREEN_MOD = "Full Screen Mod";

    /** Button Array. */
    private final List<Button> buttonsList = new ArrayList<Button>();

    /**
     * Buttons change settings map. Contains information on how to change the
     * setting.
     */
    private final Map<String, Float> settingsStepsizeMap = new HashMap<String, Float>();

    /**
     * Stepsize to identify a boolean stepsize.
     */
    private static final float BOOLEAN_STEPSIZE = -9235.232f;

    /** Boolean whether to save the settings on exit. */
    private boolean saveOnExit = false;

    /** Boolean whether to leave he settings screen. */
    private boolean leaveSettingsScreen = false;

    /** boolean saving whether the end dialog is open or not. */
    private boolean endDialogOpen = false;

    /** Reference to game Settings. */
    private final GameSettings gameSettings;

    private final OneDPong game;
    private OrthographicCamera camera;
    // private Sprite background;

    private final Stage stage;
    private final Table rootTable;

    /** Skin that will be used. */
    private final Skin skin;

    private final Map<String, String> settingsMap = new HashMap<String, String>();

    public SettingsScreen(OneDPong game) {
        this.game = game;
        this.gameSettings = GameSettingsPersistenceHandler.loadSettings();
        getAllSettings();

        // background = new Sprite(new Texture("background.png"));
        // background.setPosition(0, 0);
        // background.setSize(windowWidth, windowHeight);

        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);

        skin = SkinProvider.getSkin();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.left().top().pad(20);
        stage.addActor(rootTable);
        stage.addListener(new MenuTraverseListener());
        rootTable.setDebug(false);

        fillTable();

    }

    private void getAllSettings() {
        settingsMap.put(POINTS_TO_BE_PLAYED, String.valueOf(gameSettings.getPointsToBePlayed()));
        settingsStepsizeMap.put(POINTS_TO_BE_PLAYED, 1.0f);

        settingsMap.put(START_VELOCITY, String.valueOf(gameSettings.getBallStartVelocity()));
        settingsStepsizeMap.put(START_VELOCITY, 0.1f);

        settingsMap.put(HIT_AREA_SIZE, String.valueOf(gameSettings.getHitAreaSize()));
        settingsStepsizeMap.put(HIT_AREA_SIZE, 0.1f);

        settingsMap.put(VELOCITY_MOD, String.valueOf(gameSettings.getVelocityMod()));
        settingsStepsizeMap.put(VELOCITY_MOD, 1.0f);

        settingsMap.put(FULL_SCREEN_MOD, String.valueOf(gameSettings.isFullScreenMod()));
        settingsStepsizeMap.put(FULL_SCREEN_MOD, BOOLEAN_STEPSIZE);
    }

    private void fillTable() {
        addHeaderLabel();
        rootTable.row().left().top().padTop(20);
        addSettingsFields();
    }

    private void addHeaderLabel() {
        TextButton headerLabel = new TextButton("Settings", skin.get("settingsHeader", TextButtonStyle.class));
        headerLabel.setDisabled(true);
        headerLabel.getLabel().setFontScale(Gdx.app.getGraphics().getWidth() / 1000.0f * 1.2f);
        headerLabel.getLabel().setAlignment(Align.left);
        headerLabel.getLabelCell().padLeft(Gdx.app.getGraphics().getWidth() / 1000.0f * 20f)
                .padTop(Gdx.app.getGraphics().getWidth() / 1000.0f * 40f);
        float heightToWidth = headerLabel.getLabel().getHeight() / headerLabel.getLabel().getWidth();
        rootTable.add(headerLabel).width(Gdx.app.getGraphics().getWidth() / 2)
                .height(Gdx.app.getGraphics().getWidth() / 2 * heightToWidth);
    }

    private void addSettingsFields() {
        newSettingsButton(POINTS_TO_BE_PLAYED);
        buttonsList.get(0).setChecked(true); // checks first button

        newSettingsButton(START_VELOCITY);
        rootTable.row().padTop(5).left();

        newSettingsButton(HIT_AREA_SIZE);
        newSettingsButton(VELOCITY_MOD);

        rootTable.row().pad(5).left();
        newSettingsButton(FULL_SCREEN_MOD);
    }

    private void newSettingsButton(String guiName) {

        float width, height;
        Button settingsButton = new Button(skin, "settingsButton");
        settingsButton.setDebug(false);
        width = settingsButton.getWidth();
        height = settingsButton.getHeight();
        float finalWidth = (Gdx.graphics.getWidth() / 2.2f);
        float scaleFactor = finalWidth * 1f / 1000;

        Label property = new Label(guiName, skin);
        property.setFontScale(scaleFactor);
        property.setName("Property");

        Label value = new Label(settingsMap.get(guiName), skin);
        value.setFontScale(scaleFactor);
        value.setName("Value");

        settingsButton.add(property).align(Align.left).expand().padLeft(scaleFactor * 75);
        settingsButton.add(value).align(Align.right).padRight(scaleFactor * 75);
        settingsButton.setName(guiName);

        buttonsList.add(settingsButton);

        rootTable.add(settingsButton).width(finalWidth).height(finalWidth * (height / width));

    }

    private void updateButtons() {
        String guiName;
        String currentValue;
        for (Button button : buttonsList) {
            guiName = button.getName();
            currentValue = settingsMap.get(guiName);
            for (Actor child : button.getChildren()) {
                if (child.getName().equals("Value")) {
                    ((Label) child).setText(currentValue);
                }
            }
        }
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
        NetworkHandler.getInstance().fireKeyEvents(stage);
        updateButtons();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        // background.draw(game.batch);
        game.batch.end();

        stage.act(delta);
        stage.draw();

        if (leaveSettingsScreen) {
            if (saveOnExit) {
                writeGameSettings();
            }
            game.setScreen(new StartScreen(game));
        }
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

    private class MenuTraverseListener extends InputListener {

        /** Tracks the active button. */
        private int activeButton = 0;

        /** Boolean to state whether a button is selected. */
        private boolean buttonSelected = false;

        /** Saves old value for undo. */
        private String oldValue;

        /** The end Dialog. */
        // TODO : create dialog here
        private EndDialog dialog = new EndDialog("Save settings...", skin, "endDialog");

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (!buttonSelected) {
                if (keycode != Input.Keys.ESCAPE && !endDialogOpen) {
                    traverseButtons(keycode);
                } else if (keycode == Input.Keys.ESCAPE) {
                    handleEscape();
                } else if (endDialogOpen) {
                    if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
                        dialog.switchActive();
                    }
                }

            } else if (buttonSelected) {
                changeSettings(keycode);
            }

            return true;
        }

        private void traverseButtons(int keycode) {
            if (keycode == Input.Keys.DOWN) {
                if (activeButton < buttonsList.size() - 1) {
                    activeButton++;
                } else {
                    activeButton = 0;
                }
                setActiveButtonDown();
            } else if (keycode == Input.Keys.UP) {
                if (activeButton == 0) {
                    activeButton = buttonsList.size() - 1;
                } else {
                    activeButton--;
                }
                setActiveButtonDown();
            } else if (keycode == Input.Keys.ENTER) {
                buttonSelected = true;
                buttonsList.get(activeButton).getStyle().checked = skin.getDrawable("settingsFieldActive");
            }
        }

        private void setActiveButtonDown() {
            for (int i = 0; i < buttonsList.size(); i++) {
                if (i == activeButton) {
                    buttonsList.get(i).setChecked(true);
                } else {
                    buttonsList.get(i).setChecked(false);
                }
            }
        }

        private void uncheckAllButtons() {
            for (Button button : buttonsList) {
                button.setChecked(false);
            }
        }

        private void handleEscape() {
            if (checkIfSettingsChanged()) {
                dialog.setName("test");
                if (!endDialogOpen) {
                    dialog.show(stage);
                    endDialogOpen = true;
                    uncheckAllButtons();
                } else {
                    endDialogOpen = false;
                    setActiveButtonDown();
                }
            } else {
                leaveSettingsScreen = true;
                saveOnExit = false;
            }

        }

        private void changeSettings(int keycode) {
            String guiName = buttonsList.get(activeButton).getName();
            String currentStringValue = settingsMap.get(guiName);
            Float stepSize = settingsStepsizeMap.get(guiName);

            if (oldValue == null) {
                oldValue = currentStringValue;
            }

            if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK || keycode == Input.Keys.BACKSPACE) {
                buttonSelected = false;
                settingsMap.put(guiName, oldValue);
                oldValue = null;
                buttonsList.get(activeButton).getStyle().checked = skin.getDrawable("settingsFieldFocused");
            } else if (keycode == Input.Keys.ENTER) {
                buttonSelected = false;
                oldValue = null;
                buttonsList.get(activeButton).getStyle().checked = skin.getDrawable("settingsFieldFocused");
            } else if (keycode == Input.Keys.UP) {
                doSteps(guiName, currentStringValue, stepSize, 1);
            } else if (keycode == Input.Keys.DOWN) {
                doSteps(guiName, currentStringValue, stepSize, -1);
            }
        }

        private void doSteps(String guiName, String currentStringValue, float stepSize, int direction) {
            if (guiName.equals(VELOCITY_MOD)) {
                VelocityMod currentMod = VelocityMod.valueOf(currentStringValue);
                VelocityMod newMod;
                int ordinal = currentMod.ordinal();
                if (direction > 0) {
                    if (ordinal == VelocityMod.values().length - 1) {
                        newMod = VelocityMod.values()[0];
                    } else {
                        newMod = VelocityMod.values()[ordinal + 1];
                    }
                } else if (direction < 0) {
                    if (ordinal == 0) {
                        newMod = VelocityMod.values()[VelocityMod.values().length - 1];
                    } else {
                        newMod = VelocityMod.values()[ordinal - 1];
                    }
                } else {
                    newMod = currentMod;
                }

                settingsMap.put(guiName, newMod.toString());

            } else if (stepSize == BOOLEAN_STEPSIZE) {
                boolean currentValue = Boolean.valueOf(currentStringValue);
                settingsMap.put(guiName, String.valueOf(!currentValue));
            } else {
                if (stepSize == 1.0f) {
                    int currentValue = Integer.valueOf(currentStringValue);
                    currentValue += direction;
                    currentValue = clip(currentValue, (int) stepSize);
                    settingsMap.put(guiName, String.valueOf(currentValue));
                } else {
                    float currentValue = Float.valueOf(currentStringValue);
                    currentValue += stepSize * direction;
                    currentValue = clip(currentValue, stepSize);
                    DecimalFormat df = new DecimalFormat("#.##");
                    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                    dfs.setDecimalSeparator('.');
                    df.setDecimalFormatSymbols(dfs);
                    settingsMap.put(guiName, df.format(currentValue));
                }

            }
        }

        private <T extends Comparable<T>> T clip(T value, T clipValue) {
            if (value.compareTo(clipValue) < 0) {
                return clipValue;
            } else {
                return value;
            }
        }
    }

    private GameSettings constructGameSettings() {
        GameSettings gameSettingsReturn = GameSettingsPersistenceHandler.loadSettings();
        gameSettingsReturn.setBallStartVelocity(Float.valueOf(settingsMap.get(START_VELOCITY)));
        gameSettingsReturn.setFullScreenMod(Boolean.valueOf(settingsMap.get(FULL_SCREEN_MOD)));
        gameSettingsReturn.setHitAreaSize(Float.valueOf(settingsMap.get(HIT_AREA_SIZE)));
        gameSettingsReturn.setVelocityMod(VelocityMod.valueOf(settingsMap.get(VELOCITY_MOD)));
        gameSettingsReturn.setPointsToBePlayed(Integer.valueOf(settingsMap.get(POINTS_TO_BE_PLAYED)));
        return gameSettingsReturn;
    }

    private void writeGameSettings() {
        GameSettingsPersistenceHandler.writeGameSettings(constructGameSettings());
    }

    private class EndDialog extends Dialog {

        private TextButtonStyle unhitStyle = new TextButtonStyle(skin.getDrawable("settingsField"),
                skin.getDrawable("settingsField"), skin.getDrawable("settingsField"), skin.getFont("default-font"));

        private TextButtonStyle hitStyle = new TextButtonStyle(skin.getDrawable("settingsFieldActive"),
                skin.getDrawable("settingsFieldActive"), skin.getDrawable("settingsFieldActive"),
                skin.getFont("default-font"));

        private TextButton applyButton = new TextButton("Apply Settings", hitStyle);

        private TextButton discardButton = new TextButton("Discard Settings", unhitStyle);

        public EndDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
            this.button(applyButton, true);
            this.button(discardButton, false);
            this.key(Input.Keys.ESCAPE, false);
            this.key(Input.Keys.ENTER, true);
            float scale = 2.0f / 5.0f;
            float buttonWidth = Gdx.graphics.getWidth() * scale;
            float widthToHeight = applyButton.getWidth() / applyButton.getHeight();
            this.getButtonTable().getCell(applyButton).width(buttonWidth);
            this.getButtonTable().getCell(applyButton).height(buttonWidth / widthToHeight);
            this.getButtonTable().getCell(discardButton).width(buttonWidth);
            this.getButtonTable().getCell(discardButton).height(buttonWidth / widthToHeight);
            applyButton.getLabel().setFontScale(scale);
            discardButton.getLabel().setFontScale(scale);
            this.setDebug(true);
            saveOnExit = true;
        }

        @Override
        protected void result(Object object) {
            if ((Boolean) object) {
                leaveSettingsScreen = true;
            } else {
                leaveSettingsScreen = false;
            }
        }

        public void switchActive() {
            if (applyButton.getStyle().up.equals(skin.getDrawable("settingsField"))) {
                applyButton.getStyle().up = skin.getDrawable("settingsFieldActive");
                discardButton.getStyle().up = skin.getDrawable("settingsField");
                saveOnExit = true;
            } else {
                discardButton.getStyle().up = skin.getDrawable("settingsFieldActive");
                applyButton.getStyle().up = skin.getDrawable("settingsField");
                saveOnExit = false;
            }
        }

    }

    private boolean checkIfSettingsChanged() {
        GameSettings currentSettings = constructGameSettings();
        GameSettings previousSettings = GameSettingsPersistenceHandler.loadSettings();
        if (currentSettings.equals(previousSettings)) {
            return false;
        }
        return true;
    }

}

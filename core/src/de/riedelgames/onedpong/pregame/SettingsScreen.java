package de.riedelgames.onedpong.pregame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.riedelgames.onedpong.OneDPong;

public class SettingsScreen implements Screen {

    /** Settings Field. */
    private static final String FIELD_POINTS_TO_BE_PLAYED = "Points To Be Played";

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

        // background = new Sprite(new Texture("background.png"));
        // background.setPosition(0, 0);
        // background.setSize(windowWidth, windowHeight);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = SkinProvider.getSkin();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.left().top().pad(20);
        stage.addActor(rootTable);
        rootTable.setDebug(true);

        fillTable();

    }

    private void fillTable() {
        addHeaderLabel();
        rootTable.row();
        addSettingsFields();
    }

    private void addHeaderLabel() {
        TextButton headerLabel = new TextButton("Settings", skin.get("settingsHeader", TextButtonStyle.class));
        headerLabel.setDisabled(true);
        headerLabel.getLabel().setFontScale(0.7f);
        headerLabel.getLabel().setAlignment(Align.topLeft);
        headerLabel.getLabelCell().padLeft(20).padTop(28);
        rootTable.add(headerLabel).width(307.8f).height(60);
    }

    private void addSettingsFields() {

    }

    private HorizontalGroup newSettingsGroup(String guiName, String identifier) {
        HorizontalGroup returnGroup = new HorizontalGroup();
        Label nameLabel = new Label(guiName, StartScreen.standardStyle);
        nameLabel.setFontScale(0.6f);

        return returnGroup;
    }

    @Override
    public void show() {
        if (StartScreen.fullScreenMod) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
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

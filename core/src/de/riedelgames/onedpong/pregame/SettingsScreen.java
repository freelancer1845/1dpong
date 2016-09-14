package de.riedelgames.onedpong.pregame;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.riedelgames.onedpong.OneDPong;
import de.riedelgames.onedpong.game.settings.GameSettings;
import de.riedelgames.onedpong.game.velocity.VelocityMod;

public class SettingsScreen implements Screen {
	
	/** Settings Name. */
	private static final String POINTS_TO_BE_PLAYED = "Points To Be Played";
	
	/** Settings Name. */
	private static final String START_VELOCITY = "Start Velocity";
	
	/** Settings Name. */
	private static final String HIT_AREA_SIZE = "Hit Area Size";
	
	/** Settings Name. */
	private static final String VELOCITY_MOD = "Velocity Mod";
	
	/** Values List. */
	private static final List<String> VELOCITY_MODS = new ArrayList<String>();
	
    /** Button Array. */
    private static final List<Button> BUTTONS_LIST = new ArrayList<Button>();
    
    /** Buttons change settings map. Contains information on how to change the setting. */
    private static final Map<String, Float> SETTINGS_STEPSIZE = new HashMap<String, Float>();
    
    /** Boolean to state whether a button is selected. */
    private boolean buttonSelected = false;
    
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

    public SettingsScreen(OneDPong game, GameSettings gameSettings) {
        this.game = game;
        this.gameSettings = gameSettings;
        getAllSettings();

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
        stage.addListener(new MenuTraverseListener());
        rootTable.setDebug(true);

        fillTable();

    }
    
    private void getAllSettings() {
    	settingsMap.put(POINTS_TO_BE_PLAYED, String.valueOf(gameSettings.getPointsToBePlayed()));
    	SETTINGS_STEPSIZE.put(POINTS_TO_BE_PLAYED, 1.0f);
    	
    	settingsMap.put(START_VELOCITY, String.valueOf(gameSettings.getBallStartVelocity()));
    	SETTINGS_STEPSIZE.put(START_VELOCITY, 0.1f);
    	
    	settingsMap.put(HIT_AREA_SIZE, String.valueOf(gameSettings.getHitAreaSize()));
    	SETTINGS_STEPSIZE.put(HIT_AREA_SIZE, 0.1f);
    	
    	settingsMap.put(VELOCITY_MOD, String.valueOf(gameSettings.getVelocityMod()));
    	SETTINGS_STEPSIZE.put(HIT_AREA_SIZE, 1.0f);
    	addVelocityMods();
    	
    }
    
    private void addVelocityMods() {
    	VelocityMod[] mods = VelocityMod.values();
    	for (int i = 0; i < mods.length; i++) {
    		VELOCITY_MODS.add(mods[i].toString());
    	}
    }

    private void fillTable() {
        addHeaderLabel();
        rootTable.row().left().top().padTop(20);
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
    	newSettingsButton(POINTS_TO_BE_PLAYED);
    	newSettingsButton(START_VELOCITY);
    	rootTable.row().padTop(5).left();
    	
    	newSettingsButton(HIT_AREA_SIZE);
    	newSettingsButton(VELOCITY_MOD);
    }

    private void newSettingsButton(String guiName) {
    	float scaleFactor = 0.38f;
    	float width, height;
    	Button settingsButton = new Button(skin, "settingsButton");
    	settingsButton.setDebug(false);
    	width = settingsButton.getWidth();
    	height= settingsButton.getHeight();
    	
    	Label property = new Label(guiName, skin);
    	property.setFontScale(scaleFactor);
    	
    	Label value = new Label(settingsMap.get(guiName), skin);
    	value.setFontScale(scaleFactor);
    	
    	settingsButton.add(property).align(Align.left).expand().padLeft(scaleFactor * 75);
    	settingsButton.add(value).align(Align.right).padRight(scaleFactor * 75);
    	settingsButton.setName(guiName);
    	
    	BUTTONS_LIST.add(settingsButton);
    	rootTable.add(settingsButton).width(width * scaleFactor).height(height * scaleFactor);
    	
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
    	
    	rootTable.clear();
    	fillTable();
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
    
    
    private class MenuTraverseListener extends InputListener {
    	
    	private int activeButton = 0;

		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if (buttonSelected) {
				traverseButtons(keycode);
			} else if (buttonSelected) {
				changeSettings(keycode);
			}
			
			
			System.out.println("ButtonSelected: " + BUTTONS_LIST.get(activeButton).getName());
			
			return true;
		}
		
		private void traverseButtons(int keycode) {
			if (keycode == Input.Keys.DOWN) {
				if (activeButton < BUTTONS_LIST.size() - 1) {
					activeButton++;
				} else {
					activeButton = 0;
				}
				setActiveButtonDown();
			} else if (keycode == Input.Keys.UP) {
				if (activeButton == 0) {
					activeButton = BUTTONS_LIST.size() - 1;
				} else {
					activeButton--;
				}
				setActiveButtonDown();
			} else if (keycode == Input.Keys.ENTER) {
				buttonSelected = true;
			}
		}
		
		private void setActiveButtonDown() {
			for (int i = 0; i < BUTTONS_LIST.size(); i++) {
				if (i == activeButton) {
					BUTTONS_LIST.get(i).setChecked(true);
				} else {
					BUTTONS_LIST.get(i).setChecked(false);
				}
			}
		}
		
		private void changeSettings(int keycode) {
			if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
				buttonSelected = false;
			} else if (keycode == Input.Keys.UP) {
				String guiName = BUTTONS_LIST.get(activeButton).getName();
				String currentStringValue = settingsMap.get(guiName);
				float value = mapStringToFloatValue(guiName, currentStringValue);
				
			}
		}
		
		private int mapStringToIntValue(String guiName, String stringValue) {
			int returnValue = 0;
			if (guiName.equals(VELOCITY_MOD)) {
				
			}
			
			return returnValue;
			
		}
    	
    }
    
    private float mapStringToFloatValue(String guiName, String value) {
    	if (guiName.equals(VELOCITY_MOD)) {
    		return VelocityMod.valueOf(value).ordinal();
    	} else {
    		return Float.valueOf(value);
    	}
    }
    
    private String mapFloatToStringValue(String guiName, float value) {
    	if (guiName.equals(VELOCITY_MOD)) {
    		for (int i = 0; i < VelocityMod.values().length; i++) {
    			if (VelocityMod.values()[i].ordinal() == value) {
    				return VelocityMod.values()[i].toString();
    			}
    		}
    		return "";
    	} else {
    		return String.valueOf(value);
    	}
    }
    
    
    
    
}

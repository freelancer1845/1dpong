package de.riedelgames.onedpong.game.settings;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import de.riedelgames.gameobjects.deadline.DeadlineType;
import de.riedelgames.onedpong.game.velocity.VelocityMod;

/**
 * Static class that exchanges settings with the file system.
 * 
 * @author freeskier
 *
 */
public class GameSettingsPersistenceHandler {

    private static final String VELOCITY_DEFAULT_MOD = VelocityMod.ParabolicHitPoint.toString();

    private static final String VELOCITY_MOD = "velocityMod";

    private static final String VELOCITY_CONSTANT_INCREASE_DEFAULT_VALUE = "0.2";

    private static final String VELOCITY_CONSTANT_INCREASE_VALUE = "velocityConstantIncreaseValue";

    private static final String VELOCITY_HITPOINT_MIN_DEFAULT = "0.1";

    private static final String VELOCITY_HITPOINT_MIN = "velocityHitpointMin";

    private static final String VELOCITY_HITPOINT_MAX_DEFAULT = "4.5";

    private static final String VELOCITY_HITPOINT_MAX = "velocityHitpointMax";

    private static final String BALL_START_VELOCITY_DEFAULT = "2";

    private static final String BALL_START_VELOCITY = "ballStartVelocity";

    private static final String DEADLINE_DEFAULT_TYPE = DeadlineType.constant.toString();

    private static final String DEADLINE_TYPE = "deadlineMod";

    private static final String HIT_AREA_SIZE_DEFAULT = "0.3";

    private static final String HIT_AREA_SIZE = "hitAreaSize";

    private static final String DEADLINE_CONSTANT_DECREASE_DEFAULT_VALUE = "0.01";

    private static final String DEADLINE_CONSTANT_DECREASE_VALUE = "deadlineConstantDecreaseValue";

    private static final String RUN_IN_FULLSCREEN_DEFAULT = "true";

    private static final String RUN_IN_FULLSCREEN = "runInFullscreen";

    private static final String WINDOW_WIDTH_DEFAULT = "640";

    private static final String WINDOW_WIDTH = "windowWidth";

    private static final String WINDOW_HEIGHT_DEFAULT = "480";

    private static final String WINDOW_HEIGHT = "windowHeight";

    private static final String POINTS_TO_BE_PLAYED_DEFAULT = "5";

    private static final String POINTS_TO_BE_PLAYED = "pointsToBePlayed";

    private GameSettingsPersistenceHandler() {}

    /**
     * Loads the GameSettings from file "config.cfg".
     * 
     * @return {@link GameSettings} as ready to use.
     */
    public static GameSettings loadSettings() {
        GameSettings gameSettings = new GameSettings();
        FileHandle settingsFile = Gdx.files.internal("config.cfg");
        Properties propertiesLoader = new Properties();

        try {
            propertiesLoader.load(settingsFile.read());
        } catch (IOException e) {
            Gdx.app.error("Settings", "IO Error while loading settings.");
            e.printStackTrace();
        }

        loadVelocitySettings(gameSettings, propertiesLoader);
        loadDeadlineSettings(gameSettings, propertiesLoader);
        loadRulesSettings(gameSettings, propertiesLoader);
        loadScreenSettings(gameSettings, propertiesLoader);

        return gameSettings;
    }

    private static void loadVelocitySettings(GameSettings settings, Properties propertiesLoader) {

        settings.setVelocityMod(VelocityMod
                .valueOf((propertiesLoader.getProperty(VELOCITY_MOD, VELOCITY_DEFAULT_MOD))));
        settings.setVelocityConstantIncreaseValue(Float.valueOf(propertiesLoader.getProperty(
                VELOCITY_CONSTANT_INCREASE_VALUE, VELOCITY_CONSTANT_INCREASE_DEFAULT_VALUE)));
        settings.setVelocityHitpointMin(Float.valueOf(propertiesLoader
                .getProperty(VELOCITY_HITPOINT_MIN, VELOCITY_HITPOINT_MIN_DEFAULT)));
        settings.setVelocityHitpointMax(Float.valueOf(propertiesLoader
                .getProperty(VELOCITY_HITPOINT_MAX, VELOCITY_HITPOINT_MAX_DEFAULT)));
        settings.setBallStartVelocity(Float.valueOf(
                propertiesLoader.getProperty(BALL_START_VELOCITY, BALL_START_VELOCITY_DEFAULT)));

    }

    private static void loadDeadlineSettings(GameSettings settings, Properties propertiesLoader) {

        settings.setDeadlineType(DeadlineType
                .valueOf(propertiesLoader.getProperty(DEADLINE_TYPE, DEADLINE_DEFAULT_TYPE)));
        settings.setDeadlineConstantDecreaseValue(Float.valueOf(propertiesLoader.getProperty(
                DEADLINE_CONSTANT_DECREASE_VALUE, DEADLINE_CONSTANT_DECREASE_DEFAULT_VALUE)));
        settings.setHitAreaSize(
                Float.valueOf(propertiesLoader.getProperty(HIT_AREA_SIZE, HIT_AREA_SIZE_DEFAULT)));

    }

    private static void loadRulesSettings(GameSettings settings, Properties propertiesLoader) {

        settings.setPointsToBePlayed(Integer.valueOf(
                propertiesLoader.getProperty(POINTS_TO_BE_PLAYED, POINTS_TO_BE_PLAYED_DEFAULT)));

    }

    private static void loadScreenSettings(GameSettings settings, Properties propertiesLoader) {

        settings.setWindowWidth(
                Integer.valueOf(propertiesLoader.getProperty(WINDOW_WIDTH, WINDOW_WIDTH_DEFAULT)));
        settings.setWindowHeight(Integer
                .valueOf(propertiesLoader.getProperty(WINDOW_HEIGHT, WINDOW_HEIGHT_DEFAULT)));
        settings.setFullScreenMod(Boolean.valueOf(
                propertiesLoader.getProperty(RUN_IN_FULLSCREEN, RUN_IN_FULLSCREEN_DEFAULT)));

    }

    /**
     * Writes the given set of {@link GameSettings} to the file "config.cfg".
     * 
     * @param settings to write
     */
    public static void writeGameSettings(GameSettings settings) {

        Properties propertiesSaver = new Properties();

        saveVelocitySettings(settings, propertiesSaver);
        saveDeadlineSettings(settings, propertiesSaver);
        saveRulesSettings(settings, propertiesSaver);
        saveScreenSettings(settings, propertiesSaver);
        FileOutputStream fileHandle;
        try {
            fileHandle = new FileOutputStream(Gdx.files.internal("config.cfg").file().getPath());
            propertiesSaver.store(fileHandle, null);
            fileHandle.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Gdx.app.log("GameSettings", "FileNotFoundException during writing of gameSettings");
        } catch (IOException e) {
            Gdx.app.log("GameSettings", "IO Exception during writing of gameSettings");
            e.printStackTrace();
        }

    }

    private static void saveVelocitySettings(GameSettings settings, Properties propertiesSaver) {

        propertiesSaver.setProperty(VELOCITY_MOD, settings.getVelocityMod().toString());
        propertiesSaver.setProperty(VELOCITY_CONSTANT_INCREASE_VALUE,
                Float.toString(settings.getVelocityConstantIncreaseValue()));
        propertiesSaver.setProperty(VELOCITY_HITPOINT_MIN,
                Float.toString(settings.getVelocityHitpointMin()));
        propertiesSaver.setProperty(VELOCITY_HITPOINT_MAX,
                Float.toString(settings.getVelocityHitpointMax()));
        propertiesSaver.setProperty(BALL_START_VELOCITY,
                Float.toString(settings.getBallStartVelocity()));

    }

    private static void saveDeadlineSettings(GameSettings settings, Properties propertiesSaver) {

        propertiesSaver.setProperty(DEADLINE_TYPE, settings.getDeadlineType().toString());
        propertiesSaver.setProperty(DEADLINE_CONSTANT_DECREASE_VALUE,
                Float.toString(settings.getDeadlineConstantDecreaseValue()));
        propertiesSaver.setProperty(HIT_AREA_SIZE, Float.toString(settings.getHitAreaSize()));

    }

    private static void saveRulesSettings(GameSettings settings, Properties propertiesSaver) {

        propertiesSaver.setProperty(POINTS_TO_BE_PLAYED,
                Integer.toString(settings.getPointsToBePlayed()));

    }

    private static void saveScreenSettings(GameSettings settings, Properties propertiesSaver) {

        propertiesSaver.setProperty(WINDOW_WIDTH, Integer.toString(settings.getWindowWidth()));
        propertiesSaver.setProperty(WINDOW_HEIGHT, Integer.toString(settings.getWindowHeight()));
        propertiesSaver.setProperty(RUN_IN_FULLSCREEN,
                Boolean.toString(settings.isFullScreenMod()));

    }
}

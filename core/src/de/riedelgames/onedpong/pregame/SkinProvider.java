package de.riedelgames.onedpong.pregame;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SkinProvider {

    private static Skin skin;

    private static TextureAtlas textureAtlas;

    private static BitmapFont standardFont;

    private static void initializeSkin() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("ui/uiElements.atlas"));
        skin = new Skin();
        skin.addRegions(textureAtlas);
        skin.load(Gdx.files.internal("ui/skin.json"));
    }

    public static Skin getSkin() {
        if (skin == null) {
            initializeSkin();
        }
        return skin;
    }

    public static BitmapFont getStandardFont() {
        if (standardFont == null) {
            standardFont = new BitmapFont(Gdx.files.getFileHandle("font/square_unique.fnt", Files.FileType.Internal));
        }
        return standardFont;
    }

    private SkinProvider() {
    }
}

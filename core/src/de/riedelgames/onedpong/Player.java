package de.riedelgames.onedpong;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.InputProcessor;

public class Player implements InputProcessor {

    private int score = 0;
    private String name = "Unknown Player";
    private boolean keyDown = false;
    private int playerKey;

    private List<Integer> keysDown = new ArrayList<Integer>();

    public Player(int playerKey) {
        this.playerKey = playerKey;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKeyDown() {
        return keyDown;
    }

    public void setKeyDown() {
        this.keyDown = true;
    }

    public void unsetKeyDown() {
        this.keyDown = false;
    }

    public int getPlayerKey() {
        return playerKey;
    }

    public void setPlayerKey(int playerKey) {
        this.playerKey = playerKey;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == this.playerKey) {
            this.keyDown = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == this.playerKey) {
            this.keyDown = false;
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

    public void addKeyDown(int keycode) {
        keysDown.add(keycode);
    }

    public void removeKeyDown(int keycode) {
        keysDown.remove(keycode);
    }

    public List<Integer> getKeysDown() {
        return keysDown;
    }

}

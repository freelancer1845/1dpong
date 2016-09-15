package de.riedelgames.onedpong.network;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.riedelgames.core.networking.api.constants.Keys;
import de.riedelgames.core.networking.api.server.GameServerWrapper;
import de.riedelgames.core.networking.api.server.NetworkKeyEvent;
import de.riedelgames.gameobjects.Player;
import de.riedelgames.onedpong.game.GameStatus;

public class NetworkHandler implements Runnable {

    private static NetworkHandler instance = null;

    /**
     * Tracks whether the save is started. This way multiple server instances
     * are blocked.
     */
    private boolean started = false;

    /** Underlying server. */
    private GameServerWrapper gameServer;

    private List<NetworkServerClient> networkClients = new ArrayList<NetworkServerClient>();

    public static NetworkHandler getInstance() {
        if (instance == null) {
            instance = new NetworkHandler();
        }
        return instance;
    }

    private NetworkHandler() {
        this.gameServer = new GameServerWrapper();
    }

    public void startServer() {
        if (!started) {
            this.started = this.gameServer.startServer();
        }
        if (!started) {
            Gdx.app.error("Network Error", "Starting server failed.");
        }
    }

    public void stopServer() {
        if (started) {
            this.gameServer.stopServer();
            this.started = false;
        }

    }

    @Override
    public void run() {
        while (true) {
            cleanList();
        }
    }

    public void setVisible(boolean visible) {
        gameServer.setVisibility(visible);
    }

    private void cleanList() {
        List<NetworkServerClient> deleteList = new ArrayList<NetworkServerClient>();
        InetAddress[] connectedClients = this.gameServer.getConnectedClients();
        for (NetworkServerClient client : networkClients) {
            boolean isConnected = false;
            for (int i = 0; i < connectedClients.length; i++) {
                if (client.getInetAddress().equals(connectedClients[i])) {
                    isConnected = true;
                    break;
                }
            }
            if (!isConnected) {
                deleteList.add(client);
            }
        }
        networkClients.removeAll(deleteList);
    }

    public void dispose() {
        this.gameServer.dispose();
    }

    public List<NetworkServerClient> getNetworkClients() {
        InetAddress[] connectedClients = gameServer.getConnectedClients();
        for (int i = 0; i < connectedClients.length; i++) {
            if (!networkClientPresent(connectedClients[i])) {
                networkClients.add(new NetworkServerClient(connectedClients[i], new Player(0)));
            }
        }
        return Collections.unmodifiableList(networkClients);
    }

    public void fireKeyEvents(InputProcessor inputProcessor) {
        InetAddress[] connectedClients = gameServer.getConnectedClients();
        gameServer.sortNetworkPackages();
        if (connectedClients.length > 0) {
            List<NetworkKeyEvent> keyList = gameServer.getSortedDataMap().get(connectedClients[0]).getKeyEventList();
            for (NetworkKeyEvent keyEvent : keyList) {
                switch (keyEvent.getKeyEventType()) {
                case NetworkKeyEvent.KEY_EVENT_DOWN:
                    inputProcessor.keyDown(keyMapper(keyEvent.getKeyEventCode()));
                    break;
                case NetworkKeyEvent.KEY_EVENT_UP:
                    inputProcessor.keyUp(keyMapper(keyEvent.getKeyEventCode()));
                    break;
                }
            }
        }
    }

    public void updateKeyStatusGame(GameStatus gameStatus) {
        InetAddress[] connectedClients = gameServer.getConnectedClients();
        gameServer.sortNetworkPackages();
        // updateForSinglePlayer(gameStatus);

        // Left Player
        List<NetworkKeyEvent> keyList = gameServer.getSortedDataMap().get(connectedClients[0]).getKeyEventList();
        for (NetworkKeyEvent keyEvent : keyList) {
            if (keyEvent.getKeyEventCode() == Keys.FIRE) {
                switch (keyEvent.getKeyEventType()) {
                case NetworkKeyEvent.KEY_EVENT_DOWN:
                    gameStatus.getLeftPlayer().setKeyDown();
                    break;
                case NetworkKeyEvent.KEY_EVENT_UP:
                    gameStatus.getLeftPlayer().unsetKeyDown();
                }
            }
        }
        // RightPlayer
        keyList = gameServer.getSortedDataMap().get(connectedClients[1]).getKeyEventList();
        for (NetworkKeyEvent keyEvent : keyList) {
            if (keyEvent.getKeyEventCode() == Keys.FIRE) {
                switch (keyEvent.getKeyEventType()) {
                case NetworkKeyEvent.KEY_EVENT_DOWN:
                    gameStatus.getRightPlayer().setKeyDown();
                    break;
                case NetworkKeyEvent.KEY_EVENT_UP:
                    gameStatus.getRightPlayer().unsetKeyDown();
                }
            }
        }
    }

    private boolean networkClientPresent(InetAddress inetAddress) {
        for (NetworkServerClient client : networkClients) {
            if (client.getInetAddress().equals(inetAddress)) {
                return true;
            }
        }
        return false;
    }

    private int keyMapper(byte networkKeyCode) {
        switch (networkKeyCode) {
        case Keys.KEY_DOWN:
            return Input.Keys.DOWN;
        case Keys.KEY_UP:
            return Input.Keys.UP;
        default:
            return -1;
        }
    }

    private void updateForSinglePlayer(GameStatus gameStatus) {
        InetAddress[] connectedClients = gameServer.getConnectedClients();
        // Left Player
        List<NetworkKeyEvent> keyList = gameServer.getSortedDataMap().get(connectedClients[0]).getKeyEventList();
        for (NetworkKeyEvent keyEvent : keyList) {
            if (keyEvent.getKeyEventCode() == Keys.KEY_UP) {
                switch (keyEvent.getKeyEventType()) {
                case NetworkKeyEvent.KEY_EVENT_DOWN:
                    gameStatus.getLeftPlayer().setKeyDown();
                    break;
                case NetworkKeyEvent.KEY_EVENT_UP:
                    gameStatus.getLeftPlayer().unsetKeyDown();
                }
            } else if (keyEvent.getKeyEventCode() == Keys.KEY_DOWN) {
                switch (keyEvent.getKeyEventType()) {
                case NetworkKeyEvent.KEY_EVENT_DOWN:
                    gameStatus.getRightPlayer().setKeyDown();
                    break;
                case NetworkKeyEvent.KEY_EVENT_UP:
                    gameStatus.getRightPlayer().unsetKeyDown();
                }
            }
        }
    }

}

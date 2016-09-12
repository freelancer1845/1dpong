package de.riedelgames.onedpong.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Inputs;

import de.riedelgames.core.networking.api.constants.Keys;
import de.riedelgames.core.networking.api.server.GameServerWrapper;
import de.riedelgames.core.networking.api.server.NetworkKeyEvent;
import de.riedelgames.gameobjects.Player;

public class NetworkHandler implements Runnable {

    private static NetworkHandler instance = null;

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
        this.gameServer.startServer();
    }

    public void stopServer() {
        this.gameServer.stopServer();
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

    public void updatePlayerKeysGame(InputProcessor inputProcessor) {
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

}

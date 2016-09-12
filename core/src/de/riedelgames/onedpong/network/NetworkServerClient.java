package de.riedelgames.onedpong.network;

import java.net.InetAddress;

import de.riedelgames.gameobjects.Player;

public class NetworkServerClient {

    /** Address that identifies this player. */
    private final InetAddress inetAddress;

    /** Player object corresponding to this network client. */
    private final Player player;

    public NetworkServerClient(InetAddress inetAddress, Player player) {
        this.player = player;
        this.inetAddress = inetAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public Player getPlayer() {
        return player;
    }

    public String getIp() {
        return inetAddress.getHostAddress();
    }

}

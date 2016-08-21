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

import de.riedelgames.onedpong.Player;

public class NetworkHandler implements Runnable {

    private static NetworkHandler instance = null;

    private final static String GROUPNAME = "229.127.12.17";

    private DatagramSocket udpSocket;

    private int portNumber;
    private String serverName;
    private ServerSocket serverSocket;

    private boolean visible = true;

    private List<NetworkServerClient> networkClients = new ArrayList<NetworkServerClient>();

    public static NetworkHandler getInstance() {
        if (instance == null) {
            instance = new NetworkHandler(4000);
        }
        return instance;
    }

    private NetworkHandler(int portNumber) {
        this.portNumber = portNumber;
        this.serverName = "Standard Server";
        try {
            this.serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(2000);

            udpSocket = new DatagramSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Error creating ServerSocket.");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (visible) {
                makeServerVisible();
            }
            listenForClient();
            cleanList();
        }
    }

    private void makeServerVisible() {
        byte[] buf = new byte[256];

        try {
            buf = serverName.getBytes();
            InetAddress group = InetAddress.getByName(GROUPNAME);
            DatagramPacket packet;
            packet = new DatagramPacket(buf, buf.length, group, portNumber);
            udpSocket.send(packet);
        } catch (UnknownHostException e) {
            Gdx.app.log("Network: ", "Error in UDP package send: UnknownHostException");
            e.printStackTrace();
        } catch (IOException e) {
            Gdx.app.log("Network: ", "Error in UDP package send: IOException");
            e.printStackTrace();
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private void listenForClient() {
        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection accepted");

            NetworkServerClient tempClient = new NetworkServerClient(clientSocket, new Player(NetworkCodes.MAIN_KEY));
            networkClients.add(tempClient);
            Thread clientThread = new Thread(tempClient);
            clientThread.start();
        } catch (IOException e) {
            // System.out.println("Timeout acception client reached.");
            // e.printStackTrace();
        }
    }

    private void cleanList() {
        List<NetworkServerClient> deleteList = new ArrayList<NetworkServerClient>();
        for (NetworkServerClient client : networkClients) {
            if (!client.isConnected()) {
                deleteList.add(client);
            }
        }
        networkClients.removeAll(deleteList);
    }

    public void dispose() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            Gdx.app.log("Network Handler: ", "Error closing the socket (already closed?");
            e.printStackTrace();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public List<NetworkServerClient> getNetworkClients() {
        return Collections.unmodifiableList(networkClients);
    }

}

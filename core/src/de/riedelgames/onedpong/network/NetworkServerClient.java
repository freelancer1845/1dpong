package de.riedelgames.onedpong.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import com.badlogic.gdx.Gdx;
import de.riedelgames.onedpong.OneDPong;
import de.riedelgames.onedpong.Player;

public class NetworkServerClient implements Runnable {

    private OneDPong game;

    private boolean handshakeReceived = false;

    private Socket socket;
    private Player player;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    SynchronousQueue<Object> currentObject;

    public NetworkServerClient(Socket socket, Player player) {
        this.socket = socket;
        this.player = player;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Caught IO Exception: ");
            System.out.println(e.getMessage());
        }
        this.game = (OneDPong) Gdx.app.getApplicationListener();
    }

    protected void finalize() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error during socket.close() in finalize");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {

            try {

                if (!handshakeReceived) {
                    waitForHandShakePackage();
                }

                int[] readArray = readIntArray();
                // System.out.println("Package ID: " + readArray[0]);
                // System.out.println("Package Length: " + readArray[1]);

                processPackage(readArray);
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    Gdx.app.log("Network", "Connection lost to client: " + e1.getMessage());
                }
                Gdx.app.log("Network", "Connection lost to client: " + e.getMessage());
                break;
            }

        }

    }

    private void writeIntArray(int key, int[] array) throws IOException {
        dataOutputStream.writeInt(key);
        dataOutputStream.writeInt(array.length);
        for (int i = 0; i < array.length; i++) {
            dataOutputStream.writeInt(array[i]);
        }
    }

    private int[] readIntArray() throws IOException {
        int key = dataInputStream.readInt();
        int packageLength = dataInputStream.readInt();
        int[] returnArray = new int[packageLength + 2];
        returnArray[0] = key;
        returnArray[1] = packageLength;
        for (int i = 0; i < packageLength; i++) {
            returnArray[i + 2] = dataInputStream.readInt();
        }
        // for (int i = 0; i < returnArray.length; i++) {
        // System.out.print("[" + returnArray[i] + "] ");
        // }
        // System.out.print("\n");
        return returnArray;
    }

    private void processPackage(int[] readArray) throws IOException {
        int packageId = readArray[0];
        switch (packageId) {
        case NetworkCodes.HANDSHAKE:
            break;
        case NetworkCodes.NAME:
            processNamePackage(readArray);
            break;
        case NetworkCodes.KEYS_PRESSED:
            processKeysPressedPackage(readArray);
            break;

        default:
            Gdx.app.log("Unkown Package Id: ", Integer.toString(packageId));
        }
    }

    private void processNamePackage(int[] readArray) throws IOException {

        char[] nameArray = new char[readArray[1]];
        for (int i = 0; i < nameArray.length; i++) {
            nameArray[i] = (char) readArray[i + 2];
        }
        String playerName = String.valueOf(nameArray);
        player.setName(playerName);
        writeIntArray(NetworkCodes.NAME_RESPONSE, new int[] { NetworkCodes.NAME_ACCEPTED });

    }

    private void processKeysPressedPackage(int[] readArray) throws IOException {
        if (readArray[3] == NetworkCodes.KEY_DOWN) {
            player.keyDown(readArray[2]);
            game.getScreenAsInputProcessor().keyDown(readArray[2]);
        } else if (readArray[3] == NetworkCodes.KEY_UP) {
            player.keyUp(readArray[2]);
            game.getScreenAsInputProcessor().keyUp(readArray[2]);
        } else {
            Gdx.app.error("Network", "Worng Key Pressed Package received");
        }

        writeIntArray(NetworkCodes.KEYS_PRESSED_RESPONSE, new int[] { NetworkCodes.KEYS_PRESSED_HANDELED });
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getIp() {
        return socket.getInetAddress().getHostAddress();
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }

    private void waitForHandShakePackage() throws IOException {
        int[] readArray = readIntArray();
        if (readArray[0] == NetworkCodes.HANDSHAKE) {
            writeIntArray(NetworkCodes.HANDSHAKE_RESPONSE, new int[0]);
            handshakeReceived = true;
        } else {
            Gdx.app.error("Network", "Expected handshake package as first package. Closed connection.");
            socket.close();
        }
    }
}

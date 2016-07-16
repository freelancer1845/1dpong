package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;

import de.riedelgames.onedpong.Player;


public class NetworkHandler implements Runnable {
	
	private final static String GROUPNAME = "229.127.12.17";
	
	private DatagramSocket udpSocket;
	
	private int portNumber;
	private String serverName;
	private ServerSocket serverSocket;
	
	private boolean visible = true;
	
	public boolean isVisible() {
		return visible;
	}

	public LinkedList<NetworkServerClient> networkClients = new LinkedList<NetworkServerClient>();
	private LinkedList<Player> playerList = new LinkedList<Player>();
	
	public NetworkHandler(int portNumber, LinkedList<Player> playerList){
		this.portNumber = portNumber;
		this.playerList = playerList;
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
		while(true){
			if (visible) {
				makeServerVisible();
			}
			listenForClient();
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
			
			if(!playerList.isEmpty()){
				NetworkServerClient tempClient = new NetworkServerClient(clientSocket, playerList.removeFirst());
				networkClients.add(tempClient);
				Thread clientThread = new Thread(tempClient);
				clientThread.start();
			} else {
				Gdx.app.log("Network Error", "All players Connected");
			}
			
		} catch (IOException e) {
			//System.out.println("Timeout acception client reached.");
			//e.printStackTrace();
		}
	}
	
	public void dispose() {
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			Gdx.app.log("Network Handler: ", "Error closing the socket (already closed?");
			e.printStackTrace();
		}
	}
	
}

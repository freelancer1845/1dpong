package Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import de.riedelgames.onedpong.Player;


public class NetworkServerClient implements Runnable {

	/**
	 * Size of the package size identifier in bytes.
	 */
	private static final int PACKAGE_SIZE_IDENTIFIER_BYTE_LENGTH = 1;
	
	/**
	 * Handshake Package.
	 */
	private static final int HANDSHAKE = 100;
	
	/**
	 * Handshake Response Package.
	 */
	private static final int HANDSHAKE_RESPONSE = 101;
	
	/**
	 * Keys Pressed Package
	 */
	private static final int KEYS_PRESSED = 201;
	
	/**
	 * Key Pressed Response Package.
	 */
	private static final int KEYS_PRESSED_RESPONSE = 202;
	private static final int KEYS_PRESSED_HANDELED = 1;
	
	/**
	 * Key Down.
	 */
	private static final int KEY_DOWN = -1;
	
	/**
	 * Key Up.
	 */
	private static final int KEY_UP = 1;
	
	
	private Socket socket;
	private Player player;
	
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;

	
	
	
	SynchronousQueue<Object> currentObject;
	
	
	public NetworkServerClient(Socket socket, Player player){
		this.socket = socket;
		this.player = player;
		// open object stream
		try{
			this.dataInputStream = new DataInputStream(socket.getInputStream());
			this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		}catch(IOException e){
			System.out.println("Caught IO Exception: ");
			System.out.println(e.getMessage());
		}
	}
	
	
	
		
	protected void finalize(){
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error during socket.close() in finalize");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		while(true) {
			
			
			try {
				
				int[] readArray = readIntArray();
				System.out.println("Package ID: " + readArray[0]);
				System.out.println("Package Length: " + readArray[1]);

				processPackage(readArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		return returnArray;
	}
	
	private void processPackage(int[] readArray) throws IOException {
		int packageId = readArray[0];
		switch (packageId) {
		case KEYS_PRESSED:
			processKeysPressedPackage(readArray);
			break;
		default:
			Gdx.app.log("Unkown Package Id: ", Integer.toString(packageId));
		}
	}
	
	private void processKeysPressedPackage(int[] readArray) throws IOException {
		System.out.println("Key package recevied.");
		for (int i = 2; i < readArray.length - 2; i = i + 2) {
			if (readArray[i] == KEY_DOWN) {
				player.keyDown(readArray[i + 1]);
			} else if (readArray[i] == KEY_UP) {
				player.keyUp(readArray[i + 1]);
			}
		}
		writeIntArray(KEYS_PRESSED_RESPONSE, new int[]{KEYS_PRESSED_HANDELED});
	}
	
	
	public Player getPlayer(){
		return this.player;
	}
}

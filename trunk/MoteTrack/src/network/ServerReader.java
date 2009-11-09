/**
 * 
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * @author bensch
 *
 */
public class ServerReader extends Observable implements Runnable {
	private String srv;
	private int port;
	private boolean run;
	
	public ServerReader(String srv, int port) {
		this.srv = srv;
		this.port = port;
		this.run = true;
	}
	
	public void startReader() {
		Thread t = new Thread(this);
		t.start();
	}
	
	protected void updateObserver(String msg) {
		this.setChanged();
		this.notifyObservers(msg);
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Socket client;
		BufferedReader reader;
		
		try {
			client = new Socket(this.srv, this.port);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while (run) {
				updateObserver(reader.readLine());
			}
			client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

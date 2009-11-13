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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bensch
 *
 */
public class ServerReader extends Observable implements Runnable {
	private final String srv;
	private final int port;
	private volatile boolean run;
        private Socket client;
        private BufferedReader reader;
        
	
	public ServerReader(final String srv, final int port) {
		this.srv = srv;
		this.port = port;
		this.run = true;
	}
	
	public void startReader() {
		Thread t = new Thread(this);
		t.start();
	}

        public void stopReader() {
            run = false;
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
		
		try {
			client = new Socket(this.srv, this.port);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String line = "";
                        
			while (run && line != null) {
				updateObserver((line = reader.readLine()));
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
                    System.err.println("connection not possible");
		} finally {
                    closeConnection();
                }
	}

        private void closeConnection() {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
}

/**
 * 
 */
package network;

import exceptions.ClientAlreadyExistsException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import listener.ConnectionListener;

/**
 * @author bensch
 *
 */
public class ServerReader extends Observable implements Runnable {
    private final String srv;
    private final int port;
    private volatile boolean run;
    private boolean connected;
    private Socket client;
    private BufferedReader reader;


    private ArrayList<ConnectionListener> listeners;
    private static HashMap<String, ServerReader> serverReaders = new HashMap<String, ServerReader>();

    public ServerReader(final String srv, final int port) throws ClientAlreadyExistsException {
        if (serverReaders.containsKey(srv+":"+port)){
            throw new ClientAlreadyExistsException(this);
        }

        this.srv = srv;
        this.port = port;
        this.run = false;
        this.connected = false;
        listeners = new ArrayList<ConnectionListener>();
        serverReaders.put(srv+":"+port, this);
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isRunning() {
        return run;
    }

    public boolean startReader() {
        if (run) return false;
        Thread t = new Thread(this);
        run = true;
        t.start();

        return run;
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
            connected = true;
            fireConnectionStateChanged(true);

            try {
                String line = "";
                while (run && line != null) {
                    updateObserver((line = reader.readLine()));
                }
            } catch (IOException e) {
                System.err.println("connection interrupted");
                fireConnectionStateChanged(false);
            } finally {
                closeConnection();
                run = false;
                serverReaders.remove(srv+":"+port);
            }
            
        } catch (UnknownHostException e) {
            System.err.println("unknown host");
            fireUnknownHost();
        } catch (IOException e) {
            System.err.println("connection not possible");
            fireConnectionStateChanged(false);
        }
    }

    private void closeConnection() {
        boolean error = false;

        if (client != null) {
            try {
                client.close();
            } catch (IOException ex) {
                error = true;
                Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                error = true;
                Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        connected = false;
        if(!error && client != null && reader != null) fireConnectionStateChanged(true);
    }

    public void addConnectionListener(ConnectionListener l) {
        listeners.add(l);
    }

    public void removeConnectionListener(ConnectionListener l) {
        listeners.remove(l);
    }

    private void fireConnectionStateChanged(boolean intended) {
        for (ConnectionListener l : listeners) {
            l.connectionStateChanged(this, connected, intended);
        }
    }

    private void fireUnknownHost() {
         for (ConnectionListener l : listeners) {
            l.unknownHost(this, srv);
        }
    }
}

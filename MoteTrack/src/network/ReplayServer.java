package network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ReplayServer{
    private volatile boolean run;

    public ReplayServer(String fileName, int rate, int port) {
        run = true;
        int id = 1;

        try {
            ServerSocket srv = new ServerSocket(port);
            Socket client;
            ClientHandler handler;
            while (run) {
                client = srv.accept();
                System.out.println("new client ("+id+")");
                handler = new ClientHandler(fileName, rate, client, id++);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopReplayServer() {
        run = false;
    }

    private class ClientHandler implements Runnable {
        private String fileName;
        private int rate;
        private Socket client;
        private int id;

        public ClientHandler(String fileName, int rate, Socket client, int id) {
            this.fileName = fileName;
            this.rate = rate;
            this.client = client;
            this.id = id;
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            PrintWriter out;
            BufferedReader reader;
            String read;

            try {
                reader = new BufferedReader(new FileReader(this.fileName));
                out = new PrintWriter(this.client.getOutputStream());
                /* die server quittiert jetzt seinen dienst sobald der stream
                 * einen error hatte - thomas */
                while (true && !out.checkError()) {
                    while ((read = reader.readLine()) != null && !out.checkError()) {
                        out.println(read);
                        Thread.sleep(rate);
                    }
                    reader.close();
                    reader = new BufferedReader(new FileReader(this.fileName));
                }
                out.close();
                client.close();
                System.out.println("client disconnected ("+id+")");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static final int DEFAULT_PORT = 5000;
    private static final int DEFAULT_RATE = 100;
    private static final String DEFAULT_FILE = "../MoteTrack/logs/situps.txt";

    public static void main(String[] args) {
        startServer(args);
    }

    public static void startServer() {
        System.out.println("STARTING DEFAULT REPLAY SERVER");
        startServer(DEFAULT_FILE, DEFAULT_RATE, DEFAULT_PORT);
    }

    private static void startServer(final String file, final int rate, final int port) {
        new ReplayServer(file, rate, port);
        System.out.println("SERVER STARTED");
    }

    public static void startServer(String[] args) {
        switch (args.length) {
            case 0:
                startServer();
                break;
            case 1:
                System.out.println("STARTING SERVER for "+args[0]+" with default rate on default port");
                startServer(args[0], DEFAULT_PORT, DEFAULT_PORT);
                break;
            case 2:
                System.out.println("STARTING SERVER for "+args[0]+" with default rate on default port");
                startServer(args[0], DEFAULT_PORT, Integer.parseInt(args[1]));
                break;
            case 3:
                System.out.println("STARTING SERVER for "+args[0]+" with default rate on default port");
                startServer(args[0], Integer.parseInt(args[2]), Integer.parseInt(args[1]));
                break;
            default:
                System.err.println("WRONG ARGUMENT COUNT");
                startServer();
                return;
       }
    }
}

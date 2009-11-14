package network.client;

import exceptions.ClientAlreadyExistsException;
import network.ServerDataReader;
import misc.ServerLogger;

public class ServerClient {
	public static void main(String[] args) throws ClientAlreadyExistsException {
            startServer();
	}

        public static ServerDataReader startServer() throws ClientAlreadyExistsException {
		System.out.println("Starting client");
		String fileName = "../MoteTrack/logs/"+System.currentTimeMillis()+".txt";
		System.out.println("Writing results to "+fileName);
		ServerLogger log = new ServerLogger(fileName);
		ServerDataReader reader = new ServerDataReader("192.168.0.10",6666);
		reader.addObserver(log);
		reader.startReader();
                return reader;
        }

        public static ServerDataReader startServer(String ids) throws ClientAlreadyExistsException {
                System.out.println("Starting client");
		String fileName = "../MoteTrack/logs/"+System.currentTimeMillis()+".txt";
		System.out.println("Writing results to "+fileName);
		ServerLogger log = new ServerLogger(fileName);
		ServerDataReader reader = new ServerDataReader("192.168.0.10",6666, ids);
		reader.addObserver(log);
		reader.startReader();
                return reader;
        }
}

package network.client;

import data.Position;
import network.ServerDataReader;
import misc.ServerLogger;
import network.NormalizedServerDataReader;

public class ReplayServerClient {
	public static void main(String[] args) {
            startServer();
	}

        public static ServerDataReader startServer() {
		System.out.println("Starting client");
		String fileName = "../MoteTrack/logs/ReplayLogs/"+System.currentTimeMillis()+".txt";
		System.out.println("Writing results to "+fileName);
		ServerLogger log = new ServerLogger(fileName);
		ServerDataReader reader = new ServerDataReader("localhost",5000);//,"020000111249;020000136188");
		reader.addObserver(log);
		reader.startReader();
                return reader;
        }

        public static ServerDataReader startNormalizedClient() {
            	System.out.println("Starting normalized client");
		String fileName = "../MoteTrack/logs/NormalizedReplayLogs/"+System.currentTimeMillis()+".txt";
		System.out.println("Writing results to "+fileName);
		ServerLogger log = new ServerLogger(fileName);
                NormalizedServerDataReader obs = new NormalizedServerDataReader(new Position(0.15,0.15,0.15),1);
		ServerDataReader reader = new ServerDataReader("localhost",5000);//,"020000111249;020000136188");
		reader.addObserver(log);
                reader.addObserver(obs);
		reader.startReader();
                return reader;
        }
}

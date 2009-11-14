package test;

import exceptions.ClientAlreadyExistsException;
import network.ServerDataReader;
import misc.ServerLogger;

public class TestServerConnection {
	public static void main(String[] args) throws ClientAlreadyExistsException {
		System.out.println("Starting client");
		String fileName = "../MoteTrack/logs/"+System.currentTimeMillis()+".txt";
		System.out.println("Writing results to "+fileName);
		ServerLogger log = new ServerLogger(fileName);
		ServerDataReader reader = new ServerDataReader("192.168.0.10",6666,"020000111249;020000136188");
		reader.addObserver(log);
		reader.startReader();
	}
}

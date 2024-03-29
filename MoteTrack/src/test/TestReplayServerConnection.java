package test;

import exceptions.ClientAlreadyExistsException;
import network.ServerDataReader;
import misc.ServerLogger;

public class TestReplayServerConnection {
	public static void main(String[] args) throws ClientAlreadyExistsException {
		System.out.println("Starting client");
		String fileName = "logs/ReplayLog"+System.currentTimeMillis()+".txt";
		System.out.println("Writing results to "+fileName);
		ServerLogger log = new ServerLogger(fileName);
		ServerDataReader reader = new ServerDataReader("localhost",5000,"020000111249;020000136188");
		reader.addObserver(log);
		reader.startReader();
	}
}

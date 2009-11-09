package network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ReplayServer {
	public ReplayServer(String fileName, int rate, int port) {
		try {
			ServerSocket srv = new ServerSocket(port);
			Socket client;
			ClientHandler handler;
			while (true) {
				client = srv.accept();
				System.out.println("new client");
				handler = new ClientHandler(fileName, rate, client);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class ClientHandler implements Runnable {
		private String fileName;
		private int rate;
		private Socket client;
		public ClientHandler(String fileName, int rate, Socket client) {
			this.fileName = fileName;
			this.rate = rate;
			this.client = client;
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
				while (true) {
					while ((read = reader.readLine()) != null) {
						out.println(read);
						wait(this.rate);
					}
					reader.close();
					reader = new BufferedReader(new FileReader(this.fileName));
				}
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
	
	public static void main(String[] args) {
		new ReplayServer("test.txt",1000,5000);
	}
}

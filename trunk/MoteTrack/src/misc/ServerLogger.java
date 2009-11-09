package misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ServerLogger implements Observer{
	private PrintWriter writer;
	
	public ServerLogger(String fileName) {
		try {
			writer = new PrintWriter(new FileWriter(fileName,true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println(arg1);
		writer.println(arg1);
		writer.flush();
	}

	public void closeFile() {
		writer.close();
	}
}

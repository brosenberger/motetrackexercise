package misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ServerLogger implements Observer{
	private PrintWriter writer, writerV2;
        private long lastEvent;
	
	public ServerLogger(String fileName) {
		try {
                    File file = new File(fileName);
                    String fileNameV2 = file.getParent()+"\\V2_"+file.getName();
			writer = new PrintWriter(new FileWriter(fileName,true));
                        writerV2 = new PrintWriter(new FileWriter(fileNameV2, true));
                        lastEvent = System.currentTimeMillis();
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

                long systime = System.currentTimeMillis();
                long delay = systime-lastEvent;
                lastEvent = systime;
                writerV2.println(delay+" "+arg1);
                writerV2.flush();
	}

	public void closeFile() {
		writer.close();
                writerV2.close();
	}
}

package misc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class Configurator {
	private String fileName=null;
	private HashMap<String, String> config;
	
	public Configurator(String fileName) {
		this.fileName = fileName;
		config = new HashMap<String, String>();
	}
	
	public void loadConfiguration(String fileName) throws FileNotFoundException {
		BufferedReader reader;
		String read;
		String[] split;

		config.clear();
		reader = new BufferedReader(new FileReader(fileName));
		try {
			while ((read=reader.readLine())!=null) {
                            // SKIP EMPTY LINES AND LINES STARTING WITH '--'
                            if (!(read.startsWith("--") || read.equals("\n") || read.equals("\r") || read.equals(""))){
				split=read.split(":");
				config.put(split[0], split[1]);
                            }
                            
			}
			reader.close();
		} catch (IOException e) {
			config.clear();
			e.printStackTrace();
		}
	}
	
	public void loadConfiguration() throws FileNotFoundException {
		if (fileName==null) throw new FileNotFoundException();
		this.loadConfiguration(this.fileName);
	}
	
	public void saveConfiguration(String fileName) throws Exception {
		PrintWriter writer;
		Iterator<String> it;
		String confName,confVal;
		
		writer = new PrintWriter(new FileWriter(fileName));
		it = config.keySet().iterator();
		while (it.hasNext()) {
			confName=it.next();
			confVal = config.get(confName);
			if (confVal == null) continue;
			writer.println(confName+":"+confVal);
		}
	}
	
	public boolean containsKey(String key) {
		return config.containsKey(key);
	}
	
	public String get(String key) {
		return config.get(key);
	}
	public void put(String key, String value) {
		config.put(key, value);
	}
	
	public void saveConfiguration() throws Exception {
		if (fileName==null) throw new Exception("no filename set");
		this.saveConfiguration(fileName);
	}
}
